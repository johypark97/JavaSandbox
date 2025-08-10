package javasandbox;

public record BlockingTask(long millis, long interval) implements Runnable {
    public BlockingTask() {
        this(1000);
    }

    public BlockingTask(long mills) {
        this(mills, 100);
    }

    @Override
    public void run() {
        boolean interrupted = false;

        for (long x = System.currentTimeMillis() + millis; System.currentTimeMillis() < x; ) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }

        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }
}
