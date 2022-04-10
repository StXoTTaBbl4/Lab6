package Program.Common.DataClasses;

import java.io.Serializable;

public class Transporter implements Serializable {
    String message = "def";
    String command = "def";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
