//declare all variables for each clickable buttons 
var salmon = document.getElementById('salmon'), 
    prawn = document.getElementById('prawn'),
    soup = document.getElementById('soup'),
    lamb = document.getElementById('lamb'),
    fish = document.getElementById('fish'),
    creme = document.getElementById('creme'),
    chocolate = document.getElementById('chocolate'),
    brownie = document.getElementById('brownie'),
    clicks = {}; 

function click(e){
    var id = e.target.id; 
    if(!clicks[id])
        clicks[id]=0;
    clicks[id]++;
    e.target.textContent = clicks[id];
}

salmon.addEventListener('click', click); 
prawn.addEventListener('click', click); 
soup.addEventListener('click', click); 
lamb.addEventListener('click', click); 
fish.addEventListener('click', click); 
creme.addEventListener('click', click); 
chocolate.addEventListener('click', click); 
brownie.addEventListener('click', click); 
