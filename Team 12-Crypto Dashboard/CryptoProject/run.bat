@echo off
set PATH_TO_FX="C:\javafx-sdk\lib"
javac --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml src\ui\Main.java
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml ui.Main
pause
