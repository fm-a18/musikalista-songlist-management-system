//Group 5

import managers.RequestQueue;
import managers.SongBook;
import utils.DataValidation;
import utils.DisplayUtils;

public class Main {

    private static final String BANNER = """
                __  __ _   _ ___ ___ _  __   _   _    ___ ___ _____
               |  \\/  | | | / __|_ _| |/ /  /_\\ | |  |_ _/ __|_   _|
               | |\\/| | |_| \\__ \\| || ' <  / _ \\| |__ | |\\__ \\ | |
               |_|  |_|\\___/|___/___|_|\\_\\/_/ \\_\\____|___|___/ |_|
               
            """;

    public static void main(String[] args) {
        SongBook songBook = SongBook.getInstance();
        RequestQueue requestQueue = RequestQueue.getInstance();

        printBanner(songBook, requestQueue);

        boolean isRunning = true;

        while (isRunning) {
            DisplayUtils.printMenu("WELCOME, RAKS!",
                    "#Requests",
                    "Request Song",                         // 1 (Enqueue)
                    "View Current Song Playing",            // 2 (Peek)
                    "Play Next Requested Song",             // 3 (Dequeue, FIFO)
                    "View Requests",                        // 4
                    "#Song List",
                    "View Song List",                       // 5
                    "Search Song List",                     // 6
                    "Sort Song List",                       // 7
                    "#Close Program",
                    "Exit");                                // 8

            int selectOption = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 8);
            System.out.println();

            switch (selectOption) {
                case 1 -> requestQueue.addRequest();
                case 2 -> requestQueue.viewCurrentSong();
                case 3 -> requestQueue.playNext();
                case 4 -> requestQueue.viewRequests();
                case 5 -> songBook.viewSongList();
                case 6 -> songBook.searchSongList();
                case 7 -> songBook.sortSongsList();
                case 8 -> {
                    printGoodbye();
                    isRunning = false;
                }
            }
            System.out.println();
        }
    }

    private static void printBanner(SongBook songBook, RequestQueue requestQueue) {
        String line = "=".repeat(DisplayUtils.MENU_WIDTH);

        System.out.println();
        System.out.println(line);
        System.out.print(BANNER);
        System.out.println(line);
        System.out.println();

        DisplayUtils.printSummaryBox("SYSTEM STATUS", new String[][]{
                {"Songs Loaded", String.valueOf(songBook.getSongs().size())},
                {"Pending Requests", String.valueOf(requestQueue.size())}
        });
        System.out.println();
    }

    private static void printGoodbye() {
        String line = "=".repeat(DisplayUtils.MENU_WIDTH);

        System.out.println(line);
        System.out.println(centered("Thank you for using Musikalist!", DisplayUtils.MENU_WIDTH));
        System.out.println(centered("Keep the music playing. Goodbye!", DisplayUtils.MENU_WIDTH));
        System.out.println(line);
    }

    private static String centered(String text, int width) {
        int padding = Math.max(0, (width - text.length()) / 2);
        return " ".repeat(padding) + text;
    }
}