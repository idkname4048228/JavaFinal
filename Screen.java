import java.util.ArrayList;

public class Screen {
    static void display(String str) {
        System.out.println(str);
    }

    static void displayMainMenu() {
        display("****************************************");
        display("1.Show_a 2.Show_p 3.Show_by_c 4.Search 5.Mod 6.Del 7.Add_job ");
        display("8.Add_cat 9.Show_cat 10.Set_field 11.Set_page 12.Set_order 13.Set_sort");
        display("14.Show_r 15.Opt 16.Show_acc 17.Add_acc 18.Del_acc 19.Mod_acc 20.Logout 99.Exit");
        display("****************************************");
    }

    static void display(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            display(list.get(i));
        }
    }

    static void backOrExit() {
        display("[0].Go_back_to_main_menu [99].Exit_system");
    }
}
