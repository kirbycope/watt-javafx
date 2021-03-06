var contextMenuHandler =
	function(e) {
		// Disable default context menu
		e.preventDefault(e.target);
		// Get the context menu
		var menu = document.getElementById('context-menu');
		// If the HTML for the menu is already in the DOM
		if (menu != null ) {
			// Remove 'old' context menu
			document.body.removeChild(menu);
		}
		// Add the HTML of the context menu to current page
		addHtml(e.target);
		// Adjust the Context Menu position
		adjustContextMenuPosition(e.clientX, e.clientY);
		// Add an event listener to 'hide' the menu when any click occurs
		document.addEventListener("click", hideMenu, true);
	}
;

function addHtml(eventTarget) {
	// Create the locator for the element that was right-clicked
	var target = createLocator(eventTarget);

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
	item01.onclick = function(e) {
		app.addTestStep('open', location.href, '');
		hideMenu();
	}
	// Append the item to the items container
	ulItems.appendChild(item01);

	// Item - verifyTitle
	var verifyTitle =  document.createElement('li');
	verifyTitle.className = 'li';
	verifyTitle.textContent = truncateString('verifyTitle', document.title);
	verifyTitle.onclick = function(e) {
		app.addTestStep('verifyTitle', document.title, '');
		hideMenu();
	}
	// Append the item to the items container
	ulItems.appendChild(verifyTitle);

	// Item - verifyValue
	var verifyValue =  document.createElement('li');
	verifyValue.className = 'li-disabled';
	verifyValue.textContent = 'verifyValue';
	// Handle value
	if (eventTarget.value != undefined) {
		verifyValue.className = 'li';
		verifyValue.textContent = truncateString('verifyValue', eventTarget.value);
		verifyValue.onclick = function(e) {
			app.addTestStep('verifyValue', target, eventTarget.value);
			hideMenu();
		}
	}
	// Append the item to the items container
	ulItems.appendChild(verifyValue);

	// Item - verifyText
	var verifyText =  document.createElement('li');
	verifyText.textContent = 'verifyText';
	verifyText.className = 'li-disabled';
	// TODO: Get the text and handle it
	// Append the item to the items container
	ulItems.appendChild(verifyText);

	// Item - verifyTable
	var verifyTable =  document.createElement('li');
	verifyTable.className = 'li-disabled';
	verifyTable.textContent = 'verifyTable';
	// Handle eventTarget type
	if ((eventTarget.tagName.toLowerCase() == 'th') || (eventTarget.tagName.toLowerCase() == 'td')) {
		verifyTable.className = 'li';
		verifyTable.textContent = truncateString('verifyTable', eventTarget.textContent);
		verifyTable.onclick = function(e) {
			app.addTestStep('verifyTable', target, eventTarget.textContent);
			hideMenu();
		}
	}
	// Append the item to the items container
	ulItems.appendChild(verifyTable);

	// Item - verifyElementPresent
	var verifyElementPresent =  document.createElement('li');
	verifyElementPresent.className = 'li-last';
	verifyElementPresent.textContent = truncateString('verifyElementPresent', target);
	verifyElementPresent.onclick = function(e) {
		app.addTestStep('verifyElementPresent', target, '');
		hideMenu();
	}
	// Append the item to the items container
	ulItems.appendChild(verifyElementPresent);

	// Append the items container to the menu
	divMenu.appendChild(ulItems);
	// Append the menu to the document body
	document.body.appendChild(divMenu);
	// Add event listener to hide the menu
	document.addEventListener("click", hideMenu, true);
}

function adjustContextMenuPosition(eventX, eventY) {
	// Get the Context Menu
	var contextMenu = document.getElementById("context-menu");
	// If the Mouse Event's X-position plus the Context Menu's width are greater than the ViewPort's width
	if ((eventX + contextMenu.offsetWidth) > window.innerWidth) {
		// Find out by how much of the menu is outside of the ViewPort
		var dif = (eventX + contextMenu.offsetWidth) - window.innerWidth;
		// Move the menu to the left to compensate
		contextMenu.style.left = (pageXOffset + eventX - dif) + "px";
	}
	else {
		// Set the Context Menu position to the mouse event's position
		contextMenu.style.left = (pageXOffset + eventX) + "px";
	}
	// If the Mouse Event's Y-position plus the Context Menu's height are greater than the ViewPort's height
	if ((eventY + contextMenu.offsetHeight) > window.innerHeight) {
		// Find out by how much of the menu is outside of the ViewPort
		var dif = (eventY + contextMenu.offsetHeight) - window.innerHeight;
		// Move the menu to the left to compensate
		contextMenu.style.top = (pageYOffset + eventY - dif) + "px";
	}
	else {
		// Set the Context Menu position to the mouse event's position
		contextMenu.style.top = (pageYOffset + eventY) + "px";
	}
	// Show the Context Menu
	contextMenu.style.visibility = "visible";
}

function createLocator(eventTarget) {
	var target;
	// Create locator id=
	if ( eventTarget.getAttribute('id') != null) {
		target = 'id=' + eventTarget.getAttribute('id');
	}
	// Create locator link=
	else if (eventTarget.tagName.toLowerCase() == 'a') {
		target = 'link=' + eventTarget.innerText;
	}
	// Create locator css=
	else {
		target = 'css=' + fullPath(eventTarget);
	}
	return target;
}

function hideMenu() {
	// Get the context menu
	var menu = document.getElementById('context-menu');
	// Make menu visible
	menu.style.visibility = 'hidden';
}

//http://stackoverflow.com/a/4588211/6933359
function fullPath(el) {
  var names = [];
  while (el.parentNode) {
    if (el.id){
      names.unshift('#'+el.id);
      break;
    }else{
      if (el==el.ownerDocument.documentElement) names.unshift(el.tagName);
      else{
        for (var c=1,e=el;e.previousElementSibling;e=e.previousElementSibling,c++);
        names.unshift(el.tagName+":nth-child("+c+")");
      }
      el=el.parentNode;
    }
  }
  return names.join(" > ");
}

function truncateString(command, target) {
	var text = command + ' ' + target;
	if (text.length > 40) {
		text = text.substring(0, 37) + "..."
	}
	return text;
}
