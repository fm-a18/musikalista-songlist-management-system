package managers;

import models.Song;
import models.SongRequest;
import utils.DataValidation;
import utils.DisplayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RequestQueue {
    private static final int MAX_QUEUE_SIZE = 100;
    private static final int MAX_PER_STUDENT = 3;

    private static RequestQueue instance;
    private final Queue<SongRequest> queue = new LinkedList<>();
    private SongRequest currentRequest;

    private RequestQueue() {
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

        int number = DataValidation.validateSongNumber();
        Song song = SongBook.getInstance().findBySongNumber(number);

        if (song == null) {
            System.out.println("Error: No song with number " + number + ".");
            return;
        }

        if (isQueued(song)) {
            System.out.println("Error: That song is already in the queue.");
            return;
        }

        SongRequest request = new SongRequest(student, song);
        queue.offer(request);
        System.out.println("Request added (position " + queue.size() + "): " + request);
    }

    // Dequeue
    public void playNext() {
        currentRequest = queue.poll();

        if (currentRequest == null) {
            System.out.println("No pending requests.");
            return;
        }
        System.out.println("Now playing: " + currentRequest);
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
        return Collections.unmodifiableList(new ArrayList<>(queue));
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}