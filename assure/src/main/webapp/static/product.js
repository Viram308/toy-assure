var errorArray = [];

// get url
function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}


//BUTTON ACTIONS
function searchProduct(event){
	//Set the values to add
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	
	var $form = $("#product-form");
	var json = toJson($form);
	var url = getProductUrl()+"/search";
	// call api
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
	   		displayProductList(response);  
	   	},
	   	error: handleAjaxError
	   });

	return false;
}


function updateProduct(){
	if(!validateUpdateFields()){
            return;
     }
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();	
	var url = getProductUrl() + "/" + id;
	//Set the values to update
	var $form = $("#product-edit-form");
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
                    text: 'Product info updated.',
                    position: 'bottom-right',
                    showHideTransition: 'fade',
                    hideAfter: 3000,
                    icon: 'success',
                    allowToastClose: true,
                });
			$('#edit-product-modal').modal('toggle');
	   		searchProduct();  
	   	},
	   	error: handleAjaxError
	   });

	return false;
}


function getProductList(){
	var url = getProductUrl();
	// call api
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
	   		// display data
	   		displayProductList(data);  
	   	},
	   	error: handleAjaxError
	   });
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processDataProduct(){
	if(validateFields()){
        var file = $('#productFile')[0].files[0];
		readFileData(file, readFileDataCallbackProduct);
	}
}

function readFileDataCallbackProduct(results){
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
	uploadRowsProduct();
}

function uploadRowsProduct(){
	//Update progress
	updateUploadDialogProduct();
	var clientName = $('#upload-product-modal select[name=clientName]').val();
    var dataArr = [];
    var jsonArray = {};
    for (var i = 0; i < fileData.length; i++){
        fileData[i].clientName = clientName;
        dataArr.push(fileData[i])
    }
    console.log(dataArr);
    jsonArray.product = dataArr;
	var json = JSON.stringify(jsonArray);
	console.log(json);
	var url = getProductUrl();

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
                text: 'Product info added.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'success',
                allowToastClose: true,
            });
            searchProduct();
        },
        error: function(jqXHR){
            errorArray = [];
            errorArray = handleAjaxError(jqXHR);
            console.log(errorArray);
        }
	});

}

function downloadErrorsProduct(){
	writeErrors(errorArray);
}

//UI DISPLAY METHODS

function displayProductList(data){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	var j=1;
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button class="btn btn-outline-success" onclick="displayEditProduct(' + e.globalSkuId + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.clientSkuId + '</td>'
		+ '<td>' + e.clientName + '</td>'
		+ '<td>'  + e.productName + '</td>'
		+ '<td>' + e.brandId + '</td>'
		+ '<td>' + e.mrp + '</td>'
		+ '<td>' + e.description + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		j++;
		$tbody.append(row);
	}
}

function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	// call api
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
	   		// display data
	   		displayProduct(data);   
	   	},
	   	error: handleAjaxError
	   });	
}

function resetUploadDialogProduct(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialogProduct();
}

// update data
function updateUploadDialogProduct(){
	$('#rowCountProduct').html("" + fileData.length);
	$('#processCountProduct').html("" + processCount);
	$('#errorCountProduct').html("" + errorData.length);
}

function updateFileNameProduct(){
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
}

function displayUploadDataProduct(){
	resetUploadDialogProduct(); 	
	$('#upload-product-modal').modal('toggle');
}


function validateFields(){
   var clientDropDown = $("#upload-product-modal select[name=clientName]");
   if (clientDropDown.val() == '' || clientDropDown.val() == undefined || clientDropDown.val() == 0) {
       infoToast("Please select Client from the dropdown to proceed!");
       return false;
   }
   if( document.getElementById("productFile").files.length == 0 ){
       infoToast("No file selected.Please choose a file to proceed.");
       return false;
   }
   return true;
}

function validateUpdateFields() {
	var productName=$("#product-edit-form input[name=productName]").val();
	var brand=$("#product-edit-form input[name=brandId]").val();
	var mrp=$("#product-edit-form input[name=mrp]").val();
	var description=$("#product-edit-form input[name=description]").val();
    if (productName==null || productName.trim()=="") {
       infoToast("Please enter Product name value.");
       return false;
    }
    if (brand==null || brand.trim()=="") {
       infoToast("Please enter Brand ID value.");
       return false;
    }
    if(mrp<=0 || mrp.trim==""){
    	infoToast("Please enter valid MRP value.");
       return false;
    }
    if (description==null || description.trim()=="") {
       infoToast("Please enter description.");
       return false;
    }
    return true;
}

// fill entries
function displayProduct(data){
	$("#product-edit-form input[name=clientSkuId]").val(data.clientSkuId);	
	$("#product-edit-form input[name=clientName]").val(data.clientName);
	$("#product-edit-form input[name=productName]").val(data.productName);
	$("#product-edit-form input[name=brandId]").val(data.brandId);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=description]").val(data.description);
	$("#product-edit-form input[name=globalSkuId]").val(data.globalSkuId);
	$('#edit-product-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#search-product').click(searchProduct);
	$('#upload-product-data').click(displayUploadDataProduct);
	$('#process-data-product').click(processDataProduct);
	$('#download-errors-product').click(downloadErrorsProduct);
	$('#productFile').on('change', updateFileNameProduct);
}

$(document).ready(init);
$(document).ready(getProductList);
