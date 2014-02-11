package net.pme;

import java.lang.management.ManagementFactory;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import net.pme.math.Vector3D;
import net.pme.network.Packet;
import net.pme.objects.Player;

/**
 * A basic player implementation
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class TestPlayer extends Player {
	private double xAxis = 0;
	private double yAxis = 0;
	private double zAxis = 0;
	private double rotate = 0;

	/**
	 * A Test player.
	 * 
	 * @param position
	 *            Initial position.
	 * @param front
	 *            Initial front axis.
	 * @param up
	 *            Initial up axis.
	 */
	public TestPlayer(Vector3D position, Vector3D front, Vector3D up) {
		super(position, front, up, -1);
	}

	@Override
	public void move(double elapsedTime) {
		move(new Vector3D(xAxis * elapsedTime * 3.0, yAxis * elapsedTime * 3.0,
				zAxis * elapsedTime * 6.0));
		rotateAroundFrontAxis(elapsedTime * rotate * 50.0);
		Display.setTitle(String.format("PenguinMenaceEngine Test [%.0f@%.2f]",
				1.0 / elapsedTime, ((double)ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime())*1E-9));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.pme.objects.Networkable#retrievePacket(net.pme.objects.Packet)
	 */
	@Override
	public void retrievePacket(Packet p) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.pme.objects.Player#keyboardInputHandler(int, boolean)
	 */
	@Override
	public void keyboardInputHandler(int eventKey, boolean eventKeyState) {
		if (eventKey == Keyboard.KEY_ESCAPE) {
			Test.game.stopGame();
		}
		if (eventKey == Keyboard.KEY_E) {
			if (eventKeyState) {
				rotate++;
			} else {
				rotate--;
			}
		}
		if (eventKey == Keyboard.KEY_Q) {
			if (eventKeyState) {
				rotate--;
			} else {
				rotate++;
			}
		}
		if (eventKey == Keyboard.KEY_W) {
			if (eventKeyState) {
				zAxis--;
			} else {
				zAxis++;
			}
		}
		if (eventKey == Keyboard.KEY_S) {
			if (eventKeyState) {
				zAxis++;
			} else {
				zAxis--;
			}
		}
		if (eventKey == Keyboard.KEY_A) {
			if (eventKeyState) {
				xAxis--;
			} else {
				xAxis++;
			}
		}
		if (eventKey == Keyboard.KEY_D) {
			if (eventKeyState) {
				xAxis++;
			} else {
				xAxis--;
			}
		}
		if (eventKey == Keyboard.KEY_R) {
			if (eventKeyState) {
				yAxis++;
			} else {
				yAxis--;
			}
		}
		if (eventKey == Keyboard.KEY_F) {
			if (eventKeyState) {
				yAxis--;
			} else {
				yAxis++;
			}
		}
		xAxis = Math.signum(xAxis);
		yAxis = Math.signum(yAxis);
		zAxis = Math.signum(zAxis);
		rotate = Math.signum(rotate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.pme.objects.Player#mouseInputHandler(int, boolean)
	 */
	@Override
	public void mouseInputHandler(int eventButton, boolean eventButtonState) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.pme.objects.Player#mouseMoveHandler(int, int)
	 */
	@Override
	public void mouseMoveHandler(int dx, int dy) {
		rotateAroundPitchAxis(-dy * 0.2);
		rotateAroundUpAxis(dx * 0.2);
	}

}
