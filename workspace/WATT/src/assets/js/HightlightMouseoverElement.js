prevElement = null;
var hoverHandler =
	function(e){
		var elem = e.target || e.srcElement;
		if (prevElement!= null) {prevElement.classList.remove("mouseOn");}
		elem.classList.add("mouseOn");
		prevElement = elem;
	}
 ;