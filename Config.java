import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Config {
    RelateFile file = new RelateFile("config.txt");
    Map<String, String> table = new TreeMap<String, String>();
    COLUMN[] columnArray = { COLUMN.ID, COLUMN.NAME, COLUMN.START, COLUMN.END, COLUMN.DEGREE, COLUMN.STATE,
            COLUMN.NUMBER, COLUMN.CATALOG, COLUMN.WORK };

    Config() {
        ArrayList<String> allLine = file.getAllLine();
        for (int i = 0; i < allLine.size(); i++) {
            String[] line = allLine.get(i).split(" *: *", 2);
            table.put(line[0], line[1]);
        }
    }

    public String getSetting(String matchStr) {
        return table.get(matchStr);
    }

    public void setAllField() {
        for (int i = 1; i < 9; i++) {
            Screen.display("New_show_" + columnArray[i].getDesc().toLowerCase() + "(0/1):");
            String input = Keyboard.input();
            while (!(input.equals("0") || input.equals("1"))) {
                Screen.display("Input_error_plaese_input_0_or_1:");
                input = Keyboard.input();
            }
            setSetting("show_" + columnArray[i].getDesc().toLowerCase(), input.equals("1") ? "true" : "false");
        }
    }

    public boolean getSortOrder() {
        return getSetting("show_sort_order").equals("des");
    }

    public COLUMN getsortColumn() {
        for (int i = 0; i < columnArray.length; i++) {
            if (getSetting("show_sort_field").equals(columnArray[i].getDesc().toLowerCase()))
                return columnArray[i];
        }
        return null;
    }

    public boolean[] getValidColumn() {
        boolean[] returnArr = { true, false, false, false, false, false, false, false, false };
        for (int i = 1; i < 9; i++) {
            returnArr[i] = getSetting("show_" + columnArray[i].getDesc().toLowerCase()).equals("true") ? true : false;
        }
        return returnArr;
    }

    public void setSetting(String setName, String setValue) {
        table.replace(setName, setValue);
    }

    public ArrayList<String> toArrayList() {
        ArrayList<String> returnList = new ArrayList<String>();
        for (String i : table.keySet()) {
            String tmp = i + ": " + table.get(i);
            returnList.add(tmp);
        }
        return returnList;
    }

    public String getFieldList() {
        String returnStr = "";
        for (int i = 1; i < 9; i++) {
            returnStr += "[" + String.valueOf(i) + "].";
            returnStr += "Show_" + columnArray[i].getDesc().toLowerCase() + ":";
            returnStr += (getSetting("show_" + columnArray[i].getDesc().toLowerCase()).equals("true")) ? "1" : "0";
            returnStr += " ";
        }
        return returnStr;
    }

}
