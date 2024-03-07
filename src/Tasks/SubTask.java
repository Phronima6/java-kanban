package Tasks;

import Tasks.Task;

public class SubTask extends Task {
    public SubTask(String name, String description) {
        super(name, description);
    }

    @Override
    public String toString() {
        String result = "Tasks.SubTask{" +
                "name='" + getName() + '\'';
        if (getDescription() != null) {
            result += ", description.lenght='" + getDescription().length() + '\'';
        } else {
            result += ", description.lenght='null" + '\'';
        }
        return result += ", status='" + getStatus() + '\'' +
                '}';
    }
}