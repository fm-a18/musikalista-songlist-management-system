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

        String songTitle = utils.DataValidation.validateText("Enter Song Title");
        String songArtist = utils.DataValidation.validateText("Enter Artist Name");
        SongBook songBook = SongBook.getInstance();

        Song song = songBook.findSong(songTitle, songArtist);

        if (song == null) {
            Genre genre = DataValidation.validateGenre();
            song = songBook.addSong(songTitle, songArtist, genre);
        } else if (isQueued(song)) {
            System.out.println("Error: That song is already in the queue.");
            return;
        } else {
            System.out.println("Song found in list: " + song);
        }

        SongRequest request = new SongRequest(student, song);
        queue.offer(request);

        if (fileHandler.appendRequest(request)) {
            System.out.println("Request added (position " + queue.size() + "): " + request);
        } else {
            System.out.println("Request added but could not save to file.");
        }
    }

    // Dequeue
    public void playNext() {
        currentRequest = queue.poll();

        if (currentRequest == null) {
            System.out.println("No pending requests.");
            return;
        }

        if (!fileHandler.saveAllRequests(queue)) {
            System.out.println("Warning: could not update the request file.");
        }
    }

    // View

    public void viewCurrentSong() {
        if (currentRequest == null) {
            System.out.println("Nothing is playing right now.");
            return;
        }
        System.out.println("Now playing: " + currentRequest);
    }

    public void viewRequests() {
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