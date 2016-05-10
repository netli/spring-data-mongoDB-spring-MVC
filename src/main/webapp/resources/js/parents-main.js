$(document).ready(function(){
	wireActions();
	
	initDialogs();
	
	loadData();
});

var dlgEditAccount = {
	dlg: null,
	selectedAccount: null,
	allowanceValidator: null,
	allowanceChanged: false,
	allowanceForm: null,
	editMode: true,

	init: function() {
		var outer = this;
		function submitNewAccount(form) {
			outer.close();
			
			$.ajax({
				type: 'POST',
				url: config.buildUrl("/parents/accounts/add"),
				data: $(form).serialize(),
				success: function(data) {
					loadData();
				},
				error: function(jqXHR) {
					if (jqXHR.status == 409/*conflict*/) {
						$('#dlgDupAccount').dialog({
							title: "Duplicate Account",
							modal: true,
							buttons: [{ 
								text: "OK",
								click: function() {
									$(this).dialog("close");
								}
							}]
						});
					}
				}
			});
		}
		
		function prepareFieldsForEditing() {
			$("#dlgEditAccount input[name='name']")[0].value = outer.selectedAccount.name;
			$.get(config.buildUrl("/parents/accounts/"+outer.selectedAccount.id+"/currencySymbol"), function(data) {
				$("#fldCurrencySymbol").html(data);
			});
			$.get(config.buildUrl("/parents/accounts/"+outer.selectedAccount.id+"/allowance"), function(data) {
				if (data) {
					$("#day-"+data.dayOfWeek)[0].checked = true;
					$("#inAllowanceAmount")[0].value = data.amount;
					$("#inAllowanceAmount")[0].disabled = false;
					$("#inAllowanceDesc")[0].value = data.description;
					$("#inAllowanceDesc")[0].disabled = false;
				}
				// else defaults are just right
			});
		}
		
		function handleOpen(event, ui) {
			outer.allowanceChanged = false;
			
			$("#inEditName").val("");
			$("#fldCurrencySymbol").html("");
			$("#day-0")[0].checked = true;
			$("#inAllowanceAmount").val("");
			$("#inAllowanceAmount")[0].disabled = true;
			$("#inAllowanceDesc").val("Allowance");
			$("#inAllowanceDesc")[0].disabled = true;
			
			if (outer.editMode) {
				prepareFieldsForEditing();
			}
			
			if (outer.editMode) {
				outer.dlg.dialog("option", "title", "Edit Account");
				outer.dlg.dialog("option", {buttons: {
					"OK": function() {
						var valid = true;
						if (outer.allowanceChanged) {
							if (!outer.allowanceValidator.form()) {
								valid = false;
							}
							else {
								outer.submitAllowance();
							}
						}
						
						if (valid) {
							$(this).dialog("close");
						}
					},
					"Delete": function() {
						outer.detachAccount();
					}
			
				}});
			}
			else {
				outer.dlg.dialog("option", "title", "Add Account");
				outer.dlg.dialog("option", {buttons: {
					"OK": function() {
						$("#frmEditAccount").submit();
					},
					"Cancel": function() {
						outer.close();
					}
				}});
			}

		}
		
			$("#inEditName").change(function(evt) {
				if (outer.editMode) {
					submitChangedName(dlgEditAccount.selectedAccount.id, evt.target.value);
					evt.preventDefault = true;
				}
			});
		
		$("#frmEditAccount input[name='day']").change(function(){
			$("#inAllowanceAmount")[0].disabled = $("#day-0")[0].checked;
			$("#inAllowanceDesc")[0].disabled = $("#day-0")[0].checked;
			if (!$("#day-0")[0].checked) {
				$("#inAllowanceAmount").focus();
			}
			dlgEditAccount.allowanceChanged = true;
		});
		
		$("#inAllowanceAmount").change(function(){
			dlgEditAccount.allowanceChanged = true;
		});
		
		this.allowanceForm = $("#frmEditAccount");
		this.allowanceValidator = this.allowanceForm.validate({
			rules: {
				name: { required: true },
				amount: { number: true, required: "#day-0:unchecked" },
				description: { required: "#day-0:unchecked" }
			},
			submitHandler: submitNewAccount
		});

		this.dlg = $("#dlgEditAccount").dialog({
			autoOpen: false,
			modal: true,
			width: 440,
			height: 470,
			open: handleOpen
		});

	},
	
	submitAllowance: function() {
		$.ajax({
			type: 'POST',
			url: config.buildUrl("/parents/accounts/"+dlgEditAccount.selectedAccount.id+"/configureAllowance"),
			data: dlgEditAccount.allowanceForm.serialize()
		});
	},
	
	detachAccount: function() {
		$("#dlgConfirm").dialog({
			modal: true,
			title: "Confirm",
			height: 220,
			buttons: {
				"Yes": function() {
					$(this).dialog("close");
					sendDetach(dlgEditAccount.selectedAccount.id);
					dlgEditAccount.close();
				},
				"No": function() {
					$(this).dialog("close");
				}
			}
		});
	},

	open: function(account) {
		this.editMode = true;
		this.selectedAccount = account;
		this.dlg.dialog('open');
	},
	
	openNew: function() {
		this.editMode = false;
		this.dlg.dialog('open');
	},
	
	close: function() {
		this.dlg.dialog('close');
	}
};

