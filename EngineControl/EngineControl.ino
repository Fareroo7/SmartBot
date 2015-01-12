#include <SimpleTimer.h>
#include <EngineController.h>
#include <EngineTask.h>
#include <Engine.h>
#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>

AndroidAccessory acc("Smartbot",
		     "EC",
		     "Engine Control",
		     "1.0",
		     "http://www.android.com",
		     "4200000123456789");

byte packet[9] = {EngineTask::START, 0xff, EngineTask::INSERT, EngineTask::FORWARD, 150, 100, 50, 140, EngineTask::END };

Engine one(3, 2, 31);
Engine two(4, 5, 32);
Engine three(7, 6, 33);
Engine four(8, 9, 34);
Engine five(11, 10, 35);
Engine six(12, 13, 36);

EngineController controller(one, three, five, two, four, six);

SimpleTimer timer;

void setup() {
  Serial.begin(9600);
  acc.powerOn();
  /*
  EngineTask testTask(packet);
  sendAckTest(controller.handle(testTask));
  testTask.setDuration(2,54);
  testTask.setDutyCycleLeft(100);
  testTask.setDutyCycleRight(50);
  testTask.setDirectionCode(EngineTask::BACKWARD);
  sendAckTest(controller.handle(testTask));
  testTask.setDuration(36,54);
  testTask.setDutyCycleLeft(80);
  testTask.setDutyCycleRight(150);
  testTask.setDirectionCode(EngineTask::FORWARD);
  sendAckTest(controller.handle(testTask));
  testTask.setDuration(20,54);
  testTask.setDutyCycleLeft(20);
  testTask.setDutyCycleRight(200);
  testTask.setDirectionCode(EngineTask::BACKWARD);
  sendAckTest(controller.handle(testTask));
  sendAckTest(controller.handle(testTask));
  sendAckTest(controller.handle(testTask));
  sendAckTest(controller.handle(testTask));
  sendAckTest(controller.handle(testTask));
  controller.stop();
  delay(1000);
  */
  controller.start();
  callMeLater(); 
}

void loop() {
  
  if(acc.isConnected()){
      int len  = acc.read(packet, sizeof(packet), 1);
      if (len > 0) {
         unsigned int error = controller.doTask(EngineTask(packet));
         sendAckTest(error);
      }
  }
  //timer.run();
}

void callMeLater(){
  unsigned long out = controller.doNext();
  long time = out >> 16;
  byte id = (out >> 8) & 0xff;
  byte ack = out & 0xff;
  Serial.println(time);
  Serial.println(id);
  Serial.println(ack);
  timer.setTimeout(time, callMeLater);
}

void sendAck(unsigned int data){
  byte output[] = { EngineTask::START, (data >> 8) & 0xff, data & 0xff, EngineTask::END };
  acc.write(output, sizeof(output));
}

void sendAckTest(unsigned int data){
  byte output[] = { EngineTask::START, (data >> 8) & 0xff, data & 0xff, EngineTask::END };
  Serial.print((data >> 8) & 0xff);
  Serial.print(", ");
  Serial.println(data & 0xff);
}

void sendAck(byte id, byte err){
  byte output[] = { EngineTask::START, id, err, EngineTask::END };
  acc.write(output, sizeof(output));
}

void outputReturn(unsigned long data){
  Serial.print((data >> 16));
  Serial.print(", ");
  Serial.print((data >> 8) & 0xff);
  Serial.print(", ");
  Serial.println(data & 0xff);
}

