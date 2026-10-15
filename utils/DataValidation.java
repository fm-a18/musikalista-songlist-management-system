package utils;

import models.Genre;

import java.util.Scanner;

public class DataValidation {
    public static Scanner sc = new Scanner(System.in);
    private static final int MAX_TEXT_LENGTH = 50;

    public static String validateText(String prompt) {
        boolean isValidated = false;
        String input = "";

        while (!isValidated) {
            System.out.print(prompt + ": ");
            input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.printf("Error: %s cannot be empty.%n", prompt);
            } else if (input.length() > MAX_TEXT_LENGTH) {
                System.out.printf("Error: %s must be %d characters or fewer.%n", prompt, MAX_TEXT_LENGTH);
            } else {
                isValidated = true;
            }
        }
        return input;
    }

    public static String validateStudentName() {
        boolean isValidated = false;
        String input = "";

        while (!isValidated) {
            System.out.print("Receiver Name: ");
            try {
                input = sc.nextLine().trim();
            } catch (java.util.NoSuchElementException e) {
                System.out.println("Error: No input available. Please try again.");
                continue;
            }

            if (input.isEmpty()) {
                System.out.println("Error: Receiver Name cannot be empty.");
            } else if (!input.matches("^\\p{L}[\\p{L} .'-]{0,49}$")) {
                System.out.println("Error: Receiver Name must be up to 50 characters (letters, spaces, . ' - only).");
            } else {
                isValidated = true;
            }
        }
        return input;
    }

    public static Genre validateGenre() {
        Genre[] genres = Genre.values();

        for (int i = 0; i < genres.length; i++) {
            System.out.printf(" [%d] %s%n", i + 1, genres[i]);
        }

        int choice = validateInt("Genre", 1, genres.length);
        return genres[choice - 1];
    }

    public static int validateSongNumber() {
        return validateInt("Song Number", 1, 999999); //Subject to change
    }

    public static int validateInt(String prompt, int min, int max) {
        int value = 0;
        boolean isValidated = false;

        while (!isValidated) {
            System.out.print(prompt + ": ");
            String input = sc.nextLine().trim();

            if (!input.matches("\\d+")) {
                System.out.printf("Error: Please enter a whole number from %d to %d.%n", min, max);
                continue;
            }

            try {
                value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.printf("Error: Please enter a whole number from %d to %d.%n", min, max);
                } else {
                    isValidated = true;
                }
            } catch (NumberFormatException e) {
                System.out.printf("Error: Please enter a whole number from %d to %d.%n", min, max);
            }
        }
        return value;
    }

    public static int intChoiceValidation(String prompt, int... choices) {
        int value = 0;
        boolean isValidated = false;
        while (!isValidated) {
            System.out.print(prompt + ": ");
            String input = sc.nextLine().trim();

            if (!input.matches("[1-9][0-9]*")) {
                printValidChoices(choices);
                continue;
            }

            try {
                value = Integer.parseInt(input);
                boolean found = false;
                for (int choice : choices) {
                    if (value == choice) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    isValidated = true;
                } else {
                    printValidChoices(choices);
                }
            } catch (NumberFormatException e) {
                printValidChoices(choices);
            }
        }
        return value;
    }

    public static void printValidChoices(int... choices) { //int values
        System.out.print("Error: Please select only from (");
        for (int i = 0; i < choices.length; i++) {
            System.out.print(choices[i]);
            if (i < choices.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(").");
    }
}
