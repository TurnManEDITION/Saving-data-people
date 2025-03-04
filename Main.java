import java.io.FileReader;
import java.util.Objects;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;


public class Main {
    public static void main(String[] args) throws Exception {
        long max_day = 0;
        JSONObject jsonObject = (JSONObject) readJsonSimpleDemo("src/days.json");
        String[] snp = new String[3];
        int[] dmy = {0, 0, 0};
        int i = 0;
        while (i != 3) {
            do {
                String[] snp_choice = {"surname", "name", "patronymic"};
                print("Enter " + snp_choice[i] + ": ");
                snp[i] = inputStr();
            } while (snp[i].isEmpty());
            i += 1;
        } i = 2;

        while (i != -1) {
            while (true) {
                String[] dmy_choice = {"day", "month", "year"};
                print("Enter birth " + dmy_choice[i] + ": ");
                dmy[i] = inputInt();
                if (i == 2) {
                    i -= 1;
                }
                if (i == 1 & 0 < dmy[1] & dmy[1] <= 12) {
                    if (dmy[2] % 4 == 0 && dmy[1] == 2) {
                        max_day = (long) jsonObject.get(String.valueOf(dmy[1])) + 1;
                    } else {
                        max_day = (long) jsonObject.get(String.valueOf(dmy[1]));
                    }
                    i -= 1;
                    break;
                }
                if (i == 0) {
                    if (0 < dmy[i] & dmy[i] <= max_day) {
                        i -= 1;
                        break;
                    }
                }
            }
        }
        String snp_data = snp[0] + " " + snp[1] + " " + snp[2];
        String dmy_data = re_dmy(String.valueOf(dmy[0]), String.valueOf(dmy[1]), String.valueOf(dmy[2]));
        println(snp_data + "\n" + dmy_data);
    }


    public static String re_dmy(String d, String m, String y) {
        if (d.length() == 1) {
            d = "0" + d;
        }
        if (m.length() == 1) {
            m = "0" + m;
        }
        if (y.length() == 1) {
            y = "200" + y;
        }
        if (y.length() == 2 & Integer.parseInt(y) <= 25) {
            y = "20" + y;
        }
        if (y.length() == 2 & Integer.parseInt(y) > 25) {
            y = "20" + y;
        }
        return d + "." + m + "." + y;
    }

    

    public static Object readJsonSimpleDemo(String filename) throws Exception {
        FileReader reader = new FileReader(filename);
        JSONParser jsonParser = new JSONParser();
        return jsonParser.parse(reader);
    }
    public static void print(String args) {
        System.out.print(args);
    }
    public static void println(String args) {
        System.out.println(args);
    }
    public static String inputStr() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
    public static int inputInt() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }


}