var dlgKidLink = {
	dlg: null,
	
	init: function() {
		this.dlg = $("#dlgKidLink").dialog({
			modal: true,
			autoOpen: false,
			title: "Share Accounts",
			width: 420,
			buttons: {
				"OK": function() {
					$(this).dialog("close");
				}
			}
		});
	},
	
	open: function(kidLinkShare) {
		var link = $("#kidLinkUrl");
		link.html(kidLinkShare.joinUrl);
		link.attr("href", kidLinkShare.joinUrl);
		
		$("#kidLinkCode").html(kidLinkShare.code);
		
		this.dlg.dialog("open");
	}
};

var dlgUseKidLinkCode = {
	dlg: null,
	
	init: function() {
		var outer = this;
		
		function submitCode(form) {
			$.ajax({
				type: 'GET',
				url: config.buildUrl("/parents/accounts/join/"+$("#inKidLinkCode").val()),
				success: function(data) {
					outer.close();
					loadData();
				}
			});
		}
		
		function handleOK() {
			$("#frmUseKidLink").submit();
		}
		
		this.dlg = $("#dlgUseKidLink").dialog({
			modal: true,
			autoOpen: false,
			title: "Use Kid Link Code",
			
			buttons: {
				"OK": handleOK,
				"Cancel": function() {
					$(this).dialog("close");
				}
			},
			
			open: function(event, ui) {
				$("#inKidLinkCode").val("");
			}
		});
		
		$("#frmUseKidLink").validate({
			rules: {
				code: {
					required: true,
					minlength: 4,
					maxlength: 4,
					number: true
				}
			},
			submitHandler: submitCode
		});
	},
	
	open: function() {
		this.dlg.dialog("open");
	},
	
	close: function() {
		this.dlg.dialog("close");
	}
};

function initDialogs() {
	dlgEditAccount.init();
	dlgKidLink.init();
	dlgUseKidLinkCode.init();
}

function wireActions() {
	$("#btnAddAccount").click(function(){
		dlgEditAccount.openNew();
	});
	
	$("#btnShare").click(function() {
		createShareAll();
	});
	
	$("#btnUseCode").click(function() {
		dlgUseKidLinkCode.open();
	});
	
	$("#kidLinkUrl").click(function(evt) {
		evt.preventDefault();
	});
}

function createShareAll() {
	$.ajax({
		type: 'GET',
		url: config.buildUrl("/parents/accounts/share"),
		success: function(/*KidLinkShare*/ kidLinkShare) {
			dlgKidLink.open(kidLinkShare);
		}
	});
}

function processAccountsData(data) {
	$("#accountsLoading").hide();
	var accounts = $("#accounts");
	accounts.empty();
	
	if (data.length == 0) {
		$("#noneYet").show();
		accounts.hide();
		return;
	}
	else {
		$("#noneYet").hide();
		$("#accounts").show();
		$.each(data, function(i, acct){
			createAccountWidget(accounts, acct);
		});
	}
}

function createAccountWidget(accountsWidget, acct) {
	var widget = $($("#accountWidgetTemplate").render(acct));
	widget.data().acct = acct;
	widget.click(function(){
		widget.find(".actions").show();
	});
	widget.hover(function(){
		//in
		widget.find(".actions").show();
	},
	function() {
		//out
		widget.find(".actions").hide();
	});
	widget.find("button").button();
	widget.find(".btnTransactions").click(function(){
		window.location = config.buildUrl("/parents/transactions/"+acct.id);
	});
	widget.find(".btnEdit").click(function(){
		doEditAccount(acct);
	});
	$("#accounts").append(widget);
}

function doEditAccount(account) {
	dlgEditAccount.open(account);
}

function sendDetach(accountId) {
	$.ajax({
		type: 'POST',
		url: config.buildUrl("/parents/accounts/"+accountId+"/detach"),
		success: function(data) {
			loadData();
		}
	});
}

function submitChangedName(accountId, value) {
	$.ajax({
		type: 'POST',
		url: config.buildUrl("/parents/accounts/"+accountId+"/edit"),
		data: { name: value },
		success: function(data) {
			loadData();
		}
	});
}

function loadData() {
	$.ajax({
		type: 'GET',
		url: config.buildUrl("/parents/accounts"),
		success: function(data) {
			processAccountsData(data);
		}
	});
}