package comparators;

import models.Song;
import utils.DisplayUtils;

import java.util.ArrayList;
import java.util.Comparator;

public class TitleComparator implements Comparator<Song> {
    ArrayList<Song> songs = new ArrayList<>();

    @Override
    public int compare(Song a, Song b){
        return a.getTitle().compareToIgnoreCase(b.getTitle());
    }

    public void filterByTitle(String title) {
        ArrayList<Song> matches = new ArrayList<>();

        Comparator<String> titleComparator = String.CASE_INSENSITIVE_ORDER;

        for (Song song : songs) {
            if (titleComparator.compare(song.getTitle(), title.trim()) == 0) {
                matches.add(song);
            }
        }

        DisplayUtils.displaySongs(matches);
    }
}
