cmdName = "Attack"
cmdCall = "attack"

function onCall(caller)
	print "Someone called attack"
	caller:print(caller.javaClient:getUser():getName() .. ": You're attacking!!!")
end