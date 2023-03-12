package eu.hansolo.fx.tetris;

import javafx.scene.paint.Color;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class Constants {
    public static final String HOME_FOLDER          = new StringBuilder(System.getProperty("user.home")).append(File.separator).toString();
    public static final String PROPERTIES_FILE_NAME = "tetris.properties";
    public static final String HIGHSCORE_KEY        = "highscore";

    public static final Color  GITHUB_GRAY_BLOCK    = Color.rgb(22, 27, 34);

    public static final Map<Integer, Long> LEVEL_SPEED_MAP = new HashMap<>() {{
        put(1, 500_000_000l);
        put(2, 500_000_000l);
        put(3, 500_000_000l);
        put(4, 500_000_000l);
        put(5, 500_000_000l);
        put(6, 500_000_000l);
        put(7, 500_000_000l);
        put(8, 500_000_000l);
        put(9, 500_000_000l);
        put(10, 400_000_000l);
        put(11, 400_000_000l);
        put(12, 400_000_000l);
        put(13, 300_000_000l);
        put(14, 300_000_000l);
        put(15, 200_000_000l);
        put(16, 200_000_000l);
        put(17, 200_000_000l);
        put(18, 100_000_000l);
        put(19, 100_000_000l);
        put(20, 100_000_000l);
    }};
}

