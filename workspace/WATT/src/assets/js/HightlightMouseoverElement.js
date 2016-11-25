// http://stackoverflow.com/a/6735955/1106708
prevElement = null;

var handler =
	function(e){
		var elem = e.target || e.srcElement;
		if (prevElement!= null) {prevElement.classList.remove("mouseOn");}
		elem.classList.add("mouseOn");
		prevElement = elem;
	}
 ;