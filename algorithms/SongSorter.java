package algorithms;

import models.Song;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SongSorter {
    private SongSorter() {
    }

    public static List<Song> mergeSort(List<Song> songs, Comparator<Song> comparator) {
        if (songs == null) {
            throw new IllegalArgumentException("Song list cannot be null.");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null.");
        }
        return sort(songs, comparator);
    }

    private static List<Song> sort(List<Song> songs, Comparator<Song> comparator) {
        if (songs.size() <= 1) {
            return new ArrayList<>(songs);
        }

        int mid = songs.size() / 2;
        List<Song> left = sort(songs.subList(0, mid), comparator);
        List<Song> right = sort(songs.subList(mid, songs.size()), comparator);

        return merge(left, right, comparator);
    }

    private static List<Song> merge(List<Song> left, List<Song> right, Comparator<Song> comparator) {
        List<Song> merged = new ArrayList<>(left.size() + right.size());
        int i = 0;
        int j = 0;

        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                merged.add(left.get(i));
                i++;
            } else {
                merged.add(right.get(j));
                j++;
            }
        }

        while (i < left.size()) {
            merged.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            merged.add(right.get(j));
            j++;
        }

        return merged;
    }
}
