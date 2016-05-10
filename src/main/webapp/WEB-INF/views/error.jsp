<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>
<html>
<head>
<%@ include file="/WEB-INF/pieces/common-head.jsp" %>
<link href="${resourceurl}/css/parents-tx.css" rel="Stylesheet" type="text/css">

</head>
<body>

<%@ include file="/WEB-INF/pieces/header.jsp" %>

<div id="content">

<h2>Oops...we had a problem</h2>

Usually you can just try again and then it'll work. Trying <a href="${kidsbankBaseUrl}/logout">logging out</a> first.

</div>

<%@ include file="/WEB-INF/pieces/footer.jsp" %>

</body>
</html>
