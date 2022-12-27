import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Account {
    RelateFile file = new RelateFile("account.txt");
    Map<String, String> table = new TreeMap<String, String>();

    Account() {
        ArrayList<String> allLine = file.getAllLine();
        for (int i = 0; i < allLine.size(); i++) {
            String[] line = allLine.get(i).split(" +", 2);
            table.put(line[0], line[1]);
        }
    }

    public boolean checkLogin(String accString, String pasString) {
        if (table.containsKey(accString))
            return table.get(accString).equals(pasString);
        return false;
    }

    public ArrayList<String> getAllAccount() {
        ArrayList<String> returnList = new ArrayList<String>();
        for (String i : table.keySet()) {
            String tmp = String.format("%-12s", i) + " " + String.format("%-12s", table.get(i));
            returnList.add(tmp);
        }
        return returnList;
    }

    public void add() {
        Screen.display("New_account:");
        String acc = Keyboard.input();
        Screen.display("New_password:");
        String pas = Keyboard.input();
        table.put(acc, pas);
    }

    public void delete() {
        Screen.display("Delete_account:");

        while (true) {
            String input = Keyboard.input();
            if (table.containsKey(input)) {
                table.remove(input);
                Screen.display("Delete_account_success");
                return;
            } else {
                Screen.display("No_account_please_try_again:");
            }
        }
    }

    public void modify() {
        Screen.display("Modify_account:");
        String match = Keyboard.input();
        while (true) {
            if (table.containsKey(match))
                break;
            Screen.display("No_account_please_try_again:");
            match = Keyboard.input();
        }
        String acc = null;
        String pas = null;

        Screen.display("New_account:");

        acc = Keyboard.input();
        if (acc.length() == 0) {
            acc = match;
        }

        Screen.display("New_password:");
        pas = Keyboard.input();
        if (pas.length() == 0) {
            pas = table.get(match);
        }

        table.remove(match);
        table.put(acc, pas);
        Screen.display("Modify_account_success");
    }

}
