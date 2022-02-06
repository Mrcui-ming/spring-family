    1.
    MultipartFile file;
    InputStream inputStream = file.getInputStream();    获取instream
    String fileName = file.getOriginalFilename()        获取文件名称

    2.
    mybatis-plus中的方法
    mybatis-plus中的service层中 可使用baseMapper关键字获取dao对象
    baseMapper.selectList()...
    BeanUtils.copyProperties(旧对象,新对象);  把旧对象的get方法 调用新对象的set方法 实现数据复制

    3.
    字符串的方法:
    StringUtils.isEmpty(字符串)===new String(字符串).isEmpty()    判断字符串是否为空
    StringUtils.join(数组,分隔符);   把["1","2","3"] 封装成字符串 "1,2,3"
    字符串凭借的方法:
    String str = "http://localhost:3000?" +
                        "appid=%s" +
                        "&secret=%s" +
                        "&code=%s" +
                        "&grant_type=authorization_code";
    String appendStr = String.format(
                baseAccessTokenUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                ConstantWxUtils.WX_OPEN_APP_SECRET,
                code
    );

    4.
    gson的方法(谷歌)
    Gson gson = new Gson();
    HashMap mapAccessToken = gson.fromJson(字符串, HashMap.class);     json字符串转成json对象
    String  strAccessToekn = gson.toJson(mapAccessToken);             json对象转成json字符串

    5.
    jackson的方法(springboot)
    ObjectMapper mapper=new ObjectMapper();
    Student  stu = mapper.readValue(字符串, Student.class)             json字符串转成json对象
    String json1 = mapper.writeValueAsString(stu);                    json对象转成json字符串

    6.
    fastjson的方法(阿里)
    Staff staff    = JSON.parseObject(字符串, Staff.class);            json字符串转成json对象
    String strJson = JSON.toJSONString(staff);                        json对象转成json字符串

