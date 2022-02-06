一些结论:
    1.结论1:
        同一父工程之下的子项目就算不打包 也可互相引入(service_edu的pom 引入了 service_base项目)
      结论2:
        同一项目中 为什么子工程1 引入 子工程2 子工程1手动编译报错 怎么办?
        因为本地直接编译的时候子工程1中其实并没有子工程2 这种情况需要tomcat运行起来作为环境 帮助我们进行编译
      结论之下的结论:
        默认每个项目只扫描当下的所有注解 如果需要扫描别的项目中的注解 需要加@ComponentScan(basePackages = {"com.atguigu"}) 加大扫描范围

    2.使用element-admin中的文件上传组件 组件内部会把文件名称改为file.png进行上传 因为他害怕你的图片名称包含中文

    3.项目中要掌握的点:
        3.1 BeanUtils.copyProperties(旧对象,新对象); 把就对象的属性get出来 set进新对象里面
        3.2 在mybatis-plus中的service层中 可以通过baseMapper来直接得到 mapper对象

    4.mybatis的使用离不开2步
        4.1 第1步就是 必须指定sql映射文件的编译路径
        4.2 第2步就是 必须让xml文件参与编译

        这2步有俩种做法
            第一种:
                把所有的xml文件放在resource目录中 因为resource目录中的文件默认都会被编译(所以不需要在pom中添加编译的resource了)
                然后指定xml文件的编译路径
            第二种:
                把所有xml文件放在dao目录中 因为maven默认只编译java目录下的java文件(所以这时候需要在pom中指定编译的resource)
                然后指定xml文件的编译路径

    5. 阿里云视频点播:
       问题:
          如果上传的视频进行了hls加密处理 那么你的视频需要域名才能播放 否则不能播放
       解决:
          根据视频id获取视频凭证 通过视频凭证配个视频播放器实现播放视频
          (说穿了! 不是直接获取视频的播放地址播放 而是通过凭证配合播放器进行播放)

    6. 如果不想使用idea默认的maven仓库去下载资源 可以在项目中单独配置该项项目的下载地址
       在pom文件中配置和<build>同级的标签<repositories>
       <repositories>
         <repository>
            <id></id>
            <name></name>
            <url></url>
             <releases>
                   <enabled>true</enabled>
             </releases>
            <snapshots>
               <enabled>true</enabled>
            </snapshots>
         </repository>
       </repositories>

    7.问题:
        视频上传报错 后端:The field file exceeds its maximum permitted size of 1048576 bytes.
                   前端:跨域错误
      原因:
        后端:默认后端只能处理1MB的请求体数据 前端:超出了nginx课处理请求体的范围
      解决:
        我们只需要修改配置文件 把默认值改大就可以了
        后端(application.properties)和前端(nginx.conf)
        application.properties:
            spring.servlet.multipart.max-file-size=1024MB
            spring.servlet.multipart.max-request-size=1024MB
        nginx.conf:
            client_max_body_size 1024m;

    8.问题:
        删除课程的接口 出现一直feign调用404
      解决:
        由于vod这个Controller在类上面也写了RequestMapping("/.../..")
        刚开始没有注意到 所以一直路径有问题

    9.1问题
        删除课程 首先要根据couseID获取小节 发现获取到的每一个小节的video_source_id 和 video_original-name为空
       解决:
        前端添加小节中 上传视频的时候返回值有点慢 你等一等再点确认按钮

    9.2问题:
        批量删除视频接口
        public R removeMoreAlyVideo(List<String> videoIdList)
        一直报错No primary or default constructor found for interface java.util.List
      解决:
        因为第一次写这样的接口 不知道规范 所以一直报错 必须加@RequestParam("videoIdList") 进行约束
        public R removeMoreAlyVideo(@RequestParam("videoIdList") List<String> videoIdList)

    10.知识点:
       nacos整合Hystrix:
       1.properties中必须配置hystrix的默认超时时间(会报错)
            hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=6000
       2.写个全局的兜底方法处理类(处理方便)
            手写一个openFeign的实现类就可以了
       以上2步已经完成了 多并发超时问题处理 和 服务器宕机的处理
       3.如果某些接口需要单独配置(特殊业务)
            通过@HystrixCommand(commandProperties={}) 配置
            但是不会进入全局的兜底类 可以单独写个兜底方法 如果不写兜底方法可以写个全局捕获类

       nacos整合eureka:
       简单多了
       1.不全局超市配置 也不会报错
       2.通过@HystrixCommand(commandProperties={})配置 会进入全局的兜底类

    11. springcloud中seata分布式事务管理在连接数据库的时候 / mysql8以上 / springboot2.1以上
               需要加:
                   ?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2B8
               完整url:
                   jdbc:mysql://localhost:3306/sql?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2B8
               也是很坑爹的

               mysql驱动8以上 需要使用com.mysql.cj.jdbc.Driver
               mysql驱动8以下 可以使用com.mysql.jdbc.Driver

    12. nignx跨域无法解决:
        如果Nginx那上传大小和转发都配置了nginx也重启了还是出现跨域的话，重启电脑就好了 ，Windows版nginx bug



