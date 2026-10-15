//Group 5

import utils.DataValidation;
import utils.DisplayUtils;

public class Main {
    public static void main(String[] args) {
        DisplayUtils.printMenu("WELCOME, RAKS!",
                "#Song List",
                "Add New Song",
                "View Current Song Playing",
                "Play Next Requested Song / Skip Song",
                "#Filler", //Better name
                "View Song List",
                "Search Song List",
                "Sort Song List",
                "#Close Program",
                "Exit");

        int selectOption = DataValidation.intChoiceValidation("Select Option", 1, 2, 3, 4, 5, 6, 7);

    }

}
