<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>系统异常--mq_admin</title>
</head>
<body style="background-color: white;">
<div style="text-align: center; color:#0099ff;">
    错误描述:${errorMsg}<br/>
    请联系管理员！
</div>

<div style="background-color: white; color: white;">
    <c:forEach items="${errorResult}" var="error">
        ${error}<br/>
    </c:forEach>
</div>
</body>
</html>