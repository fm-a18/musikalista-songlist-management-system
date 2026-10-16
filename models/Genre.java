package models;

public enum Genre {
    ROMANCE, POP, ROCK, HIPHOP, INDIE;

    public static Genre fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be null or empty.");
        }
        try {
            return Genre.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Invalid genre: '" + value + "'. Valid genres: ROMANCE, POP, ROCK, HIPHOP, INDIE.");
        }
    }
}
