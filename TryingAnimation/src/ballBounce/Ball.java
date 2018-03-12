package ballBounce;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Ball extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int ballSize, numberOfBalls;
	Timer time = new Timer(15, this);
	ArrayList<int[]> position;
	ArrayList<int[]> vectors;

	public Ball(int ballSize, int numberOfBalls, ArrayList<int[]> position, ArrayList<int[]> vectors) {
		this.setBackground(Color.BLACK);
		this.ballSize = ballSize;
		this.numberOfBalls = numberOfBalls;
		this.position = position;
		this.vectors = vectors;
		time.start();
	}

	public Ball(int ballSize, int numberOfBalls, int width, int height) {
		this.setBackground(Color.BLACK);
		this.ballSize = ballSize;
		this.numberOfBalls = numberOfBalls;
		this.position = new ArrayList<int[]>();
		this.vectors = new ArrayList<int[]>();
		Random random = new Random();
		int[] dimensions = new int[2];
		dimensions[0] = width - ballSize;
		dimensions[1] = height - ballSize;
		for (int i = 0; i < numberOfBalls; i++) {
			position.add(new int[2]);
			vectors.add(new int[2]);
			for (int j = 0; j < 2; j++) {
				position.get(i)[j] = random.nextInt(dimensions[j]);
				vectors.get(i)[j] = random.nextInt(5) + 1;
			}
		}
		time.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < numberOfBalls; i++) {
			if(i == 0) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.CYAN);
			}
			paintBall(position.get(i)[0], position.get(i)[1], ballSize, g);
		}
	}

	private void paintBall(int x, int y, int ballSize, Graphics g) {
		
		g.fillOval(x, y, ballSize, ballSize);
	}

	public void actionPerformed(ActionEvent arg0) {
		checkWallCollison();
		checkBallCollison();
		changePosition();
		repaint();
	}

	private void checkBallCollison() {
		for (int i = 0; i < numberOfBalls; i++) {
			for (int j = i + 1; j < numberOfBalls; j++) {
				if (checkSpace(i, j)) {
					int vX1 = vectors.get(i)[0];
					int vY1 = vectors.get(i)[1];
					vectors.get(i)[0] = vectors.get(j)[0];
					vectors.get(i)[1] = vectors.get(j)[1];
					vectors.get(j)[0] = vX1;
					vectors.get(j)[1] = vY1;
				}
			}
		}
	}

	private boolean checkSpace(int i, int j) {
		int xDifference = Math.abs(position.get(i)[0] - position.get(j)[0]);
		int yDifference = Math.abs(position.get(i)[1] - position.get(j)[1]);
		boolean xOverlap = xDifference <= this.ballSize;
		boolean yOverlap = yDifference <= this.ballSize;
		return xOverlap && yOverlap;
	}

	private void changePosition() {
		for (int i = 0; i < numberOfBalls; i++) {
			position.get(i)[0] += vectors.get(i)[0];
			position.get(i)[1] += vectors.get(i)[1];
		}
	}

	private void checkWallCollison() {
		for (int i = 0; i < numberOfBalls; i++) {
			if (position.get(i)[0] < 0 && vectors.get(i)[0] < 0) {
				vectors.get(i)[0] *= -1;
			} else if (position.get(i)[0] + ballSize > this.getWidth() && vectors.get(i)[0] > 0) {
				vectors.get(i)[0] *= -1;
			}
			if (position.get(i)[1] < 0 && vectors.get(i)[1] < 0) {
				vectors.get(i)[1] *= -1;
			} else if (position.get(i)[1] + ballSize > this.getHeight() && vectors.get(i)[1] > 0) {
				vectors.get(i)[1] *= -1;
			}
		}
	}

	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "true");
		int width = 1500, height = 1000;
		Ball test = new Ball(10, 50, width, height);
		JFrame frame = new JFrame();
		frame.setSize(width, height);
		frame.setTitle("Ball Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(test);
		frame.setVisible(true);
	}
}
