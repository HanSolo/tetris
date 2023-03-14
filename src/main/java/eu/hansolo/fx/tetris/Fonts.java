package eu.hansolo.fx.tetris;

import javafx.scene.text.Font;


public class Fonts {
    private static final String SILKWORM_NAME;
    private static       String silkwormName;

    private Fonts() {}


    static {
        try {
            silkwormName  = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/fx/tetris/Silkworm.ttf"), 10).getName();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        SILKWORM_NAME = silkwormName;
    }


    // ******************** Methods *******************************************
    public static Font silkworm(final double size) { return new Font(SILKWORM_NAME, size); }

}
