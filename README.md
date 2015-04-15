notedown
========

demo
----

http://notedown-demo.zimmem.com


## deploy to google appengine 

create maven resouce filter file (such as filter.properties) in project folder: 

	#evernote server , could be 'SANDBOX', 'EVERNOTE' OR 'YINXIANG'
	evernote.endpoint= SANDBOX
	# your evernote oauth key
	evernote.oauth.key = ${evernote.oauth.key}
	# your evernote oauth secret
	evernote.oauth.secret = ${evernote.oauth.secret}
	
	# your google appengine app id 
	appengine.app.id =  notedown-demo
	appengine.app.version = 1

	
run `mvn appengine:update  -Dnotedown.filter=filter.properties`