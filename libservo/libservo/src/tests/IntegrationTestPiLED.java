package tests;

import Hardware.ControleServo;
import Hardware.Hardware;
import Hardware.PiLED;

public class IntegrationTestPiLED 
{

	public static void main(String[] args) 
	{
		System.out.println("Test intregration LED:");
		
		Hardware test = new Hardware();
		PiLED led = test.getBtnListener().getLed();
		
		int x = 0;
		System.out.println("Toggle the LEDs");
		while ( x < 10)
		{
			led.toggleLED();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			x++;
		}
		

	}

}
