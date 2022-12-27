import java.util.ArrayList;

public class Data {
    RelateFile file = new RelateFile("data.txt");
    ArrayList<String[]> database = null;
    int[] ruleLength = { 5, 13, 9, 9, 9, 13, 9, 13, 27 };
    COLUMN[] columnArray = { COLUMN.ID, COLUMN.NAME, COLUMN.START, COLUMN.END, COLUMN.DEGREE, COLUMN.STATE,
            COLUMN.NUMBER, COLUMN.CATALOG, COLUMN.WORK };

    Data() {
        ArrayList<String> allLine = file.getAllLine();
        database = new ArrayList<String[]>();
        for (int i = 0; i < allLine.size(); i++) {
            String[] line = allLine.get(i).split(" +", 9);
            database.add(line);
        }
    }

    public int getSize() {
        return database.size();
    }

    public String getColumns(boolean[] vaildColumn) {
        String returnStr = "";
        for (int i = 0; i < 9; i++) {
            if (vaildColumn[i]) {
                String tmp = "[" + columnArray[i].getDesc() + "]";
                for (int j = tmp.length(); j < ruleLength[i]; j++) {
                    tmp += " ";
                }
                returnStr += tmp;
            }
        }
        return returnStr;
    }

    public ArrayList<String> getData(Config config) {
        boolean[] vaildColumn = config.getValidColumn();
        ArrayList<String> returnList = new ArrayList<String>();
        for (int i = 0; i < getSize(); i++) {
            if (database.get(i) == null)
                continue;
            returnList.add(format(database.get(i), vaildColumn));
        }
        return returnList;
    }

    public ArrayList<String> getRawData(Config config) {
        ArrayList<String> allLine = file.getAllLine();
        ArrayList<String[]> rawData = new ArrayList<String[]>();
        for (int i = 0; i < allLine.size(); i++) {
            String[] line = allLine.get(i).split(" +", 9);
            rawData.add(line);
        }
        boolean[] vaildColumn = config.getValidColumn();
        ArrayList<String> returnList = new ArrayList<String>();
        for (int i = 0; i < getSize(); i++) {
            returnList.add(format(rawData.get(i), vaildColumn));
        }
        return returnList;
    }

    public ArrayList<String> getData(Config config, COLUMN chooseColumn, String target) {
        if (chooseColumn == COLUMN.ID)
            target = formatID(target);

        boolean[] vaildColumn = config.getValidColumn();
        int index = chooseColumn.getIndex();
        ArrayList<String> returnList = new ArrayList<String>();
        for (int i = 0; i < getSize(); i++) {
            if (database.get(i) == null)
                continue;
            if (database.get(i)[index].equals(target))
                returnList.add(format(database.get(i), vaildColumn));
        }
        return returnList;
    }

    public ArrayList<String> getData(Config config, int startIndex, int endIndex) {
        boolean[] vaildColumn = config.getValidColumn();
        ArrayList<String> returnList = new ArrayList<String>();
        for (int i = startIndex; i < endIndex; i++) {
            if (database.get(i) != null)
                returnList.add(format(database.get(i), vaildColumn));
        }
        return returnList;
    }

    public int pageIncrease(int startIndex, int dataPerPage) {
        int count = 0;
        int index;
        for (index = startIndex; index < getSize(); index++) {
            if (database.get(index) != null) {
                if (++count == dataPerPage)
                    return index + 1;
            }
        }
        return getSize();
    }

    public int pageDecrease(int startIndex, int dataPerPage) {
        int count = 0;
        int index;
        for (index = startIndex - 1; index >= 0; index--) {
            if (database.get(index) != null) {
                if (++count == dataPerPage)
                    break;
            }
        }
        return index;
    }

    public int getStartIndex() {
        for (int i = 0; i < getSize(); i++) {
            if (database.get(i) != null)
                return i;
        }
        return 0;
    }

    public int getEndIndex() {
        for (int i = getSize() - 1; i >= 0; i--) {
            if (database.get(i) != null)
                return i + 1;
        }
        return getSize();
    }

    public void modifyData(String target, Catalog catalog) {
        int index = 0;
        for (; index < getSize(); index++) {
            if (database.get(index)[0].equals(target))
                break;
        }
        for (int i = 1; i < 7; i++) {
            Screen.display("New_" + columnArray[i].getDesc().toLowerCase() + ":");
            String input = Keyboard.input();
            while (true) {
                if (input.length() == 0)
                    break;
                else {
                    if (checkData(columnArray[i], input)) {
                        database.get(index)[i] = String.copyValueOf(input.toCharArray());
                        break;
                    } else {
                        Screen.display("Error_wrong_data\nPlease_input_again:");
                        input = Keyboard.input();
                    }
                }
            }
        }
        Screen.display("Catalogs:" + catalog.getCatalogList());
        Screen.display("New_catalog:");
        String input = "";
        input = Keyboard.input();
        while (true) {
            if (input.length() == 0)
                break;
            else {
                if (catalog.checkIndex(input)) {
                    database.get(index)[7] = catalog.getCatalog(Integer.parseInt(input));
                    break;
                } else {
                    Screen.display("Error_wrong_data\nPlease_input_again:");
                    input = Keyboard.input();
                }
            }
        }
        Screen.display("New_" + columnArray[8].getDesc().toLowerCase() + ":");
        input = Keyboard.input();
        while (true) {
            if (input.length() == 0)
                break;
            else {
                if (checkData(COLUMN.WORK, target)) {
                    database.get(index)[8] = String.copyValueOf(input.toCharArray());
                    break;
                } else {
                    Screen.display("Error_wrong_data\nPlease_input_again:");
                    input = Keyboard.input();
                }
            }
        }
    }

