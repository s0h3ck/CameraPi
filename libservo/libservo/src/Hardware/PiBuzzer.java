package Hardware;

import com.pi4j.wiringpi.Gpio;

/**
 * The Class PiBuzzer.
 */
public class PiBuzzer 
{
	
	/**
	 * Instantiates a new pi buzzer.
	 * Set gpio 0 as output pin
	 */
	public PiBuzzer()
	{
		Gpio.pinMode(0, Gpio.OUTPUT);
		Gpio.digitalWrite(0, Gpio.LOW);
	}
	
	/**
	 * Activate buzzer for 1 second
	 */
	public void ActivateBuzzer()
	{
		int x = 0;
		while (x < 100)
		{
			Toggle();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			x = x + 1;
		}
		Gpio.digitalWrite(0, Gpio.LOW);
	}
	
	/**
	 * Change the output of gpio 0 from low to high or 
	 * high to low according to the current state.
	 */
	private void Toggle()
	{
		if(Gpio.digitalRead(0) == 1) // buzzer is high
			Gpio.digitalWrite(0, Gpio.LOW);
		else
			Gpio.digitalWrite(0, Gpio.HIGH);
	}
	
	
}
