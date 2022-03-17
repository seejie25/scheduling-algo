import java.io.IOException;
import java.util.*;

public class RoundRobin {
    private Menu menu = new Menu();

    public RoundRobin() throws IOException {
        int processNum = menu.validateInteger("Enter number of process (3-10): ", 3, 10);
        
        // gets arrival time input from user
        int[] arrivalArr = new int[processNum];
        menu.validateTime("arrival time", arrivalArr, processNum);

        // gets burst time input from user
        int[] burstArr = new int[processNum];
        menu.validateTime("burst time", burstArr, processNum);

        int tempQuantum = menu.validateInteger("Enter time quantum: ", 1, Integer.MAX_VALUE);
        
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
        
        // determines the first process and the first running time
        int switchTime = processList.get(0).getArrival();
        Process firstProcess = processList.get(0);
        
        Queue<Process> processQ = new LinkedList<Process>(); // creates a queue list to for the process queue
        processQ.add(firstProcess); // adds the first process into the queue

        ArrayList<Integer> runningProcessArr = new ArrayList<>();
        ArrayList<Integer> switchTimeArr = new ArrayList<>();
        ArrayList<TempProcess> finishProcessArr = new ArrayList<>();

        switchTimeArr.add(switchTime);

        // starts scheduling
        while (!(processQ.size() == 0)) {
            int quantum = tempQuantum;
            int processBurst = processQ.peek().getBurst(); // gets the burst time of the current process

            if (processBurst < quantum) { // if burst time is less than quantum
                quantum = processBurst;
            }
            
            processBurst -= quantum;
            firstProcess.setBurst(processBurst); // updates new burst time

            switchTime += quantum;
            switchTimeArr.add(switchTime); // adds the switch time into an array

            runningProcessArr.add(firstProcess.getProcess()); // adds the process into running process array

            // adds the process into finish array if the process is done
            if (processBurst == 0) {
                for (TempProcess process : tempProcessList) {
                    if (process.getProcess() == firstProcess.getProcess()) {
                        process.setFinish(switchTime);
                        finishProcessArr.add(process);
                    }
                }
            }

            for (Process process : processList) { // finds process that already arrived and add into the queue
                if (process.getArrival() <= switchTime && !(process.getBurst() == 0) && (!processQ.contains(process))) {
                    processQ.add(process);
                }
            }

            processQ.remove(); // removes the process from the queue

            if (processQ.peek() == null && firstProcess.getBurst() != 0) { // special situation if the last process in the queue haven't complete
                processQ.add(firstProcess);
            }

            if (processQ.peek() == firstProcess) { // if the current process still have remaining burst time
                processQ.add(firstProcess); // adds into the tail of the queue
                processQ.remove(); // remove from the head of the queue
            }

            firstProcess = processQ.peek(); // define the head of the queue as the next process going to execute
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
        System.out.print(switchTimeArr.get(0));   

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