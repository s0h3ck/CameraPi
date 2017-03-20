package tests;

import Hardware.ControleServo;
import Hardware.Hardware;

/**
* Hello world!
*
*/
public class TestIntegration 
{
   public static void main( String[] args )
   {
       System.out.println( "Hello World!" );
       try 
        { 
            //ControleServo monServo = new ControleServo(23);
    	    Hardware hardware = new Hardware();
    	    ControleServo monServo = hardware.getServo();
            
            System.out.println("--Test1: position preconfig --\n");
            System.out.println("--Test Set Position to 0");
            monServo.setPosTo0();
            System.out.println("PositionInDegree = " + monServo.getPositionInDegree() + "\n");
            Thread.sleep(2000);
            System.out.println("--Test Set Position to 45");
            monServo.setPosTo45();
            System.out.println("PositionInDegree = " + monServo.getPositionInDegree() + "\n");
            Thread.sleep(2000);
            System.out.println("--Test Set Position to 90");
            monServo.setPosTo90();
            System.out.println("PositionInDegree = " + monServo.getPositionInDegree() + "\n");
            Thread.sleep(2000);
            System.out.println("--Test Set Position to 135");
            monServo.setPosTo135();
            System.out.println("PositionInDegree = " + monServo.getPositionInDegree() + "\n");
            Thread.sleep(2000);
            System.out.println("--Test Set Position to 180");
            monServo.setPosTo180();
            System.out.println("PositionInDegree = " + monServo.getPositionInDegree() + "\n");
            Thread.sleep(2000);
            
            System.out.println("--Test Move Position to the left");
            //goLeft------------
            for(int i = 0; i < 20; i++)
            {
                monServo.goLeft();
                Thread.sleep(500);
            }
            System.out.println("\n");

            System.out.println("--Test Move Position to the right and pass the boundary");
            //goRight------------
            for(int j = 0; j < 25; j++)
            {
                monServo.goRight();
                Thread.sleep(200);
            }
            monServo.setPosTo90();
            Thread.sleep(1000);
            System.out.println("\n");
            
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("fin de prog");
   }
}