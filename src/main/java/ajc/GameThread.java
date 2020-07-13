package ajc;

public class GameThread extends Thread {
	@Override
	public void run() {
		System.out.println("GameThread: started");
		long lastFpsUpdateTime = System.currentTimeMillis();
		while(true) {
			try {
				long preTickTime = System.currentTimeMillis();
				sleep(20);
				Main.frame.render(); //update game draw
				long currentTime = System.currentTimeMillis();
				if (
					System.currentTimeMillis() - preTickTime != 0 && 
					currentTime - lastFpsUpdateTime > 1000
				) { //currentTime % 100 == 0 && 
					Main.frame.setTitle("AgarJavaClient FPS:" + 1000 / (currentTime - preTickTime));
					lastFpsUpdateTime = currentTime;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
