package Hardware;

import com.pi4j.wiringpi.Gpio;


/**
 * The Class Hardware.
 */
public class Hardware 
{
	
	/** The servo moteur. */
	private ControleServo servoMoteur;
	
	/** The button listener. */
	private PiButton buttonListener;
	
	
	/**
	 * Instantiates a new hardware.
	 */
	public Hardware()
	{
		Gpio.wiringPiSetup();
		servoMoteur = new ControleServo(23);
		buttonListener = new PiButton();

	}
	
	/**
	 * Gets the servo.
	 *
	 * @return the servo
	 */
	public ControleServo getServo() 
	{
		return servoMoteur;
	}
	
	/**
	 * Gets the btn listener.
	 *
	 * @return the btn listener
	 */
	public PiButton getBtnListener()
	{
		return buttonListener;
	}
	
}

