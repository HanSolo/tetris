module eu.hansolo.fx.tetris {
    // Java
    requires java.base;

    // Java-FX
    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive javafx.media;


    exports eu.hansolo.fx.tetris to javafx.base,javafx.graphics,javafx.controls,javafx.media;
}