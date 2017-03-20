package tests;

import Hardware.ControleServo;
import Hardware.Hardware;
import Hardware.PiButton;
import Hardware.PiBuzzer;
import Hardware.PiLED;

public class IntegrationTestHardware 
{
	public static void main(String[] args) 
	{
		System.out.println("Tests Hardware:");
		
		Hardware test = new Hardware();
		
		ControleServo servo = test.getServo();
		

		PiButton btn = test.getBtnListener();
		
		PiLED led = btn.getLed();
		PiBuzzer buzzer = btn.getBuzzer();
		
		btn.listen();
		
		System.out.println("toggle LED");
		led.toggleLED();
		try 
		{
			Thread.sleep(1000);
			
			servo.setPosTo0();
			Thread.sleep(2000);
			servo.setPosTo45();
			Thread.sleep(2000);
			servo.setPosTo90();
			Thread.sleep(2000);

		
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("toggle LED");
		led.toggleLED();
		
		buzzer.ActivateBuzzer();
		
	}
}
