//HELPER METHOD
// form to json converter
function toJson($form){
    var serialized = $form.serializeArray();
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}

// handle error for REST api failure
function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
	$.toast({
            heading: 'Error',
            text: response.message,
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 3000,
            icon: 'error',
            allowToastClose: true
        });
}


function writeErrors(data) {
    var csv = 'Line number,ErrorText\n';
    data.forEach(function(row) {
            csv += row.join(',');
            csv += "\n";
    });

    console.log(csv);
    var hiddenElement = document.createElement('a');
    hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csv);
    hiddenElement.target = '_blank';
    hiddenElement.download = 'error.csv';
    hiddenElement.click();
}

function readFileData(file, callback){
    var config = {
        header: true,
        delimiter: ',',
        skipEmptyLines: "greedy",
        complete: function(results) {
            callback(results);
        }   
    }
    Papa.parse(file, config);
}

function writeFileData(arr){
    console.log(arr);
    var config = {
        quotes: true,
        delimiter: ',',
        header: false,
        download: true,
        skipEmptyLines: true
    };
    
    var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/csv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'file.csv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'file.csv');
    tempLink.click(); 
}


function init(){
    $('.modal').on('hidden.bs.modal', function(){
    $(this).find('form')[0].reset();
    $(this).find('tbody').empty();
     
});
}

$(document).ready(init);

