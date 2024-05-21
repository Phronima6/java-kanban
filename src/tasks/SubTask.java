package tasks;

import tasks.type.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    public SubTask(String name, String description, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
    }

    @Override
    public Type getType() {
        return  Type.SUBTASK;
    }

    @Override
    public String toString() {
        String result = "Tasks.SubTask{" + "name='" + getName() + '\'';
        if (getDescription() != null) {
            result += ", description.lenght='" + getDescription().length() + '\'';
        } else {
            result += ", description.lenght='null" + '\'';
        }
        result += ", status='" + getStatus() + '\'';
        if (getStartTime() != null) {
            result += ", startTime='" + getStartTime() + '\'';
        } else {
            result += ", startTime='null" + '\'';
        }
        if (getDuration() != null) {
            result += ", duration='" + getDuration() + '\'';
        } else {
            result += ", duration='null" + '\'';
        }
        if (getEndTime() != null) {
            result += ", endTime='" + getEndTime() + '\'';
        } else {
            result += ", endTime='null" + '\'';
        }
        return result += '}';
    }

}