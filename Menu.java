import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    public Menu() {};
    
    public void showMenu() throws IOException {
        
        System.out.println("\n====================================");
        System.out.println("        SCHEDULING ALGORITHM        ");
        System.out.println("====================================");
        System.out.println("1. Round Robin");
        System.out.println("2. Preemptive SJF");
        System.out.println("3. Non Preemptive Priority");

        int selection = validateInteger("\nEnter a selection between 1 to 3: ", 1, 3);

        switch (selection) {
            case 1: {
                System.out.println("\n====================================");
                System.out.println("             ROUND ROBIN            ");
                System.out.println("====================================");
                new RoundRobin();
                break;
            }
            case 2: {
                System.out.println("\n====================================");
                System.out.println("           PREEMPTIVE SJF           ");
                System.out.println("====================================");
                new PreSJF();
                break;
            }
            case 3: {
                System.out.println("\n====================================");
                System.out.println("       NON PREEMPTIVE PRIORITY      ");
                System.out.println("====================================");
                new NonPrePrio();
                break;
            }
        }
    }

    public int validateInteger(String prompt, int min, int max) {
        Scanner input = new Scanner(System.in);
        int selection = 0;

        do {
            try {
                System.out.print(prompt);
                selection = input.nextInt();
                if (selection < min || selection > max) {
                    System.out.println("Invalid input! Please enter an integer between " + min + " to " + max + ".\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter an integer between " + min + " to " + max + ".\n");
            }
            input.nextLine();
        } while (selection < min || selection > max);

        return selection;
    }


    public int[] validateTime(String input, int[] integerArr, int processNum) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] stringArr;
        do {
            System.out.print("Enter " + input + " for each process (seperate with space): ");
            stringArr = reader.readLine().split(" ");

            if (stringArr.length != processNum)
                if (input.equals("priority"))
                    System.out.println("Number of priorities must match with number of processes.\n");
                else
                    System.out.println("Number of " + input + "s must match with number of processes.\n");

        } while (stringArr.length != processNum);

        for (int i = 0; i < stringArr.length; i++) {
            integerArr[i] = Integer.parseInt(stringArr[i]);
        }

        return integerArr;
    } 

    public void newRound() throws IOException {
        Scanner input = new Scanner(System.in);
        String selection;
        
        do { 
            System.out.print("\nDo you want to start a new simulation? (Y/N): ");
            selection = input.nextLine().toUpperCase();

            if (selection.equals("Y")) {
                new Menu().showMenu();
            }
            else if (selection.equals("N")) {
                System.out.println("Thank you & Goodbye!");
                System.exit(0);
            }
            else {
                System.out.println("Invalid input! Please enter a selection between Y or N.");
            }
        } while (!selection.equals("Y") && (!selection.equals("N")));
    }
}
