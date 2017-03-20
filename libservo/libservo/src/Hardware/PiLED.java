package Hardware;

import com.pi4j.wiringpi.Gpio;

/**
 * The Class PiLED.
 */
public class PiLED 
{
	
	/** The pin of the red and green LED */
	private int pinRedLED, pinGreenLED;
	
	/**
	 * Instantiates a new pins for LEDs.
	 * Set the output of the red LED as High and of the green LED as Low.
	 * 
	 * @param redLED the red LED
	 * @param greenLED the green LED
	 */
	public PiLED(int redLED, int greenLED)
	{
		pinRedLED = redLED;
		pinGreenLED = greenLED;
		Gpio.pinMode(pinGreenLED, Gpio.OUTPUT);
		Gpio.digitalWrite(pinGreenLED, Gpio.LOW);
		Gpio.pinMode(pinRedLED, Gpio.OUTPUT);
		Gpio.digitalWrite(pinRedLED, Gpio.HIGH);
	}
	
	/**
	 * Change the output of gpio for LED pins from low to high or 
	 * high to low according to the current state.
	 */
	public void toggleLED()
	{
		if(Gpio.digitalRead(pinRedLED) == 1) // Red LED is high
		{
			Gpio.digitalWrite(pinGreenLED, Gpio.HIGH);
			Gpio.digitalWrite(pinRedLED, Gpio.LOW);
		}
		else
		{
			Gpio.digitalWrite(pinGreenLED, Gpio.LOW);
			Gpio.digitalWrite(pinRedLED, Gpio.HIGH);
		}			
	}
	
	/**
	 * Check if alarm system is active
	 *
	 * @return true, if successful
	 */
	public boolean systemIsArmed() {
		return Gpio.digitalRead(pinGreenLED) == 1 && Gpio.digitalRead(pinRedLED) == 0;
	}
}
