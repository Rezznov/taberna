print 'Loaded client file!'

Client = {
	javaClient=nil,
};

Client.__index = Client

function Client.new(j)
	print 'Made a new Client object'
	local self = setmetatable({}, Client)
	self.javaClient=j
	return self
end

function Client.print(self, str)
	self.javaClient:getOut():println(str)
	return
end

function newClient(j)
	print 'Ran Custructor'
	return Client.new(j)
end