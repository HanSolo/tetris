package eu.hansolo.fx.tetris;

import javafx.scene.text.Font;


public class Fonts {
    private static final String EMULOGIC_NAME;
    private static final String SILKWORM_NAME;
    private static       String emulogicName;
    private static       String silkwormName;

    private Fonts() {}


    static {
        try {
            emulogicName  = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/fx/tetris/Emulogic-zrEw.ttf"), 10).getName();
            silkwormName  = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/fx/tetris/G7Silkwormttf-z9qX.ttf"), 10).getName();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        EMULOGIC_NAME = emulogicName;
        SILKWORM_NAME = silkwormName;
    }


    // ******************** Methods *******************************************
    public static Font emulogic(final double size) { return new Font(EMULOGIC_NAME, size); }

    public static Font silkworm(final double size) { return new Font(SILKWORM_NAME, size); }

}
