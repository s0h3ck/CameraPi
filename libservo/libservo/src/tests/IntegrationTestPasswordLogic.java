package tests;

import Hardware.ControleServo;
import Hardware.Hardware;
import Hardware.PiButton;
import Hardware.PiLED;

public class IntegrationTestPasswordLogic {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		System.out.println("Tests Hardware:");
		
		Hardware test = new Hardware();

		PiButton btn = test.getBtnListener();
		
		btn.listen();
		
		while (true)
		{
			try 
			{
				Thread.sleep(50);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

}
