$(document).ready(function(){
	$("#transactionsArea").hide();
	
	wireActions();
	$("input.datePicker").datepicker({
		dateFormat: "M d, yy",
		onSelect: function() {
			// auto focus on description next
			$("#editor-inDesc").focus();
		}
	});
	
	loadTransactions();
});

var txData = {
		disableRowHover: false,
		insideForm: false
};

var transactionEditor = {
		txSummaryObj: null,
		editMode: true,
		
		init: function() {
			var outer = this;
			
			
			function handleSave() {
				$("#frmEditTx").submit();
			}

			function handleDelete() {
				$.ajax({
					type: 'POST',
					url: config.buildUrl("/parents/transactions/"+config.accountId+"/"+outer.txSummaryObj.id+"/delete"),
					success: function(data) {
						loadTransactions();
						outer.close();
					}
				});
			}
			
			
			$("#editor-btnCancel").click(function() {
				outer.close();
			});

			$("#editor-btnSave").click(function() {
				handleSave();
			});
			
			$("#editor-btnDelete").click(function() {
				handleDelete();
			});
			
			$("#transactionEditor").keydown(function(evt){
				if (evt.which == 27/*esc*/) {
					outer.close();
				}
			});
			
			$("#editor-inExpense").keydown(function(evt){
				if (evt.which == 13) {
					handleSave();
				}
			});
			$("#editor-inIncome").keydown(function(evt){
				if (evt.which == 13) {
					handleSave();
				}
			});

			function submitDone(data) {
				loadTransactions();
				outer.close();
			}
			
			mutuallyExclusiveTextFields("#editor-inExpense", "#editor-inIncome");
			
			$("#editor-inDesc").autocomplete({
				minLength: 1,
				source: config.buildUrl("/parents/transactions/"+config.accountId+"/suggestDescriptions")
			});

			$("#frmEditTx").validate({
				// combine this validation rule with new transaction when refactoring the two into each other
				rules: {
					date: "required",
					description: "required",
					income: {required: "#editor-inExpense:blank", number:true},
					expense: {required: "#editor-inIncome:blank", number:true}
				},
				submitHandler: function(form){
					if (outer.editMode) {
						$.ajax({
							type: 'POST',
							url: config.buildUrl("/parents/transactions/"+config.accountId+"/"+outer.txSummaryObj.id+"/edit"),
							data: $(form).serialize(),
							success: submitDone
						});
					}
					else {
						$.ajax({
							type: 'POST',
							url: config.buildUrl("/parents/transactions/"+config.accountId+"/add"),
							data: $(form).serialize(),
							success: submitDone
						});
						
					}
				}
			});
		},
		
		close: function() {
			$("#transactionEditor").css("display", "");
			// In order to properly recycle the show() and position() it was necessary
			// to clear the top and left styles applied by position()
			$("#transactionEditor").css("top", "");
			$("#transactionEditor").css("left", "");
			txData.disableRowHover = false;
			$("#btnNewTx").button("enable");
			txData.insideForm = false;
			this.txSummaryObj = null;
		},
		
		edit: function(jRow, txSummaryObj) {
			this._commonOpen(true);
			
			this.txSummaryObj = txSummaryObj;
			$("#editor-inDate").val(txSummaryObj.when);
			$("#editor-inDesc").val(txSummaryObj.description);
			if (txSummaryObj.amountValue > 0) {
				$("#editor-inIncome").val(txSummaryObj.amountValue);
				$("#editor-inExpense").val("");
			}
			else {
				$("#editor-inIncome").val("");
				$("#editor-inExpense").val(-txSummaryObj.amountValue);
			}
			
			$("#transactionEditor").position({
				my: "left top",
				at: "left top",
				of: jRow,
				offset: "-7 0",
				collision: "none"
			});
			$("#transactionEditor").show("puff", "fast");
		},
		
		_commonOpen: function(editMode) {
			this.editMode = editMode;
			if (editMode) {
				$("#editor-btnDelete").show();
			}
			else {
				$("#editor-btnDelete").hide();
			}
			$("#btnNewTx").button("disable");
			txData.insideForm = true;
		},
		
		openForNew: function() {
			this._commonOpen(false);
			
			$("#editor-inDate").val("");
			$("#editor-inDesc").val("");
			$("#editor-inIncome").val("");
			$("#editor-inExpense").val("");
			
			$("#transactionEditor").position({
				my: "left top",
				at: "left bottom",
				of: "#mainHeadingRow",
				offset: "-7 0",
				collision: "none"
			});

			$("#transactionEditor").show("puff", "fast", function() {
				$("#editor-inDate").focus();
			});
		}
};

