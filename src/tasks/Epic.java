package tasks;

import tasks.type.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class Epic extends Task {

    public Epic(String name, String description, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
    }

    @Override
    public Type getType() {
        return  Type.EPIC;
    }

    @Override
    public String toString() {
        String result = "Tasks.Epic{" + "name='" + getName() + '\'';
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