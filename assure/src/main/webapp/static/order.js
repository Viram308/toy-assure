var errorArray = [];
var fileData = [];

function getClientUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/client";
}
// get url
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}


//BUTTON ACTIONS
function searchOrder(event){
	//Set the values to add
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	
	var $form = $("#order-form");
	var json = toJson($form);
	var url = getOrderUrl()+"/search";
	// call api
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
	   		displayOrderList(response);  
	   	},
	   	error: handleAjaxError
	   });

	return false;
}


function getOrderList(){
	var url = getOrderUrl();
	// call api
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
	   		// display data
	   		displayOrderList(data);  
	   	},
	   	error: handleAjaxError
	   });
}


function processDataOrder(){
	if(validateFields()){
        var file = $('#orderFile')[0].files[0];
		readFileData(file, readFileDataCallbackOrder);
	}
}

function readFileDataCallbackOrder(results){
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
	uploadRowsOrder();
}

function uploadRowsOrder(){
	//Update progress
	updateUploadDialogOrder();
	var clientId = $('#upload-order-modal select[name=clientName]').val();
	var customerId = $('#upload-order-modal select[name=customerName]').val();
	var channelOrderId = $('#upload-order-modal input[name=channelOrderId]').val();
	var channelId = $('#upload-order-modal input[name=channelId]').val();

    var dataArr = [];
    var jsonArray = {};
    for (var i = 0; i < fileData.length; i++){
        fileData[i].clientId = clientId;
        fileData[i].customerId = customerId;
        fileData[i].channelOrderId = channelOrderId;
        fileData[i].channelId = channelId;
        dataArr.push(fileData[i])
    }
    console.log(dataArr);
    jsonArray.orderItemFormList = dataArr;
	var json = JSON.stringify(jsonArray);
	console.log(json);
	var url = getOrderUrl();

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
                text: 'Order info added.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'success',
                allowToastClose: true,
            });
            $('#upload-order-modal').modal('toggle');
            searchOrder();
        },
        error: function(jqXHR){
            errorArray = [];
            errorArray = handleAjaxError(jqXHR);
            $('#errorCountOrder').html("" + errorArray.length);
            if(errorArray != null && errorArray.length > 0){
                document.getElementById('download-errors-order').focus();
            }
            document.getElementById("order-upload-form").reset();
            var $file = $('#orderFile');
	$file.val('');
	$('#orderFileName').html("Choose File");
        }
	});

}

function downloadErrorsOrder(){
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


function getOrderItems(id){
    var $tbodyViewOrder = $('#view-order-table').find('tbody');
    $tbodyViewOrder.empty();
	var url = getOrderUrl() + "/items/" + id;
   // call api
   $.ajax({
   	url: url,
   	type: 'GET',
   	success: function(data) {
            // display data
            displayOrderItems(data);   
        },
        error: handleAjaxError
    });   
}
function displayOrderItems(data){
	var $tbodyViewOrder = $('#view-order-table').find('tbody');
	$tbodyViewOrder.empty();
	var count = 1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + count + '</td>'
		+ '<td>' + e.clientSkuId + '</td>'
		+ '<td>' + e.productName + '</td>'
		+ '<td>' + e.brandId + '</td>'
		+ '<td>' + e.orderedQuantity + '</td>'
		+ '<td>' + e.sellingPricePerUnit + '</td>'
		+ '</tr>';
		$tbodyViewOrder.append(row);
	}
}

function viewOrder(id){
	$('#view-order-modal').modal('toggle');
	getOrderItems(id);
}


function fulfillOrder(id){
	var url = getOrderUrl()+"/fulfill/"+id;
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
                        $.toast({
                            heading: 'Success',
                            text: 'Order list updated',
                            position: 'bottom-right',
                            showHideTransition: 'fade',
                            hideAfter: 3000,
                            icon: 'success',
                            allowToastClose: true,
                        });
                        searchOrder();
        },
        error: handleAjaxError
	});
}

function downloadPdf(id){
	var url=getOrderUrl()+"/download/"+id;
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            var sampleArr = base64ToArrayBuffer(data);
            $.toast({
                heading: 'Success',
                text: 'Downloading PDF.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'success',
                allowToastClose: true,
                afterShown: function () {
                    saveByteArray(sampleArr);                }
                });
        },
        error: handleAjaxError
	});
}


function saveByteArray(byte) {
        var currentdate = new Date();

    var blob = new Blob([byte], {type: "application/pdf"});
    var link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = "invoice_"+ currentdate.getDate() + "/"
    + (currentdate.getMonth()+1)  + "/" 
    + currentdate.getFullYear() + "@"  
    + currentdate.getHours() + "h_"  
    + currentdate.getMinutes() + "m_" 
    + currentdate.getSeconds()+"s.pdf";
    link.click();
}

