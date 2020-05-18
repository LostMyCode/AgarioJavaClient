package ajc;

public class GameThread extends Thread implements Runnable {
	@Override
	public void run() {
		while(true) {
			long preTickTime = System.currentTimeMillis();
			Main.updateGame();
			if (System.currentTimeMillis() % 100 == 0) {
				Main.frame.setTitle("AgarJavaClient" + 1000 / (System.currentTimeMillis() - preTickTime));
			}
		}
	}
}
