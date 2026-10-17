package comparators;

import models.Song;

import java.util.Comparator;

public class ArtistComparator implements Comparator<Song> {
    @Override
    public int compare(Song a, Song b){
        return a.getArtist().compareToIgnoreCase(b.getArtist());
    }
}
