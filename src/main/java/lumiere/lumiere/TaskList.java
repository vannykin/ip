package lumiere;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class TaskList {
    List<Task> list = new ArrayList<>();

    public void loadTask(Task task) {
        this.list.add(task);
    }

    public void printList() {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(Integer.toString(i + 1) + "." + list.get(i).stringify());
        }
    }

    public void addTask(String command, Storage storage) throws IOException {
        if (command.contains("unmark")) {
            System.out.println("OK, I've marked this task as not done yet:");
            int space = command.indexOf(" ");
            int num = Integer.parseInt(command.substring(space + 1)); // unmark X
            Task curr = list.get(num - 1);
            curr.unmark();
            list.set(num - 1, curr);
            storage.saveTasksToFile(list);
            System.out.println(list.get(num - 1).stringify());
        } else if (command.contains("mark")) {
            System.out.println("Nice! I've marked this task as done:");
            int space = command.indexOf(" ");
            int num = Integer.parseInt(command.substring(space + 1)); // mark X
            Task curr = list.get(num - 1);
            curr.mark();
            list.set(num - 1, curr);
            storage.saveTasksToFile(list);
            System.out.println(list.get(num - 1).stringify());
        } else if (command.contains("delete")) {
            System.out.println("Noted. I've removed this task:");
            int space = command.indexOf(" ");
            int num = Integer.parseInt(command.substring(space + 1)); // delete X
            System.out.println(list.get(num - 1).stringify());
            list.remove(num - 1);
            storage.saveTasksToFile(list);
            System.out.println("Now you have " + list.size() + " tasks in the list.");
        } else if (command.contains("todo") || command.contains("deadline") || command.contains("event")) {
            // create Task object with command
            if (command.equals("todo") || command.equals("deadline") || command.equals("event")) {
                System.out.println("OOPS!!! The description of a todo cannot be empty.");
            } else {
                System.out.println("Got it. I've added this task:");
                int space = command.indexOf(" ");
                String type = command.substring(0, space);

                if (type.equals("todo")) {
                    String rest = command.substring(space + 1);
                    Todo task = new Todo(rest, false);
                    System.out.println(task.stringify());
                    list.add(task);
                } else if (type.equals("deadline")) {
                    String rest = command.substring(space + 1);
                    String[] description = rest.split(" /by ");
                    Deadline task = new Deadline(description[0], false, description[1]);
                    System.out.println(task.stringify());
                    list.add(task);
                } else if (type.equals("event")) {
                    String rest = command.substring(space + 1);
                    String[] description = rest.split(" /from ");
                    String[] time = description[1].split(" /to ");
                    Event task = new Event(description[0], false, time[0], time[1]);
                    System.out.println(task.stringify());
                    list.add(task);
                }
                storage.saveTasksToFile(list);
                System.out.println("Now you have " + Integer.toString(list.size()) + " tasks in the list.");
            }
        } else {
            System.out.println("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }
}