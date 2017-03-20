package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import Hardware.PINLogic;


public class PINLogicTest {
	
	private PINLogic test;

	@Before
	public void init()
	{
		test = new PINLogic();
	}
	
	@Test
	public void TESTCheckPINInvalidPIN() 
	{
		test.setInput("--123--");
		boolean check = test.CheckPIN();
		assertFalse(check);
	}
	
	@Test
	public void TESTCheckPINValidPIN() 
	{
		test.setInput("--12345--");
		boolean check = test.CheckPIN();
		assertTrue(check);
	}
	
	@Test
	public void TESTgetPinValueInvalidGPIOPin()
	{
		int check = test.getPinValue(25);
		assertEquals(check, 9);
	}
	@Test
	public void TESTgetPinValueValidGPIOPin4()
	{
		int check = test.getPinValue(4);
		assertEquals(check, 1);
	}
	@Test
	public void TESTgetPinValueValidGPIOPin5()
	{
		int check = test.getPinValue(5);
		assertEquals(check, 2);
	}
	@Test
	public void TESTgetPinValueValidGPIOPin6()
	{
		int check = test.getPinValue(6);
		assertEquals(check, 3);
	}
	@Test
	public void TESTgetPinValueValidGPIOPin26()
	{
		int check = test.getPinValue(26);
		assertEquals(check, 4);
	}
	
	@Test
	public void TESTgetPinValueValidGPIOPin27()
	{
		int check = test.getPinValue(27);
		assertEquals(check, 5);
	}
	
	@Test
	public void TESTbtnStartPushedFirstTime()
	{
		String out = test.BtnPushed(25);
		assertEquals(out, "startPIN = true");	
	}
	
	@Test
	public void TESTbtnPushedAfterStart()
	{
		test.setStartPIN(true);
		String out = test.BtnPushed(4);
		assertEquals(out, "input: " + test.getInput());	
	}
	
	@Test
	public void TESTbtnStartPushedAfterStart()
	{
		test.setStartPIN(true);
		String out = test.BtnPushed(25);
		assertEquals(out, "1");	
	}
	
	@Test
	public void TESTbtnStartPushedAfter3Attempts()
	{
		test.setStartPIN(true);
		test.setAttempt(4);
		String out = test.BtnPushed(25);
		assertEquals(out, "--buzzer activated--");	
	}
	
	@Test
	public void TESTbtnStartPushedGoodPIN()
	{
		test.setStartPIN(true);
		test.setInput("--12345");
		String out = test.BtnPushed(25);
		assertEquals(out, "--toggle led--");	
	}
	
}
