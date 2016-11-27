prevElement = null;
var hoverHandler =
	function(e){
		// Get context menu
		var menu = document.getElementById('context-menu');
		// If the context menu (from ContextMenu.js) exists...
		if (menu != null) {
			// If the context menu is not visible...
			if (menu.style.visibility == 'hidden') {
				updateStyles(e);
			}
		}
		else {
			updateStyles(e);
		}
	}
 ;

 function updateStyles(e) {
	// Get the element being hovered
	var elem = e.target || e.srcElement;
	// Remove hover style from previous hovered elements
	if (prevElement!= null) {prevElement.classList.remove("mouseOn");}
	// Add hover style for the currently hovered element
	elem.classList.add("mouseOn");
	// Update previous element global variable
	prevElement = elem;
 }