function base64ToArrayBuffer(base64) {
    var binaryString = window.atob(base64);
    var binaryLen = binaryString.length;
    var bytes = new Uint8Array(binaryLen);
    for (var i = 0; i < binaryLen; i++) {
       var ascii = binaryString.charCodeAt(i);
       bytes[i] = ascii;
    }
    return bytes;
 }



 function allocateOrder(){
	var url = getOrderUrl()+"/allocate";
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            $.toast({
                heading: 'Info',
                text: 'Order list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
                   searchOrder();
                }
            });
        },
        error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayOrderList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	var count=1;
	for(var i in data){
		var e = data[i];
		if(e.status === 'ALLOCATED') {
		    var buttonHtml1 = '<button type="button" class="btn btn-outline-primary" onclick="fulfillOrder('+e.orderId+')">Fulfill Order</button>'
		} else{
            var buttonHtml1 = '<button type="button" class="btn btn-outline-secondary" disabled>Fulfill Order</button>'
        }
		if(e.status === 'FULFILLED') {
		    var buttonHtml2 = '<button type="button" class="btn btn-outline-success" onclick="downloadPdf('+ e.orderId +')">Download</button>'
		}else{
		    var buttonHtml2 = '<button type="button" class="btn btn-outline-success" disabled>Download</button>'
		}
		var buttonHtml3 = '<button class="btn btn-outline-primary" onclick="viewOrder(' + e.orderId + ')">View</button>'
		var row = '<tr>'
		+ '<td>' + count + '</td>'
		+ '<td>' + e.clientName + '</td>'
		+ '<td>' + e.customerName + '</td>'
		+ '<td>' + e.channelName + '</td>'
        + '<td>' + e.channelOrderId + '</td>'
        + '<td>' + e.status + '</td>'
		+ '<td>' + buttonHtml1 + '</td>'
		+ '<td>' + buttonHtml2 + '</td>'
		+ '<td>' + buttonHtml3 + '</td>'
		+ '</tr>';
        $tbody.append(row);
        count++;
	}
}

function resetUploadDialogOrder(){
	//Reset file name
	var $file = $('#orderFile');
	$file.val('');
	$('#orderFileName').html("Choose File");
	//Reset various count
	fileData = [];
	errorArray = [];
	//Update counts	
	updateUploadDialogOrder();
}

// update data
function updateUploadDialogOrder(){
	$('#rowCountOrder').html("" + fileData.length);
	$('#errorCountOrder').html("" + errorArray.length);
}

function updateFileNameOrder(){
	var $file = $('#orderFile');
	var fileName = $file.val();
	$('#orderFileName').html(fileName);
}

function displayUploadDataOrder(){
	resetUploadDialogOrder(); 	
	$('#upload-order-modal').modal('toggle');
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

function displayChannelDropDownList(data){
    $('#channelSelect').empty();
    var options = '<option value="0" selected>Select Channel</option>';
    $.each(data, function(index, value) {
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#channelSelect').append(options);
}

function displayCustomerDropDownList(data){
    
    $('#customerSelected').empty();
    var options = '<option value="0" selected>Select Customer</option>';
    $.each(data, function(index, value) {
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    
    $('#customerSelected').append(options);
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

function getCustomerList(){
	var url = getClientUrl() + "/allCustomers";
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayCustomerDropDownList(data);
        },
        error: handleAjaxError
	});
}

function getChannelList(){
	var url = getOrderUrl() + "/allChannel";
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
   var clientDropDown = $("#upload-order-modal select[name=clientName]");
   var customerDropDown = $("#upload-order-modal select[name=customerName]");
   var channelOrderId = $("#upload-order-modal input[name=channelOrderId]").val().trim();
   if (clientDropDown.val() == '' || clientDropDown.val() == undefined || clientDropDown.val() == 0) {
       infoToast("Please select Client from the dropdown to proceed!");
       return false;
   }
   if (customerDropDown.val() == '' || customerDropDown.val() == undefined || customerDropDown.val() == 0) {
       infoToast("Please select Customer from the dropdown to proceed!");
       return false;
   }
   if (channelOrderId == '') {
       infoToast("Please enter channel orderId");
       return false;
   }
   if( document.getElementById("orderFile").files.length == 0 ){
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
	$('#search-order').click(searchOrder);
	$('#upload-order-data').click(displayUploadDataOrder);
	$('#process-data-order').click(processDataOrder);
	$('#download-errors-order').click(downloadErrorsOrder);
	$('#orderFile').on('change', updateFileNameOrder);
	$('#allocate-order').click(allocateOrder);
}

$(document).ready(init);
$(document).ready(getOrderList);
$(document).ready(getClientList);
$(document).ready(getCustomerList);
$(document).ready(getChannelList);
