import java.util.ArrayList;
import java.util.List;

public class Hypervisor {
    public static void main(String[] args) throws InterruptedException {
        int vmCount = 2;
        int iterations = 5;
        boolean useSync = true;

        for (String a : args) {
            if (a.startsWith("--vms=")) {
                vmCount = Integer.parseInt(a.substring("--vms=".length()));
            } else if (a.startsWith("--iters=")) {
                iterations = Integer.parseInt(a.substring("--iters=".length()));
            } else if (a.equals("--nosync")) {
                useSync = false;
            } else if (a.equals("--sync")) {
                useSync = true;
            }
        }

        NetworkCard nic = new NetworkCard(useSync);
        System.out.println("=== Hypervisor iniciado. useSync=" + useSync +
                " | vmCount=" + vmCount + " | iterations=" + iterations + " ===\n");

        List<Thread> threads = new ArrayList<>();
        for (int i = 1; i <= vmCount; i++) {
            Thread t = new Thread(new VirtualMachine("VM" + i, nic, iterations), "VM-" + i);
            t.start();
            threads.add(t);
        }

        for (Thread t : threads) t.join();

        System.out.println("\n=== Hypervisor finalizado. useSync=" + useSync + " ===");
    }
}
