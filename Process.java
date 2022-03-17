public class Process {
    int process, arrival, burst, priority;

    Process(int process, int arrival, int burst) {
        this.process = (process + 1);
        this.arrival = arrival;
        this.burst = burst;
    }

    Process(int process, int arrival, int burst, int priority) {
        this.process = (process + 1);
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
    }

    public Integer getProcess() {
        return process;
    }

    public Integer getArrival() {
        return arrival;
    }

    public Integer getBurst() {
        return burst;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setBurst(int burst) {
        this.burst = burst;
    }
}
