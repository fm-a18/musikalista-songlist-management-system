package models;

public class SongRequest {
    private final int requestNumber;
    private final String studentName;
    private Song song;
    private static int requestCounter = 1;

    public SongRequest(String studentName, Song song){
        this(requestCounter, studentName, song);
    }

    public SongRequest(int requestNumber, String studentName, Song song) {
        if (requestNumber < 1) {
            throw new IllegalArgumentException("Request number must be positive.");
        }
        validateStudentName(studentName);
        validateSong(song);

        this.requestNumber = requestNumber;
        this.studentName = studentName.trim();
        this.song = song;

        if (requestNumber >= requestCounter) {
            requestCounter = requestNumber + 1;
        }
    }

    private static void validateStudentName(String studentName) {
        if (studentName == null || studentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or empty.");
        }
        if (!studentName.trim().matches("[\\p{L} .'-]+")) {
            throw new IllegalArgumentException(
                    "Student name may only contain letters, spaces, periods, apostrophes, and hyphens.");
        }
    }

    private static void validateSong(Song song) {
        if (song == null) {
            throw new IllegalArgumentException("Requested song cannot be null.");
        }
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        validateSong(song);
        this.song = song;
    }

    @Override
    public String toString() {
        return "Request #" + requestNumber + " by " + studentName + " -> " + song;
    }
}
