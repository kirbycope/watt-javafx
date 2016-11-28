var target;
var clickHandler =
	function (e) {
		// Record click only if it did not originate in the context menu (ContextMenu.js)
		if ( (e.target.className != 'ul') && (e.target.className != 'li') && (e.target.className != 'li-last') && (e.target.className != 'li-disabled') ) {
			var target = createLocator(e.target);
			app.addTestStep('click', target, '');
		}
	}
;
