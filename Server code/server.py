import socket
from socket import *
import serial

ser = serial.Serial('/dev/ttyATH0', 115200)
HOST = '192.168.0.104' 
PORT = 8020
s = socket(AF_INET, SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(1) 
conn, addr = s.accept() 
print 'Connected by', addr 
i = True
while i is True:
        data = conn.recv(1024) 
        print data
        ser.write(data)


