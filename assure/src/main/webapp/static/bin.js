var errorArray = [];
var fileData = [];

function getClientUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/client";
}

function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function getBinUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/bin";
}

function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}


function addBin(){
	//Set the values to add
	var $form = $("#bin-add-form");
	var json = toJson($form);
	var url = getBinUrl();

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
	   		$('#add-bin-modal').modal('toggle');
	   		$.toast({
            heading: 'Success',
            text: "Bins added successfully !!",
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 3000,
            icon: 'success',
            allowToastClose: true
        });
	   		searchBin();
	   	},
	   	error: handleAjaxError
	   });

	return false;
}

//BUTTON ACTIONS
function searchBin(event){
	//Set the values to add
	var $tbody = $('#bin-table').find('tbody');
	$tbody.empty();
	
	var $form = $("#bin-form");
	var json = toJson($form);
	var url = getBinUrl()+"/search";
	// call api
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
	   		displayBinListInTable(response);  
	   	},
	   	error: handleAjaxError
	   });

	return false;
}


function updateBin(){
	if(!validateUpdateFields()){
            return false;
     }
	//Get the ID
	var id = $("#bin-edit-form input[name=binSkuId]").val();	
	var url = getBinUrl() + "/" + id;
	//Set the values to update
	var $form = $("#bin-edit-form");
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

			$.toast({
                    heading: 'Success',
                    text: 'Bin info updated.',
                    position: 'bottom-right',
                    showHideTransition: 'fade',
                    hideAfter: 3000,
                    icon: 'success',
                    allowToastClose: true,
                });
			$('#edit-bin-modal').modal('toggle');
	   		searchBin();  
	   	},
	   	error: handleAjaxError
	   });

	return false;
}


function getBinList(){
	var url = getBinUrl();
	// call api
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
	   		// display data
	   		displayBinListInTable(data);
	   		displayBinListInDropDown(data);  
	   	},
	   	error: handleAjaxError
	   });
}


function processDataBin(){
	if(validateFields()){
        var file = $('#binInventoryFile')[0].files[0];
		readFileData(file, readFileDataCallbackBin);
	}
}

function readFileDataCallbackBin(results){
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
	uploadRowsBin();
}

function uploadRowsBin(){
	//Update progress
	updateUploadDialogBin();
	var clientId = $('#upload-binInventory-modal select[name=clientName]').val();
    var dataArr = [];
    var jsonArray = {};
    for (var i = 0; i < fileData.length; i++){
        fileData[i].clientId = clientId;
        dataArr.push(fileData[i])
    }
    console.log(dataArr);
    jsonArray.binInventoryFormList = dataArr;
	var json = JSON.stringify(jsonArray);
	console.log(json);
	var url = getBinUrl();

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
                text: 'Bin Inventory info added.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'success',
                allowToastClose: true,
            });
            $('#upload-bin-modal').modal('toggle');
            searchBin();
        },
        error: function(jqXHR){
            errorArray = [];
            errorArray = handleAjaxError(jqXHR);
            $('#errorCountBinInventory').html("" + errorArray.length);
            if(errorArray != null && errorArray.length > 1){
                document.getElementById('download-errors-binInventory').focus();
            }
            document.getElementById("binInventory-upload-form").reset();
            var $file = $('#binInventoryFile');
	$file.val('');
	$('#binInventoryFileName').html("Choose File");
        }
	});

}

function downloadErrorsBin(){
	writeErrors(errorArray);

}

//UI DISPLAY METHODS

function displayBinListInTable(data){
	var $tbody = $('#bin-table').find('tbody');
	$tbody.empty();
	var j=1;
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button class="btn btn-outline-success" onclick="displayEditBin(' + e.binSkuId + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.binId + '</td>'
		+ '<td>'  + e.productName + '</td>'
		+ '<td>' + e.brandId + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		j++;
		$tbody.append(row);
	}
}

function displayEditBin(binSkuId){
	var url = getBinUrl() + "/" + binSkuId;
	// call api
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
	   		// display data
	   		displayBin(data);   
	   	},
	   	error: handleAjaxError
	   });	
}

function resetUploadDialogBin(){
	//Reset file name
	var $file = $('#binInventoryFile');
	$file.val('');
	$('#binInventoryFileName').html("Choose File");
	//Reset various count
	fileData = [];
	errorArray = [];
	//Update counts	
	updateUploadDialogBin();
}

// update data
function updateUploadDialogBin(){
	$('#rowCountBinInventory').html("" + fileData.length);
	$('#errorCountBinInventory').html("" + errorArray.length);
}

function updateFileNameBin(){
	var $file = $('#binInventoryFile');
	var fileName = $file.val();
	$('#binInventoryFileName').html(fileName);
}

function displayUploadDataBin(){
	resetUploadDialogBin(); 	
	$('#upload-binInventory-modal').modal('toggle');
}

function displayClientDropDownList(data){
    $('#clientSelected').empty();
    var options = '<option value="0" selected>Select Client</option>';
    $.each(data, function(index, value) {
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#clientSelected').append(options);
}

function displayBinListInDropDown(data){
    $('#binSelect').empty();
    var options = '<option value="0" selected>Select Bin</option>';
    $.each(data, function(index, value) {
        options += '<option value="' + value.binSkuId + '">' + value.binId + '</option>';
    });
    $('#binSelect').append(options);
}


function getClientList(){
	var url = getClientUrl() + "/allClients";
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayClientDropDownList(data);
        },
        error: handleAjaxError
	});
}

function validateFields(){
   var clientDropDown = $("#upload-binInventory-modal select[name=clientName]");
   if (clientDropDown.val() == '' || clientDropDown.val() == undefined || clientDropDown.val() == 0) {
       infoToast("Please select Client from the dropdown to proceed!");
       return false;
   }
   if( document.getElementById("binInventoryFile").files.length == 0 ){
       infoToast("No file selected.Please choose a file to proceed.");
       return false;
   }
   return true;
}

function validateUpdateFields() {
	var quantity=$("#bin-edit-form input[name=updateQuantity]").val();
    if(mrp<=0 || mrp.trim==""){
    	infoToast("Please enter valid MRP value.");
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
// fill entries
function displayBin(data){
	$("#bin-edit-form input[name=binSkuId]").val(data.binSkuId);	
	$("#bin-edit-form input[name=globalSkuId]").val(data.globalSkuId);
	$("#bin-edit-form input[name=productName]").val(data.productName);
	$("#bin-edit-form input[name=brandId]").val(data.brandId);
	$("#bin-edit-form input[name=binId]").val(data.binId);
	$("#bin-edit-form input[name=originalQuantity]").val(data.quantity);
	$('#edit-bin-modal').modal('toggle');
}

function showAddBinModal(){
	$('#add-bin-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#show-add-bin-modal').click(showAddBinModal);
	$('#search-bin').click(searchBin);
	$('#upload-binInventory-data').click(displayUploadDataBin);
	$('#process-data-binInventory').click(processDataBin);
	$('#download-errors-binInventory').click(downloadErrorsBin);
	$('#binInventoryFile').on('change', updateFileNameBin);
}

$(document).ready(init);
$(document).ready(getBinList);
$(document).ready(getClientList);