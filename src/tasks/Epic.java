package tasks;

import tasks.type.Type;

public class Epic extends Task {
    public Epic(String name, String description) {
        super(name, description);
    }

    @Override
    public Type getType() {
        return  Type.EPIC;
    }

    @Override
    public String toString() {
        String result = "Tasks.Epic{" +
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