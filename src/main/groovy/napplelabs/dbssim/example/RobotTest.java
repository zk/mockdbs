package napplelabs.dbssim.example;

import java.awt.AWTException;
import java.awt.Robot;

public class RobotTest {
	public static void main(String[] args) throws AWTException {
		Robot robot = new Robot();
		
		robot.mouseMove(100, 100);
	}
}
