package managers;

import algorithms.SongSearcher;
import algorithms.SongSorter;
import comparators.ArtistComparator;
import comparators.GenreComparator;
import comparators.TitleComparator;
import models.Genre;
import models.Song;
import utils.DataValidation;
import utils.DisplayUtils;
import utils.SongNumberGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SongBook {
    private static SongBook instance;
    private final List<Song> songs;
    private final FileHandler fileHandler = new FileHandler();

    private SongBook() {
        songs = new ArrayList<>(fileHandler.readAllSongs());
    }

    public static SongBook getInstance() {
        if (instance == null) {
            instance = new SongBook();
        }
        return instance;
    }

    public Song addSong(String title, String artist, Genre genre) {
        Song song = new Song(SongNumberGenerator.songNumberGenerator(), title, artist, genre);
        songs.add(song);

        if (!fileHandler.appendSong(song)) {
            System.out.println("Song added but could not save to file.");
        }

        return song;
    }

    public Song findSong(String title, String artist) {
        for (Song song : songs) {
            if (song.getTitle().equalsIgnoreCase(title.trim())
                    && song.getArtist().equalsIgnoreCase(artist.trim())) {
                return song;
            }
        }
        return null;
    }

    public boolean songExists(String title, String artist) {
        return findSong(title, artist) != null;
    }

    public void viewSongList() {
        if (songs.isEmpty()) {
            System.out.println("No songs available.");
            return;
        }

        DisplayUtils.songHeader("SONG LIST");
        DisplayUtils.displaySongs(songs);
    }

    public void searchSongList() {
        if (songs.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }

        boolean isDone = false;

        while (!isDone) {
            DisplayUtils.printMenu("SEARCH SONG LIST",
                    "Title",
                    "Artist",
                    "Song Number",
                    "#Exit",
                    "Back");

            int categoryChoice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4);

            switch (categoryChoice) {
                case 1 -> {
                    String title = DataValidation.validateText("Title");
                    List<Song> sorted = SongSorter.mergeSort(songs, new TitleComparator());
                    showResults(SongSearcher.searchByTitle(sorted, title));
                }
                case 2 -> {
                    String artist = DataValidation.validateText("Artist");
                    List<Song> sorted = SongSorter.mergeSort(songs, new ArtistComparator());
                    showResults(SongSearcher.searchByArtist(sorted, artist));
                }
                case 3 -> {
                    int number = DataValidation.validateSongNumber();
                    List<Song> sorted = SongSorter.mergeSort(songs,
                            Comparator.comparingInt(Song::getSongNumber));
                    Song found = SongSearcher.searchBySongNumber(sorted, number);
                    showResults(found == null ? new ArrayList<>() : List.of(found));
                }
                case 4 -> isDone = true;
            }
        }
    }

    public void sortSongsList() {
        if (songs.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }

        DisplayUtils.printMenu("Sort Song List By",
                "Title", "Artist", "Genre",
                "#Exit", "Back");
        int field = DataValidation.intChoiceValidation("Select Option", 1, 2, 3, 4);

        if (field == 4) {
            return;
        }

        DisplayUtils.printMenu("Order", "Ascending", "Descending");
        int order = DataValidation.intChoiceValidation("Select Option", 1, 2);

        Comparator<Song> comparator = switch (field) {
            case 1 -> new TitleComparator();
            case 2 -> new ArtistComparator();
            default -> new GenreComparator();
        };

        if (order == 2) {
            comparator = comparator.reversed();
        }

        String[] fieldNames = {"TITLE", "ARTIST", "GENRE"};
        String direction = (order == 1) ? "ASC" : "DESC";

        DisplayUtils.songHeader("SORTED BY " + fieldNames[field - 1] + " (" + direction + ")");
        DisplayUtils.displaySongs(SongSorter.mergeSort(songs, comparator));
    }

    public Song findBySongNumber(int number) {
        for (Song song : songs) {
            if (song.getSongNumber() == number) {
                return song;
            }
        }
        return null;
    }

    public List<Song> getSongs() {
        return Collections.unmodifiableList(songs);
    }

    private void showResults(List<Song> results) {
        if (results.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }

        DisplayUtils.songHeader("SEARCH RESULTS");
        DisplayUtils.displaySongs(results);
    }
}