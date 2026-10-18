package comparators;

import models.Genre;
import models.Song;
import utils.DisplayUtils;

import java.util.ArrayList;
import java.util.Comparator;

public class GenreComparator implements Comparator<Song> {
    ArrayList<Song> songs = new ArrayList<>();

    @Override
    public int compare(Song a, Song b){
        return a.getGenre().name().compareTo(b.getGenre().name());
    }

    public void filterByGenre(Genre genre) {
        ArrayList<Song> matches = new ArrayList<>();

        Comparator<Genre> genreComparator = Comparator.naturalOrder();

        for (Song song : songs) {
            if (genreComparator.compare(song.getGenre(), genre) == 0) {
                matches.add(song);
            }
        }

        DisplayUtils.displaySongs(matches);
    }
}
