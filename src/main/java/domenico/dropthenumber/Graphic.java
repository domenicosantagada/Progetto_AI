package domenico.dropthenumber;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static domenico.dropthenumber.RandomMatrixApp.COL_SIZE;
import static domenico.dropthenumber.RandomMatrixApp.ROW_SIZE;


public class Graphic {

    static final int CELL_SIZE = 70;

    public final Label[][] cells = new Label[ROW_SIZE][COL_SIZE];

    public final boolean[][] cellsMatch = new boolean[ROW_SIZE][COL_SIZE];

    // GridPane per mostrare la matrice
    private GridPane gridPane;

    // StackPane per visualizzare lo score
    private StackPane scorePane;

    // Label per visualizzare lo score
    private Label scoreLabel;

    public Graphic(Stage primaryStage) {

        //GridPane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5); // Spaziatura orizzontale tra le celle
        gridPane.setVgap(5); // Spaziatura verticale tra le celle
        gridPane.setAlignment(Pos.CENTER); // Centra il contenuto della griglia

        //Score
        scoreLabel = new Label();
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        scoreLabel.setTextFill(Color.WHITE);

        //stackPane for score
        scorePane = new StackPane(scoreLabel);
        scorePane.setMinSize(400, 60); // Dimensioni fisse per lo score
        scorePane.setPrefSize(455, 60);
        scorePane.setMaxSize(400, 60);
        scorePane.setAlignment(Pos.CENTER); // Centraggio del testo
        scorePane.setStyle("-fx-background-color: #90EE90; " // Sfondo simile alle celle
                + "-fx-border-color: #DADADA; " // Colore del bordo
                + "-fx-border-radius: 10; " // Bordi arrotondati
                + "-fx-background-radius: 10; " // Bordi arrotondati
                + "-fx-padding: 10;"); // Spazio interno alla cella//


        // Creazione del layout principale con BorderPane
        BorderPane root = new BorderPane();
        root.setTop(scorePane); // Posiziona lo scorePane sopra la griglia
        BorderPane.setAlignment(scorePane, Pos.CENTER); // Centra lo scorePane orizzontalmente
        BorderPane.setMargin(scorePane, new Insets(20, 0, 10, 0)); // Aggiunge spazio sopra e sotto lo score
        root.setCenter(gridPane); // Posiziona la griglia al centro
        root.setStyle("-fx-background-color: #2E2E2E;"); // Sfondo scuro per contrasto

        for (int row = 0; row < ROW_SIZE; row++) {
            for (int col = 0; col < COL_SIZE; col++) {
                // Crea un rettangolo con bordo
                Rectangle border = new Rectangle(CELL_SIZE, CELL_SIZE);
                border.setArcHeight(10);
                border.setArcWidth(10);
                border.setFill(Color.WHITE);
                border.setStroke(Color.BLACK);

                // Crea una Label
                Label label = new Label("");
                label.setFont(Font.font("Arial", FontWeight.BOLD, 20)); // Font in grassetto
                label.setTextFill(Color.WHITE); // Colore del testo
                label.setAlignment(Pos.CENTER); // Centraggio del testo
                label.setMinSize(60, 60); // Dimensioni minime
                label.setPrefSize(60, 60); // Dimensioni preferite
                label.setMaxSize(60, 60); // Dimensioni massime
                // settiamo il font Arial con dimensione 20 in grassetto
                label.setStyle("-fx-font: 20 arial; -fx-font-weight: bold;");
                cells[row][col] = label;

                // Usa uno StackPane per combinare la Label e il Rettangolo
                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(border, label);

                // Aggiungi lo StackPane alla griglia
                gridPane.add(stackPane, col, row);
            }
        }


        /*cells[ROW_SIZE - 1][0].setText("8");
        cells[ROW_SIZE - 1][1].setText("16");
        cells[ROW_SIZE - 1][2].setText("32");
        cells[ROW_SIZE - 1][3].setText("64");
        cells[ROW_SIZE - 1][4].setText("32");*/


        /*cells[ROW_SIZE - 1][0].setText("8");
        cells[ROW_SIZE - 2][0].setText("16");
        cells[ROW_SIZE - 3][0].setText("8");
        cells[ROW_SIZE - 4][0].setText("8");
        cells[ROW_SIZE - 5][0].setText("2");*/


        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Drop the Number");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    // Metodo per determinare il colore di sfondo basato sul valore della cella
    private String getBackgroundColor(int value) {
        switch (value) {

            case 2:
                return "#FF1493";
            case 4:
                return "#00FF00";
            case 8:
                return "#87CEFA";
            case 16:
                return "#6A5ACD";
            case 32:
                return "#FF4500";
            case 64:
                return "#C71585";
            case 128:
                return "#D3D3D3";
            case 256:
                return "#FFA07A";
            case 512:
                return "#FFFF00";
            case 1024:
                return "#48D1CC";
            case 2048:
                return "#7FFF00";

            default:
                return "#FAEBD7";
        }
    }


    public int[][] getMatrix() {

        int[][] copiedMatrix = new int[ROW_SIZE][COL_SIZE];

        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < COL_SIZE; j++) {
                if (cells[i][j].getText().isEmpty()) {
                    copiedMatrix[i][j] = 0;
                } else {
                    copiedMatrix[i][j] = Integer.parseInt(cells[i][j].getText());
                }

            }
        }

        return copiedMatrix;
    }

    public void setCell(int i, int j, int value) {
        if (value != 0) {
            cells[i][j].setText(String.valueOf(value));
            String backgroundColor = getBackgroundColor(value);
            cells[i][j].setStyle("-fx-background-color: " + backgroundColor + "; " // Sfondo dinamico
                    + "-fx-border-color: #DADADA; " // Colore del bordo
                    + "-fx-border-radius: 5; " // Bordi arrotondati
                    + "-fx-background-radius: 5; " // Bordi arrotondati
                    + "-fx-padding: 10;"); // Spazio interno alla cella
        } else {
            cells[i][j].setText("");
            cells[i][j].setStyle("-fx-background-color: #FFFFFF ");
        }

        scoreLabel.setText(getScore());
    }

    private String getScore() {
        int sum = 0;
        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < COL_SIZE; j++) {
                if (cells[i][j].getText() != "") {
                    sum += Integer.parseInt(cells[i][j].getText());
                }
            }
        }
        return String.valueOf(sum);
    }


    public void updateBooleanMatrixMatch(int i, int j, boolean m) {
        cellsMatch[i][j] = m;
    }

}

