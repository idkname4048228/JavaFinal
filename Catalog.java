import java.util.ArrayList;

public class Catalog {
    RelateFile file = new RelateFile("catalog.txt");
    ArrayList<String> list = null;

    Catalog() {
        ArrayList<String> allLine = file.getAllLine();
        list = new ArrayList<String>();
        for (int i = 0; i < allLine.size(); i++) {
            String line = allLine.get(i);
            list.add(line);
        }
    }

    public ArrayList<String> getList() {
        return this.list;
    }

    public String getCatalogList() {
        String returnStr = "";
        for (int i = 0; i < list.size(); i++) {
            returnStr += "[" + String.valueOf(i + 1) + "].";
            returnStr += list.get(i);
            returnStr += " ";
        }
        return returnStr;
    }

    public String getCatalog(int index) {
        return list.get(index - 1);
    }

    public boolean checkIndex(int index) {
        index -= 1;
        return index < list.size() && index >= 0;
    }

    public boolean checkIndex(String index) {
        return checkIndex(Integer.parseInt(index));
    }

    public void add() {
        Screen.display("Please_input_new_catalog:");
        String input = Keyboard.input();
        if (input.length() > 12) {
            Screen.display("Error_catalog_to_long");
            return;
        }

        String newLine = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
        if (list.contains(newLine)) {
            Screen.display("Error_catalog_existed");
            return;
        }
        list.add(newLine);
        list.sort((p1, p2) -> p1.compareTo(p2));
        file.writeList(list);
        Screen.display("Add_catalog_" + input + "_success");
    }
}
