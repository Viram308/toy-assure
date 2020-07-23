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


// Read tsv files
function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
        }	
    }
    Papa.parse(file, config);
}

// Write in tsv files
function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click(); 
}


function init(){
    $('.modal').on('hidden.bs.modal', function(){
    $(this).find('form')[0].reset();
    $(this).find('tbody').empty();
     
});
}

$(document).ready(init);

