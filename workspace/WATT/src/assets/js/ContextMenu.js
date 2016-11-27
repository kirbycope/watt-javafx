var contextMenuHandler =
	function(e) {
		// Disable default context menu
		e.preventDefault(e.target);
		// Get the context menu
		var menu = document.getElementById('context-menu');
		// If the menu doesn't exist
		if (menu == null ) {
			// Add the HTML of the context menu to current page
			addHtml(e.target);
		}
		el = document.getElementById("context-menu");
		el.style.left = pageXOffset + e.clientX + "px";
		el.style.top = pageYOffset + e.clientY + "px";
		el.style.visibility = "visible";
		document.addEventListener("click", hideMenu, true);
	}
;

function addHtml(eventTarget) {
	// Create the Menu <div>
	var divMenu = document.createElement('div');
	divMenu.id = 'context-menu';
	// Create the items container <ul>
	var ulItems = document.createElement('ul');
	ulItems.className = 'ul';

	// Create an item
	var item01 =  document.createElement('li');
	item01.className = 'li-last';
	item01.textContent = truncateString('open', location.href);
	// Append the item to the items container
	ulItems.appendChild(item01);

	// Item - verifyTitle
	var verifyTitle =  document.createElement('li');
	verifyTitle.className = 'li';
	verifyTitle.textContent = truncateString('verifyTitle', document.title);
	// Append the item to the items container
	ulItems.appendChild(verifyTitle);
	// Item - verifyValue
	var verifyValue =  document.createElement('li');
	// Handle value
	if (eventTarget.value != undefined) {
		verifyValue.className = 'li';
		verifyValue.textContent = truncateString('verifyValue', eventTarget.value);
	}
	else {
		verifyValue.className = 'li-disabled';
		verifyValue.textContent = 'verifyValue';
	}
	// Append the item to the items container
	ulItems.appendChild(verifyValue);

	// Item - verifyText
	var verifyText =  document.createElement('li');
	// Handle text
	if (eventTarget.textContext != undefined) {
		verifyText.className = 'li';
		verifyText.textContent = truncateString('verifyText', eventTarget.textContent);
	}
	else {
		verifyValue.className = 'li-disabled';
		verifyText.textContent = 'verifyText';
	}
	// Append the item to the items container
	ulItems.appendChild(verifyText);

	// Item - verifyTable
	var verifyTable =  document.createElement('li');
	// Handle eventTarget type
	if (eventTarget.tagName == 'table') {
		verifyTable.className = 'li';
		verifyTable.textContent = 'verifyTable'; // TODO
	}
	else {
		verifyTable.className = 'li-disabled';
		verifyTable.textContent = 'verifyTable';
	}
	// Append the item to the items container
	ulItems.appendChild(verifyTable);

	// Item - verifyElementPresent
	var verifyElementPresent =  document.createElement('li');
	verifyElementPresent.className = 'li-last';
	verifyElementPresent.textContent = 'verifyElementPresent';
	// Append the item to the items container
	ulItems.appendChild(verifyElementPresent);

	// Append the items container to the menu
	divMenu.appendChild(ulItems);
	// Append the menu to the document body
	document.body.appendChild(divMenu);
	// Add event listener to hide the menu
	document.addEventListener("click", hideMenu, true)
}

function hideMenu() {
	// Get the context menu
	var menu = document.getElementById('context-menu');
	// Make menu visible
	menu.style.visibility = 'hidden';
}

function truncateString(command, target) {
	var text = command + ' ' + target;
	if (text.length > 40) {
		text = text.substring(0, 37) + "..."
	}
	return text;
}