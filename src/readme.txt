1 关于登录
    验证账号和密码
    User user = Select * from tbl_user where LoginAct=？and LoginPwd=？
    user对象为null，说明不存在此用户，返回一个错误信息页
    如果对象不为null，说明账号密码正确
    继续get  1.expireTime 失效时间 若超过此时间，无法成功登录，返回账户失效信息
            2.lockState  锁定状态 经过以上验证之后，若锁定状态为0，无法成功登录，返回账户被锁定信息
            3.allowIps   检查登陆设备的ip，验证该字段是否在allowIps字段中存在，若存在则成功登录，若失败则返回未被允许的ip信息

    Ajax模板
    $.ajax({
    		url:"",
    		data:"",
    		type:"",
    		dataType:"json",
    		success:function (data) {
    	    	}
    		})
2 将前端原型中的html页面改为jsp页面需要添加basePath
    basePath模板
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%
    	String basePath = request.getScheme()+"://" +request.getServerName()+
    			":"+request.getServerPort()+request.getContextPath()+"/";
    %>
    <base href="<%=basePath%>">  //写在head第一行中

3 SSM整合

    实现步骤
    1.新建maven项目
    2.加入依赖
      spring，springMVC，Mybatis 三个框架的依赖，servlet，jsp，druid连接池，mybatis驱动，jackson依赖
    3.web.xml
      1）、注册DispatcherServlet对象 目的1.创建springmvc容器对象，才能创建并放置Controller对象
                                   目的2.创建Servlet，这样才能接收用户请求
      2）、注册spring的监听器，ContextLoaderListener 目的：创建Spring容器对象
      3）、注册字符集过滤器，解决Post请求乱码问题
    4.建包，Service包，Controller包，dao包，实体类
    5.写配置文件
      springMVC配置文件
      spring配置文件
      mybatis配置文件
      数据库属性配置文件
    6.dao接口与mapper映射文件，service接口及其实现类，Controller类，实体类，
    7.jsp页面

4 问题与解决方法
    复制粘贴一些依赖maven可能会识别不了，将依赖删除后刷新maven，再加入maven依赖，再次刷新，将依赖的jar包重新导入。

5 登录验证
    //验证user是否为空，为空则返回 账号或密码错误，请重新输入
    if (user==null){
        throw new LoginException("账号密码错误，请重新输入");
    }
    //验证失效时间
    String expireTime = user.getExpireTime();
    String currentTime = DateTimeUtil.getSysTime();
    if (expireTime.compareTo(currentTime)<0){
        throw new LoginException("账号已失效，请联系管理员");
    }
    //验证锁定状态
    String lockState = user.getLockState();
    if ("0".equals(lockState)){
        throw new LoginException("账号已锁定，请联系管理员");
    }

    //验证ip
    String allowIps = user.getAllowIps();
    if (!allowIps.contains(ip)){
        throw new LoginException("ip未被允许，请联系管理员");
    }