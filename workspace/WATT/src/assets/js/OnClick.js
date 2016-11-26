var target;
var clickHandler =
	function (e) {
		if ( e.target.getAttribute('id') != null) {
			target = 'id=' + e.target.getAttribute('id');
		}
		else if (e.target.tagName.toLowerCase() == 'a') {
			target = 'link=' + e.target.innerText;
		}
		else {
			target = 'css=' + fullPath(e.target);
		}
		app.addTestStep(target);
	}
;

// http://stackoverflow.com/a/4588211/6933359
function fullPath(el){
  var names = [];
  while (el.parentNode){
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