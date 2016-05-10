<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"  %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ include file="/WEB-INF/pieces/common-head.jsp" %>

</head>
<body>
<%@ include file="/WEB-INF/pieces/header.jsp" %>

<div id="content">

I don't know this computer. Tell me more. Here is a test value ${testAttr}. I think your name is ${kidRegistration.name}.

<form id="frmReg" method="post">
	<input type="hidden" name="ready" value="true"/>
	<c:if test="${not empty errorCode}">
		<div class="error">
			<spring:message code="${errorCode}"></spring:message>
		</div>
	</c:if>
	<table>
		<my:formRow label="Your name" name="name" formId="frmReg" value="${kidRegistration.name}"></my:formRow>
	</table>
	<button type="submit">Continue</button>
</form>

</div>
</body>
</html>