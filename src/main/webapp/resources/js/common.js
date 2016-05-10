$(document).ready(function() {
    $("button").button();
    $(".imageButton").button();

    $(".submitter").click(function(evt) {
        var formToSubmit = $(evt.target).parents("form").first();
        formToSubmit.submit();
    });
    
    $("#btnLogout").click(function() {
    	window.location = config.logoutUrl;
    });
    
    if ($("#btnSettings")) {
    	$("#btnSettings").click(function() {
    		dlgParentsSettings.open();
    	});
    	
    	dlgParentsSettings.init();
    }
});

var dlgParentsSettings = {
	dlg: null,
	
	init: function() {
		var outer = this;
		
		function handleOpen() {
			
			
			if ($("#frmParentsSettings-localeCode option").length == 0) {
				$.get(config.buildUrl("/api/countries"), function(data){
					var options = "";
					$.each(data, function(i,c){
						options += "<option ";
						if (c.userLocale) {
							options += "selected='true' ";
						}
						options += "value='"+c.locale+"'>";
						options += c.displayCountry+"</option>";
					});
					$("#frmParentsSettings-localeCode").html(options);
				}, "json");
			}
			
			$("#frmParentsSettings-nickname")[0].disabled = true;
			$.get(config.buildUrl("/parents/settings"), function(data){
				$("#frmParentsSettings-nickname").val(data.nickname);
				$("#frmParentsSettings-nickname")[0].disabled = false;
			});
		}
		
		function handleSubmit() {
			$.post(config.buildUrl("/parents/settings"), $("#frmParentsSettings").serialize(), 
					function(response) {
						outer.close();
						window.location.reload();
					});
		}
		
		$("#frmParentsSettings").submit(function(evt) {
			evt.preventDefault();
			handleSubmit();
		});
		
		this.dlg = $("#dlgParentsSettings").dialog({
			modal: true,
			autoOpen: false,
			title: "Settings",
			width: 440,
			height: 275,
			position: ["right", "top"],
			open: handleOpen,
			buttons: {
				OK: function() {
					handleSubmit();
				},
				Cancel: function() {
					outer.close();
				}
			}
		});
	},
	
	open: function() {
		this.dlg.dialog("open");
	},
	
	close: function() {
		this.dlg.dialog("close");
	}
};