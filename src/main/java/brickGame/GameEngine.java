package brickGame;

public class GameEngine {

    private OnAction onAction;
    private int fps = 15;
    private Thread updateThread;
    private Thread physicsThread;
    public boolean isStopped = true;

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * @param fps set fps and we convert it to millisecond
     */
    public void setFps(int fps) {
        this.fps = (int) 1000 / fps;
    }

    private synchronized void Update() {
        updateThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    onAction.onUpdate();
                    Thread.sleep(fps);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        updateThread.start();
    }

    private void Initialize() {
        onAction.onInit();
    }

    private synchronized void PhysicsCalculation() {
        physicsThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    onAction.onPhysicsUpdate();
                    Thread.sleep(fps);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        physicsThread.start();
    }

    public void start() {
        time = 0;
        Initialize();
        Update();
        PhysicsCalculation();
        TimeStart();
        isStopped = false;
    }

    public void stop() {
        if (!isStopped) {
            isStopped = true;

            updateThread.interrupt();
            physicsThread.interrupt();
            timeThread.interrupt();

            try {
                updateThread.join();
                physicsThread.join();
                timeThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private long time = 0;
    private Thread timeThread;

    private void TimeStart() {
        timeThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    time++;
                    onAction.onTime(time);
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        timeThread.start();
    }

    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }
}