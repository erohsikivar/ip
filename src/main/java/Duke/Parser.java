package duke;

import command.UserInterface;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Parser class will parse the input data and generate a taskList from the input the file.
 */
public class Parser {

    private Storage storage;
    private TaskManager taskManager;
    private UserInterface userInterface;

    /**
     * Instantiates a new Parser.
     *
     * @param taskManager   the task manager
     * @param userInterface the user interface
     * @param storage       the storage
     */
    public Parser(TaskManager taskManager, UserInterface userInterface, Storage storage) {
        this.taskManager = taskManager;
        this.userInterface = userInterface;
        this.storage = storage;
    }

    /**
     * Parse command string.
     *
     * @param command the command
     * @return the string
     */
    public String parseCommand(String command) {
        String[] parts = command.split(" ", 2);
        String action = parts[0];
        StringBuilder result = new StringBuilder();
        switch (action) {
        case "todo":
            result.append(this.todo(parts));
            break;
        case "deadline":
            result.append(this.deadline(parts));
            break;
        case "event":
            result.append(this.event(parts));
            break;
        case "delete":
            result.append(this.delete(parts));
            break;
        case "mark":
            result.append(this.mark(parts));
            break;
        case "umark":
            result.append(this.unmark(parts));
            break;
        case "list":
            result.append(userInterface.showTaskList(taskManager.displayList()));
            break;
        case "find":
            result.append(userInterface.showFindTasks(taskManager.find(parts[1])));
            break;
        case "bye":
            storage.save(taskManager.displayList());
            result.append(userInterface.showGoodbyeMessage());
            break;
        default:
            result.append(userInterface.showUnknownCommandMessage());
            break;
        }
        return result.toString();
    }

    /**
     * Todo string.
     *
     * @param parts the parts
     * @return the string
     */
    public String todo(String[] parts) {
        if (parts.length < 2) {
            return userInterface.showError("OOPS!!! The description of a todo cannot be empty.");
        }
        taskManager.todo(parts[1]);
        return userInterface.showTaskAddedMessage(taskManager.displayList());
    }

    /**
     * Deadline string.
     *
     * @param parts the parts
     * @return the string
     */
    public String deadline(String[] parts) {
        if (parts.length < 2) {
            return userInterface.showError("OOPS!!! The description of a deadline cannot be empty.");
        }
        String[] fullDesc = parts[1].split(" /by ");
        String description = fullDesc[0];
        LocalDateTime by = LocalDateTime.parse(fullDesc[1]);
        taskManager.deadline(description, by);
        return userInterface.showTaskAddedMessage(taskManager.displayList());
    }

    /**
     * Event string.
     *
     * @param parts the parts
     * @return the string
     */
    public String event(String[] parts) {

        if (parts.length < 2) {
            return userInterface.showError("OOPS!!! The description of a event cannot be empty.");
        }
        String[] fullDesc = parts[1].split(" /from | /to ");
        String description = fullDesc[0];
        LocalDateTime from = LocalDateTime.parse(fullDesc[1]);
        LocalTime to = LocalTime.parse(fullDesc[2]);
        taskManager.event(description, from, to);
        return userInterface.showTaskAddedMessage(taskManager.displayList());
    }

    /**
     * Delete string.
     *
     * @param parts the parts
     * @return the string
     */
    public String delete(String[] parts) {
        if (parts.length < 2) {
            return userInterface.showError("OOPS!!! Please specify the task number to delete.");
        }
        try {
            int taskIndex = Integer.parseInt(parts[1]) - 1;
            if (taskIndex < 0 || taskIndex >= taskManager.displayList().size()) {
                return userInterface.showError("OOPS!!! Invalid task number.");
            }
            taskManager.delete(taskIndex);
            return userInterface.showTaskDeletedMessage(taskManager.displayList());
        } catch (NumberFormatException e) {
            return userInterface.showError("OOPS!!! Please provide a valid task number.");
        }
    }

    /**
     * Mark string.
     *
     * @param parts the parts
     * @return the string
     */
    public String mark(String[] parts) {
        if (parts.length < 2) {
            return userInterface.showError("OOPS!!! Please specify the task number to mark.");
        }
        try {
            int taskIndex = Integer.parseInt(parts[1]) - 1;
            if (taskIndex < 0 || taskIndex >= taskManager.displayList().size()) {
                return userInterface.showError("OOPS!!! Invalid task number.");
            }
            taskManager.mark(taskIndex);
            return userInterface.showTaskMarkedMessage(taskManager.displayList());
        } catch (NumberFormatException e) {
            return userInterface.showError("OOPS!!! Please provide a valid task number.");
        }
    }

    /**
     * Unmark string.
     *
     * @param parts the parts
     * @return the string
     */
    public String unmark(String[] parts) {
        if (parts.length < 2) {
            return userInterface.showError("OOPS!!! Please specify the task number to unmark.");
        }
        try {
            int taskIndex = Integer.parseInt(parts[1]) - 1;
            if (taskIndex < 0 || taskIndex >= taskManager.displayList().size()) {
                return userInterface.showError("OOPS!!! Invalid task number.");
            }
            taskManager.unmark(taskIndex);
            return userInterface.showTaskUnmarkedMessage(taskManager.displayList());
        } catch (NumberFormatException e) {
            return userInterface.showError("OOPS!!! Please provide a valid task number.");
        }
    }
}

