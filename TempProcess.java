public class TempProcess {
    int process, arrival, burst, finish, turnaround, waiting;

    TempProcess(int process, int arrival, int burst, int finish, int turnaround, int waiting) {
        this.process = (process + 1);
        this.arrival = arrival;
        this.burst = burst;
        this.finish = finish;
        this.turnaround = turnaround;
        this.waiting = waiting;
    }

    TempProcess(int process, int finish) {
        this.process = process;
        this.finish = finish;
    }

    public Integer getProcess() {
        return process;
    }

    public int getArrival() {
        return arrival;
    }

    public int getBurst() {
        return burst;
    }

    public int getFinish() {
        return finish;
    }

    public int getTurnaround() {
        return turnaround;
    }

    public int getWaiting() {
        return waiting;
    }

    public void setBurst(int burst) {
        this.burst = burst;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public void setTurnaround(int turnaround) {
        this.turnaround = turnaround;
    }

    public void setWaiting(int waiting) {
        this.waiting = waiting;
    }
}
