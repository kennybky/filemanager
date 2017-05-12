$(document).ready(function(){
var dialog;
dialog = $( "#dialog-box" ).dialog({
	autoOpen: false,
    closeOnEscape: false,
    open: function(event, ui) {
        $(".ui-dialog-titlebar-close", ui.dialog | ui).hide();
    },
    modal: true,
    buttons: {
      "Share": share,
      Cancel: function() {
        dialog.dialog( "close" );
      }
    },
    close: function() {
  	  $("#share-form")[ 0 ].reset();
    }
  });
	
	$("#createButton").click(function(){
		$("#uploadHidden").addClass("hidden");
		$("#createHidden").toggleClass("hidden");
		
	});
	
	$("#uploadButton").click(function(){
		$("#createHidden").addClass("hidden");
		$("#uploadHidden").toggleClass("hidden");
	});
	
	$(".edit").click(function() {
		var id = $(this).attr('href');
		$(id).toggleClass("hidden");
	});
	
	function share() {
		var file = $("#dialog-box").data('file');
		var username = $("#dialog-box input:first-of-type").val();
		
		$.ajax({
	           url: "Share",
	           data: {
	              "key" : file,
	              "username" : username
	           },
	           success: function(){
	        	  var message = $("<p> File shared Successfully </p>").dialog({autoOpen:false});
	             message.dialog("open").addClass("dialogStyle");
	           },
	           error: function(xhr, status, error) {
	        	   var message = $("<p>"+ xhr.responseText +"</p>").dialog({autoOpen:false});
	        	   message.dialog("open").addClass("dialogStyle");
	           }
	       });
		dialog.dialog( "close" );
	}
	
	$( ".share" ).click(function(event) {
		event.preventDefault();
	      dialog.data("file", $(this).attr("data-file")).dialog( "open" ).addClass("dialogStyle");
	    });
	
	var form = dialog.find( "form" ).on( "submit", function( event ) {
	      event.preventDefault();
	      share();
	    });
	
	
	$('#createForm').submit(function(event) {
		var fields = $(this).serializeArray();
		var values = {};
		$.each(fields, function(i, field) {
		values[field.name] = field.value;
		});
		var name = values['name'];
		var key = values['key'];
		if (name == null || name == "") {
			alert("You must fill all fields");
		} else{
			$.ajax({
		           url: "CreateFolder",
		           type: 'POST',
		           data: {
		              "key" : key,
		              "name" : name
		           },
		           success: function(data){
		        	   var val = data.split("&");
		        	   var id = val[0];
		        	   var time = val[1];
		        	   $("#shared-row").after("<tr><td><img src='images/folder.ico'/><a href='MyFileManager?key="+id+"'>"+
		        			   name + "</a></td><td>" +  time  + "" +
       		  		"</td><td></td><td><img src='images/folder_edit.ico'/><a href='EditFolder?key="+ id +"'>Rename</a>" +
       		  				"| <img src='images/folder_delete.ico'/><a href='DeleteFolder?key="+ id +"'>Delete</a></td></tr>");
		        	  
		           }
		       });
		}
		
		$("#createHidden").addClass("hidden");
		event.preventDefault();
	});

	$('.delete').click(function(){
		var key = $(this).attr("data-file");
		var row = $(this).closest("tr");
		$.ajax({
			url: "DeleteFolder",
			data: {
				"key" : key,
			},
			success: function() {
				row.remove();
			}
		});
		event.preventDefault();
	});
	
	$('#register').submit(function(event) {
		var fields = $(this).serializeArray();
		var values = {};
		$.each(fields, function(i, field) {
		values[field.name] = field.value;
		});
		var username = values['username'];
		var password = values['password'];
		var fName = values['fName'];
		var lName = values['lName'];
		$.ajax({
			url: "FileUserRegister",
			type : 'POST',
			data: {
				'username' : username,
				'password' : password,
				'fName' : fName,
				'lName' : lName
			},
		success : function() {
			window.location.href = "MyFileManager"
		},
		error: function(xhr, status, error) {
			$('.err_message').text(xhr.responseText);
		}
		});
		event.preventDefault();
	});
	
	$('.DeleteShared').click(function(event){
		var key = $(this).attr("data-file");
		var row = $(this).closest("tr");
		$.ajax({
			url: "DeleteShared",
			data: {
				"key" : key,
			},
			success: function() {
				row.remove();
			}
		});
		event.preventDefault();
	});
	
	
	
	
	
});

	
	
	