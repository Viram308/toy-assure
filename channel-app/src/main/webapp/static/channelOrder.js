function getChannelUrl(){
  var baseUrl = $("meta[name=baseUrl]").attr("content")
  return baseUrl + "/api/channel";
}
function getChannelListingUrl(){
  var baseUrl = $("meta[name=baseUrl]").attr("content")
  return baseUrl + "/api/channelListing";
}
function getChannelOrderUrl(){
  var baseUrl = $("meta[name=baseUrl]").attr("content")
  return baseUrl + "/api/channelOrder";
}

var $totalItems=$('#totalItems');
var errorArray = [];
function createChannelOrder(){
  if($totalItems.val()==0){
      $.toast({
            heading: 'Error',
            text: "Add items to create order !!",
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 3000,
            icon: 'error',
            allowToastClose: true
        });
      return false;
    }
    var table = document.getElementById("channelOrder-item-table");
    var orderData=[];
    var j=0;
    var jsonArray={};
    for (var i = 1, row; row = table.rows[i]; i++) {
        // create json
        var json = {
          "clientId":$("#channelOrder-create-form select[name=clientName]").val(),
          "customerId":$("#channelOrder-create-form select[name=customerName]").val(),
          "channelId":$("#channelOrder-create-form select[name=channelName]").val(),
          "channelOrderId":$("#channelOrder-create-form input[name=channelOrderId]").val(),
          "clientSkuId":row.cells[0].innerHTML,
          "orderedQuantity":row.cells[4].innerHTML,
          "sellingPricePerUnit":row.cells[5].innerHTML 
        };

        orderData[j]=json;
        j++;
      }
      jsonArray.orderItemFormList = orderData;
      var json = JSON.stringify(jsonArray);
      console.log(json);
    // call api
    var url=getChannelOrderUrl();
    $.ajax({
      url: url,
      type: 'POST',
      data: json,
      headers: {
        'Content-Type': 'application/json; charset=utf-8'
      },     
      success: function(data) {
        $.toast({
                heading: 'Success',
                text: 'Order info added.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'success',
                allowToastClose: true,
            });
            $('#add-channelOrder-modal').modal('toggle');
            searchChannelOrder();
      },
error: function(jqXHR){
            errorArray = [];
            errorArray = handleAjaxError(jqXHR);
            $('#errorCountChannelOrder').html("" + errorArray.length);
            if(errorArray != null && errorArray.length > 0){
                document.getElementById('download-errors-channelOrder').focus();
            }
            
        }
});
}


function getChannelOrderList(){
  var url = getChannelOrderUrl();
    // call api
    $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
        // display list
        displayChannelOrderList(data);  
      },
      error: handleAjaxError
    });
  }


  function searchChannelOrder(){
  //Set the values to add
  var $tbody = $('#channelOrder-table').find('tbody');
  $tbody.empty();
  
  var $form = $("#channelOrder-form");
  var json = toJson($form);
  var url = getChannelOrderUrl()+"/search";
  // call api
  $.ajax({
    url: url,
    type: 'POST',
    data: json,
    headers: {
      'Content-Type': 'application/json'
    },     
    success: function(response) {
      displayChannelOrderList(response);  
    },
    error: handleAjaxError
  });

  return false;
}

