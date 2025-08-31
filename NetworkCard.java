import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class NetworkCard {
    private final Random rnd = new Random();
    private final ReentrantLock lock = new ReentrantLock(true);
    private volatile boolean useSync = true;

    public NetworkCard(boolean useSync) {
        this.useSync = useSync;
    }

    public void setUseSync(boolean v) {
        this.useSync = v;
    }

    public void accessNetwork(String vmName) {
        if (useSync) {
            lock.lock();
            try {
                criticalSection(vmName);
            } finally {
                lock.unlock();
            }
        } else {
            criticalSection(vmName);
        }
    }

    private void criticalSection(String vmName) {
        String data = randomPacketFor(vmName);
        System.out.println("[VM Usuário - " + vmName + "] Requisitando acesso à rede...");
        System.out.print("[NetworkCard - Física] Recebendo pacote: ");
        tinyWork();
        System.out.println(data);
        System.out.println("[OS Núcleo - " + vmName + "_OS] Atendendo chamada: RECEIVE_DATA");
        tinyWork();
        System.out.println("[OS Núcleo - " + vmName + "_OS] Processando dados: " + data);
        System.out.println();
        tinyWork();
    }

    private String randomPacketFor(String vmName) {
        String[] templates = {
            "Hello " + vmName,
            "Ping " + vmName,
            "Data#" + (1000 + rnd.nextInt(9000)) + " -> " + vmName,
            "Metrics-" + rnd.nextInt(100) + " for " + vmName
        };
        return templates[rnd.nextInt(templates.length)];
    }

    private void tinyWork() {
        try {
            Thread.sleep(10 + rnd.nextInt(40));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
