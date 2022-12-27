
public class Job {
    private Config config = new Config();
    private Data data = new Data();
    private Account account = new Account();
    private Catalog catalog = new Catalog();
    COLUMN[] columnArray = { COLUMN.ID, COLUMN.NAME, COLUMN.START, COLUMN.END, COLUMN.DEGREE, COLUMN.STATE,
            COLUMN.NUMBER, COLUMN.CATALOG, COLUMN.WORK };

    boolean hadLogin = false;
    String command = "";
    boolean result;

    public boolean keepRun() {
        if (!hadLogin)
            return login();
        Screen.displayMainMenu();
        while (true) {
            command = Keyboard.input();
            switch (command) {
                case "1":
                    data.sort(config);
                    showAll();
                    return backOrExit();
                case "2":
                    data.sort(config);
                    result = showPerPage();
                    return result;
                case "3":
                    data.sort(config);
                    result = showByCatalog();
                    return result;
                case "4":
                    result = search();
                    return result;
                case "5":
                    result = modify();
                    return result;
                case "6":
                    result = delete();
                    return result;
                case "7":
                    addData();
                    return backOrExit();
                case "8":
                    addCatalog();
                    return backOrExit();
                case "9":
                    showCatalog();
                    return backOrExit();
                case "10":
                    setField();
                    return backOrExit();
                case "11":
                    setPage();
                    return backOrExit();
                case "12":
                    setOrder();
                    return backOrExit();
                case "13":
                    result = setSort();
                    return result;
                case "14":
                    showRawData();
                    return backOrExit();
                case "15":
                    optimize();
                    return backOrExit();
                case "16":
                    showAccount();
                    return backOrExit();
                case "17":
                    addAccount();
                    return backOrExit();
                case "18":
                    deleteAccont();
                    return backOrExit();
                case "19":
                    modifyAccount();
                    return backOrExit();
                case "20":
                    return logout();
                case "99":
                    return false;
                default:
                    Screen.display("Error_wrong_command\nPlease_enter_again:");
            }
        }
    }

    private boolean backOrExit() {
        Screen.backOrExit();

        while (true) {
            command = Keyboard.input();
            switch (command) {
                case "0":
                    return true;
                case "99":
                    return false;
                default:
                    Screen.display("Error_wrong_command\nPlease_enter_again:");
            }
        }
    }

    private boolean login() {
        for (int i = 0; i < 3; i++) {
            Screen.display("Account:");
            String accStr = Keyboard.input();
            Screen.display("Password:");
            String pasStr = Keyboard.input();
            String rigthVerify = config.getSetting("verify_string");
            Screen.display("Verify_string:" + rigthVerify);
            Screen.display("Input_Verify_string:");
            String verify = Keyboard.input();
            if (account.checkLogin(accStr, pasStr) && verify.equals(rigthVerify)) {
                Screen.display("Login_success");
                hadLogin = true;
                break;
            } else
                Screen.display("Error_wrong_account_password_or_verify_string");
        }
        return hadLogin;
    }

    private void showAll() {
        Screen.display(data.getColumns(config.getValidColumn()));
        Screen.display(data.getData(config));
    }

