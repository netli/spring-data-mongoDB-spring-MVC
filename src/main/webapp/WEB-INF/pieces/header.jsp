<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>

<div id="header">
<my:logo resourceUrl="${resourceUrl}"></my:logo>


<div id="headerButtons">
	<button id="btnLogout">Logout</button>
	<sec:authorize url="/parents/settings">
		<button id="btnSettings">Settings</button>
	</sec:authorize>
</div>

</div>

<div id="dlgParentsSettings" class="dialog">
	<form id="frmParentsSettings">
		<table>
			<my:formRow label="Your nickname" name="nickname" sublabel="According to your kids" formId="frmParentsSettings"/>
			<my:formRowCustom label="Country" sublabel="For currency rendering" inputId="frmParentsSettings-country">
				<select id="frmParentsSettings-localeCode" name="localeCode"></select>
			</my:formRowCustom>
		</table>
	</form>
</div>