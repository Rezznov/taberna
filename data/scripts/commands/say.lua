cmdName = "Say"
cmdCall = "say"

function onCall(client)
	client:print(client.user.name .. ": You said something...?")
end