    private boolean showPerPage() {
        Screen.display("Choose_show_per_page:");
        Screen.display("[3].3_data_per_page [5].5_data_per_page [10].10_data_per_page");
        Screen.display("[d].default [0].Go_back_to_main_menu [99].Exit_system");
        int dataNum = 0;
        while (dataNum == 0) {
            command = Keyboard.input();
            switch (command) {
                case "10":
                    dataNum += 5;
                case "5":
                    dataNum += 2;
                case "3":
                    dataNum += 3;
                    break;
                case "d":
                    dataNum = Integer.parseInt(config.getSetting("show_defalt_perpage"));
                    break;
                case "0":
                    return true;
                case "99":
                    return false;
                default:
                    Screen.display("Error_wrong_command\nPlease_enter_again:");
            }
        }
        int startIndex = data.getStartIndex();
        while (true) {
            int endIndex = data.pageIncrease(startIndex, dataNum);
            Screen.display(data.getData(config, startIndex, endIndex));
            String option = "";
            if (!(startIndex == data.getStartIndex()))
                option += "[1].Last_page ";
            if (!(endIndex == data.getEndIndex()))
                option += "[2].Next_page ";
            option += "[0].Go_back_to_main_menu [99].Exit_system";
            Screen.display(option);
            boolean keepAsk = true;
            while (keepAsk) {
                command = Keyboard.input();
                switch (command) {
                    case "1":
                        if (!(startIndex == data.getStartIndex())) {
                            startIndex = data.pageDecrease(startIndex, dataNum);
                            keepAsk = false;
                        } else
                            Screen.display("Error_wrong_command\nPlease_enter_again:");
                        break;
                    case "2":
                        if (!(endIndex == data.getEndIndex())) {
                            startIndex = data.pageIncrease(startIndex, dataNum);
                            keepAsk = false;
                        } else
                            Screen.display("Error_wrong_command\nPlease_enter_again:");
                        break;
                    case "0":
                        return true;
                    case "99":
                        return false;
                    default:
                        Screen.display("Error_wrong_command\nPlease_enter_again:");
                }
            }
        }

    }

    private boolean showByCatalog() {
        Screen.display("Catalogs:");
        Screen.display(catalog.getCatalogList());
        Screen.backOrExit();
        Screen.display("Input_catalog_to_show:");
        boolean decided = false;
        String target = "";
        while (!decided) {
            command = Keyboard.input();
            if (!command.matches("[1234567890]+")) {
                Screen.display("Error_wrong_data\nPlease_input_again:");
            } else {
                switch (command) {
                    case "0":
                        return true;
                    case "99":
                        return false;
                    default:
                        target = catalog.getCatalog(Integer.parseInt(command));
                        decided = true;
                }
            }
        }
        Screen.display(data.getColumns(config.getValidColumn()));
        Screen.display(data.getData(config, COLUMN.CATALOG, target));
        return backOrExit();
    }

    private boolean search() {
        while (true) {
            if (!searchData())
                return command.equals("0");
            Screen.display("[1].Restart_search [0].Go_back_to_main_menu [99].Exit_system");
            boolean ask = true;
            while (ask) {
                command = Keyboard.input();
                switch (command) {
                    case "1":
                        ask = false;
                        break;
                    case "0":
                        return true;
                    case "99":
                        return false;
                    default:
                        Screen.display("Error_wrong_command\nPlease_enter_again:");
                }
            }
        }

    }

    private boolean searchData() {
        Screen.display("Search by:");
        Screen.display("[1].ID [2].Name [3].Start [4].End [5].Degree [6].State [7].Number [8].Work");
        Screen.backOrExit();
        COLUMN chooseColumn = null;
        while (chooseColumn == null) {
            command = Keyboard.input();
            switch (command) {
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                    chooseColumn = columnArray[Integer.parseInt(command) - 1];
                    break;
                case "8":
                    chooseColumn = columnArray[Integer.parseInt(command)];
                    break;
                case "0":
                case "99":
                    return false;
                default:
                    Screen.display("Error_wrong_data\nPlease_input_again:");
            }
        }
        Screen.display("Input_target:");
        String target = Keyboard.input();
        while (true) {
            if (data.checkData(chooseColumn, target))
                break;
            Screen.display("Error_wrong_data\nPlease_input_again:");
            target = Keyboard.input();
        }
        if (data.getData(config, chooseColumn, target).size() == 0) {
            Screen.display("Error_no_result");
            return true;
        }
        Screen.display("Search_result:");
        Screen.display(data.getColumns(config.getValidColumn()));
        Screen.display(data.getData(config, chooseColumn, target));
        return true;
    }

    private boolean modify() {
        Screen.display("Input_ID_to_be_modified:");
        String target = "";
        while (true) {
            target = Keyboard.input();
            if (data.getData(config, COLUMN.ID, target).size() != 0)
                break;
            else {
                Screen.display("Error_no_such_data");
                return true;
            }

        }
        Screen.display("Search_result:");
        Screen.display(data.getColumns(config.getValidColumn()));
        Screen.display(data.getData(config, COLUMN.ID, target));
        data.modifyData(target, catalog);
        Screen.display("Modify_data_success");
        return backOrExit();
    }

