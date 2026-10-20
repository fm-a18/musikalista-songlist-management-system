package managers;

import models.Genre;
import models.Song;
import models.SongRequest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileHandler {

    public static final String SONGS_FILE = "SONG_LIST.txt";
    public static final String REQUESTS_FILE = "REQUEST_LIST.txt";
    public static final String DELIMITER = "|";
    public static final String SPLIT_REGEX = "\\|";

    public final Path songsPath = Paths.get(SONGS_FILE);
    public final Path requestsPath = Paths.get(REQUESTS_FILE);

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

    //Requests
    public List<SongRequest> readAllRequests(List<Song> songs) {
        List<SongRequest> requests = new ArrayList<>();
        for (String line : readLines(requestsPath)) {
            SongRequest request = parseRequest(line, songs);
            if (request != null) requests.add(request);
        }
        return requests;
    }

    public boolean appendRequest(SongRequest request) {
        return appendLine(requestsPath, formatRequest(request));
    }

    public boolean saveAllRequests(Collection<SongRequest> requests) {
        List<String> lines = new ArrayList<>();
        for (SongRequest r : requests) lines.add(formatRequest(r));
        return writeLines(requestsPath, lines);
    }

    private String formatRequest(SongRequest r) {
        return r.getRequestNumber() + DELIMITER
                + r.getStudentName() + DELIMITER
                + r.getSong().getSongNumber();
    }

    private SongRequest parseRequest(String line, List<Song> songs) {
        String[] parts = line.split(SPLIT_REGEX);
        if (parts.length != 3) {
            System.out.println("Skipping malformed request line: " + line);
            return null;
        }

        try {
            int requestNumber = Integer.parseInt(parts[0].trim());
            int songNumber = Integer.parseInt(parts[2].trim());

            for (Song song : songs) {
                if (song.getSongNumber() == songNumber) {
                    return new SongRequest(requestNumber, parts[1].trim(), song);
                }
            }
            System.out.println("Skipping request for missing song #" + songNumber + ": " + line);
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("Skipping invalid request line: " + line);
            return null;
        }
    }

    public static int highestNumber(String fileName, int colIndex) {
        int max = 0;
        Path path = Paths.get(fileName);
        if (Files.notExists(path)) {
            return 0;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(SPLIT_REGEX, -1);
                if (parts.length > colIndex) {
                    try {
                        max = Math.max(max, Integer.parseInt(parts[colIndex].trim()));
                    } catch (NumberFormatException e) {
                        // skip IDs that don't end in a number
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error in reading " + fileName + ".");
        }
        return max;
    }
}
