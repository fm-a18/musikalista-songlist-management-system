package algorithms;

import models.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SongSearcher {
    private SongSearcher() {
    }

    public static List<Song> searchByTitle(List<Song> sortedByTitle, String title) {
        return searchByText(sortedByTitle, title, Song::getTitle);
    }

    public static List<Song> searchByArtist(List<Song> sortedByArtist, String artist) {
        return searchByText(sortedByArtist, artist, Song::getArtist);
    }

    public static Song searchBySongNumber(List<Song> sortedByNumber, int number) {
        if (sortedByNumber == null) {
            throw new IllegalArgumentException("Song list cannot be null.");
        }

        int low = 0;
        int high = sortedByNumber.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int midNumber = sortedByNumber.get(mid).getSongNumber();

            if (midNumber == number) {
                return sortedByNumber.get(mid);
            } else if (midNumber < number) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    private static List<Song> searchByText(List<Song> sorted, String key, Function<Song, String> field) {
        if (sorted == null) {
            throw new IllegalArgumentException("Song list cannot be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("Search key cannot be null.");
        }

        List<Song> results = new ArrayList<>();
        String target = key.trim();
        int index = findIndex(sorted, target, field);

        if (index == -1) {
            return results;
        }

        int start = index;
        while (start > 0 && field.apply(sorted.get(start - 1)).compareToIgnoreCase(target) == 0) {
            start--;
        }

        int end = index;
        while (end < sorted.size() - 1 && field.apply(sorted.get(end + 1)).compareToIgnoreCase(target) == 0) {
            end++;
        }

        for (int i = start; i <= end; i++) {
            results.add(sorted.get(i));
        }
        return results;
    }

    private static int findIndex(List<Song> sorted, String target, Function<Song, String> field) {
        int low = 0;
        int high = sorted.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = field.apply(sorted.get(mid)).compareToIgnoreCase(target);

            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }
}
