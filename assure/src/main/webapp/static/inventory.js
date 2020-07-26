function getClientUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/client";
}

function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

//BUTTON ACTIONS
function searchInventory(event){
	//Set the values to add
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	
	var $form = $("#inventory-form");
	var json = toJson($form);
	var url = getInventoryUrl()+"/search";
	// call api
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
	   		displayInventoryListInTable(response);  
	   	},
	   	error: handleAjaxError
	   });

	return false;
}

function getInventoryList(){
	var url = getInventoryUrl();
	// call api
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
	   		// display data
	   		displayInventoryListInTable(data);  
	   	},
	   	error: handleAjaxError
	   });
}

//UI DISPLAY METHODS

function displayInventoryListInTable(data){
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	var j=1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.clientName + '</td>'
		+ '<td>'  + e.productName + '</td>'
		+ '<td>' + e.brandId + '</td>'
		+ '<td>' + e.availableQuantity + '</td>'
		+ '<td>' + e.allocatedQuantity + '</td>'
		+ '<td>' + e.fulfilledQuantity + '</td>'
		+ '</tr>';
		j++;
		$tbody.append(row);
	}
}


function displayClientDropDownList(data){
	$('#clientSelect').empty();
    var options = '<option value="0" selected>Select Client</option>';
    $.each(data, function(index, value) {
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#clientSelect').append(options);
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


//INITIALIZATION CODE
function init(){
	$('#search-inventory').click(searchInventory);
}

$(document).ready(init);
$(document).ready(getInventoryList);
$(document).ready(getClientList);