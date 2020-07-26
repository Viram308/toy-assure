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


function handleAjaxError(jqXHR){
    console.log(jqXHR.status);
    var response = jQuery.parseJSON(jqXHR.responseText);
    console.log(response);
    if(jqXHR.status === 500){
        $.toast({
            heading: 'Error',
            text: "Some error occurred. Please try after some time.",
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 3000,
            icon: 'error',
            allowToastClose: true
        });
    }else if(jqXHR.status === 400) {
        var header = response.message;
        var errorText = [];
        var jsonArray = [];
        if(response.subErrors != null && response.subErrors.length>0){
            for(var i=0; i<response.subErrors.length; i++){
                console.log(response.subErrors[i]+"      SubError");
                var message = response.subErrors[i].message;
                var num = response.subErrors[i].field.match(/(\d+)/);
                var line = ""+(parseInt(num[0])+1)
                errorText = [line, message];
                jsonArray.push(errorText);
                num={};
            }
            console.log(errorText);
            console.log(jsonArray);
            $.toast({
                heading: header,
                text: "Error occurred while traversing request. Please download error file.",
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 5000,
                icon: 'error',
                allowToastClose: true
            });
            return jsonArray;
        } else {
            $.toast({
                heading: "Error",
                text: response.message,
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 5000,
                icon: 'error',
                allowToastClose: true
            });
        }
    }else if(jqXHR.status === 404) {
        if(response.subErrors != null || response.subErrors.length>1){
            var header = response.message;
            var errorText = [];
            var jsonArray = [];
            console.log(response.subErrors[0].message);
            for(var i=0; i<response.subErrors.length; i++){
                console.log(response.subErrors[i]+"      SubError");
                var message = response.subErrors[i].message;
                var num = response.subErrors[i].field.match(/(\d+)/);
                var line = ""+(parseInt(num[0])+1)
                errorText = [line, message];
                jsonArray.push(errorText);
                num={};
            }
            console.log(errorText);
            console.log(jsonArray);
            $.toast({
                heading: 'Info',
                text: 'Entity not found. Please download error file.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'info',
                allowToastClose: true
            });
            return jsonArray;
        } else {
            $.toast({
                heading: 'Info',
                text: response.message,
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 5000,
                icon: 'info',
                allowToastClose: true
            });
            return;
        }
    }
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

