package Hardware;


import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioInterrupt;
import com.pi4j.wiringpi.GpioInterruptEvent;
import com.pi4j.wiringpi.GpioInterruptListener;
import com.pi4j.wiringpi.GpioUtil;


/**
 * The Class PiButton.
 */
public class PiButton extends Thread 
{
	
	/** The password logic. */
	private PINLogic passwordLogic;
	
	/** The led. */
	private PiLED led;
	
	/** The buzzer. */
	private PiBuzzer buzzer;


	/**
	 * Instantiates a new pi button.
	 * Set led pin to gpio 21 and 22
	 * Instantiates a new PINlogic and buzzer
	 */
	public PiButton()
	{
		passwordLogic = new PINLogic();
		led = new PiLED(21, 22);
		buzzer = new PiBuzzer();
	}
	
	/**
	 * Gets the led.
	 *
	 * @return the led
	 */
	public PiLED getLed() {
		return led;
	}

	/**
	 * Gets the buzzer.
	 *
	 * @return the buzzer
	 */
	public PiBuzzer getBuzzer() {
		return buzzer;
	}

	/**
	 * Gets the password logic.
	 *
	 * @return the password logic
	 */
	public PINLogic getPasswordLogic() {
		return passwordLogic;
	}
	
	/**
	 * Listen to pins 4, 5, 6, 25, 26, 27 for events.
	 */
	public void listen()
	{
	  GpioInterrupt.addListener(new GpioInterruptListener() {
	        @Override
	        public void pinStateChange(GpioInterruptEvent event) {
	        	if (event.getState() == true)
	        	{
	        		String out = passwordLogic.BtnPushed(event.getPin());
	        		if(out.contains("--toggle led--"))
	        			led.toggleLED();
	        		if(out.contains("--buzzer activated"))
	        			buzzer.ActivateBuzzer();
	        			
	        	}
	        }
	    });
	  // Start Button 
	  setGpioPin(25);
	  // Button 1
	  setGpioPin(4);
	  // Button 2
	  setGpioPin(5);
	  // Button 3
	  setGpioPin(6);
	  // Button 4
	  setGpioPin(26);
	  // Button 5
	  setGpioPin(27);	  
	}

	/**
	 * Sets the gpio pin.
	 *
	 * @param pin the new gpio pin
	 */
	private void setGpioPin(int pin) 
	{
		GpioUtil.setEdgeDetection(pin, GpioUtil.EDGE_RISING);
		Gpio.pinMode(pin, Gpio.INPUT);
		Gpio.pullUpDnControl(pin, Gpio.PUD_DOWN);        
		GpioInterrupt.enablePinStateChangeCallback(pin);
	}
	
	
	
	

}
