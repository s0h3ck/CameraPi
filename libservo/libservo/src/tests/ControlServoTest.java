package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Hardware.ControleServo;

public class ControlServoTest {
	
	private ControleServo test;
	
	@Before
	public void executedBeforeEach()
	{
		test = new ControleServo();
	}

	@Test
	public void testpwmToDegree() 
	{
		int degree = test.pwmToDegree(10);
		System.out.println("1. testpwmToDegree : " + degree);
		assertTrue(degree == 45);
	}
	
	@Test
	public void testdegreeToPwm() 
	{
		int pwm = test.degreeToPwm(10);
		System.out.println("2. testdegreeToPwm : " + pwm);
		assertTrue(pwm == 6);
	}

	@Test
	public void testGetDegreeNegative() 
	{
		test.putDegreeInBoundaries(-69);
		assertTrue(test.getPositionInDegree() == 0);
	}
	
	@Test
	public void testGetDegreeBetweenRange() 
	{
		test.putDegreeInBoundaries(10);
		System.out.println("4. testGetDegreeBetweenRange : " + test.getPositionInPwm());
		assertTrue(test.getPositionInPwm() == 6);
	}
	
	@Test
	public void testGetDegreeOver180() 
	{
		test.putDegreeInBoundaries(666);
		assertTrue(test.getPositionInDegree() == 180);
	}

	@Test
	public void testGetPositionUnderMinimum() 
	{
		test.putPwmInBoundaries(0);
		assertTrue(test.getPositionInPwm() == 5);
	}
	@Test
	public void testGetPositionBetweenRange() 
	{
		test.putPwmInBoundaries(15);
		assertTrue(test.getPositionInPwm() == 15);
	}

	
	@Test
	public void testGetPositionOverMaximum() 
	{
		test.putPwmInBoundaries(35);
		assertTrue(test.getPositionInPwm() == 25);
	}
}
