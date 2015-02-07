from socket import *
import subprocess

HOST='192.168.0.102'
PORT=8025
s=socket(AF_INET,SOCK_STREAM)
s.bind((HOST,PORT))
s.listen(1)
i = True
while i is True:
	try:
		conn,addr=s.accept()
		print "connected",conn
		subprocess.call("C:\Python27\FB.py 1", shell=True)
		conn.close()
	except:
		print "Error"
		
		