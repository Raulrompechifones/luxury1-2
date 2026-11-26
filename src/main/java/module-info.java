module pe.edu.upeu.luxury12 {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens pe.edu.upeu.luxury12 to javafx.fxml;
    opens pe.edu.upeu.luxury12.Controller to javafx.fxml;
    opens pe.edu.upeu.luxury12.Entidad to javafx.base;


    exports pe.edu.upeu.luxury12;
}