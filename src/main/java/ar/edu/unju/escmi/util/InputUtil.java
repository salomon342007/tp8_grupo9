package ar.edu.unju.escmi.util;

import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static int inputInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número entero válido.");
            }
        }
    }

    public static long inputLong(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
            }
        }
    }

    public static String inputString(String msg) {
        System.out.print(msg);
        return scanner.nextLine().trim();
    }

    public static double inputDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número decimal válido.");
            }
        }
    }

    public static void closeScanner() {
        scanner.close();
    }
}
