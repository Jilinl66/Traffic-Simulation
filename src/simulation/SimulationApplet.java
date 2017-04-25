package simulation;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Car simulation project by JILIN LIU, Linda
 * 
 * @author LiuJilin
 */
public class SimulationApplet extends JApplet{

	private static final long serialVersionUID = 1L; 
	Random r = new Random();
	Image offImage;
	Graphics offg; 
	public int W = getWidth();
	public int H = getHeight();
			
	private static final ArrayList<Car> cars = new ArrayList<Car>();
	public TrafficLight light;
	public TrafficLight light1;
	public TrafficLight light2;

	public ArrayList<Road> roads = new ArrayList<Road>();
	
	MyPanel panel;
	
	public void init() {
		panel = new MyPanel();
		panel.setVisible(true);
		this.add(panel);
		
		// start car generation thread
		carGeneration();
		
		// start traffic light thread
		light1 = new TrafficLight(0); 
		light1.start();
		// light2 thread has to wait time: light2.greenPause + light2.yellowPause 
		// + light2.leftTurnPause before it starts
		light2 = new TrafficLight(14000); 
		light2.start();
		
		// generate roads
		Road r1 = new Road(0, getHeight()/2 - 30, getWidth(), 60, "horizontal");
		Road r2 = new Road(getWidth()/2 - 30, 0, 60, getHeight(), "vertical");
		roads.add(r1);
		roads.add(r2);
		
		new Thread(){ 
			@Override
			public void run(){
				while(true){
					updateCar();
					panel.repaint();
//					repaint();
					delayCar();
				}
			}
		}.start();
	}
	
	/**
	 * This method new a thread and generate car automatically every specific
	 *  time period
	 */
	public void carGeneration(){
		new Thread(){
			@Override
			public void run(){
				while(true){
					Rectangle recL = new Rectangle(0, getHeight()/2 + 5, 40, 20);
					Rectangle recR = new Rectangle(getWidth() - 40, getHeight()/2 - 25, 40, 20);
					Rectangle recU = new Rectangle(getWidth()/2 - 25, 0, 20, 40);
					Rectangle recD = new Rectangle(getWidth()/2 + 5, getHeight() - 40, 20, 40);
					Car newcarL = new Car(recL, new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)), Direction.EAST, 4, r.nextInt(3));
					Car newcarR = new Car(recR, new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)),Direction.WEST, 4, r.nextInt(3)); 
					Car newcarU = new Car(recU, new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)), Direction.SOUTH, 4, r.nextInt(3));
					Car newcarD = new Car(recD, new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)), Direction.NORTH, 4, r.nextInt(3));
					//initialize cars from 4 directions
					if (cars.isEmpty()){
						cars.add(newcarL);
						cars.add(newcarR);
						if (light2.flag != 0){
							cars.add(newcarU);
							cars.add(newcarD);
						}
					}
					boolean canGenerateL = true;
					boolean canGenerateR = true;
					boolean canGenerateU = true;
					boolean canGenerateD = true;
					
					for(Car car: cars){
						if (car.carRec.intersects(recL))
							canGenerateL = false;
						if (car.carRec.intersects(recR))
							canGenerateR = false;
						if (car.carRec.intersects(recU))
							canGenerateU = false;
						if (car.carRec.intersects(recD))
							canGenerateD = false;
					}
					if (canGenerateL) 
						cars.add(newcarL);
					if (canGenerateR)
						cars.add(newcarR);
					if (light2.flag != 0){
						if (canGenerateU) 
							cars.add(newcarU);
						if (canGenerateD)
							cars.add(newcarD);
					}
					// tick for generate each car
					try {
						TimeUnit.SECONDS.sleep(r.nextInt(6) + 4);						
					 } catch (InterruptedException e) {
						e.printStackTrace();
					}	
				}
			}
		}.start();
	} 
	
	/**
	 * This method determines car's behavior when come across with traffic light
	 */
	public void updateCar() {
		ArrayList<Car> copy = new ArrayList<Car>(cars);
		for (Car car: copy){
			if (car.direction == Direction.EAST || car.direction == Direction.WEST)
				light = light1;
			else
				light = light2;
			//keep the car position before moving
			Rectangle temRec = (Rectangle) car.getCarRec().clone(); // pay attention to object assign value
			Direction temDir = car.getDirection();
			int line = 0;
			int beta = car.direction.getDeltaX() + car.direction.getDeltaY();
			switch(car.direction) {
				case EAST: car.setFront(car.carRec.x + car.carRec.width);
						   line = roads.get(1).leftTopX;
						   break;
				case WEST: car.setFront(car.carRec.x);
						   line = roads.get(1).leftTopX + roads.get(1).width;
						   break;
				case SOUTH: car.setFront(car.carRec.y + car.carRec.height);
						    line = roads.get(0).leftTopY;
						    break;
				case NORTH: car.setFront(car.carRec.y);
						    line = roads.get(0).leftTopY + roads.get(0).height;
						    break;
				default: break;
			}
			int deltaDis = (line - car.getFront()) * beta;
			// whether car is within Critical section
//			int critialDis;
//			if (car.direction == Direction.NORTH || car.direction == Direction.SOUTH)
//				roadLength = getHeight();
//			else 
//				roadLength = getWidth();
			if (deltaDis == ((getWidth() - 60)/2 - 40) % car.speed){
				// car will go straight in this crossrooad
				if (car.turnFlag == 0){
					if (light.flag == 1)
						car.move();
					else if (light.flag == 2 && (car.getFront() + car.speed * beta - line) * beta > 0)
						car.move();
					else if (light.flag == 3){}
				}
				// car will turn in this crossrooad
				else{
					if (car.turnFlag == 1){
						if (light.flag == 4){
							car.turn(car.turnFlag);
						}
					}	
					else if (car.turnFlag == 2){
						car.turn(car.turnFlag);
						handleConfliction(car, temRec, temDir);
					}
				}
			}
			else {
				car.move();
				handleConfliction(car, temRec, temDir);
			} 
		}
	}
	
	/**
	 * This method check possible confiction after each move, if there exist
	 *  confliction, car should recall last move
	 * @param car
	 * @return
	 */
	public void handleConfliction(Car car, Rectangle temR, Direction temD){
		ArrayList<Car> copy = new ArrayList<Car>(cars);
		for(Car c: copy){
			if(c.carRec.intersects(car.carRec) && !c.equals(car)){
				car.setCarRec(temR);
				car.setDirection(temD);
				break;
			}
		}
	}
	
	public void delayCar() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void updatePaint(Graphics g) {
//		if (offImage == null){
//			 offImage = createImage(getWidth(), getHeight());
//			 offg = offImage.getGraphics();
//		}
//		Graphics2D g2d = (Graphics2D)offg;
// 		g2d.setBackground(Color.YELLOW);
//		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		for (Car car: cars){
			g.setColor(car.color);
			g.fillRect(car.carRec.x, car.carRec.y, car.carRec.width, car.carRec.height);
		}
		paintRoad(g);
		paintLight(g, light1, 430, 240);
		paintLight(g, light2, 650, 235);
