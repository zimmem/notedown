$(function() {

	var Notedown = {
		server : location.origin,
		guid : function() {
			function S4() {
				return (((1 + Math.random()) * 0x10000) | 0).toString(16)
						.substring(1);
			}
			return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-"
					+ S4() + S4() + S4());
		}
	};

	var User = Backbone.Model.extend({
		url : "/evernote/user",
		initialize : function() {
			this.on("error", function() {
				console.info(auguments);
			});
			this.on("sync", function() {
				console.info(arugments);
			})
		}
	});

	var Note = Backbone.Model.extend({
		urlRoot : '/evernote/notes',
		idAttribute : 'guid',
		initialize : function() {
		},
		saveLocal : function() {
			if (!this.get('local_guid')) {
				this.set('local_guid', Notedown.guid());
			}
			localStorage.setItem("note." + this.get('local_guid'), JSON
					.stringify(this.toJSON()));
		}
	});

	var NoteList = Backbone.Collection.extend({
		url : '/evernote/notes'
	});

	var Mask = Backbone.View.extend({
		el : "#mask",
		show : function() {
			this.$el.show();
		},
		hide : function() {
			this.$el.hide();
		}
	});

	var mask = new Mask();

	var TogglePannel = Backbone.View.extend({
		toggle : function() {
			this.$el.toggleClass('pop');
			if (this.$el.hasClass('pop')) {
				mask.show();
			} else {
				mask.hide();
			}
		}
	});

	var menu = new TogglePannel({
		el : '#user-pannel'
	});
	var NoteListPannel = TogglePannel.extend({
		el : '#note-pannel',
		events : {
			'click .switcher' : 'toggle',
			'click #note-list-holder li' : 'openNote'

		},
		_render : template('template-note-list'),
		initialize : function() {
			var that = this;
			var nodeList = new NoteList();
			nodeList.fetch({
				success : function() {
					that.render(nodeList.toJSON());
				}
			});
		},
		render : function(nodeList) {
			this.$('#note-list-holder').html(this._render({
				list : nodeList
			}));
		},
		openNote : function(e) {
			var that = this;
			var guid = $(e.currentTarget).data('guid');
			var note = new Note({
				guid : guid
			});
			note.fetch({
				success : function() {
					debugger;
					that.trigger('note.open', note);
				}
			});
			this.toggle();
		}
	});

	var NoteEditor = Backbone.View.extend({
		el : '#editor-wrapper',
		editor : null,
		preview : null,
		events : {
			'click #btn-save' : 'save'
		},
		initialize : function() {
			var langTools = ace.require("ace/ext/language_tools");
			var that = this;
			var editor = this.editor = ace.edit("editor");
			editor.setTheme("ace/theme/monokai");
			editor.getSession().setMode("ace/mode/markdown");
			editor.setFontSize(18);
			editor.setOptions({
				enableBasicAutocompletion : true,
				enableSnippets : true,
				enableLiveAutocompletion : true
			});
			langTools.addCompleter({
				getCompletions : function(editor, session, pos, prefix,
						callback) {
					// console.info(pos);
					// console.info(callback);
					if (prefix.length === 0) {
						return callback(null, []);
					} else {
						return callback(null, [ {
							"meta" : "function",
							"caption" : "noteaddShape",
							"value" : "addShape（）P\\{ddd\\}",
							"score" : 1
						}, ]);
					}
				}
			});
			var p = this.preview = $('#preview');
			editor.getSession().on('change', function(e) {
				that.cache();

			});

			editor.commands.addCommand({
				name : 'markdown-save',
				bindKey : {
					win : 'Ctrl-S',
					mac : 'Command-S'
				},
				exec : function() {
					that.save();
				}
			});

		},
		cache : function() {
//			markked.setOptions({
//				renderer : new marked.Renderer(),
//				gfm : true,
//				tables : true,
//				breaks : false,
//				pedantic : false,
//				sanitize : true,
//				smartLists : true,
//				smartypants : false
//			});
			var renderer = new marked.Renderer();
			var title = null;
			renderer.heading = function(text, level, raw) {
				if (!title && level == 1) {
					title = text;
				}
				return "<h" + level + '>' + text + "</h" + level + ">\n";
			}
			var html = '<div style="position:absolute;">';
			html += marked(this.editor.getValue(), {
				renderer : renderer,
				xhtml : true
			});
			html += '<center style="display:none;">' + this.editor.getValue()
					+ '</center>';
			html += '</div>';
			this.preview.html(html);
			if (!this.currentNote) {
				this.currentNote = new Note();
			}
			console.info(html);
			this.currentNote.set({
				title : title ? title : 'Untitle note',
				content : html,
				markdown : this.editor.getValue()
			});
			this.currentNote.saveLocal();
		},
		save : function() {
			if (this.currentNote) {
				this.currentNote.save();
			}
		},
		update : function(note) {
			this.currentNote = note;
			var markdown = $(note.get('content')).find('center').text();
			this.editor.setValue(markdown);
			this.editor.moveCursorTo(0, 0);
		}
	});

	var notePannel = new NoteListPannel();
	var editor = new NoteEditor();
	editor.listenTo(notePannel, 'note.open', function(note) {
		debugger;
		editor.update(note);
	})

});