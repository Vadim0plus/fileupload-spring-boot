var form = document.getElementById('file-form');
var fileSelect = document.getElementById('file-select');
var uploadButton = document.getElementById('upload-button');
	
form.onsubmit = function(event) {
  	event.preventDefault();

  	// Update button text.
  	uploadButton.innerHTML = 'Uploading...';

  	// The rest of the code will go here...
  	
  	//Get the selected files from the input
  	var files = fileSelect.files;
  	
  	var formData = new FormData();
  	
  	formData.append("extraField", document.getElementById("extrafield").value);
  	
  	for(var i = 0; i < files.length; i++) {
  		var file = files[i];
  		
  		// Check the Type
  		/*
  		if(!file.type.match('image.*')) {
  			continue;
  		}
  		*/
  		
  		// Add the file to the request
  		formData.append('files',file, file.name);
  	}
  	
  	// Set up the request.
	var xhr = new XMLHttpRequest();
	
	// Open the connection.
	xhr.open('POST', '/api/upload/multi', true);
	
	// Set up a handler for when the request finishes.
	xhr.onload = function () {
  		if (xhr.status === 200) {
    		// File(s) uploaded.
    		uploadButton.innerHTML = 'Upload';
    		document.getElementById("ans-text").innerHTML = xhr.responseText;
  		} else {
    		alert('An error occurred!');
  		}
  	};
  	
  	// Send the Data.
	xhr.send(formData);
};
