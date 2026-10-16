package managers;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
 
public class RequestQueue {
 
    public static final int DEFAULT_MAX_QUEUE_SIZE = 100;
    public static final int DEFAULT_MAX_PER_REQUESTER = 3;
    public static final int MAX_TITLE_LENGTH = 100;
    public static final int MAX_NAME_LENGTH = 50;
 
    public enum AddResult {
        ADDED,
        INVALID_TITLE,
        INVALID_REQUESTER,
        DUPLICATE_SONG,
        QUEUE_FULL,
        REQUESTER_LIMIT_REACHED
    }
 
    public static class Request {
        private final String songTitle;
        private final String requestedBy;
        private final long timestamp;
 
        public Request(String songTitle, String requestedBy) {
            if (!isValidText(songTitle, MAX_TITLE_LENGTH)) {
                throw new IllegalArgumentException("Invalid song title");
            }
            if (!isValidText(requestedBy, MAX_NAME_LENGTH)) {
                throw new IllegalArgumentException("Invalid requester name");
            }
            this.songTitle = normalize(songTitle);
            this.requestedBy = normalize(requestedBy);
            this.timestamp = System.currentTimeMillis();
        }
 
        public String getSongTitle() {
            return songTitle;
        }
 
        public String getRequestedBy() {
            return requestedBy;
        }
 
        public long getTimestamp() {
            return timestamp;
        }
 
        @Override
        public String toString() {
            return "\"" + songTitle + "\" requested by " + requestedBy;
        }
    }
 
    private final LinkedList<Request> queue = new LinkedList<>();
    private final int maxQueueSize;
    private final int maxPerRequester;
 
    public RequestQueue() {
        this(DEFAULT_MAX_QUEUE_SIZE, DEFAULT_MAX_PER_REQUESTER);
    }
 
    public RequestQueue(int maxQueueSize, int maxPerRequester) {
        if (maxQueueSize <= 0) {
            throw new IllegalArgumentException("Max queue size must be greater than 0");
        }
        if (maxPerRequester <= 0) {
            throw new IllegalArgumentException("Max requests per person must be greater than 0");
        }
        if (maxPerRequester > maxQueueSize) {
            throw new IllegalArgumentException("Max requests per person cannot exceed max queue size");
        }
        this.maxQueueSize = maxQueueSize;
        this.maxPerRequester = maxPerRequester;
    }
 
    public synchronized AddResult addRequest(String songTitle, String requestedBy) {
        if (!isValidText(songTitle, MAX_TITLE_LENGTH)) {
            return AddResult.INVALID_TITLE;
        }
        if (!isValidText(requestedBy, MAX_NAME_LENGTH)) {
            return AddResult.INVALID_REQUESTER;
        }
        if (queue.size() >= maxQueueSize) {
            return AddResult.QUEUE_FULL;
        }
        if (isQueued(songTitle)) {
            return AddResult.DUPLICATE_SONG;
        }
        if (countRequestsBy(requestedBy) >= maxPerRequester) {
            return AddResult.REQUESTER_LIMIT_REACHED;
        }
        queue.addLast(new Request(songTitle, requestedBy));
        return AddResult.ADDED;
    }
 
    public synchronized Optional<Request> playNext() {
        return Optional.ofNullable(queue.pollFirst());
    }
 
    public synchronized Optional<Request> peekNext() {
        return Optional.ofNullable(queue.peekFirst());
    }
 
    public synchronized boolean removeRequest(String songTitle) {
        if (songTitle == null || normalize(songTitle).isEmpty()) {
            return false;
        }
        String target = normalize(songTitle);
        return queue.removeIf(r -> r.getSongTitle().equalsIgnoreCase(target));
    }
 
    public synchronized boolean isQueued(String songTitle) {
        if (songTitle == null || normalize(songTitle).isEmpty()) {
            return false;
        }
        String target = normalize(songTitle);
        for (Request r : queue) {
            if (r.getSongTitle().equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }
 
    public synchronized int getPosition(String songTitle) {
        if (songTitle == null || normalize(songTitle).isEmpty()) {
            return -1;
        }
        String target = normalize(songTitle);
        int position = 1;
        for (Request r : queue) {
            if (r.getSongTitle().equalsIgnoreCase(target)) {
                return position;
            }
            position++;
        }
        return -1;
    }
 
    public synchronized int countRequestsBy(String requestedBy) {
        if (requestedBy == null || normalize(requestedBy).isEmpty()) {
            return 0;
        }
        String target = normalize(requestedBy);
        int count = 0;
        for (Request r : queue) {
            if (r.getRequestedBy().equalsIgnoreCase(target)) {
                count++;
            }
        }
        return count;
    }
 
    public synchronized List<Request> getAllRequests() {
        return Collections.unmodifiableList(new ArrayList<>(queue));
    }
 
    public synchronized int size() {
        return queue.size();
    }
 
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
 
    public synchronized boolean isFull() {
        return queue.size() >= maxQueueSize;
    }
 
    public synchronized void clear() {
        queue.clear();
    }
 
    private static String normalize(String text) {
        return text.trim().replaceAll("\\s+", " ");
    }
 
    private static boolean isValidText(String text, int maxLength) {
        if (text == null) {
            return false;
        }
        String cleaned = normalize(text);
        if (cleaned.isEmpty() || cleaned.length() > maxLength) {
            return false;
        }
        return cleaned.chars().noneMatch(Character::isISOControl);
    }
 
    public static void main(String[] args) {
        RequestQueue rq = new RequestQueue(5, 2);
        System.out.println(rq.addRequest("Amazing Grace", "Maria"));
        System.out.println(rq.addRequest("  amazing   grace ", "Sam"));
        System.out.println(rq.addRequest("", "John"));
        System.out.println(rq.addRequest("Hallelujah", null));
        System.out.println(rq.addRequest("Hallelujah", "Maria"));
        System.out.println(rq.addRequest("Imagine", "Maria"));
        System.out.println(rq.peekNext().map(Request::toString).orElse("Queue is empty"));
        System.out.println(rq.playNext().map(Request::toString).orElse("Queue is empty"));
        System.out.println(rq.getAllRequests());
    }
}
 
