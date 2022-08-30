package duke.command;

import duke.Storage;
import duke.TaskList;
import duke.Ui;
import duke.command.Command;

public class ExitCommand extends Command {

    /*
     * Duke exits
     */
    @Override
    public String execute(Ui ui, TaskList taskList, Storage storage) {
        toggleExit();
        return ui.showBye();
    }
}