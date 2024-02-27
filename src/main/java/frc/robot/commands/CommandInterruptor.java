package frc.robot.commands;

import java.util.HashMap;

//Don't know if this class is really needed, keeping in case we need way to interrupt command. 
//Each command has to call "checkIsInterruptedAndReset" at initialize and then call "checkIsInterruptedAndReset" everytime in  isFinished method
public class CommandInterruptor {

    HashMap<String, Boolean> subsystemflags;
    private static CommandInterruptor instance;

    private CommandInterruptor() {
        subsystemflags = new HashMap<>();
    }

    public static CommandInterruptor getInstance() {
        if (instance == null) {
            instance = new CommandInterruptor();
        }
        return instance;
    }

    public void interruptSubsystem(String subSystemName) {
        subsystemflags.put(subSystemName, true);
    }

    //mutable, it resets interrupted flag once used
    public boolean checkIsInterruptedAndReset(String subSystemName) {
        Boolean interruptflag = subsystemflags.get(subSystemName);
        if (interruptflag == null) {
            interruptflag = false;
        }
        subsystemflags.put(subSystemName, false);
        return interruptflag;
    }
    

}
