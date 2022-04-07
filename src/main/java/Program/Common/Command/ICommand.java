package Program.Common.Command;

import Program.Common.DataClasses.Worker;

import java.util.LinkedList;
/**
 * интерфейс, служащий гарантом того что любая команда будет содержать требуемый набор методов.
 */
public interface ICommand {
    /**
     *
     * @param args Аргументы команды.
     * @param WorkersData Коллекция с объектами.
     * @return Коллекция после ее обработки командой.
     */
    LinkedList<Worker> handle(String args, LinkedList<Worker> WorkersData);

    /** Метод возвращает имя команды.
     *
     * @return имя команды
     */
    String getName();

    /** Метода возвращает развернутое описание команды.
     *
     * @return информация о команде
     */
    String getHelp();

}
