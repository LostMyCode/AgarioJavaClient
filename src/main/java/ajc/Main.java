package ajc;

public class Main {
    public static Main INSTANCE;
    public static GameThread thread;
    public static MainFrame frame;
    public static Game game;
    
    public Main() {}

    public static void main(String[] args) {
    	
        INSTANCE = new Main();
        thread = new GameThread();
        frame = new MainFrame();
        game = new Game();
        thread.run();
    }
    
    public static void updateGame() {
    	frame.render();
    }

}
