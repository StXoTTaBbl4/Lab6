package Program.Common.Command;

import Program.Common.Command.Commands.*;
import Program.Common.Command.Commands.AddIfMax.AddIfMaxCommand;
import Program.Common.DataClasses.Worker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс, который отвечает за обработку пользовательских команд и передачу их дальше для исполнения.
 */
public class CommandManager {

    private final List<ICommand> commands = new ArrayList<>();

    /**
     * @return Список доступных команд.
     */
    public List<ICommand> getCommands() {
        return commands;
    }

    /** Конструктор, в который добавляются все объекты классов-команд с помощью {@link CommandManager#addCommand(ICommand)}.
     *
     *
     * @param path Путь к файлу, содержащему коллекцию.
     */
    public CommandManager(String path){
        addCommand(new AddCommand());
        addCommand(new AddIfMaxCommand(this));
        addCommand(new ClearCommand());
        addCommand(new CountGtpCommand());
        addCommand(new ExecuteScriptCommand(this));
        addCommand(new ExitCommand());
        addCommand(new FilterGtsCommand());
        addCommand(new GroupCbsCommand());
        addCommand(new HelpCommand(this));
        addCommand(new InfoCommand(path));
        addCommand(new RemoveIdCommand());
        addCommand(new RemoveLastCommand());
        addCommand(new SaveCommand());
        addCommand(new ShowCommand());
        addCommand(new SortCommand());
        addCommand(new UpdateIdCommand());
    }

    /** Внутренний метод для добавления команды в общий список доступных команд.
     *
     * @param cmd Объект класса-команды
     */
    private void addCommand(ICommand cmd){
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));
        if(nameFound){
            throw new IllegalArgumentException("Command: "+cmd.getName()+" already exists");
        }
        commands.add(cmd);
    }

    /** Метод для поиска команды по ее имени.
     *
     * @param search имя искомой команды.
     * @return В случае существования команды возвращает объект класса-команды.
     */
    public ICommand getCommand(String search){
        String searchLower = search.toLowerCase();

        for (ICommand cmd: this.commands) {
            if(cmd.getName().equals(searchLower)){
                return cmd;
            }
        }
        return null;
    }

    /** Метод, передающий параметры для исполнения команды методу handle объекта класса-команды.
     *
     * @param input Строка, содержащая има команды и аргументы.
     * @param WorkerData Коллекция, с которой взаимодействует программа.
     * @return Коллекция после ее обработки в соответствии с командой.
     */
    public LinkedList<Worker> CommandHandler(String input,LinkedList<Worker> WorkerData){
        String[] data = input.split(" ");

        ICommand cmd  = this.getCommand(data[0]);

        if(cmd != null){
            List<String> args = Arrays.asList(data).subList(1, data.length);

              WorkerData = cmd.handle(args.toString().substring(1,args.toString().length()-1), WorkerData);
        }else{
            System.out.println("Command not found!\n");
        }
        return WorkerData;
    }
}
