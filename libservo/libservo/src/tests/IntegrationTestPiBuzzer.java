package tests;

import Hardware.Hardware;
import Hardware.PiBuzzer;

public class IntegrationTestPiBuzzer 
{
	public static void main(String[] args) 
	{
System.out.println("Test intregration LED:");
		
		Hardware test = new Hardware();
		PiBuzzer buzzer = test.getBtnListener().getBuzzer();
		
		System.out.println("Activate buzzer");
		buzzer.ActivateBuzzer();
		System.out.println("Buzzer stopped");
	}
}
