package tasks;

import tasks.status.Status;
import tasks.type.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Task {

    private String name;
    private String description;
    private Status status = Status.NEW;
    private LocalDateTime startTime;
    private Duration duration;
    private LocalDateTime endTime;

    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = Optional.ofNullable(startTime)
                .map(localDateTime -> localDateTime.plus(duration))
                .orElse(null);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return Type.TASK;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) &&
                Objects.equals(status, task.status) && Objects.equals(startTime, task.startTime) &&
                Objects.equals(duration, task.duration) && Objects.equals(endTime, task.endTime);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        if (name != null) {
            hash += name.hashCode();
        }
        hash *= 11;
        if (description != null) {
            hash += description.hashCode();
        }
        hash *= 13;
        if (status != null) {
            hash += status.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        String result = "Tasks.Task{" + "name='" + name + '\'';
        if (description != null) {
            result += ", description.lenght='" + description.length() + '\'';
        } else {
            result += ", description.lenght='null" + '\'';
        }
        result += ", status='" + status + '\'';
        if (startTime != null) {
            result += ", startTime='" + startTime + '\'';
        } else {
            result += ", startTime='null" + '\'';
        }
        if (duration != null) {
            result += ", duration='" + duration + '\'';
        } else {
            result += ", duration='null" + '\'';
        }
        if (endTime != null) {
            result += ", endTime='" + endTime + '\'';
        } else {
            result += ", endTime='null" + '\'';
        }
        return result += '}';
    }

}