var target;
var clickHandler =
	function (e) {
		// Record click only if it did not originate in the context menu (ContextMenu.js)
		if ( (e.target.className != 'ul') && (e.target.className != 'li') && (e.target.className != 'li-last') && (e.target.className != 'li-disabled') ) {
			// Create the 'target' for the element
			target = createLocator(e.target); // createLocator() located in ContextMenu.js
			// Handle select list
			if (e.target.tagName.toLowerCase() != 'select') {
				app.addTestStep('click', target, '');
			}
			else {
				// Set an event listener on the select list
				e.target.addEventListener("change", changeHandler, true);
			}
		}
	}
;

var changeHandler =
	function (e) {
		var selectedIndex = e.target.selectedIndex
		//var selectedValue = e.target..options[selectedIndex].value;
		var selectedText = e.target.options[selectedIndex].text;
		app.addTestStep('select', target, 'label=' + selectedText);
		e.target.removeEventListener("change", changeHandler, true);
	}
;
