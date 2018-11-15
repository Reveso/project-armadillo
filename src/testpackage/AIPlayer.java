package testpackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class AIPlayer extends Player {

    private Process process;

    public AIPlayer(File dir, String command) {
        try {
            //TODO: run a command in given dir, maybe (exec("cd dir.path") will work)
            Process process = Runtime.getRuntime().exec( command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Move askForMove(String freeCells) {
        try {
            BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            bf.write(freeCells);
            bf.flush();
            bf.close();


        } catch (IOException e) {
            e.printStackTrace();
            //TODO: This will rather be thrown to Game class
        }
        return null;
    }

    @Override
    public void killPlayer() {
        process.destroy();
    }
}
