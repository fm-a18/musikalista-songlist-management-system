package comparators;

import models.Song;
import utils.DisplayUtils;

import java.util.ArrayList;
import java.util.Comparator;

public class ArtistComparator implements Comparator<Song> {
    ArrayList<Song> songs = new ArrayList<>();

    @Override
    public int compare(Song a, Song b){
        return a.getArtist().compareToIgnoreCase(b.getArtist());
    }

    public void filterByArtist(String artist) {
        ArrayList<Song> matches = new ArrayList<>();

        Comparator<String> artistComparator = String.CASE_INSENSITIVE_ORDER;

        for (Song song : songs) {
            if (artistComparator.compare(song.getArtist(), artist.trim()) == 0) {
                matches.add(song);
            }
        }

        DisplayUtils.displaySongs(matches);
    }
}
