import java.io.IOException;
import java.util.*;

public class PreSJF {
    private Menu menu = new Menu();

    public PreSJF() throws IOException {
        int processNum = menu.validateInteger("Enter number of process (3-10): ", 3, 10);

        // gets arrival time input from user
        int[] arrivalArr = new int[processNum];
        menu.validateTime("arrival time", arrivalArr, processNum);

        // gets burst time input from user
        int[] burstArr = new int[processNum];
        menu.validateTime("burst time", burstArr, processNum);

        // creates final process object
        ArrayList<TempProcess> tempProcessList = new ArrayList<>();
        for (int i = 0; i < processNum; i++) {
            TempProcess tempProcess = new TempProcess(i, arrivalArr[i], burstArr[i], 0, 0, 0);
            tempProcessList.add(tempProcess);
        }

        // creates process object
        ArrayList<Process> processList = new ArrayList<>();
        for (int i = 0; i < processNum; i++) {
            Process process = new Process(i, arrivalArr[i], burstArr[i]);
            processList.add(process);
        }

        // sorts the processes according to arrival times
        Collections.sort(processList, new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                return o1.getArrival().compareTo(o2.getArrival());
            }
        });

        ArrayList<Integer> runningProcessArr = new ArrayList<>();
        ArrayList<Integer> switchTimeArr = new ArrayList<>();
        ArrayList<TempProcess> finishProcessArr = new ArrayList<>();
        ArrayList<TempProcess> allProcess = new ArrayList<>();

        // gets total burst time
        int totalBurst = 0;
        for (Process process: processList) {
            totalBurst += process.getBurst();
        }

        // starts scheduling
        for (int i = 0; i < totalBurst; i++) {
            Process firstProcess;
            ArrayList<Process> arrived = new ArrayList<>();

            // finds all processes that have arrived
            for (Process process : processList) {
                if (process.getArrival() <= i) {
                    arrived.add(process);
                }
            }

            if (arrived.size() != 0) {
                firstProcess = arrived.get(0);

                // finds the process that having the smallest arrival time
                for (Process process : arrived) {
                    if (process.getArrival() < firstProcess.getArrival()) {
                        firstProcess = process;
                    } 
                }

                // if same arrival time, checks for burst time
                for (Process process : arrived) {
                    if (process.getArrival() == firstProcess.getArrival() && process.getArrival() < firstProcess.getArrival()) {
                        if (process.getBurst() < firstProcess.getBurst()) {
                            firstProcess = process;
                        }
                    } 
                }

                // the smallest burst size first
                for (Process process : arrived) {
                    if (process.getBurst() < firstProcess.getBurst()) {
                        firstProcess = process;
                    }
                }

                int processBurst = firstProcess.getBurst();
                processBurst--; // minus burst time by one
                firstProcess.setBurst(processBurst); // updates new burst time

                // records the sequence of processes
                TempProcess processes = new TempProcess(firstProcess.getProcess(), i + 1);
                allProcess.add(processes);

                // adds the process into finish array if the process is done
                if (processBurst == 0) {
                    processList.remove(firstProcess);
                    for (TempProcess process : tempProcessList) {
                        if (process.getProcess() == firstProcess.getProcess()) {
                            process.setFinish(i + 1);
                            finishProcessArr.add(process);
                        }
                    }
                }
            }
        }

        // finds context switch times
        ArrayList<TempProcess> duplicateProcess = new ArrayList<>();
        for (int i = 1; i < allProcess.size(); i++) {
            if (allProcess.get(i-1).getProcess() == allProcess.get(i).getProcess()) {
                duplicateProcess.add(allProcess.get(i-1));
            }
        }

        allProcess.removeAll(duplicateProcess);

        switchTimeArr.add(arrivalArr[0]);

        for (TempProcess tp : allProcess) {
            runningProcessArr.add(tp.getProcess());
            switchTimeArr.add(tp.getFinish());
        }

        System.out.println("\nOutput:\n");

        // draws gantt chart
        int[] durationArr = new int[runningProcessArr.size()];

        for (int i = 1; i < switchTimeArr.size(); i++) {
            int duration = switchTimeArr.get(i) - switchTimeArr.get(i-1);
            durationArr[i-1] = duration;
        }

        int dashCount = runningProcessArr.size()*5 + Arrays.stream(durationArr).sum();
        System.out.print("-".repeat(dashCount) + "\n|");

        for (int i = 0; i < runningProcessArr.size(); i++) {
            System.out.print("P" + runningProcessArr.get(i) + " " + " ".repeat(durationArr[i]) + "| ");
        }

        System.out.print("\n" + "-".repeat(dashCount) + "\n");
        System.out.print(arrivalArr[0]);   

        for (int i = 1; i < switchTimeArr.size(); i++) {
            System.out.printf(" ".repeat(durationArr[i-1]) + "%5d", switchTimeArr.get(i));
        }

        // calculates turnaround time and waiting time
        int turnaround;
        int waiting;
        double totalTurnaround = 0;
        double totalWaiting = 0;
        double avgTurnaround = 0;
        double avgWaiting = 0;
        
        for (TempProcess process : finishProcessArr) {
            turnaround = process.getFinish() - process.getArrival();
            process.setTurnaround(turnaround);
            totalTurnaround += turnaround;
            avgTurnaround = totalTurnaround/processNum;

            waiting = turnaround - process.getBurst();
            process.setWaiting(waiting);
            totalWaiting += waiting;
            avgWaiting = totalWaiting/processNum;
        }

        // prints table
        Collections.sort(finishProcessArr, new Comparator<TempProcess>() {
            @Override
            public int compare(TempProcess o1, TempProcess o2) {
                return o1.getProcess().compareTo(o2.getProcess());
            }
        });

        System.out.println("\n\n--------------------------------------------------------------------------------------");
        System.out.println("| Process | Arrival Time | Burst Time | Finish Time | Turnaround Time | Waiting Time |");
        System.out.println("--------------------------------------------------------------------------------------");
            for (TempProcess process : finishProcessArr) {
                System.out.printf("|    P%d   |      %2d      |     %2d     |      %2d     |        %2d       |      %2d      |\n", process.getProcess(), process.getArrival(), process.getBurst(), process.getFinish(), process.getTurnaround(), process.getWaiting());
            }
        System.out.println("--------------------------------------------------------------------------------------");

        System.out.printf("Total Turnaround Time: %.0f\n", totalTurnaround);
        System.out.printf("Total Waiting Time: %.0f\n\n", totalWaiting);
        System.out.printf("Average Turnaround Time: %.3f\n", avgTurnaround); 
        System.out.printf("Average Waiting Time: %.3f\n", avgWaiting);

        menu.newRound();
    }
}
