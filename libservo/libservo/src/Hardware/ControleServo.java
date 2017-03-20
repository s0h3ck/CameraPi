package Hardware;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

/**
 * Class controleServo controls the servo HS-422 with a pwm output to a GPIO of the RaspberryPI
 * @author Charles-Étienne et Marc-Éric
 * 
 *
 */
public class ControleServo {

	private static final int positionMax = 25;
	private static final int positionMin = 5;
	private int pinPwmOutput = 23;
	//position is in pwm standart, and not in degrees
	private int position = (positionMax + positionMin) / 2;
	
	/**
	 * for test
	 */
	public ControleServo() // pour les tests
	{
		
	}
	
	/**Constructor of the RaspberryPI with the input parameter of the pin whitch will be set the pwm
	 * @param pinOutput
	 */
	public ControleServo(int pinOutput)
	{
		pinPwmOutput = pinOutput;
//		softPwmCreate();
		setServoPosition();
		System.out.println("Servo cree");
	}
	
	/**
	 * Creates softPwmOutput to the GPIO pin of the RaspberryPI
	 */
	public void softPwmCreate() {
		SoftPwm.softPwmCreate(pinPwmOutput, 0, 200);
		Gpio.pinMode(21, Gpio.OUTPUT);
		Gpio.digitalWrite(21, Gpio.HIGH);
	}
	
	
	/** End control of output softpwm
	 * 
	 */
	public void close()
	{
		SoftPwm.softPwmStop(pinPwmOutput);
	}
	
	/** Convert the pwm to his equivalent in degree
	 * @param pwm pwm to convert in degree
	 * @return int: position in degree
	 */
	public int pwmToDegree(int pwm) 
	{
		float degree = (float)(pwm - positionMin)/(positionMax - positionMin);
		degree *= 180;
		return (int)degree;
	}
	
	/** Convert degree to pwm
	 * @param degree position to convert in pwm
	 * @return int : position in pwm
	 */
	public int degreeToPwm(int degree) 
	{
		float pwm = ((float)degree/180);
		pwm *= positionMax - positionMin;
		return (int)pwm + positionMin;
	}
	
	/** Set servo position with the position stored in memory
	 * 
	 */
	public void setServoPosition() 
	{
		SoftPwm.softPwmWrite(pinPwmOutput, position);
	}
	
	/** Set position to the servo, will protect the limits of the Maximum and Minimum position
	 * @param pos : position to send to the servo
	 */
	public void setPositionInPwm(int pos)
	{
		putPwmInBoundaries(pos);
		setServoPosition();
		System.out.println("PositionInPwm = " + position);
	}
	
	/**Set position to limits boundaries of the servo.
	 * @param degree: position in degree.
	 */
	public void putDegreeInBoundaries(int degree) 
	{
		if (degree < 0) {
			degree = 0;
		} else if (degree > 180) {
			degree = 180;
		}
		position = degreeToPwm(degree);
	}
	
	/**Convert the position in input to a bounded position associated to the maximum and the minimum acceptable
	 * @param pos position to modify
	 */
	public void putPwmInBoundaries(int pos) 
	{
		if (pos < positionMin) 
		{
			position = positionMin;
		} 
		else if (pos > positionMax) 
		{
			position = positionMax;
		} 
		else 
		{
			position = pos;
		}
	}
	
	/**Return the actual position of the servo located in the class ControleServo
	 * @return position: position du servomoteur présentement en mémoire
	 */
	public int getPositionInPwm()
	{
		return position;	
	}
	
	/*////////////////////////////////////////
	// Functions for buttons on the website //
	////////////////////////////////////////*/
	 
	//Function to use in the text field on the website
	
	/** Set the position of the servo from input degree. Protection for max and min position
	 * @param degree: position in degree.
	 */
	public void setPositionInDegree(int degree) {
		putDegreeInBoundaries(degree);
		setServoPosition();
	}
	
	//To display the current position
	
	/**
	 * @return position: actual position in degree
	 */
	public int getPositionInDegree() 
	{
		return pwmToDegree(position);
	}
	
	//Buttons for hardcoded positions
	/**
	 * Hardcode the position to 0 degree for a button in the web
	 */
	public void setPosTo0() 
	{
		setPositionInDegree(0);
	}
	
	/**
	 * Hardcode the position to 45 degree for a button in the web
	 */
	public void setPosTo45() 
	{
		setPositionInDegree(45);
	}
	
	/**
	 * Hardcode the position to 90 degree for a button in the web
	 */
	public void setPosTo90() 
	{
		setPositionInDegree(90);
	}
	
	/**
	 * Hardcode the position to 135 degree for a button in the web
	 */
	public void setPosTo135() 
	{
		setPositionInDegree(135);
	}
	
	/**
	 * Hardcode the position to 180 degree for a button in the web
	 */
	public void setPosTo180()
	{
		setPositionInDegree(180);
	}
	
	/**
	 * Move servo to the right from one unit increment
	 */
	public void goRight() {
		if (position + 1 <= positionMax) 
		{
			position++;
			setPositionInPwm(position);
		}
	}
	
	/**
	 * Move servo to the left from one unit increment
	 */
	public void goLeft() {
		if (position - 1 >= positionMin) 
		{
			position--;
			setPositionInPwm(position);
		}
	}
}
