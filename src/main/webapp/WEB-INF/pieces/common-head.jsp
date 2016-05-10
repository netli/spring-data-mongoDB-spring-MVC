<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<title>kidsbank - kids' allowance for the modern world</title>

<spring:url value="/static" var="resourceUrl" />
<spring:url value="/static/js/jquery" var="jqueryJsBase" />

<sec:authorize access="hasRole('parent')">
	<spring:url value="/parents/logout" var="logoutUrl" />
</sec:authorize>
<sec:authorize access="hasRole('kid')">
	<spring:url value="/kids/logout" var="logoutUrl" />
</sec:authorize>

<spring:eval expression="${spring-ui-version}" var="springUiVersion"></spring:eval>

<!-- myBase*Url are provided by CommonInterceptor -->

<meta property="og:description" content="kidsbank is a free, online application to help you and your kids keep track of their allowance and spending money"/>
<meta property="og:image" content="${myBaseAbsUrl}/static/img/preview.png"/>
<link rel="image_src" href="${myBaseAbsUrl}/static/img/preview.png" />

<script type="text/javascript">
	config = {
		baseurl : "${myBaseUrl}",
		resourceurl : "${resourceUrl}",
		logoutUrl : "${logoutUrl}",
		
		getBaseUrl : function() {
			return this.baseurl;
		},
		
		buildUrl : function(subpath) {
			return this.getBaseUrl() + subpath;
		}
	}
</script>
<link href="${resourceUrl}/css/jquery/ui-lightness/jquery-ui-1.9.2.custom.css" rel="Stylesheet" type="text/css">
<link href="${resourceUrl}/css/jquery/jquery.qtip.css" rel="Stylesheet" type="text/css">
<link href="${resourceUrl}/css/kidsbank.css" rel="Stylesheet" type="text/css">


<script src="${jqueryJsBase}/jquery-1.8.3.min.js"></script>
<script src="${jqueryJsBase}/jquery-ui-1.9.2.custom.min.js"></script>
<script src="${jqueryJsBase}/jquery.validate.js"></script>
<script src="${jqueryJsBase}/jquery.qtip.js"></script>
<script src="${jqueryJsBase}/jquery.form.js"></script>
<script src="${resourceUrl}/js/BorisMoore-jsrender-fb077a3/jsrender.js"></script>
<script type="text/javascript" src="${resourceUrl}/js/common.js"></script>

<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-34045209-1']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