function wireActions() {
	$("#btnNewTx").click(function(){
		doNewTransaction();
	});
	
	$(document).keypress(function(evt){
		if (!txData.insideForm && evt.which == 110/*n*/) {
			doNewTransaction();
			evt.preventDefault();
		}
	});
	
	mutuallyExclusiveTextFields("#inIncome", "#inExpense");
	
	$("#btnNext").click(function(){
		++config.currentPage;
		loadTransactions();
	});
	
	$("#btnPrev").click(function(){
		if (config.currentPage > 0) {
			--config.currentPage;
			loadTransactions();
		}
	});
	
	transactionEditor.init();
}

function doNewTransaction() {
	transactionEditor.openForNew();
}

//TODO put in utils
function mutuallyExclusiveTextFields(one, two) {
	excludeTheOtherTextField(one,two);
	excludeTheOtherTextField(two,one);
}

//TODO put in utils
function excludeTheOtherTextField(mine, other) {
	$(mine).keypress(function(evtObject){
		$(other).val("");
	});
}

function loadBalance() {
	$.ajax({
		type: 'GET',
		url: config.buildUrl("/parents/accounts/"+config.accountId+"/balance"),
		success: function(/*double*/ data) {
			$("#fldBalance").html(data);
		}
	});

}

function loadTransactions() {
	$.ajax({
		type: 'GET',
		url: config.buildUrl("/parents/transactions/"+config.accountId+"/summaries"),
		data: {
			"page.page": config.currentPage+1,
			"page.size": config.pageSize
		},
		success: function(/*TransactionSummaryPage*/ data) {
			refreshTransactionList(data);
		}
	});

	loadBalance();
}

function refreshTransactionList(/*TransactionSummaryPage*/ data) {
	$("#transactions").empty();
	
	if (data.totalPages > 0) {
		var content = data.content;
		$("#transactions").html("<tbody>"+$("#transactionTemplate").render(data.content)+"</tbody>");
		$("#transactions > tbody > tr:even").addClass("even");
		$("#transactions button").button();
		
		$("#currentPage").html(data.currentPage+1);
		$("#totalPages").html(data.totalPages);
		$("#transactionsAreaStatus").hide();
		$("#transactionsArea").show();
		
		$("#btnPrev").toggle(data.currentPage > 0);
		$("#btnNext").toggle(data.totalPages > 1 && data.currentPage < data.totalPages-1);
		
		function toggleActions(row, hide) {
			row.find("td.actions button").toggleClass("hidden", hide);
		}
		
		$("#transactions > tbody > tr").hover(
				function() /*in*/ {
					if (!txData.disableRowHover) {
						toggleActions($(this), false);
					}
				},
				function() /*out*/ {
					if (!txData.disableRowHover) {
						toggleActions($(this), true);
					}
				});
		$("#transactions > tbody > tr").click(function() {
			toggleActions($(this), false);
		});
		
		$("#transactions button.edit").click(function(evt) {
			evt.stopPropagation(); // otherwise the tr click picks this up too
			
			txData.disableRowHover = true;
			var jRow = $(this).closest("tr");
			toggleActions(jRow, true);
			var index = jRow.attr("id").substr(4);
			var txSummaryObj = content[index];
			
			transactionEditor.edit(jRow, txSummaryObj);
		});
	}
	else {
		$("#transactionsArea").hide();
		$("#transactionsAreaStatus").html("No transactions yet");
		$("#transactionsAreaStatus").show();
		
	}
}