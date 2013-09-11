io = require("socket.io").listen(3060)


io.sockets.on(
  "connection",
  (s)->
    s.emit("helloWorld")
    s.emit("hello",{"what":"is this"})
    setInterval(
      ->
        ten(s)
      5000
    )
)

ten = (socket)->
  socket.emit("fiveSecond")