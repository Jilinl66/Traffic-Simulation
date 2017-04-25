package simulation;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

/**
 * Define car object and relative functions
 * 
 * @author LiuJilin
 */
public class Car {
		Rectangle carRec;
		Color color;
		Direction direction;
		int speed;
		int turnFlag;
		int front = 0;
		
		Random r = new Random();

		public Car(){
			carRec.x = 0;
			carRec.y = 100;
			carRec.width = 40;
			carRec.height = 20;
			color = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
			direction = Direction.EAST;
			speed = 10; // millionsecond
			turnFlag = 1;
		}
		
		public Car(Rectangle carRec, Color color, Direction direction, int speed, int turnFlag) {
			super();
			this.carRec = carRec;
			this.color = color;
			this.direction = direction;
			this.speed = speed;
			this.turnFlag = turnFlag;
		}

		public Object clone(){  
		    try{  
		        return super.clone();  
		    }catch(Exception e){ 
		        return null; 
		    }
		}
		
		public Rectangle getCarRec() {
			return carRec;
		}


		public void setCarRec(Rectangle carRec) {
			this.carRec = carRec;
		}


		public Direction getDirection() {
			return direction;
		}


		public void setDirection(Direction direction) {
			this.direction = direction;
		}


		public int getFront() {
			return front;
		}

		public void setFront(int front) {
			this.front = front;
		}

		public void move() {
			carRec.x = carRec.x + speed * direction.getDeltaX();
			carRec.y = carRec.y + speed * direction.getDeltaY();
		} 
		
		public void turn(int turnFlag) {
			int temW = this.carRec.width;
			int temH = this.carRec.height;
			this.carRec.width = temH;
			this.carRec.height = temW;
			// 1 means turn left
			if (turnFlag == 1){
				switch(this.direction){
					case EAST: this.direction = Direction.NORTH;
							   this.setCarRec(new Rectangle(605, 330, 20, 40));
							   break;
					case NORTH: this.direction = Direction.WEST;
								this.setCarRec(new Rectangle(530, 375, 40, 20));
							    break;
					case WEST: this.direction = Direction.SOUTH;
							   this.setCarRec(new Rectangle(575, 430, 20, 40));
							   break;
					case SOUTH: this.direction = Direction.EAST;
								this.setCarRec(new Rectangle(630, 405, 40, 20));
								break;
				}
			}
			//2 means turn right
			else if (turnFlag == 2){
				switch(this.direction){
					case EAST: this.direction = Direction.SOUTH;
							   this.setCarRec(new Rectangle(575, 430, 20, 40));
							   break;
					case NORTH: this.direction = Direction.EAST;
								this.setCarRec(new Rectangle(630, 405, 40, 20));	
							    break;
					case WEST: this.direction = Direction.NORTH;
							   this.setCarRec(new Rectangle(605, 330, 20, 40));
							   break;
					case SOUTH: this.direction = Direction.WEST;
								this.setCarRec(new Rectangle(530, 375, 40, 20));
								break;
				}
			}
		}  
	}