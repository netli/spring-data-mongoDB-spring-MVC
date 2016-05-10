<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ include file="/WEB-INF/pieces/common-head.jsp" %>

<link href="${resourceUrl}/openid-selector/css/openid.css" rel="Stylesheet" type="text/css">
<script type="text/javascript" src="${resourceUrl}/openid-selector/js/openid-jquery.js">
</script>
<script src="${resourceUrl}/openid-selector/js/openid-en.js" type="text/javascript">
</script>


	<script type="text/javascript">
		$(document).ready(function() {
			openid.img_path = "${resourceUrl}/openid-selector/images/";
			openid.init('openid_identifier');
		});
	
</script>


</head>
<body>
<my:logo resourceUrl="${resourceUrl}"></my:logo>

<div id="content">

<p>
Parents have full access to manage accounts and apply transactions. Access to this area is secured by your choice of Open ID provider. 
</p>
<p>
You will be momentarily redirected to the chosen provider and then returned here. 
</p>

	<spring:url value="/parents/login_check"
		var="form_url_openid" />

	<form action="${form_url_openid}" method="post" id="openid_form">
		<input type="hidden" name="action" value="verify" />
			<div id="openid_choice">
				<div id="openid_btns"></div>
			</div>
			<noscript>
				<p>OpenID is service that allows you to log-on to many different websites using a single indentity.
				Find out <a href="http://openid.net/what/">more about OpenID</a> and <a href="http://openid.net/get/">how to get an OpenID enabled account</a>.</p>
			</noscript>
	</form>

</div>

<%@ include file="/WEB-INF/pieces/footer.jsp" %>

</body>
</html>