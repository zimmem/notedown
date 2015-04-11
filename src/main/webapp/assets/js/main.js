$(function() {
	var langTools = ace.require("ace/ext/language_tools");
	var editor = ace.edit("editor");
	editor.setTheme("ace/theme/monokai");
	editor.getSession().setMode("ace/mode/markdown");
	editor.setOptions({
		enableBasicAutocompletion : true,
		enableSnippets : true,
		enableLiveAutocompletion : true
	});
	langTools.addCompleter({
		getCompletions : function(editor, session, pos, prefix, callback) {
			console.info(pos);
			console.info(callback);
			if (prefix.length === 0) {
				return callback(null, []);
			} else {
				return callback(null, [{"meta":"function", "caption":"noteaddShape", "value":"addShape（）P\\{ddd\\}","score":1},]);
			}
		}
	});
	var p = $('#preview');
	editor.getSession().on('change', function(e) {
		p.html(marked(editor.getValue()));
	});
});