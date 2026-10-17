package managers;

import models.Genre;
import models.Song;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static final String SONGS_FILE = "SONG_LIST.txt";
    private static final String REQUESTS_FILE = "REQUEST_LIST.txt";
    private static final String DELIMITER = "|";
    private static final String SPLIT_REGEX = "\\|";

    private final Path songsPath = Paths.get(SONGS_FILE);
    private final Path requestsPath = Paths.get(REQUESTS_FILE);

    public FileHandler() {
        createIfMissing(songsPath);
        createIfMissing(requestsPath);
    }

    public List<Song> readAllSongs() {
        List<Song> songs = new ArrayList<>();
        for (String line : readLines(songsPath)) {
            Song song = parseSong(line);
            if (song != null) songs.add(song);
        }
        return songs;
    }

    public boolean appendSong(Song song) {
        return appendLine(songsPath, formatSong(song));
    }

    public int getNextSongNumber() {
        int max = 0;
        for (Song s : readAllSongs()) {
            if (s.getSongNumber() > max) max = s.getSongNumber();
        }
        return max + 1;
    }

    public boolean updateSong(Song updated) {
        List<Song> songs = readAllSongs();
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getSongNumber() == updated.getSongNumber()) {
                songs.set(i, updated);
                return saveAllSongs(songs);
            }
        }
        return false;
    }

    public boolean saveAllSongs(List<Song> songs) {
        List<String> lines = new ArrayList<>();
        for (Song s : songs) lines.add(formatSong(s));
        return writeLines(songsPath, lines);
    }

    public List<Integer> readRequestQueue() {
        List<Integer> numbers = new ArrayList<>();
        for (String line : readLines(requestsPath)) {
            try {
                numbers.add(Integer.parseInt(line.trim()));
            } catch (NumberFormatException e) {
                System.out.println("Skipping invalid request line: " + line);
            }
        }
        return numbers;
    }

    public boolean appendRequest(int songNumber) {
        return appendLine(requestsPath, String.valueOf(songNumber));
    }

    public boolean saveRequestQueue(List<Integer> songNumbers) {
        List<String> lines = new ArrayList<>();
        for (int n : songNumbers) lines.add(String.valueOf(n));
        return writeLines(requestsPath, lines);
    }

    private void createIfMissing(Path path) {
        try {
            if (Files.notExists(path)) Files.createFile(path);
        } catch (IOException e) {
            System.out.println("Could not create " + path + ": " + e.getMessage());
        }
    }

    private List<String> readLines(Path path) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading " + path + ": " + e.getMessage());
        }
        return lines;
    }

    private boolean appendLine(Path path, String line) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            writer.write(line);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to " + path + ": " + e.getMessage());
            return false;
        }
    }

    private boolean writeLines(Path path, List<String> lines) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving " + path + ": " + e.getMessage());
            return false;
        }
    }

    private String formatSong(Song s) {
        return s.getSongNumber() + DELIMITER
                + clean(s.getTitle()) + DELIMITER
                + clean(s.getArtist()) + DELIMITER
                + s.getGenre().name();
    }

    private String clean(String text) {
        return text.replace("|", "").trim();
    }

    private Song parseSong(String line) {
        String[] parts = line.split(SPLIT_REGEX);
        if (parts.length != 4) {
            System.out.println("Skipping malformed song line: " + line);
            return null;
        }
        try {
            int number = Integer.parseInt(parts[0].trim());
            Genre genre = Genre.valueOf(parts[3].trim().toUpperCase());
            return new Song(number, parts[1].trim(), parts[2].trim(), genre);
        } catch (IllegalArgumentException e) {
            System.out.println("Skipping invalid song line: " + line);
            return null;
        }
    }
}
