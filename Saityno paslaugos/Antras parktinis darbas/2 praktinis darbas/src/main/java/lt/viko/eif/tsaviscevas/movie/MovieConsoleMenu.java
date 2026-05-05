package lt.viko.eif.tsaviscevas.movie;

import java.util.Scanner;

public class MovieConsoleMenu {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n=== MOVIE SYSTEM MENU ===");
            System.out.println("1 - SOAP (get movies)");
            System.out.println("2 - Generate HTML (XSL)");
            System.out.println("3 - Generate PDF (XSL-FO)");
            System.out.println("0 - Exit");
            System.out.print("Choose: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("Running SOAP client...");
                    SoapTestClient.main(null);
                    break;

                case 2:
                    System.out.println("Open browser:");
                    System.out.println("http://localhost:8090/generate-html");
                    break;

                case 3:
                    System.out.println("Open browser:");
                    System.out.println("http://localhost:8090/generate-pdf");
                    break;

                case 0:
                    System.out.println("Bye!");
                    return;

                default:
                    System.out.println("Invalid option");
            }
        }
    }
}