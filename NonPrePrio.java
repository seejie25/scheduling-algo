import java.io.IOException;
import java.util.*;

public class NonPrePrio {
    private Menu menu = new Menu();

    public NonPrePrio() throws IOException {
        int processNum = menu.validateInteger("Enter number of process (3-10): ", 3, 10);

        // assigns array to get the arrival time
        int[] arrivalArr = new int[processNum];
        menu.validateTime("arrival time", arrivalArr, processNum);

        // assigns array to get the burst time for each process
        int[] burstArr = new int[processNum];
        menu.validateTime("burst time", burstArr, processNum);

        // assigns array to get the priority for each process
        int[] priorityArr = new int[processNum];
        menu.validateTime("priority", priorityArr, processNum);

        // creates final process object
        ArrayList<TempProcess> tempProcessList = new ArrayList<>();
        for (int i = 0; i < processNum; i++) {
            TempProcess tempProcess = new TempProcess(i, arrivalArr[i], burstArr[i], 0, 0, 0);
            tempProcessList.add(tempProcess);
        }

        // creates process object
        ArrayList<Process> processList = new ArrayList<>();
        for (int i = 0; i < processNum; i++) {
            Process process = new Process(i, arrivalArr[i], burstArr[i], priorityArr[i]);
            processList.add(process);
        }

        Collections.sort(processList, new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                return o1.getArrival().compareTo(o2.getArrival());
            }
        });

        Process firstProcess = processList.get(0);
        int processBurst = firstProcess.getArrival();

        ArrayList<Integer> runningProcessArr = new ArrayList<>();
        ArrayList<Integer> switchTimeArr = new ArrayList<>();
        ArrayList<TempProcess> finishProcessArr = new ArrayList<>();

        int totalburst = 0;
        switchTimeArr.add(totalburst);

        for (int i = 0; i < processNum; i++) {
            ArrayList<Process> arrived = new ArrayList<>();

            // finds all processes that have arrived
            for (Process process : processList) {
                if (process.getArrival() <= totalburst) {
                    arrived.add(process);
                }
            }

            if (arrived.size() != 0) {

                // finds the process that having the smallest priority
                for (Process process : arrived) {
                    if (process.getPriority() < firstProcess.getPriority()) {
                        firstProcess = process;
                    } 
                }

                // if same priority, checks for arrival time
                for (Process process : arrived) {
                    if (process.getPriority() == firstProcess.getPriority()) {
                        if (process.getArrival() < firstProcess.getArrival()) {
                            firstProcess = process;
                        }
                    } 
                }

                // if same arrival time and priority, checks for burst time
                for (Process process : arrived) {
                    if (process.getArrival() == firstProcess.getArrival() && process.getPriority() == firstProcess.getPriority()) {
                        if (process.getBurst() < firstProcess.getBurst()) {
                            firstProcess = process;
                        }
                    } 
                }

                runningProcessArr.add(firstProcess.getProcess());
                processList.remove(firstProcess);
                
                processBurst = firstProcess.getBurst();
                totalburst += processBurst;
                switchTimeArr.add(totalburst);

                for (TempProcess process : tempProcessList) {
                    if (process.getProcess() == firstProcess.getProcess()) {
                        process.setFinish(totalburst);
                        finishProcessArr.add(process);
                    }
                }

                if (processList.size() != 0) {
                    firstProcess = processList.get(0);
                }
            }
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

