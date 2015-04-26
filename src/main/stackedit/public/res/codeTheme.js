define([
    'eventMgr',
    'text!../res-min/code.styles/default.css'
], function(eventMgr, themeCss){
	
	var codeTheme = {};
	
	eventMgr.addListener('onReady', function(){
		var element = codeTheme.element = document.createElement('style');
		element.innerHTML = themeCss;
		element.id = "code-theme";
		document.head.appendChild(element);
		eventMgr.onCodeThemeInited(codeTheme);
		
	});
	
	eventMgr.onCodeThemeCreated(codeTheme);
	return codeTheme;
	
});