import java.util.Random;
class Account {
    private double balance;
    public Account(double initialBalance) {
        this.balance = initialBalance;
    }
    public synchronized void deposit(double amount) {
        balance += amount;
        notify();
    }
    public synchronized void withdraw(double amount) throws InterruptedException {
        while (balance < amount) {
            wait();
        }
        balance -= amount;
    }
    public synchronized double getBalance() {
        return balance;
    }
}
public class Main {
    public static void main(String[] args) {
        Account account = new Account(1000);
        Thread depositThread = new Thread(() -> {
            Random random = new Random();
            for (int i = 0; i < 20; i++) {
                double amount = random.nextDouble() * 100;
                account.deposit(amount);
                System.out.println("Пополнено: " + amount + " | Баланс: " + account.getBalance());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread withdrawThread = new Thread(() -> {
            try {
                account.withdraw(1500);
                System.out.println("Деньги сняты. Новый баланс: " + account.getBalance());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        depositThread.start();
        withdrawThread.start();
        try {
            depositThread.join();
            withdrawThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Остаток на балансе: " + account.getBalance());
    }
}