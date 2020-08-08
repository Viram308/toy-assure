//  get url
function getClientUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/client";
}

//BUTTON ACTIONS
function addClient(){
	//Set the values to add
	var $form = $("#client-add-form");
	var name=$("#client-add-form input[name=name]").val().trim();
		if(name==""){
			$.toast({
            heading: 'Info',
            text: "Please enter name !!",
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 3000,
            icon: 'info',
            allowToastClose: true
        });
			return false;
		}
	var json = toJson($form);
	var url = getClientUrl();

	// call api
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function(response) {
	   		// get list
	   		$('#add-client-modal').modal('toggle');
	   		$.toast({
            heading: 'Success',
            text: "Client added successfully !!",
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 3000,
            icon: 'success',
            allowToastClose: true
        });
	   		searchClient();
	   	},
	   	error: handleAjaxError
	   });

	return false;
}

function getClientList(){
	var url = getClientUrl();
	// call api
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
	   		// display list
	   		displayClientList(data);
	   	},
	   	error: handleAjaxError
	   });
}

function updateClient(event){

   //Get the ID
   var id = $("#client-edit-form input[name=id]").val();
   var url = getClientUrl() + "/" + id;

   //Set the values to update
   var $form = $("#client-edit-form");
   var name=$("#client-edit-form input[name=name]").val().trim();
		if(name==""){
			$.toast({
            heading: 'Info',
            text: "Please enter name !!",
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 3000,
            icon: 'info',
            allowToastClose: true
        });
			return false;
		}
   var json = toJson($form);
   // call api
   $.ajax({
   	url: url,
   	type: 'PUT',
   	data: json,
   	headers: {
   		'Content-Type': 'application/json'
   	},
   	success: function(response) {
            // get list
            $('#edit-client-modal').modal('toggle');
            $.toast({
            heading: 'Success',
            text: "Client updated successfully !!",
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 3000,
            icon: 'success',
            allowToastClose: true
        });
            searchClient();
        },
        error: handleAjaxError
    });

   return false;
}

function displayEditClient(id){
	var url = getClientUrl() + "/" + id;
   // call api
   $.ajax({
   	url: url,
   	type: 'GET',
   	success: function(data) {
            // display client
            displayClient(data);
        },
        error: handleAjaxError
    });
}

// fill entries
function displayClient(data){
	$("#client-edit-form input[name=name]").val(data.name);
	$("#client-edit-form select[name=type]").val(data.type);
	$("#client-edit-form input[name=id]").val(data.id);
	$('#edit-client-modal').modal('toggle');
}


//UI DISPLAY METHODS

function displayClientList(data){
	var $tbody = $('#client-table').find('tbody');
	$tbody.empty();
	var j=1;
	for(var i in data){
		var e = data[i];
		// dynamic buttons
		var buttonHtml = ' <button class="btn btn-outline-success" onclick="displayEditClient(' + e.id + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.type + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		$tbody.append(row);
		j++;
	}
}

function searchClient(){
	//Set the values to add
	var $tbody = $('#client-table').find('tbody');
	$tbody.empty();
	var type=$("#client-form select[name=type]").val();
	if(type==""){
		var name=$("#client-form input[name=name]").val().trim();
		if(name==""){
			getClientList();
		}
		else{
		var url = getClientUrl()+"/searchByName/"+name;
	// call api
	$.ajax({
		url: url,
		type: 'GET',
		success: function(response) {
	   		displayClientList(response);
	   	},
	   	error: handleAjaxError
	   });
	}
}
	else{
	var $form = $("#client-form");
	var json = toJson($form);
	var url = getClientUrl()+"/search";
	// call api
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function(response) {
	   		displayClientList(response);
	   	},
	   	error: handleAjaxError
	   });
	}
	return false;
}



function showAddClientModal(){
	$('#add-client-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#show-add-client-modal').click(showAddClientModal);
	$('#search-client').click(searchClient);
}

$(document).ready(init);
$(document).ready(getClientList);

