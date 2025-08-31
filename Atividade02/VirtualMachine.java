import java.util.Random;

public class VirtualMachine implements Runnable {
    private final String name;
    private final NetworkCard nic;
    private final int iterations;
    private final Random rnd = new Random();

    public VirtualMachine(String name, NetworkCard nic, int iterations) {
        this.name = name;
        this.nic = nic;
        this.iterations = iterations;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            try {
                Thread.sleep(100 + rnd.nextInt(400));
                nic.accessNetwork(name);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
