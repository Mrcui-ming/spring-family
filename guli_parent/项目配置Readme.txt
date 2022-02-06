guili-parent教育网站项目 17号开始做的

    1.swagger2:
        是什么:
            swagger2提供了对项目中所有接口测试的 ui图形化界面
        怎么用:
            1. 创建common公共项目 内部创建service_base项目 并配置swaggerConfig对象
            2. 在service_edu中 depeneccy中引入jar包
            3. 在service_edu主启动中 加上@ComponentScan(basePackages = {"com.atguigu"})注解
            4. 访问swagger图形化界面 地址:协议+域名+端口号+swagger-ui.html

    2.项目跨域解决:
        第一种: Controller上加注解 @CrossOrgin
        第二种: 使用网关

    3.普通情况只能存储在一台服务器 但是存在阿里云对象oss 可以实现共享数据
    阿里云对象存储oss:
        1. 注册阿里云(通过支付宝 需要实名认证)
        2. 开通阿里云对象存储oss
        3. 进入阿里云oss管理控制台
        4. 创建Bucket仓库 选择配置项
            4.1 存储类型: 标准/低频
            4.2 读写权限: 公共读/公共读写
            4.3 同城冗余: 不开启 备份仓库一份(需要花钱)
            4.4 版本控制: 不开启 每次提交有版本序号(需要花钱)
        5. 进入Bucket列表进行上传/下载 或者 通过java代码上传/下载 如何通过java对象实现上传/下载?
            5.1 获取Access Key
                5.1.1 进入阿里云oss管理控制台
                5.1.2 点击入口内的Access Key 生成密钥和id(这一步之后的弹框点第一个就行)
                5.1.3 创建Access Key
            5.2 配置yml中的oss服务
            5.3 默认会扫描数据源 而没有配置数据源俩种解决方案:
                5.3.1 加入数据源的配置
                5.3.2 在主启动中排除springboot对数据源的扫描
            5.4 通过java代码上传资源
                5.4.1 手动找: 阿里云对象存储oss -> sdk实例 -> java -> 上传文件 -> 简单上传 -> 文件上传
                5.4.1 地址找: https://help.aliyun.com/document_detail/84781.html?spm=a2c4g.11186623.2.2.40217a74odUG9Q
                5.4.2 通过示例代码进行上传
                记俩个方法:
                    InputStream inputStream = (MultipartFile file).getInputStream();    获取instream
                    String fileName = (MultipartFile file).getOriginalFilename()        获取文件名称

    4.阿里云视频上传功能依赖于一个包 aliyun-java-void-upload 这个包没办法在maven中import导入 需要手动下载；
      4.1 下载
        4.1.1 第一种方式:打开阿里云 -> 视屏点播 -> 上传SDK -> 服务端上传 -> java上传 -> SDK下载
        4.1.2 第二种方式:
        mvn install:install-file -DgroupId=com.aliyun -DartifactId=aliyun-sdk-vod-upload -Dversion1.4.11 -Dpackaging=jar -Dfile=aliyun-java-vod-upload-1.4.11.jar
      4.2 下载之后把aliyun-java-void-upload包 手动放入你的maven本地仓库就可以了

     5.redis缓存首页数据
        5.1 redis缓存哪些数据?
             一般来讲 把经常查询 但不经常修改的数据放到redis作为缓存
             所以我们把客户端网站的首页数据进行redis缓存
        5.2 怎么做?
            5.2.1 首先加入redis的依赖 和 redis需要的数据源依赖

            5.2.2 修改properties文件(连接redis)

            5.2.2 redis配置类 @EnableCaching  //开启缓存 @Configuration  //表明配置类
                  并创建 redisTemplate对象(redisTemplate做缓存的 比如缓存哪些数据)
                        CacheManager对象(做缓存管理的 比如 缓存多久呀)

            5.2.3 我们是基于redis中的注解方式进行缓存数据 所以使用3个注解
                5.2.3.1 @Cacheable(value="...",key="'...'") value::key组成redis中的key
                        使用该注解标志的方法，表示该方法是做缓存的，对其方法返回值进行缓存,
                        下次请求时,如果redis中的缓存存在,就读取缓存,如果不存在,就执行方法,
                        把方法返回值存入指定缓存
                        重要的是把结果缓存,注解应该加载返回数据的方法上,如果放在controoler的话，把json数据缓存就不对了，需要缓存list集合
                        一般用在查询方法上
                5.2.3.2 @CacheEvict(value="...",key="'...'",allEntries=true)
                        使用该注解标志的方法,会清空指定key的缓存,一般用于更新或修改方法上
                        因为修改之后的数据要求是最新的，所以要情况，然后重新缓存
                        一般用在更新，删除方法上
                5.2.3.3 @CachePut(value="...",key="'...'")
                        使用该注解标志的方法,每次都会执行.并将结果存入指定缓存中,其他方法直接从响应
                        的缓存中读取数据，而不需要查询数据库
                        一般用在添加方法上

     6. 阿里云短信服务:
            加入jar包 然后写个发送方法(方法内需要一些id,select...)可以了 没别的配置
        腾讯云短信服务:
            加入jar包 然后写个发送方法(方法内需要一些id,select...)可以了 没别的配置


     7. oAuth2能做什么?
            OAuth2不是一种协议，而针对特定问题提供的一种解决方案。它主要解决两大问题：
            1. 开放系统间授权
               使用场景:照片拥有者想要在云冲印服务上打印照片，云冲印服务需要访问云存储服务上的资源,这样就会存在授权的问题。
            2. 分布式访问问题（比如：单点登录）
               OAuth2解决方案，通过令牌（token）值进行判断。
               第一步：登录之后，返回token信息，token就是按照一定规则生成字符串，包含了用户信息，把token放到地址栏；
               第二步：访问其它服务时，在地址栏带着token信息发送请求，根据token值获得用户信息，如果获得用户信息就是登录。

     8. 微信登录怎么做?
        微信开发者平台地址: https://open.weixin.qq.com/
        微信登录流程文档地址: https://developers.weixin.qq.com/doc/oplatform/Website_App/WeChat_Login/Wechat_Login.html
        1. 注册: 分为3步(填写基本信息 -> 邮箱激活 -> 完善开发者资料)
           注册之后会给你俩个值 1微信id 2微信密钥
        2. 需要在微信开发者平台做一个资质认证(只支持企业注册认证才能做到功能，个人注册的无法实现功能)
           准备营销执照 1-2天工作日审批 300元
        3. 申请网站应用名称(就是手机扫描二维码 显示的目标网站名称)
           提交审核 1星期左右
        4. 需要一个域名地址(微信扫完二维码之后会跳转到指定的域名地址)
        5. 熟悉一下微信登录流程

        根据微信官网说道:
            1. /login: 请求一个微信提供的网址并传递相关参数(url,state,code...) 会给你返回一个二维码

            2. 扫描二维码后自动访问一个地址 就是上一步中你配置的url参数(/callback)
               /callback方法接收微信登录用户给你传过来的登录信息(state,code)

            3. 拿到code参数请求微信提供的网址 返回access_token 和 openid

            4. 拿着access_token和openid请求微信提供的网址 返回微信扫码人信息

            5. 判断如果数据库没有信息就添加 如果有就不添加

            6. 最后把用户信息封装成token字符串发送给前端

     9. 视频播放SDK:
        配置教程地址:https://help.aliyun.com/document_detail/125570.html?spm=a2c4g.11186623.6.1177.4fdf7d44OpXhRT
        更多配置地址:  广告、弹幕。。。
        https://player.alicdn.com/aliplayer/presentation/index.html?type=videoAd

        其实就是
        1.准备一个html盒子 div...
        2.引入阿里云视频的js 和 css
        3.调用js封装好的对象即可

     10. 后端中的定时任务(定时器): 不需要安装jar包
         1. 主启动加注解 @EnableScheduling
         2. 写一个配置类 里面进行定时期得开启操作
         了解:
             定时器的时间设置: cron表达式 也称为 七域表达式(年/月/日/周/时/分/秒)
             这个表达式在springboot中只支持6域 因为默认年就是当前年
             cron表达式生成地址: https://www.pppet.net
