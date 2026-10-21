package managers;

import models.Genre;
import models.Song;
import models.SongRequest;
import utils.DataValidation;
import utils.DisplayUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RequestQueue {
    private static final int MAX_QUEUE_SIZE = 100;
    private static final int MAX_PER_STUDENT = 3;

    private static RequestQueue instance;
    private final Queue<SongRequest> queue = new LinkedList<>();
    private final FileHandler fileHandler = new FileHandler();
    private SongRequest currentRequest;

    private RequestQueue() {
        queue.addAll(fileHandler.readAllRequests((SongBook.getInstance().getSongs())));
    }

    public static RequestQueue getInstance() {
        if (instance == null) {
            instance = new RequestQueue();
        }
        return instance;
    }

    // Enqueue
    public void addRequest() {
        if (queue.size() >= MAX_QUEUE_SIZE) {
            System.out.println("Error: The request queue is full.");
            return;
        }

        String student = DataValidation.validateStudentName();

        if (countRequestsBy(student) >= MAX_PER_STUDENT) {
            System.out.println("Error: " + student + " already has "
                    + MAX_PER_STUDENT + " pending requests.");
            return;
        }

        Song song = promptForSong();

        if (isQueued(song)) {
            System.out.println("Error: That song is already in the queue.");
            return;
        }

        SongRequest request = new SongRequest(student, song);
        queue.offer(request);

        boolean saved = fileHandler.appendRequest(request);
        showRequestAdded(request);

        if (!saved) {
            System.out.println("Warning: could not save to file.");
        }
    }

    private Song promptForSong() {
        String title = DataValidation.validateText("Song Title");
        String artist = DataValidation.validateText("Artist Name");

        SongBook songBook = SongBook.getInstance();
        Song song = songBook.findSong(title, artist);

        if (song == null) {
            Genre genre = DataValidation.validateGenre();
            song = songBook.addSong(title, artist, genre);
        }
        return song;
    }

    private void showRequestAdded(SongRequest request) {
        Song song = request.getSong();

        DisplayUtils.printSummaryBox("REQUEST ADDED", new String[][]{
                {"Position", String.valueOf(queue.size())},
                {"Student", request.getStudentName()},
                {"Title", song.getTitle()},
                {"Artist", song.getArtist()},
                {"Song No", song.getFormattedNumber()}
        });
    }

    // Dequeue
    public void playNext() {
        currentRequest = queue.poll();

        if (currentRequest == null) {
            System.out.println("No pending requests. Nothing is playing now.");
            return;
        }

        if (!fileHandler.saveAllRequests(queue)) {
            System.out.println("Warning: could not update the request file.");
        }
        viewCurrentSong();
    }

    // View

    // Peek: look at the next request without removing it
    public SongRequest peekNext() {
        return queue.peek();
    }

    public void viewCurrentSong() {
        if (currentRequest == null) {
            System.out.println("Nothing is playing right now.");
        } else {
            Song song = currentRequest.getSong();
            DisplayUtils.printSummaryBox("NOW PLAYING", new String[][]{
                    {"Title", song.getTitle()},
                    {"Artist", song.getArtist()},
                    {"Genre", song.getGenre().toString()},
                    {"Requested By", currentRequest.getStudentName()},
                    {"Request No", String.valueOf(currentRequest.getRequestNumber())},
                    {"Song No", song.getFormattedNumber()}
            });
        }

        SongRequest next = peekNext();
        if (next == null) {
            System.out.println("Up next: (queue is empty)");
        } else {
            System.out.println("Up next: \"" + next.getSong().getTitle() + "\" by "
                    + next.getSong().getArtist() + " (requested by " + next.getStudentName() + ")");
        }
    }

    public void viewRequests() {
        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        DisplayUtils.requestHeader("REQUEST QUEUE");
        DisplayUtils.displayRequests(queue);
    }

    public boolean isQueued(Song song) {
        for (SongRequest r : queue) {
            if (r.getSong().equals(song)) {
                return true;
            }
        }
        return false;
    }

    public int countRequestsBy(String student) {
        int count = 0;
        for (SongRequest r : queue) {
            if (r.getStudentName().equalsIgnoreCase(student.trim())) {
                count++;
            }
        }
        return count;
    }

    public List<SongRequest> getRequests() {
        return List.copyOf(queue);
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}