<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>
<html>
<head>
<%@ include file="/WEB-INF/pieces/common-head.jsp" %>
<link href="${resourceUrl}/css/parents-tx.css" rel="Stylesheet" type="text/css">


<script type="text/javascript">
// Page specific data and config
config.accountId = "${accountId}";
config.pageSize = 10;
config.currentPage = 0;
config.shortcutsDisabled = false;
</script>

<script src="${resourceUrl}/js/parents-tx.js"></script>

</head>
<body>

<%@ include file="/WEB-INF/pieces/header.jsp" %>

<div id="content">

<h2>Transactions for <span>${accountName}</span></h2>

<div>
<a href="${myBaseUrl}/parents/">Back to accounts</a>
</div>

<h3>Balance : <span id="fldBalance"></span></h3>

<div>
	<button id="btnNewTx">New Transaction</button>
</div>

<table id="mainHeading">
<tr id="mainHeadingRow"><th class="date">Date</th><th class="description">Description</th><th class="amount">Amount</th></tr>
</table>

<script id="transactionTemplate" type="text/x-jsrender">
<tr id="row-{{:#index}}">
	<td class="date">{{>when}}</td>
	<td class="description">{{>description}}</td>
	<td class="amount">{{>amount}}</td>
	<td class="actions"><button class="edit hidden">Edit</button></td>
</tr>
</script>


<div id="transactionsArea">
<table id="transactions"></table>
<div id="pagingInfo">
	<button id="btnPrev">Previous</button>
	Page <span id="currentPage">0</span> of <span id="totalPages">0</span>
	<button id="btnNext">Next</button>
</div>
</div>
<div id="transactionsAreaStatus">Loading...</div>


</div>

<div id="transactionEditor" class="initiallyHidden">
	<form id="frmEditTx">
		<table>
			<tr>
				<th/>
				<th/>
				<th class="income">Income</th>
				<th class="expense">Expense</th>
			</tr>
			<tr><td class="date"><input id="editor-inDate" name="date" class="datePicker"/></td>
			<td class="description"><input id="editor-inDesc" name="description"/></td>
			<td class="editAmount"><span class="currencySymbol">${currencySymbol}</span><input id="editor-inIncome" name="income" size="6"/></td>
			<td class="editAmount"><span class="currencySymbol">${currencySymbol}</span><input id="editor-inExpense" name="expense" size="6"/>
		</table>
	</form>
	<div id="transactionEditor-actions">
		<button id="editor-btnSave">Save</button>
		<button id="editor-btnCancel">Cancel</button>
		<button id="editor-btnDelete">Delete</button>
	</div>
</div>

<%@ include file="/WEB-INF/pieces/footer.jsp" %>

</body>
</html>
