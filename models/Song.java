package models;

public class Song {
    private final int songNumber;
    private String title;
    private String artist;
    private Genre genre;
    private static int songCounter = 1;

    //For newly added songs.
    public Song(String title, String artist, Genre genre) {
        this(songCounter, title, artist, genre);
    }

    public Song(int songNumber, String title, String artist, Genre genre) {
        if (songNumber < 1) {
            throw new IllegalArgumentException("Song number must be positive.");
        }
        validateTitle(title);
        validateArtist(artist);
        validateGenre(genre);

        this.songNumber = songNumber;
        this.title = title.trim();
        this.artist = artist.trim();
        this.genre = genre;

        if (songNumber >= songCounter) {
            songCounter = songNumber + 1;
        }
    }

    private static void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Song title cannot be null or empty.");
        }
    }

    private static void validateArtist(String artist) {
        if (artist == null || artist.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist cannot be null or empty.");
        }
    }

    private static void validateGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null.");
        }
    }

    public int getSongNumber() {
        return songNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Genre getGenre() {
        return genre;
    }

    public static int getSongCounter() {
        return songCounter;
    }

    public void setTitle(String title) {
        validateTitle(title);
        this.title = title.trim();
    }

    public void setArtist(String artist) {
        validateArtist(artist);
        this.artist = artist.trim();
    }

    public void setGenre(Genre genre) {
        validateGenre(genre);
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song other)) return false;
        return title.equalsIgnoreCase(other.title) && artist.equalsIgnoreCase(other.artist);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(title.toLowerCase(), artist.toLowerCase());
    }

    @Override
    public String toString() {
        return "Song #" + songNumber + ": \"" + title + "\" by " + artist + " [" + genre + "]";
    }
}
