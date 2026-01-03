/**
 * Module info.
 */
module edu.westga.comp2320.project3part3cedricjones {
    requires javafx.controls;
    requires javafx.fxml;

    opens viewmodel to javafx.fxml;
    opens view to javafx.fxml;
    exports view;
    exports viewmodel;
    exports model;
}