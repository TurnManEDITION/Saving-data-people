import java.io.*;
import java.security.MessageDigest;
import java.util.Scanner;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;


public class Main {
    public static void main(String[] args) throws Exception {
        while (true) {
            print("Enter method(Write|Read): ");
            String answer = inputStr().toLowerCase();
            if (answer.equals("write") | answer.equals("w")) {
                write();
            } else if (answer.equals("read") | answer.equals("r")) {
                String[] snp = new String[3];
                int i = 0;
                while (i != 3) {
                    do {
                        String[] snp_choice = {"surname", "name", "patronymic"};
                        print("Enter " + snp_choice[i] + ": ");
                        snp[i] = inputStr();
                    } while (snp[i].isEmpty());
                    i += 1;
                }
                String snp_data = snp[0] + snp[1] + snp[2];
                read(snp_data);
            } else {
                println("Error");
            }
        }
    }
    public static void write() throws Exception {
        long max_day = 0;
        // Обращение к json файлу и сохранение данных в переменную
        try {
            JSONObject jsonObject = (JSONObject) readJsonSimpleDemo("src/days.json");
        } catch (FileNotFoundException e) {
            URL link = new URL("https://raw.githubusercontent.com/TurnManEDITION/Saving-data-people/refs/heads/main/days.json");
            println("Error! File not found! \n" +
                    "Download file: " + link +
                    "Please RESTART Code!");
            downloadFile(link, "src/days.json");
            System.exit(0);
        }

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
        }
        i = 2;

        while (i != -1) {
            while (true) {
                String[] dmy_choice = {"day", "month", "year"};
                print("Enter birth " + dmy_choice[i] + ": ");
                while (true) {
                    try {
                        dmy[i] = inputInt();
                        break;
                    } catch (Exception e) {
                        println("Error");
                        print("Enter birth " + dmy_choice[i] + ": ");
                    }
                }

                if (i == 2) {
                    i -= 1;
                }
                if (i == 1 & 0 < dmy[1] & dmy[1] <= 12) {
                    JSONObject jsonObject = (JSONObject) readJsonSimpleDemo("src/days.json");
                    if (dmy[2] % 4 == 0 && dmy[1] == 2) {
                        // взятие определённого параметра из json файла
                        max_day = (long) jsonObject.get(String.valueOf(dmy[1])) + 1;
                    } else {
                        // взятие определённого параметра из json файла
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

        // Описание
        String addition = "";
        String description = "";
        println("Description: (exit to exit)");
        while (true) {
            addition = inputStr();
            if (addition.toLowerCase().equals("exit")) {
                break;
            }
            description += addition + "\n";
        }

        // Запись определённых данных в одну переменную
        String snp_data = snp[0] + snp[1] + snp[2];
        String name_file = gen_name_file(snp_data);
        String dmy_data = re_dmy(String.valueOf(dmy[0]), String.valueOf(dmy[1]), String.valueOf(dmy[2]));

        String all_data = data(snp, dmy_data, description);
        try {
            // Создание переменной файла
            File file = new File(name_file + ".hex");
            // Создание файла
            if (file.createNewFile()) {
                println("The file was created successfully!");
            } else {
                println("The data in the file has been changed!");
            }
        } catch (IOException e) {
            println("Error when creating the file!");
            e.printStackTrace();
        }
        try {
            String hashed_data = hashed(all_data);
            // Запись данных в файл
            FileWriter writer = new FileWriter(name_file + ".hex");
            writer.write(hashed_data);
            writer.close();
        } catch (IOException e) {
            println("Error when writing to a file!");
            e.printStackTrace();
        }
    }

    public static void read(String snp_data) throws Exception {
        String str = "";
        String name_file = gen_name_file(snp_data);
        BufferedReader reader = new BufferedReader(new FileReader(name_file + ".hex"));
        String line;
        while ((line = reader.readLine()) != null) {
            str += line;
        } reader.close();
        str = rehashed(str);
        println(str);
    }

    public static String gen_name_file(String snp) throws Exception {
        return hash_sha256(snp).replaceAll("[0-9]", "");
    }

    public static String hash_sha256(String data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(data.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String hashed(String str) {
        String ascii = "";
        String hex = "";
        char[] chars_text = str.toCharArray();
        for (int i = 0; i != str.length(); i++) {
            ascii += String.valueOf((int) chars_text[i]) + " ";
        }
        String[] chars_ascii = ascii.split(" ");
        for (int i = 0; i != str.length(); i++) {
            hex += Integer.toHexString(Integer.parseInt(chars_ascii[i])) + " ";
        }
        return hex;
    }

    public static String rehashed(String hex) throws IOException {
        String ascii = "";
        String str = "";
        String[] chars = hex.split(" ");
        for (int i = 0; i != chars.length; i++) {
            ascii += Integer.parseInt(chars[i], 16) + " ";
        }
        String[] chars_ascii = ascii.split(" ");
        for (int i = 0; i != chars.length; i++) {
            str += Character.toString((char) Integer.parseInt(chars_ascii[i]));
        }
        return str;
    }

    public static String data(String[] snp, String dmy_data, String description) {
        // Возврат данных, как должны выглядеть
        return ("Surname: " + snp[0] + "\n" +
                "Name: " + snp[1] + "\n" +
                "Patronymic: " + snp[2] + "\n" +
                "Birth: " + dmy_data) + "\n" +
                "Description: \n" + description;
    }

    public static void downloadFile(URL url, String outputFileName) throws IOException
    {
        try (InputStream in = url.openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(outputFileName)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
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
            y = "19" + y;
        }
        return d + "." + m + "." + y;
    }

    // Для чтения файла
    public static Object readJsonSimpleDemo(String filename) throws Exception {
        FileReader reader = new FileReader(filename);
        JSONParser jsonParser = new JSONParser();
        return jsonParser.parse(reader);
    }
    // Сокращённые функции для удобства
    public static void print(String args) {
        System.out.print(args);
    }
    public static void println(String args) {
        System.out.println(args);
    }
    public static String inputStr() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    public static int inputInt() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
}