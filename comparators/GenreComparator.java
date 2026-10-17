package comparators;

import models.Song;

import java.util.Comparator;

public class GenreComparator implements Comparator<Song> {
    @Override
    public int compare(Song a, Song b){
        return a.getGenre().name().compareTo(b.getGenre().name());
    }
}
