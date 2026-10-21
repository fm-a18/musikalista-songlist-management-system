package utils;

import models.Song;
import models.SongRequest;

import java.util.Collection;

public class DisplayUtils {
    public static final int TABLE_WIDTH = 100;
    public static final int MENU_WIDTH = 60;
    private static final String SONG_FORMAT = "%-8s | %-28s | %-22s | %-10s%n";
    private static final String REQUEST_FORMAT = "%-7s | %-18s | %-28s | %-22s%n";

    private static void centeredTitle(String tableName, int width) {
        System.out.println("=".repeat(width));
        int padding = (width - tableName.length()) / 2;
        System.out.println(" ".repeat(Math.max(0, padding)) + tableName);
        System.out.println("=".repeat(width));
    }

    private static String fit(String text, int max) {
        return text.length() > max ? text.substring(0, max - 3) + "..." : text;
    }

    public static void songHeader(String tableName) {
        centeredTitle(tableName, TABLE_WIDTH);
        System.out.printf(SONG_FORMAT, "Song No", "Title", "Artist", "Genre");
        System.out.println("-".repeat(TABLE_WIDTH));
    }

    public static void requestHeader(String tableName) {
        centeredTitle(tableName, TABLE_WIDTH);
        System.out.printf(REQUEST_FORMAT, "Req No", "Student", "Title", "Artist");
        System.out.println("-".repeat(TABLE_WIDTH));
    }

    public static void displaySongs(Collection<Song> songs) {
        if (songs.isEmpty()) {
            System.out.println("No songs available.");
            System.out.println("-".repeat(TABLE_WIDTH));
            return;
        }

        for (Song song : songs) {
            System.out.printf(
                    SONG_FORMAT,
                    String.format("%05d", song.getSongNumber()),
                    fit(song.getTitle(), 28),
                    fit(song.getArtist(), 22),
                    song.getGenre()
            );
        }
        System.out.println("-".repeat(TABLE_WIDTH));
    }

    public static void displayRequests(Collection<SongRequest> requests) {
        if (requests.isEmpty()) {
            System.out.println("No pending requests");
            System.out.println("-".repeat(TABLE_WIDTH));
            return;
        }

        for (SongRequest request : requests) {
            Song song = request.getSong();
            System.out.printf(
                    REQUEST_FORMAT,
                    request.getRequestNumber(),
                    fit(request.getStudentName(), 18),
                    fit(song.getTitle(), 28),
                    fit(song.getArtist(), 22)
            );
        }
        System.out.println("-".repeat(TABLE_WIDTH));
    }

    public static void printDivider(String tableName) {
        String divider = " " + tableName + " ";
        int dashCount = (MENU_WIDTH - divider.length()) / 2;

        System.out.println(
                "-".repeat(Math.max(0, dashCount))
                        + divider
                        + "-".repeat(Math.max(0,
                        MENU_WIDTH - dashCount - divider.length())));
    }

    public static void printMenu(String tableName, String... options) {
        centeredTitle(tableName, MENU_WIDTH);
        int ctr = 1;

        for (String option : options) {
            if (option.startsWith("#")) {
                System.out.println();
                printDivider(option.substring(1));
                continue;
            }
            System.out.printf(" [%d] %s%n", ctr++, option);
        }
        System.out.println("=".repeat(MENU_WIDTH));
    }

    public static void printSummaryBox(String tableName, String[][] rows) {
        int labelWidth = 0;
        for (String[] row : rows) {
            labelWidth = Math.max(labelWidth, row[0].length());
        }

        centeredTitle(tableName, MENU_WIDTH);
        for (String[] row : rows) {
            System.out.printf(" %-" + labelWidth + "s : %s%n", row[0], row[1]);
        }
        System.out.println("=".repeat(MENU_WIDTH));
    }
}