// 		g.drawImage(offImage, 0, 0, this);
// 		g2d.dispose();
	}
	
	public void paintRoad(Graphics g) {
		g.setColor(Color.BLACK);
		for (Road road: roads){
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setStroke(new BasicStroke(3));
			g2d.drawRect(road.leftTopX, road.leftTopY, road.width, road.height);
			if (road.extend.equals("vertical")){
				g.drawRect(road.leftTopX, road.leftTopY, road.width/2, (road.height - road.width)/2);
				g.drawRect(road.leftTopX, road.leftTopY + road.width + (road.height - road.width)/2, 
						road.width/2, (road.height - road.width)/2);
			}
			else{
				g.drawRect(road.leftTopX, road.leftTopY, (road.width - road.height)/2, road.height/2);
				g.drawRect(road.leftTopX + (road.width - road.height)/2 + road.height, road.leftTopY, 
						(road.width - road.height)/2, road.height/2);
			}
		} 
	}
	 
	/**
	 * paint traffic light and change light color and position according to traffic flag
	 * @param g
	 */
	public void paintLight(Graphics g, TrafficLight light, int initX, int initY){
		Font f = new Font("Arial", Font.BOLD, 20);
		g.setFont(f);
		g.setColor(Color.BLACK);
		g.fillRoundRect(initX, initY, 40, 30, 10, 10);
		g.fillRoundRect(initX + 30, initY, 40, 75, 10, 10);
		g.setColor(Color.gray);
		g.fillRect(initX + 40, initY + 75, 20, 40);
		g.setColor(Color.black);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setStroke(new BasicStroke(2));
		g2d.drawLine(630, 250, 650, 250);
		g2d.drawLine(480, 355, 480, 370);
		switch(light.flag) {
		case 1: g.drawString(light.display, initX + 80, initY + 70);
				g.setColor(Color.GREEN);
				g.fillOval(initX + 40, initY + 50, 20, 20);
				g.setColor(Color.RED);
				g.fillOval(initX + 5, initY + 5, 20, 20);
				break;
		case 2: g.drawString(light.display, initX + 80, initY + 40);
				g.setColor(Color.YELLOW);
				g.fillOval(initX + 40, initY + 25, 20, 20);
				g.setColor(Color.RED);
				g.fillOval(initX + 5, initY + 5, 20, 20);
				break;
		case 3: g.drawString(light.display, initX + 80, initY + 20);
				g.setColor(Color.RED);
				g.fillOval(initX + 40, initY + 5, 20, 20);
				g.setColor(Color.RED);
				g.fillOval(initX + 5, initY + 5, 20, 20);
				break;
		case 4: g.drawString(light.display, initX - 45, initY + 20);
				g.setColor(Color.GREEN);
				g.fillOval(initX + 5, initY + 5, 20, 20);
				g.setColor(Color.RED);
				g.fillOval(initX + 40, initY + 50, 20, 20);
				break;
		default: break;
		}
	}
	
	/**
	 * Paint components
	 * @author LiuJilin
	 */
	public class MyPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		Container parent;
		boolean fullscreen=false;
		JFrame frame = new JFrame();
		SimulationApplet simulationApplet = new SimulationApplet();
		
		public MyPanel(){
			setForeground(Color.WHITE);
			setBackground(Color.LIGHT_GRAY);
			setFont(new Font("Arial", Font.BOLD, 25));
			frame.add(this);
			frame.pack();
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(getWidth(), getHeight());
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			updatePaint(g);
		}
	}
}