package testpackage;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();
//        String command = "java -jar F:\\Development\\messenger\\out\\artifacts\\messenger_jar\\messenger.jar";
        String[] command = {"java", "-jar", "messenger.jar"};

        try {
            Process process = rt.exec(command);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            System.out.println(reader.readLine());

            writer.write("Lukasz ma racje");
            writer.flush();
            writer.close();

            System.out.println(reader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