    public void deleteData(String target) {
        int index = 0;
        for (; index < getSize(); index++) {
            if (database.get(index)[0].equals(target))
                break;
        }
        database.set(index, null);
    }

    public void addData(Catalog catalog) {
        String id = String.valueOf(getSize() + 1);
        id = formatID(id);
        String[] tmp = { id, "", "", "", "", "", "", "", "" };
        for (int i = 1; i < 7; i++) {
            Screen.display(columnArray[i].getDesc() + ":");
            String input = Keyboard.input();
            while (!checkData(columnArray[i], input)) {
                Screen.display("Error_wrong_data\nPlease_input_again:");
                input = Keyboard.input();
            }
            tmp[i] = input;
        }
        Screen.display("Catalogs:" + catalog.getCatalogList());
        Screen.display("Catalog:");
        String input = Keyboard.input();
        while (!catalog.checkIndex(input)) {
            Screen.display("Error_wrong_data\nPlease_input_again:");
            input = Keyboard.input();
        }
        tmp[7] = catalog.getCatalog(Integer.parseInt(input));
        Screen.display(columnArray[8].getDesc() + ":");
        input = Keyboard.input();
        while (!checkData(columnArray[8], input)) {
            Screen.display("Error_wrong_data\nPlease_input_again:");
            input = Keyboard.input();
        }
        tmp[8] = input;
        database.add(tmp);
        Screen.display("Add_contact_success");
    }

    public void optimizeData(Config config) {
        sort(config);
        file.writeList(getData(config));
        Screen.display("Data_optimize_success");
    }

    public void sort(Config config) {
        boolean reverse = config.getSortOrder();
        int index = config.getsortColumn().getIndex();
        if (reverse)
            database.sort((first, second) -> compare(second, first, index));
        else
            database.sort((first, second) -> compare(first, second, index));
    }

    private int compare(String[] first, String[] second, int index) {
        return (first == null || second == null) ? 0 : first[index].compareTo(second[index]);
    }

    private String format(String[] unformat, boolean[] vaildColumn) {
        String formated = "";
        for (int column = 0; column < 9; column++) {
            if (vaildColumn[column]) {
                String tmp = unformat[column];
                for (int i = tmp.length(); i < ruleLength[column]; i++) {
                    tmp += " ";
                }
                formated += tmp;
            }
        }
        return formated;
    }

    public boolean checkData(COLUMN chooseColumn, String target) {
        switch (chooseColumn) {
            case ID:
                return checkID(target);
            case NAME:
                return checkName(target);
            case START:
            case END:
                return checkTime(target);
            case DEGREE:
                return checkDegree(target);
            case STATE:
                return checkState(target);
            case NUMBER:
                return checkNumber(target);
            case WORK:
                return checkWork(target);
            default:
                return false;
        }
    }

    public boolean checkID(int target) {
        return true;
    }

    private boolean checkID(String target) {
        return target.length() <= 4 && checkID(Integer.parseInt(target));
    }

    private boolean checkName(String target) {
        return target.length() < ruleLength[COLUMN.NAME.getIndex()]
                && target.matches("[QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm]+");
    }

    private boolean checkTime(String target) {
        if (target.length() != 8)
            return false;
        if (!target.matches("[1234567890]{2}:[1234567890]{2}:[1234567890]{2}"))
            return false;
        int hour = Integer.parseInt(target.substring(0, 2));
        if (hour > 23 || hour < 0)
            return false;
        int min = Integer.parseInt(target.substring(3, 5));
        if (min > 59 || min < 0)
            return false;
        int sec = Integer.parseInt(target.substring(6, 8));
        if (sec > 59 || sec < 0)
            return false;
        return true;
    }

    private boolean checkDegree(String target) {
        if (!target.matches("[1234567890]{1,3}"))
            return false;
        return Integer.parseInt(target) >= 0 && Integer.parseInt(target) <= 100;
    }

    private boolean checkState(String target) {
        return target.equals("Unfinish") || target.equals("Doing") || target.equals("Finish");
    }

    private boolean checkNumber(String target) {
        return target.matches("[QWERTYUIOPASDFGHJKLZXCVBNM]{1}[1234567890]{5}");
    }

    private boolean checkWork(String target) {
        return target.length() < 27;
    }

    private String formatID(String target) {
        int id = Integer.parseInt(target);
        return String.format("%04d", id);
    }

}
