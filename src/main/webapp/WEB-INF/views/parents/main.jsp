<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>

<%@ page session="true"%>
<html>
<head>
<%@ include file="/WEB-INF/pieces/common-head.jsp" %>
<script type="text/javascript" src="${resourceUrl}/js/parents-main.js"></script>


</head>
<body>

<%@ include file="/WEB-INF/pieces/header.jsp" %>

<div id="content">
<h2>Managed Accounts</h2>

<div id="accountsArea">
<div id="accountsLoading">Loading...</div>
<div id="noneYet" class="initiallyHidden">No accounts yet</div>

<script id="accountWidgetTemplate" type="text/x-jsrender">
	<div class="accountWidget ui-corner-all">
		<div class="info">
			<div class="name">{{>name}}</div>
			<div class="balance">{{>balance}}</div>
			<div class="moreInfo initiallyHidden">X transactions, locked?, loan balance?</div>
		</div>
		<div class="actions">
			<div><button class="btnTransactions">Transactions</button></div>
			<div><button class="btnEdit">Edit</button></div>
		</div>
	</div>
</script>

<div id="accounts" class="initiallyHidden"></div>
</div>

<div id="allAccountActions">
<button id="btnAddAccount">Add Account</button>
<button id="btnShare">Share Accounts</button>
<button id="btnUseCode">Use Kid Link Code</button>
</div>

</div>

<%@ include file="/WEB-INF/pieces/footer.jsp" %>

<div id="dialogs">

	<div id="dlgEditAccount">
		<form id="frmEditAccount">
			<table>
				<my:formRow label="Name" name="name" formId="frmEditAccount" inputId="inEditName"/>
				<my:formRowCustom inputId="day-0" label="Allowance">
						<div><input type="radio" name="day" value="0" id="day-0" checked="checked"/><label for="day-0">Off</label></div>
						<!-- align values on DAY_OF_WEEK values in Calendar -->
						<div><input type="radio" name="day" value="7" id="day-7"/><label for="day-7">Saturday</label></div>
						<div><input type="radio" name="day" value="1" id="day-1"/><label for="day-1">Sunday</label></div>
						<div><input type="radio" name="day" value="2" id="day-2"/><label for="day-2">Monday</label></div>
						<div><input type="radio" name="day" value="3" id="day-3"/><label for="day-3">Tuesday</label></div>
						<div><input type="radio" name="day" value="4" id="day-4"/><label for="day-4">Wednesday</label></div>
						<div><input type="radio" name="day" value="5" id="day-5"/><label for="day-5">Thursday</label></div>
						<div><input type="radio" name="day" value="6" id="day-6"/><label for="day-6">Friday</label></div>
						<table>
							<tr>
							<td><label for="inAllowanceDesc">Description</label></td>
							<td><!-- currency placeholder --></td>
							<td><input id="inAllowanceDesc" name="description" value="Allowance" disabled="disabled"/></td>
							</tr>
							<tr>
							<td><label for="inAllowanceAmount">Amount</label></td>
							<td><span id="fldCurrencySymbol"></span></td>
							<td><input id="inAllowanceAmount" name="amount" disabled="disabled"></input></td>
							</tr>
						</table>
						<div>
						</div>
				</my:formRowCustom>
			</table>
		</form>
	</div>
	
	<div id="dlgKidLink">
		<div>Provide this URL for sharing</div>
		<div><a href="" id="kidLinkUrl"></a></div>
		<div>or ask them to use this Kid Link Code</div>
		<div id="kidLinkCode"></div>
		<div id="kidLinkDisclaimer">Please note that sharers have full access to these accounts including transactions.</div>
	</div>
	
	<div id="dlgUseKidLink">
		<form id="frmUseKidLink">
			<div><label for="inKidLinkCode">Kid Link Code</label></div>
			<div><input name="code" id="inKidLinkCode" size="4"></input></div>
		</form>
	</div>
	
	<div id="dlgDupAccount">
	   You already have an account with the same name. The new one has been
	   ignored.
	</div>
</div>

	
	<div id="dlgConfirm" class="dialog">
		Are you sure?
	</div>


</body>
</html>
