package Hardware;

/**
 * The Class PINLogic.
 */
public class PINLogic 
{
	
	/** The input from the pushed buttons */
	private String input;
	
	/** The correct password */
	private String PIN;
	
	/** Start reading value of pushed buttons and append them to the input */
	private boolean startPIN;
	
	/** The count of attempts and attempt threshold. */
	private int attempt, attemptFail;

	/**
	 * Instantiates a new PIN logic.
	 * Set attempt count to 0, attempt threshold to 3,
	 * input from buttons to "--" , the correct password to
	 * "--12345--" and reading to false.
	 */
	public PINLogic()
	{
		attempt = 0;
		attemptFail = 3;
		input = "--";
		PIN = "--12345--";
		startPIN = false;
	}
	
	/**
	 * Sets the number of attempt.
	 *
	 * @param in the new attempt
	 */
	public void setAttempt(int in)
	{
		attempt = in;
	}


	/**
	 * Sets the input.
	 *
	 * @param in the new input
	 */
	public void setInput(String in)
	{
		input = in;
	}
	
	/**
	 * Gets the input.
	 *
	 * @return the input
	 */
	public String getInput()
	{
		return input;
	}
	
	/**
	 * Sets the boolean startPIN.
	 *
	 * @param in the new start PIN
	 */
	public void setStartPIN(boolean in)
	{
		startPIN = in;
	}
	
	/**
	 * Check if the input equals the correct password.
	 * Otherwise, increase the number of attempt made by the user.
	 *
	 * @return true, if equals
	 */
	public boolean CheckPIN()
	{
		boolean checkPin = false; 
		if(input.contains(PIN))
		{
			checkPin = true;
			attempt = 0;
		}
		else
			attempt++;				
		return checkPin;
	}
	
	/**
	 * Append user input if start button have been pushed.
	 * Check if the user input equals the correct password when start button is pushed twice.
	 * If input equals the password, returns a string to toggle the LEDs.
	 * Otherwise, if number of attempt is over the threshold, returns a string to activate the buzzer.  
	 * 
	 * @param numPin the gpio pin
	 * @return the string out, result after the button have been pushed
	 */
	public String BtnPushed(int numPin) 
	{
		String out = "";
		
		if(numPin == 25) //Start btn
		{
			if (startPIN)
			{
				startPIN = false;
				input += "--";
				boolean check = CheckPIN();
				System.out.println("Check PIN: " + check + " attempts: " + attempt);
				if(check)
				{
					out = "--toggle led--";
				}
				else{
					if (attempt > attemptFail){
						out = "--buzzer activated--";
					}
					else
						out = "" + attempt;
				}
				input = "--";
			}
			else
			{
				startPIN = true;
				out = "startPIN = true";
			}	
		}
		else if(startPIN)
		{
			int number = getPinValue(numPin);
			input += Integer.toString(number);	
			out = "input: " + input;
			System.out.println(out);
		}
		return out;
	}
	
	/**
	 * Gets the numerical value for the corresponding gpio pin.
	 *
	 * @param numPin the gpio pin number
	 * @return the pin numerical value
	 */
	public int getPinValue(int numPin) {
		int number = 0;
		switch(numPin)
		{
		case 4:
			number = 1;
			break;
		case 5:
			number = 2;
			break;
		case 6:
			number = 3;
			break;
		case 26:
			number = 4;
			break;
		case 27:
			number = 5;
			break;
		default:
			number = 9;
		}
		return number;

	}
}