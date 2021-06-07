<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>系统异常--mq_admin</title>
	<script type="text/javascript">
		var url = '${pageContext.request.contextPath}';
		setTimeout("toSearchIndex()", 5000);

		function  toSearchIndex(){
			window.location.href = url+"";
		}
	</script>
</head>
<body>
	<div style="text-align: center;color: red;">
		你访问的页面不存在,5秒后跳转至数据维护页....
	</div>
</body>
</html>