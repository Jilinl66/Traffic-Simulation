package inf295.project.warmup;

 /**
 * This class is class for traffic light. The light change indepedently
 * from other classes.
 * 
 * @author LiuJilin
 */
public class TrafficLight implements Runnable{
	public int pause, greenPause, redPause, yellowPause, leftTurnPause;
	public int flag;
	public String display = "Go";
	int initPause;
	Thread thread = new Thread(this);

	public TrafficLight(int initPause) {
		greenPause = 8000;
		redPause = 14000;
		yellowPause = 2000;
		leftTurnPause =  4000;
		this.initPause = initPause;
		flag = 0;
	}
	
	public void run() {
		try {
			Thread.sleep(initPause);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		flag = 1;
		while(true){
			System.out.println("Traffic light thread it is running!");
			switch(flag){
			// green light
		 	case 1: try {
						Thread.sleep(greenPause);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					flag = 2;
					display = "Ready";
					break; 
			// yellow light
			case 2: try {
						Thread.sleep(yellowPause);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					flag = 3;
					display = "Stop";
					break;
			// red light
			case 3: try {
				 		Thread.sleep(redPause);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					flag = 	4;
					display = "Left";
					break;
			// left turn light
			case 4: try {
						Thread.sleep(leftTurnPause);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					flag = 	1;
					display = "Go";
					break;
			default: break;
			}
		}
	}
		
	public void start(){
		thread.start();
	}
	
	@SuppressWarnings("deprecation")
	public void stop(){
		thread.stop();
	}
}
