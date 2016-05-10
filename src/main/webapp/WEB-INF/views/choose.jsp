<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>

<%@ page session="true"%>
<html>
<head>
<%@ include file="/WEB-INF/pieces/common-head.jsp" %>
</head>
<body>
<my:logo resourceUrl="${resourceUrl}"></my:logo>


<div id="content">

<p>
<span class="yellowish">k</span>ids<span class="yellowish">b</span>ank is an online, virtual bank to keep up with money, allowance, and where it has gone.
</p>

<p>
This service is free, involves no real money, and is secured by your choice of third-party account provider, such as Google.
</p>

<div id="choose">
<div>Are you a</div>
<div>
<a href="go?to=parent" id="parentLink">Parent</a> or <a href="go?to=kid" id="kidLink">Kid</a> ?
</div>
</div>

</div>

	<div id="featuresForParents" class="initiallyHidden">
	<h3>Features for Parents</h3>
	<ul>
		<li></li>
	</ul>
	</div>

<%@ include file="/WEB-INF/pieces/footer.jsp" %>

</body>
</html>
