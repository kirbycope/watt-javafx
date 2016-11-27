var contextMenuHandler =
	function(e) {
		// Disable default context menu
		e.preventDefault();
		// Get the context menu
		var menu = document.getElementById('context-menu');
		// If the menu doesn't exist
		if (menu == null ) {
			// Add the HTML of the context menu to current page
			addHtml();
		}
		el = document.getElementById("context-menu");
		el.style.left = pageXOffset + e.clientX + "px";
		el.style.top = pageYOffset + e.clientY + "px";
		el.style.visibility = "visible";
		document.addEventListener("click", hideMenu, true);
	}
;

function addHtml() {
	// Create the Menu <div>
	var divMenu = document.createElement('div');
	divMenu.id = 'context-menu';
	// Create the items container <ul>
	var ulItems = document.createElement('ul');
	ulItems.className = 'ul';
	// Create an item
	var item01 =  document.createElement('li');
	item01.className = 'li-last';
	item01.textContent = 'open ' + location.href;
	// Create an item
	//var item01 =  document.createElement('li');
	//item01.textContent = 'Example 01';
	//item01.onclick = function(e) {
		// Do something
	//}
	// Append the item to the items container
	ulItems.appendChild(item01);
	// Append the items container to the menu
	divMenu.appendChild(ulItems);
	// Append the menu to the document body
	document.body.appendChild(divMenu);
	//
	document.addEventListener("click", hideMenu, true)
}

function hideMenu() {
	// Get the context menu
	var menu = document.getElementById('context-menu');
	// Make menu visible
	menu.style.visibility = 'hidden';
}