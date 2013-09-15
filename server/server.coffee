io = require("socket.io").listen(3060)
arr = []

io.sockets.on(
  "connection",
  (s)->
    s.on("server_hello",
      ()->
        s.join("food_server")
        io.of("food_server").emit("hello_server","you joined food_server")
    )
    s.on("client_hello",
      ()->
        rand = Math.random().toString(36).substring(8)
        console.log(rand)
        arr.push(rand)
        console.log(arr)
        s.join(rand)
        io.of(rand).emit("hello_client","you joined client with number"+rand)
    )
)

ten = (socket)->
  socket.emit("fiveSecond")