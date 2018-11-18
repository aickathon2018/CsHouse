var video = document.getElementById('video');
var canvas = document.getElementById('canvas');


var video = document.getElementById('video');
if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
 navigator.mediaDevices.getUserMedia({ video: true }).then(function(stream) {
 video.src = window.URL.createObjectURL(stream);
 video.play();
 });
}

var context = canvas.getContext('2d');
document.getElementById("snap").addEventListener("click", function() {
	context.canvas.width  = 800;
	context.canvas.height = 640;
	context.drawImage(video, 0, 0, 800, 640);
	uploadFile();
});

var API_URL = 'https://face.recoqnitics.com/analyze'
var ACCESS_KEY = '4a47bc97e4c9b8d96ac2'
var SECRET_KEY = '232668f1cd4761eac912746716058f65197b8e92'

function uploadFile() {
  var file = dataURLtoFile(canvas.toDataURL('image/jpeg'), 'filename.jpeg')
  console.log(file);
  context.clearRect(0, 0, canvas.width, canvas.height);
  context.canvas.width  = 0;
  context.canvas.height = 0;
  var formData = new FormData();
  formData.append("filename", file);

  // change the access_key and secret_key here
  formData.append("access_key", ACCESS_KEY);
  formData.append("secret_key", SECRET_KEY);

  
	let xhr = new XMLHttpRequest()
  xhr.open('POST', API_URL)
  xhr.onload = () =>
    xhr.status === 200
      ? updatedatabase(JSON.parse(xhr.response))
      : console.log(xhr.status)
  xhr.send(formData)
}


function dataURLtoFile(dataurl, filename) {
    var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
    while(n--){
        u8arr[n] = bstr.charCodeAt(n);
    }
    return new File([u8arr], filename, {type:mime});
}

async function updatedatabase(data) {
	var size = data.faces.length;
	console.log(data);
	for(var i = 0; i < size; i++){
	var gender = data.faces[i].gender.value;
	var age = data.faces[i].age;
	create_data(firebase,age,gender);
	await sleep(2000);
	}
		
	
}
function sleep(ms){
	return new Promise(resolve => setTimeout(resolve,ms));
}

function create_data(firebase,age,gender){
            var rootRef = firebase.database().ref();
            var childData = "1";
            var x = Date().toString();
			var currenttime = x.split(" ");
			var time = parseInt(currenttime[4]);
			var date = parseInt(currenttime[2]);
			var year = parseInt(currenttime[3]);
			var month = currenttime[1];
			if(month=="Nov"){
				month = 11;
			}
			if(time <3 ){
				time = "0-3";
			}
			else if(time < 6){
				time = "3-6";
			}
			else if(time < 9){
				time = "6-9";
			}
			else if(time < 12){
				time = "9-12";
			}
			else if(time < 15){
				time = "12-15";
			}
			else if(time < 18){
				time = "15-18";
			}
			else if(time < 21){
				time = "18-21";
			}
			else if(time < 24){
				time = "21-24";
			}
			
            rootRef.child('Location A').child(year).child(month).child(date).child(time).once('value', function(snapshot){
                if(snapshot.exists()){
                       if(age<15){
							
						   var increase = parseInt(snapshot.child('Age').child('15 below').val());
						   increase = increase + 1;
						   rootRef.child('Location A').child(year).child(month).child(date).child(time).child('Age').update({'15 below':increase});
							if(gender == "Male"){
								
								var increase_gender = parseInt(snapshot.child('Gender').child('Male').val());
								increase_gender = increase_gender+1;
								rootRef.child('Location A').child(year).child(month).child(date).child(time).child('Gender').update({'Male':increase_gender});
							}
							else{
								var increase_gender = parseInt(snapshot.child('Gender').child('Female').val());
								increase_gender = increase_gender+1;
								rootRef.child('Location A').child(year).child(month).child(date).child(time).child('Gender').update({'Female':increase_gender});
							}
							
								
					   }
                       else if(age<55){
						   
						   var increase = parseInt(snapshot.child('Age').child('15-55').val());
						   
						   increase = increase + 1;
						   rootRef.child('Location A').child(year).child(month).child(date).child(time).child('Age').update({'15-55':increase});
							if(gender == "Male"){
								
								var increase_gender = parseInt(snapshot.child('Gender').child('Male').val());
								increase_gender = increase_gender+1;
								rootRef.child('Location A').child(year).child(month).child(date).child(time).child('Gender').update({'Male':increase_gender});
							}
							else{
								var increase_gender = parseInt(snapshot.child('Gender').child('Female').val());
								increase_gender = increase_gender+1;
								rootRef.child('Location A').child(year).child(month).child(date).child(time).child('Gender').update({'Female':increase_gender});
							}
					  }
                       else if(age>54){

						   var increase = parseInt(snapshot.child('Age').child('55 above').val());
						   increase = increase + 1;
						   rootRef.child('Location A').child(year).child(month).child(date).child(time).child('Age').update({'55 above':increase});
							if(gender == "Male"){
								var increase_gender = parseInt(snapshot.child('Gender').child('Male').val());
								increase_gender = increase_gender+1;
								rootRef.child('Location A').child(year).child(month).child(date).child(time).child('Gender').update({'Male':increase_gender});
							}
							else{
								var increase_gender = parseInt(snapshot.child('Gender').child('Female').val());
								increase_gender = increase_gender+1;
								rootRef.child('Location A').child(year).child(month).child(date).child(time).child('Gender').update({'Female':increase_gender});
							}
					   }
                    }   
                else{
                    
                }
            });
        }