    private boolean delete() {
        Screen.display("Input_ID_to_be_deleted:");
        String target = "";
        while (true) {
            target = Keyboard.input();
            if (data.getData(config, COLUMN.ID, target).size() != 0)
                break;
            else {
                Screen.display("Error_no_such_data");
                return true;
            }
        }
        data.deleteData(target);
        Screen.display("Delete_data_success");
        return backOrExit();
    }

    private void addData() {
        int last_id = Integer.parseInt(config.getSetting("used_last_id"));
        config.setSetting("used_last_id", String.valueOf(last_id + 1));
        data.addData(catalog);
    }

    private void addCatalog() {
        catalog.add();
    }

    private void showCatalog() {
        Screen.display("[Catalog]");
        Screen.display(catalog.getList());
    }

    private void setField() {
        Screen.display(config.getFieldList());
        config.setAllField();
        Screen.display(config.getFieldList());
    }

    private void setPage() {
        Screen.display("show_defalt_perpage:" + config.getSetting("show_defalt_perpage"));
        String input = Keyboard.input();
        while (true) {
            if (input.matches("[12457890]+"))
                break;
            Screen.display("Input_error_plaese_input_asc_or_des:");
            input = Keyboard.input();
        }
        config.setSetting("show_defalt_perpage", input);
        Screen.display("new_show_defalt_perpage:");
        Screen.display("show_defalt_perpage:" + config.getSetting("show_defalt_perpage"));
    }

    private void setOrder() {
        Screen.display("show_sort_order:" + config.getSetting("show_sort_order"));
        Screen.display("Please_input_new_sort_order:");
        String input = Keyboard.input();
        while (true) {
            if (input.equals("asc") || input.equals("des"))
                break;
            Screen.display("Input_error_plaese_input_asc_or_des:");
            input = Keyboard.input();
        }
        config.setSetting("show_sort_order", input);
        Screen.display("show_sort_order:" + config.getSetting("show_sort_order"));
    }

    private boolean setSort() {
        Screen.display("[1].ID [2].Name [3].Start [4].End [5].Degree [6].State [7].Number [8].Catalog [9].Work");
        Screen.backOrExit();
        boolean ask = true;
        int index = 0;
        while (ask) {
            String input = Keyboard.input();
            index = 0;
            switch (input) {
                case "9":
                    index += 1;
                case "8":
                    index += 1;
                case "7":
                    index += 1;
                case "6":
                    index += 1;
                case "5":
                    index += 1;
                case "4":
                    index += 1;
                case "3":
                    index += 1;
                case "2":
                    index += 1;
                case "1":
                    ask = false;
                    break;
                case "0":
                    return true;
                case "99":
                    return false;
                default:
                    Screen.display("Error_wrong_command\nPlease_enter_again:");
            }
        }
        config.setSetting("show_sort_field", columnArray[index].getDesc().toLowerCase());
        ;
        Screen.display("Sorted_by:" + columnArray[index].getDesc());
        return backOrExit();
    }

    private void showRawData() {
        Screen.display(data.getColumns(config.getValidColumn()));
        Screen.display(data.getRawData(config));
    }

    private void optimize() {
        Screen.display("Please_confirm_data_optimize_y_or_n:");
        String input = Keyboard.input();
        while (!(input.equals("y") || input.equals("n"))) {
            Screen.display("Error_wrong_command\nPlease_enter_again:");
            input = Keyboard.input();
        }
        if (input.equals("y"))
            data.optimizeData(config);
    }

    private void showAccount() {
        Screen.display("[Account]    [Password]   ");
        Screen.display(account.getAllAccount());
    }

    private void addAccount() {
        account.add();
    }

    private void deleteAccont() {
        account.delete();
    }

    private void modifyAccount() {
        account.modify();
    }

    private boolean logout() {
        Screen.display("Please_confirm_to_logout_y_or_n:");
        String input = Keyboard.input();
        while (!(input.equals("y") || input.equals("n"))) {
            Screen.display("Error_input\nPlease_input_again:");
            input = Keyboard.input();
        }
        if (input.equals("y"))
            hadLogin = false;
        return true;
    }

}