module DropTheNumber {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.antlr.antlr4.runtime;


    opens domenico.dropthenumber to javafx.fxml;
    exports domenico.dropthenumber;
}