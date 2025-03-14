package duke;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import duke.exceptions.ImproperDeadlineFormatException;
import duke.exceptions.ImproperEventFormatException;

/**
 * Represents a storage of task generated by user.
 * A <code>Storage</code> object corresponds to
 * a collection of Tasks  e.g., <code>Deadline</code>
 */
public class Storage {
    private static final String FILE_NAME = "storage.txt";
    private static final String DIVIDER = "[|]";
    private final File storageFile;

    public Storage() {
        this.storageFile = new File(FILE_NAME);
    }

    /**
     * Checks if storage.txt file has been created.
     * If not created, storage.txt file will created in
     * ./storage.txt or .\storage.txt
     *
     */
    public void isCreated() {
        try {
            this.storageFile.createNewFile();
        } catch (IOException e) {
            System.out.println("COULD NOT CREATE NEW FILE\n"
                    + "REASON: "
                    + e.getMessage());
        }
    }

    /**
     * Load data stored in storage.txt file into the
     * taskList
     *
     * @param taskList target taskList where data from storage.txt
     *                 will be stored into.
     * @throws FileNotFoundException If storage.txt file not found.
     * @throws ImproperDeadlineFormatException If deadline data not formatted correctly.
     * @throws ImproperEventFormatException If event data not formatted correctly.
     */
    public void load(TaskList taskList)
            throws FileNotFoundException,
            ImproperDeadlineFormatException,
            ImproperEventFormatException {
        assert taskList.size() == 0 : "task list should be empty before loading existing data";
        Scanner reader = new Scanner(this.storageFile);
        while (reader.hasNext()) {
            String curr = reader.nextLine();
            // curr: T/D/E|0/1|task|date
            String[] currArr = curr.split(DIVIDER);
            // currArr: ["T/D/E", "0/1", "task", "date"]
            String currKeyword = currArr[0];
            String isDone = currArr[1];
            switch (currKeyword) {
            case ("T"):
                if (isDone.equals("1")) {
                    Task toDo = new ToDo(currArr[2]);
                    toDo.toggleStatus();
                    taskList.addTask(toDo);
                    break;
                }
                taskList.addTask(new ToDo(currArr[2]));
                break;
            case ("D"):
                if (isDone.equals("1")) {
                    Task deadline = new Deadline(currArr[2], currArr[3]);
                    deadline.toggleStatus();
                    taskList.addTask(deadline);
                    break;
                }
                taskList.addTask(new Deadline(currArr[2], currArr[3]));
                break;
            case ("E"):
                if (isDone.equals("1")) {
                    Task event = new Event(currArr[2], currArr[3]);
                    event.toggleStatus();
                    taskList.addTask(event);
                    break;
                }
                taskList.addTask(new Event(currArr[2], currArr[3]));
                break;
            default:
                throw new FileNotFoundException();
            }
        }
    }

    /**
     * Updates the data stored in the storage.txt file.
     *
     * @param taskList target taskList to extract new data from.
     */
    public void save(TaskList taskList) {
        try {
            storageFile.delete();
            FileWriter writer = new FileWriter(FILE_NAME);
            writer.write(taskList.generateSave());
            writer.close();
        } catch (IOException e) {
            System.out.println("CANNOT SAVE");
        }
    }
}
