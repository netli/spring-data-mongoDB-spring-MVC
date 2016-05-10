
<%@ attribute name="inputId" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="sublabel" required="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tr class="form-row">
	<td class="form-label"><label for="${inputId}">${label}:</label>
	<c:if test="${not empty sublabel}"><div class="sub-label">${sublabel}</div></c:if>
	</td>
	<td class="form-field">
		<jsp:doBody/>
	</td>
</tr>
