//declare all variables for each clickable buttons 
var salmon = document.getElementById('salmon'), 
    prawn = document.getElementById('prawn'),
    soup = document.getElementById('soup'),
    lamb = document.getElementById('lamb'),
    fish = document.getElementById('fish'),
    creme = document.getElementById('creme'),
    chocolate = document.getElementById('chocolate'),
    brownie = document.getElementById('brownie'),
    wine = document.getElementById('wine'), 
    beer = document.getElementById('beer'), 
    water = document.getElementById('water'),
    clicks = {}; 

var salmonItem = document.getElementById('salmonItem'),
    prawnItem = document.getElementById('prawnItem'), 
    asparagusItem = document.getElementById('asparagusItem'),
    lambItem = document.getElementById('lambItem'), 
    fishItem = document.getElementById('fishItem'), 
    cremeItem = document.getElementById('cremeItem'), 
    chocolateItem = document.getElementById('chocolateItem'), 
    brownieItem = document.getElementById('brownieItem'), 
    wineItem = document.getElementById('wineItem'),
    beerItem = document.getElementById('beerItem'),
    waterItem = document.getElementById('waterItem'), 
    
    arrayItem = [salmonItem, prawnItem, lambItem, fishItem, cremeItem, chocolateItem, brownieItem, wineItem, beerItem, waterItem, asparagusItem]; 

//counter
var count=0; 
var countel=document.getElementById("count"); 

function plus(){
    count++;
    countel.value='x'+count; 
}
function minus(){
    if(count!=0){
        count--; 
        countel.value='x'+count; 
}
    }
    




 

function click(e){
    var id = e.target.id; 
    if(!clicks[id])
        clicks[id]=0;
    clicks[id]++;
    e.target.textContent = clicks[id]; 
     
    
    
    for(i=0; i<arrayItem.length; i++){
        
        if(arrayItem[i].textContent==id){
            //arrayItem[i].style.display="block"; 
        }
    }
    }
    


salmon.addEventListener('click', click); 
prawn.addEventListener('click', click); 
soup.addEventListener('click', click); 
lamb.addEventListener('click', click); 
fish.addEventListener('click', click); 
creme.addEventListener('click', click); 
chocolate.addEventListener('click', click); 
brownie.addEventListener('click', click); 
wine.addEventListener('click', click); 
beer.addEventListener('click', click); 
water.addEventListener('click', click); 


