<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html >
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ page isErrorPage= "false" %>
<html>
<head>
<meta  charset="UTF-8">
<script type="text/javascript">
function openNav() {
	  document.getElementById("mySidenav").style.width = "150px";
	  document.getElementById("menuB").style.visibility ="hidden";
	 /*  document.getElementById("AllBody").style.marginLeft ="20px"; */
	 
	}
function closeNav() {
	  document.getElementById("mySidenav").style.width = "0";
	  document.getElementById("menuB").style.visibility ="visible";
	  /* document.getElementById("AllBody").style.marginLeft ="-20px"; */
	} 


</script>
<style type="text/css">
 body {
	background-image: url("resources/img/background.jpg");
background-size: cover;
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-position: center;
} 
</style>
</head>
<body class="img-fluid">

<div class='container-fluid'>

<div style="margin-top: 130px; " align="center"><font size="10"   ><span style="color:orange;"><s:message code="label.welcomeors" /></span></font></div>
</div>
</body>
</html>