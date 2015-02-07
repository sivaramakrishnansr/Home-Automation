#include <Servo.h> 
#include<Process.h>
#define PIR_MOTION_SENSOR 2 
Servo myservo;  
char cmd;
int pos = 0; 
int redPin = 11;
int greenPin = 10;
int bluePin = 9;
void setup() {
  Bridge.begin();   // Initialize the Bridge
  Serial.begin(115200);
  Serial1.begin(115200);
  pinMode(7,OUTPUT);
  pinMode(8,OUTPUT);
  digitalWrite(7,HIGH);
  digitalWrite(8,HIGH);
  digitalWrite(13,LOW);
  myservo.attach(6);
  myservo.write(180);
}

void loop() {
  // put your main code here, to run repeatedly:
        int sensorValue = digitalRead(PIR_MOTION_SENSOR);
	if(sensorValue == HIGH)//if the sensor value is HIGH?
	{
		 digitalWrite(13,HIGH);
                 Process p;
                 p.runShellCommand("/root/run_my_picture.sh");

                 // do nothing until the process finishes, so you get the whole output:
                  while (p.running()){ ; }
                
                  while (p.available()) { ; }
                  delay(5000);  
                  digitalWrite(13,LOW);
	}
        if(Serial1.available()>0)
        {
        cmd=(char)Serial1.read();
            if(cmd=='0'){
                        digitalWrite(7,LOW);
                        }
            else if(cmd=='1'){
                        digitalWrite(7,HIGH);
                        }
            else if(cmd=='2'){
                        digitalWrite(8,LOW);
                        }
            else if(cmd=='3'){
                        digitalWrite(8,HIGH);
                        }
            else if(cmd=='4'){
                        digitalWrite(13,HIGH);
                        for(pos = 180; pos>=95; pos-=1)     // goes from 180 degrees to 0 degrees 
                        {                                
                              myservo.write(pos);              // tell servo to go to position in variable 'pos' 
                              delay(15);                       // waits 15ms for the servo to reach the position 
                        } 
                        delay(5000);
                        myservo.write(180); 
                        }
            else if(cmd=='5'){
                        setColor(250, 105, 0);
                        }
            else if(cmd=='6'){
                        setColor(250, 40, 0);
                        }
            else if(cmd=='7'){
                        setColor(255, 0, 0);  
                        }
            else if(cmd=='8'){
                         setColor(10, 10, 255);  
                         }
            }
}

void setColor(int red, int green, int blue)
{
    analogWrite(redPin, 255-red);
    analogWrite(greenPin, 255-green);
    analogWrite(bluePin, 255-blue);
}
