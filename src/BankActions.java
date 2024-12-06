//Author: @WillThompson
import java.util.ArrayList;

public interface BankActions {
    void home();
    void deposit();
    void withdraw();
    void update();
    void transfer(ArrayList<User> users);
    void cancel(ArrayList<User> users);
}
