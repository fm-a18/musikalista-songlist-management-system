package utils;

import models.SongRequest;

public class SongNumberGenerator {
    private static final int MAX_SONG_NUMBER = 99999;
    private static int ctr = 0;
    private static boolean isSeeded = false;


    private SongNumberGenerator(){
    }

    public static int songNumberGenerator() {
        if (!isSeeded) {
            ctr = managers.FileHandler.highestNumber(managers.FileHandler.SONGS_FILE, 0);
            isSeeded = true;
            }

        if(ctr >= MAX_SONG_NUMBER){
            throw new IllegalStateException("No more song numbers available.");
        }
        ctr++;
        return ++ctr;
    }
}
