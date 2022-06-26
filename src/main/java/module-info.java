module com.example.regexvisualizer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires richtextfx.fat;

    opens com.example.regexvisualizer to javafx.fxml;
    exports com.example.regexvisualizer;
}