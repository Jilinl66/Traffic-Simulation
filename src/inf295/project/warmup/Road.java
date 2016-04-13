package inf295.project.warmup;
/**
 * Road object
 * 
 * @author LiuJilin
 */
public class Road {
	int leftTopX;
	int leftTopY;
	int width;
	int height;
	String extend;
	
	public Road(int leftTopX, int leftTopY, int width, int height, String extend) {
		super();
		this.leftTopX = leftTopX;
		this.leftTopY = leftTopY;
		this.width = width;
		this.height = height;
		this.extend = extend;
	}

}