function downloadPdf(id){
  var url=getChannelOrderUrl()+"/download/"+id;
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

// download pdf with proper name
function downloadBillPdf(blob){
    let link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    var currentdate = new Date();
    link.download = "invoice_"+ currentdate.getDate() + "/"
    + (currentdate.getMonth()+1)  + "/" 
    + currentdate.getFullYear() + "@"  
    + currentdate.getHours() + "h_"  
    + currentdate.getMinutes() + "m_" 
    + currentdate.getSeconds()+"s.pdf";
    link.click();
}   



function getOrderItems(id){
  var $tbodyViewOrder = $('#view-channelOrder-table').find('tbody');
  $tbodyViewOrder.empty();
  var url = getChannelOrderUrl() + "/items/" + id;
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
  var $tbodyViewOrder = $('#view-channelOrder-table').find('tbody');
  $tbodyViewOrder.empty();
  var count = 1;
  for(var i in data){
    var e = data[i];
    var row = '<tr>'
    + '<td>' + count + '</td>'
    + '<td>' + e.clientSkuId + '</td>'
    + '<td>' + e.channelSkuId + '</td>'
    + '<td>' + e.productName + '</td>'
    + '<td>' + e.brandId + '</td>'
    + '<td>' + e.orderedQuantity + '</td>'
    + '<td>' + e.sellingPricePerUnit + '</td>'
    + '</tr>';
    $tbodyViewOrder.append(row);
    count++;
  }
}


function viewChannelOrder(id){
  $('#view-channelOrder-modal').modal('toggle');
  getOrderItems(id);
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

function displayChannelByClientDropDownList(data){
  $('#channelSelected').empty();

  var options = '<option value="0" selected>Select Channel</option>';
  $.each(data, function(index, value) {
    if(value.id!=1){
      options += '<option value="' + value.id + '">' + value.name + '</option>';
    }
  });
  $('#channelSelected').append(options);

}


function displayChannelDropDownList(data){
  $('#channelSelect').empty();

  var options = '<option value="0" selected>Select Channel</option>';
  $.each(data, function(index, value) {
    if(value.id!=1){
      options += '<option value="' + value.id + '">' + value.name + '</option>';
    }
  });
  $('#channelSelect').append(options);

}

function displayChannelListingbyChannel(data){
  $('#channelSkuIdSelected').empty();

  var options = '<option value="0" selected>Select Channel Sku</option>';
  $.each(data, function(index, value) {
    if(value.channelId!=1){
     options += '<option value="' + value.id + '">' + value.channelSkuId + '</option>';
   }
 });
  $('#channelSkuIdSelected').append(options);

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

function getCustomerList(){
  var url = getChannelUrl() + "/allCustomers";
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

function getChannelListByClientId(clientId){
  var url = getChannelUrl() + "/getByClient/"+clientId;
  $.ajax({
    url: url,
    type: 'GET',
    success: function(data) {
      displayChannelByClientDropDownList(data);
    },
    error: handleAjaxError
  });
}

function getChannelListingData(channelId,clientId){
  var url = getChannelListingUrl() + "/getByChannelAndClient/"+channelId+"/"+clientId;
  $.ajax({
    url: url,
    type: 'GET',
    success: function(data) {
      displayChannelListingbyChannel(data);
    },
    error: handleAjaxError
  });
}


function displayChannelOrderList(data){
  var $tbody = $('#channelOrder-table').find('tbody');
  $tbody.empty();
  var count=1;
  for(var i in data){
    var e = data[i];
    if(e.status === 'FULFILLED') {
      var buttonHtml2 = '<button type="button" class="btn btn-outline-success" onclick="downloadPdf('+ e.orderId +')">Download</button>'
    }else{
      var buttonHtml2 = '<button type="button" class="btn btn-outline-success" disabled>Download</button>'
    }
    var buttonHtml3 = '<button class="btn btn-outline-primary" onclick="viewChannelOrder(' + e.orderId + ')">View</button>'
    var row = '<tr>'
    + '<td>' + count + '</td>'
    + '<td>' + e.clientName + '</td>'
    + '<td>' + e.customerName + '</td>'
    + '<td>' + e.channelName + '</td>'
    + '<td>' + e.channelOrderId + '</td>'
    + '<td>' + e.status + '</td>'
    + '<td>' + buttonHtml2 + '</td>'
    + '<td>' + buttonHtml3 + '</td>'
    + '</tr>';
    $tbody.append(row);
    count++;
  }
}




function updateQuantityInTable(clientSkuId,finalQuantity){
  clientSkuId = clientSkuId.toLowerCase();
  var table = document.getElementById('channelOrder-item-table');
  var checkEquality=9;
  // if barcode exist then add quantity in table row
  for (var i = 1, row; row = table.rows[i]; i++) {
    var cellClientSkuId=row.cells[0].innerHTML;
    checkEquality=cellClientSkuId.localeCompare(clientSkuId);
    if(checkEquality==0)
    {
      row.cells[4].innerHTML=finalQuantity;
    }
  }
}


function validateFields(){

 var clientDropDown = $("#channelOrder-create-form select[name=clientName]");
 if (clientDropDown.val() == '' || clientDropDown.val() == undefined || clientDropDown.val() == 0) {
   infoToast("Please select Client from the dropdown to proceed!");
   return false;
 }

 var customerDropDown = $("#channelOrder-create-form select[name=customerName]");
 if (customerDropDown.val() == '' || customerDropDown.val() == undefined || customerDropDown.val() == 0) {
   infoToast("Please select Customer from the dropdown to proceed!");
   return false;
 }

 var channelDropDown = $("#channelOrder-create-form select[name=channelName]");
 if (channelDropDown.val() == '' || channelDropDown.val() == undefined || channelDropDown.val() == 0 || channelDropDown.val() == 1) {
   infoToast("Please select Channel from the dropdown to proceed!");
   return false;
 }
 var channelOrderId = $("#channelOrder-create-form input[name=channelOrderId]").val().trim();
 if (channelOrderId == '') {
   infoToast("Please enter channel orderId");
   return false;
 }

 var channelSkuDropDown = $("#channelOrder-create-form select[name=channelSkuId]");
 if (channelSkuDropDown.val() == '' || channelSkuDropDown.val() == undefined || channelSkuDropDown.val() == 0) {
   infoToast("Please select Channel sku from the dropdown to proceed!");
   return false;
 }

 var quantity = $("#channelOrder-create-form input[name=quantity]").val();
 if (quantity<=0) {
   infoToast("Please enter positive quantity");
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


function downloadErrorsChannelOrder(){
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

function addItemInTable(){
  if(validateFields()){
    var quantity = $("#channelOrder-create-form input[name=quantity]").val();
    var channelListingId = $("#channelOrder-create-form select[name=channelSkuId]").val();
    var url = getChannelListingUrl() +"/"+channelListingId;
    $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
        var quantityInTable = checkItemExist(data.clientSkuId);
        if(quantityInTable != 0){
          var finalQuantity = +quantity + +quantityInTable;
          updateQuantityInTable(data.clientSkuId,finalQuantity);
        }
        else{
            var $tbody = $('#channelOrder-item-table').find('tbody');
          var itemId=$totalItems.val();
          itemId++;
          $totalItems.val(itemId);
  // dynamic buttons

  var buttonHtml = ' <input type="button" class="btn btn-outline-danger" value="Delete" id="deleteItemButton">'

  var row = '<tr>'
  + '<td>' + data.clientSkuId + '</td>'
  + '<td>' + data.channelSkuId + '</td>'
  + '<td>'  + data.productName + '</td>'
  + '<td>' + data.brandId + '</td>'
  + '<td>' + quantity + '</td>'
  + '<td>' + data.sellingPricePerUnit + '</td>'
  + '<td>' + buttonHtml + '</td>'
  + '</tr>';
  $tbody.append(row);
}

  $('#channelSkuIdSelected').selectedIndex = 0;
  $("#channelOrder-create-form select[name=quantity]").val('');

},
error: handleAjaxError
});

  }
  else{
    return false;
  }
}


function checkItemExist(clientSkuId){
  clientSkuId = clientSkuId.toLowerCase();
  var table = document.getElementById('channelOrder-item-table');
  var quantity=0;
  var checkEquality=9;
  // check row wise each cell
  for (var i = 1, row; row = table.rows[i]; i++) {
    var cellClientSkuId=row.cells[0].innerHTML;
    checkEquality=cellClientSkuId.localeCompare(clientSkuId);
    if(checkEquality==0)
    {
      quantity=row.cells[4].innerHTML;
    }
  }
  return quantity;
}

function showChannelOrderModal(){
  var $tbody = $('#channelOrder-item-table').find('tbody');
  $tbody.empty();
  $('#errorCountChannelOrder').html(0);
  $totalItems.val(0);
  $('#add-channelOrder-modal').modal('toggle');

}


  //INITIALIZATION CODE
  function init(){


    $('#search-channelOrder').click(searchChannelOrder);
    $('#show-add-channelOrder-modal').click(showChannelOrderModal);
    $('#add-item-button').click(addItemInTable);
    $('#process-data-channelOrder').click(createChannelOrder);
    $('#channelOrder-create-form select[name=clientName]').change(function(){
      var $tbody = $('#channelOrder-item-table').find('tbody');
      $tbody.empty();
      $totalItems.val(0);
      var clientId = $(this).find("option:selected").val();
      getChannelListByClientId(clientId);
    });

    $('#channelOrder-create-form select[name=channelName]').change(function(){
      var $tbody = $('#channelOrder-item-table').find('tbody');
      $tbody.empty();
      $totalItems.val(0);
      var clientId = $('#channelOrder-create-form select[name=clientName]').val();
      var channelId = $(this).find("option:selected").val();
      getChannelListingData(channelId,clientId);
    });

    



    // delete button action
    $('#channelOrder-item-table').on('click', 'input[type="button"]', function () {
      $(this).closest('tr').remove();
      var itemId=$totalItems.val();
      itemId--;
      $totalItems.val(itemId);
    });
    $('#download-errors-channelOrder').click(downloadErrorsChannelOrder);
    
  }

  $(document).ready(init);
  $(document).ready(getChannelOrderList);
  $(document).ready(getClientList);
  $(document).ready(getCustomerList);
  $(document).ready(getChannelList);




