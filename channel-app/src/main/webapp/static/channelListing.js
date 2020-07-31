var errorArray = [];
var fileData = [];
// get url
function getClientUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/client";
}

function getChannelUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/channel";
}
function getChannelListingUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/channelListing";
}


//BUTTON ACTIONS
function searchChannelListing(event){
	//Set the values to add
	var $tbody = $('#channelListing-table').find('tbody');
	$tbody.empty();
	
	var $form = $("#channelListing-form");
	var json = toJson($form);
	var url = getChannelListingUrl()+"/search";
	// call api
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
	   		displayChannelListingList(response);  
	   	},
	   	error: handleAjaxError
	   });

	return false;
}


function getChannelListingList(){
	var url = getChannelListingUrl();
	// call api
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
	   		// display data
	   		displayChannelListingList(data);  
	   	},
	   	error: handleAjaxError
	});
}


function processDataChannelListing(){
	if(validateFields()){
        var file = $('#channelListingFile')[0].files[0];
		readFileData(file, readFileDataCallbackChannelListing);
	}
}

function readFileDataCallbackChannelListing(results){
	fileData = results.data;
	// check no of rows
	if(fileData.length > 5000)
	{
		$.toast({
                    heading: 'Error',
                    text: 'File Contains more than 5000 rows !!',
                    position: 'bottom-right',
                    showHideTransition: 'fade',
                    hideAfter: 3000,
                    icon: 'error',
                    allowToastClose: true,
                });
		return;
	}
	uploadRowsChannelListing();
}

function uploadRowsChannelListing(){
	//Update progress
	updateUploadDialogChannelListing();
	var clientId = $('#upload-channelListing-modal select[name=clientName]').val();
    var channelId = $('#upload-channelListing-modal select[name=channelName]').val();
    var dataArr = [];
    var jsonArray = {};
    for (var i = 0; i < fileData.length; i++){
        fileData[i].clientId = clientId;
        fileData[i].channelId = channelId; 
        dataArr.push(fileData[i])
    }
    console.log(dataArr);
    jsonArray.channelListingFormList = dataArr;
	var json = JSON.stringify(jsonArray);
	console.log(json);
	var url = getChannelListingUrl();

	$.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            $.toast({
                heading: 'Success',
                text: 'ChannelListing info added.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'success',
                allowToastClose: true,
            });
            $('#upload-channelListing-modal').modal('toggle');
            searchChannelListing();
        },
        error: function(jqXHR){
            errorArray = [];
            errorArray = handleAjaxError(jqXHR);
            $('#errorCountChannelListing').html("" + errorArray.length);
            if(errorArray != null && errorArray.length > 0){
                document.getElementById('download-errors-channelListing').focus();
            }
            document.getElementById("channelListing-upload-form").reset();
            var $file = $('#channelListingFile');
	$file.val('');
	$('#channelListingFileName').html("Choose File");
        }
	});

}

function downloadErrorsChannelListing(){
	if(errorArray.length==0){
		$.toast({
                    heading: 'Error',
                    text: 'There are no errors to download !!',
                    position: 'bottom-right',
                    showHideTransition: 'fade',
                    hideAfter: 3000,
                    icon: 'error',
                    allowToastClose: true,
                });
		return false;
	}
	writeErrors(errorArray);

}

//UI DISPLAY METHODS

function displayChannelListingList(data){
	var $tbody = $('#channelListing-table').find('tbody');
	$tbody.empty();
	var j=1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.channelName + '</td>'
		+ '<td>' + e.channelSkuId + '</td>'
		+ '<td>'  + e.clientName + '</td>'
		+ '<td>'  + e.productName + '</td>'
		+ '<td>' + e.brandId + '</td>'
		+ '</tr>';
		j++;
		$tbody.append(row);
	}
}

function resetUploadDialogChannelListing(){
	//Reset file name
	var $file = $('#channelListingFile');
	$file.val('');
	$('#channelListingFileName').html("Choose File");
	//Reset various count
	fileData = [];
	errorArray = [];
	//Update counts	
	updateUploadDialogChannelListing();
}

// update data
function updateUploadDialogChannelListing(){
	$('#rowCountChannelListing').html("" + fileData.length);
	$('#errorCountChannelListing').html("" + errorArray.length);
}

function updateFileNameChannelListing(){
	var $file = $('#channelListingFile');
	var fileName = $file.val();
	$('#channelListingFileName').html(fileName);
}

function displayUploadDataChannelListing(){
	resetUploadDialogChannelListing(); 	
	$('#upload-channelListing-modal').modal('toggle');
}


function displayChannelDropDownList(data){
    $('#channelSelect').empty();
    $('#channelSelected').empty();
    var options = '<option value="0" selected>Select Channel</option>';
    $.each(data, function(index, value) {
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#channelSelect').append(options);
    $('#channelSelected').append(options);
}

function displayClientDropDownList(data){
    $('#clientSelect').empty();
    $('#clientSelected').empty();
    var options = '<option value="0" selected>Select Client</option>';
    $.each(data, function(index, value) {
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#clientSelect').append(options);
    $('#clientSelected').append(options);
}

function getClientList(){
	var url = getChannelUrl() + "/allClients";
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayClientDropDownList(data);
        },
        error: handleAjaxError
	});
}

function getChannelList(){
	var url = getChannelUrl();
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayChannelDropDownList(data);
        },
        error: handleAjaxError
	});

}

function validateFields(){

   var channelDropDown = $("#upload-channelListing-modal select[name=channelName]");
   if (channelDropDown.val() == '' || channelDropDown.val() == undefined || channelDropDown.val() == 0) {
       infoToast("Please select Channel from the dropdown to proceed!");
       return false;
   }
   var clientDropDown = $("#upload-channelListing-modal select[name=clientName]");
   if (clientDropDown.val() == '' || clientDropDown.val() == undefined || clientDropDown.val() == 0) {
       infoToast("Please select Client from the dropdown to proceed!");
       return false;
   }
   
   if( document.getElementById("channelListingFile").files.length == 0 ){
       infoToast("No file selected.Please choose a file to proceed.");
       return false;
   }
   return true;
}

function infoToast(infoText) {
    $.toast({
        heading: 'Info',
        text: infoText,
        position: 'bottom-right',
        showHideTransition: 'fade',
        hideAfter: 3000,
        icon: 'info',
        allowToastClose: true
    });
}

//INITIALIZATION CODE
function init(){
	$('#search-channelListing').click(searchChannelListing);
	$('#upload-channelListing-data').click(displayUploadDataChannelListing);
	$('#process-data-channelListing').click(processDataChannelListing);
	$('#download-errors-channelListing').click(downloadErrorsChannelListing);
	$('#channelListingFile').on('change', updateFileNameChannelListing);
}

$(document).ready(init);
$(document).ready(getChannelListingList);
$(document).ready(getChannelList);
$(document).ready(getClientList);