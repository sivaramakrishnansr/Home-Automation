import os


commands.getstatusoutput('wget http://192.168.1.10:8080/?action=snapshot -O output3434.jpg')
s = socket.socket()
s.connect(("192.168.1.7",4455))
f=open ("testing.jpg", "rb")
l = f.read(1024)
while (l):
    s.send(l)
    l = f.read(1024)
s.close()
