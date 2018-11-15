package testpackage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class Player {

    public abstract Move askForMove(String freeCells);

    public abstract void killPlayer();

}
