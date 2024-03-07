package Tasks;

import Status.Status;
import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private Status status = Status.NEW;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
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


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) &&
                Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        int hash = 13;
        if (name != null) {
            hash += name.hashCode();
        }
        hash *= 17;
        if (description != null) {
            hash += description.hashCode();
        }
        hash *= 31;
        if (status != null) {
            hash += status.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        String result = "Tasks.Task{" +
                "name='" + name + '\'';
                if (description != null) {
                    result += ", description.lenght='" + description.length() + '\'';
                } else {
                    result += ", description.lenght='null" + '\'';
                }
        return result += ", status='" + status + '\'' +
                '}';
    }
}