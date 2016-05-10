
<%@ attribute name="formId" required="true" %>
<%@ attribute name="inputId" required="false" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="sublabel" required="false" %>
<%@ attribute name="value" required="false" %>
<%@ attribute name="type" required="false" description="The input field type. Defaults to 'text'" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${empty type}">
	<c:set var="type" value="text"/>
</c:if>

<c:if test="${empty inputId}">
	<c:set var="inputId" value="${formId}-${name}"/>
</c:if>

<tr class="form-row">
	<td class="form-label"><label for="${inputId}">${label}:</label>
	<c:if test="${not empty sublabel}"><div class="sub-label">${sublabel}</div></c:if>
	
	<td class="form-field"><input id="${inputId}" name="${name}" type="${type}" value="${value}"></input></td>
</tr>
