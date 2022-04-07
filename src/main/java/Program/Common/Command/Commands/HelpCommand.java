package Program.Common.Command.Commands;

import Program.Common.Command.CommandManager;
import Program.Common.Command.ICommand;
import Program.Common.DataClasses.Worker;

import java.util.LinkedList;

/**
 * Выводит справку по доступным командам.
 */
public class HelpCommand implements ICommand {

    private final CommandManager manager;

    /** Запрашивает существующий commandManager для возможности вызова у них методов help и getName.
     *
     * @param manager CommandManager для доступа к методам других команд.
     */
    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public LinkedList<Worker> handle(String args, LinkedList<Worker> WorkersData) {

        if(args.isEmpty()){
            StringBuilder builder = new StringBuilder();

            builder.append("Command List:\n");

            manager.getCommands().stream().map(ICommand::getName).forEach((it) -> builder.append(it).append('\n'));
            System.out.println(builder);
        }else {

            ICommand command = manager.getCommand(args);

            if (command == null) {
                System.out.printf("Command + %s not found.\n", args);
            }else{
                System.out.println(command.getHelp());
            }
        }
        return WorkersData;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Use this command to get help on other commands\n" +
                "help [command name].";
    }
}
