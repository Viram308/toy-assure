//  get url
function getChannelUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/channel";
}

//BUTTON ACTIONS
function addChannel(){
	//Set the values to add
	var $form = $("#channel-add-form");
	var name=$("#channel-add-form input[name=channelName]").val().trim();
		if(name==""){
			$.toast({
            heading: 'Error',
            text: "Please enter name !!",
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 3000,
            icon: 'error',
            allowToastClose: true
        });
			return false;
		}
	var json = toJson($form);
	var url = getChannelUrl();

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
	   		$('#add-channel-modal').modal('toggle');
	   		$.toast({
            heading: 'Success',
            text: "Channel added successfully !!",
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 3000,
            icon: 'success',
            allowToastClose: true
        });
	   		searchChannel();
	   	},
	   	error: handleAjaxError
	   });

	return false;
}

function getChannelList(){
	var url = getChannelUrl();
	// call api
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
	   		// display list
	   		displayChannelList(data);
	   	},
	   	error: handleAjaxError
	   });
}

function updateChannel(){

   //Get the ID
   var id = $("#channel-edit-form input[name=id]").val();
   var url = getChannelUrl() + "/" + id;

   //Set the values to update
   var $form = $("#channel-edit-form");
   var name=$("#channel-edit-form input[name=channelName]").val().trim();
		if(name==""){
			$.toast({
            heading: 'Error',
            text: "Please enter name !!",
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 3000,
            icon: 'error',
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
            $('#edit-channel-modal').modal('toggle');
            $.toast({
            heading: 'Success',
            text: "Channel updated successfully !!",
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 3000,
            icon: 'success',
            allowToastClose: true
        });
            searchChannel();
        },
        error: handleAjaxError
    });

   return false;
}

function displayEditChannel(id){
	var url = getChannelUrl() + "/" + id;
   // call api
   $.ajax({
   	url: url,
   	type: 'GET',
   	success: function(data) {
            // display channel
            displayChannel(data);
        },
        error: handleAjaxError
    });
}

// fill entries
function displayChannel(data){
	$("#channel-edit-form input[name=channelName]").val(data.name);
	$("#channel-edit-form select[name=invoiceType]").val(data.invoiceType);
	$("#channel-edit-form input[name=id]").val(data.id);
	$('#edit-channel-modal').modal('toggle');
}


//UI DISPLAY METHODS

function displayChannelList(data){
	var $tbody = $('#channel-table').find('tbody');
	$tbody.empty();
	var j=1;
	for(var i in data){
		var e = data[i];
		// dynamic buttons
		var buttonHtml = ' <button class="btn btn-outline-success" onclick="displayEditChannel(' + e.id + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.invoiceType + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		$tbody.append(row);
		j++;
	}
}

function searchChannel(){
	//Set the values to add
	var $tbody = $('#channel-table').find('tbody');
	$tbody.empty();
	var $form = $("#channel-form");
	var json = toJson($form);
	var url = getChannelUrl()+"/search";
	// call api
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function(response) {
	   		displayChannelList(response);
	   	},
	   	error: handleAjaxError
	   });
	return false;
}



function showAddChannelModal(){
	$('#add-channel-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#show-add-channel-modal').click(showAddChannelModal);
	$('#search-channel').click(searchChannel);
}

$(document).ready(init);
$(document).ready(getChannelList);

