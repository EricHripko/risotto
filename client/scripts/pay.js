


for(i=1; i<=12; i++){
    var option = document.createElement("OPTION");
    option.text=i; 
    var select = document.getElementById("month"); 
    select.appendChild(option); 
}

var d = new Date(); 
var el = d.getFullYear(); 

for(i=el; i<=el+15; i++){
    var option = document.createElement("OPTION");
    option.text=i; 
    var select = document.getElementById("year"); 
    select.appendChild(option); 
}


 
//var okIcon = document.getElementById('okIcon'); 
var inp = document.getElementById("number");
var cvv = document.getElementById("cvv"); 
inp.addEventListener('input', checkInp1);
cvv.addEventListener('input', checkInp2); 


