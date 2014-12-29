package se.arun.Game2D;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import se.arun.Game2D.gfx.SpriteSheet;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 160;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 3;
	public static final String NAME = "Game";

	private JFrame frame;

	public boolean running = false;
	public int updateCounter = 0;
	
	private BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
	
	
	// imported stuff
	private SpriteSheet spriteSheet = new SpriteSheet("/SpriteSheet.png");
	
	
	public Game() {
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		frame = new JFrame(NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();// keep everything sized correctly
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

	}

	public synchronized void Start() {
		running = true;
		new Thread(this).start();
	}

	public synchronized void stop() {
		running = false;
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerUpdate = 1000000000D / 60D; // how many nano seconds have

		int updates = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double deltaTime = 0; // elapsed time since last Time

		while (running) {
			long now = System.nanoTime();
			deltaTime += (now - lastTime) / nsPerUpdate;
			lastTime = now;
			boolean shouldRender = true;
			
			while (deltaTime >= 1) {
				updates++;
				Update();
				deltaTime -= 1;
				shouldRender = true;
			}
			
			try { Thread.sleep(2); } catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (shouldRender) {
				frames++;
				Render();
			}

			if (System.currentTimeMillis() - lastTimer > -1000) {
				lastTimer += 1000;
				System.out.println(frames + " : " + updates);
				frames = 0;
				updates = 0;
			}
			// System.out.println(frames + " : " + updates);
		}

	}

	public void Update() {
		updateCounter++;
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = i  +  updateCounter;
		}
	}

	public void Render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth(), getHeight());
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		new Game().Start();
	}

}
