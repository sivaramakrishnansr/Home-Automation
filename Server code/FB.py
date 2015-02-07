import socket
#Yun's IP
HOST='192.168.0.101'
PORT=8020
s=socket.socket()
s.connect((HOST,PORT))
word = ":)"
print word
if word == ":)":
	mood = "happy"
	conn.send("1")
if word == ":(":
	mood = "sad"
	conn.send("2")
if word == ":/":
	mood = "bleh"
	conn.send("3")
if word == ":D":
	mood = "laughin"
	conn.send("4")
if word == ":X":
	mood = "angry"
	conn.send("5")
if word == ":O":
	mood = "surprised"
	conn.send("6")

s.close()
