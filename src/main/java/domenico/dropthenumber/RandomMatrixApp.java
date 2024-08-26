package domenico.dropthenumber;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static javafx.scene.paint.Color.RED;


public class RandomMatrixApp extends Application implements EventHandler<ActionEvent> {

    // Costanti per le dimensioni della matrice
    static final int ROW_SIZE = 6;
    static final int COL_SIZE = 5;
    static final int CELL_SIZE = 70;
    //STOP
    private static boolean running = true;
    //HANDLE
    private static Handler handler;
    // Matrice di Label per visualizzare i numeri
    public final Label[][] cells = new Label[ROW_SIZE][COL_SIZE];
    //List numeri
    public final ArrayList<Integer> listRandValueBlock = new ArrayList<>() {{
        add(2);
    }};
    // Matrice di booleani per tenere traccia delle celle riempite
    private final boolean[][] filledCells = new boolean[ROW_SIZE][COL_SIZE];
    // Oggetto Random per generare numeri casuali
    private final Random random = new Random();
    // Mappa dei numeri multipli di 2 con i colori associati
    private final Map<Integer, Color> numberColorMap = new HashMap<>();
    //FACT AND RULE
    InputProgram fixedProgram = new ASPInputProgram();
    InputProgram variableProgram = new ASPInputProgram();

    //RandNumber
    public static int getRandom(ArrayList<Integer> listRandValueBlock) {
        int rnd = new Random().nextInt(listRandValueBlock.size());
        return listRandValueBlock.get(rnd);
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Creazione dell'handler per Windows (manca DLV nella cartella lib)
    //private static void createHandler() { handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));}

    // Creazione dell'handler per MacOS
    private static void createHandler() {
        handler = new DesktopHandler(new DLV2DesktopService("lib/dlv-2.1.1-macos"));
    }

    @Override
    public void handle(ActionEvent event) {
        if (!running) {
            resetGame();
        } else {

            MapPossibleBlockArrayMatrix map = new MapPossibleBlockArrayMatrix(getMatrix());
            map.printMap();

            map.searchForMatch();

            map.printMap();


            printMatrix();


            //new block ->

            //System.out.println("Dentro else");
            //findAndAddAvailablePositionsAndBlock();
            handler.addProgram(variableProgram);


            AnswerSets answerSets = (AnswerSets) handler.startSync();
            AnswerSet optimal = new AnswerSet(null);

            try {

                optimal = answerSets.getOptimalAnswerSets().get(0);

                System.out.println("Found Optimal : " + optimal.toString());

                try {
                    for (Object obj : optimal.getAtoms()) {

                        if (obj instanceof newBlock) {
                            newBlock b = (newBlock) obj;
                            cells[b.getRow()][b.getColumn()].setText(String.valueOf(b.getValue()));

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                System.out.println("No moves left. AI loose!");
                System.exit(0);
            }

            variableProgram.clearAll();

            //Calcolo delle mosse disponibili
            //Scelta mossa in base al punteggio con funzione che conserva le istanze della matrice ad ogni accopp.
        }
    }

    private void initScene(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        for (int row = 0; row < ROW_SIZE; row++) {
            for (int col = 0; col < COL_SIZE; col++) {
                // Crea un rettangolo con bordo
                Rectangle border = new Rectangle(CELL_SIZE, CELL_SIZE);
                border.setFill(Color.WHITE);
                border.setStroke(Color.BLACK);

                // Crea una Label
                Label label = new Label("");
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
        Scene scene = new Scene(gridPane, COL_SIZE * CELL_SIZE, ROW_SIZE * CELL_SIZE);
        primaryStage.setTitle("DROP THE NUMBERS");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    @Override
    public void start(Stage primaryStage) {

        // Inizializza la mappa dei colori
        initializeColorMap();

        //Crea handler
        createHandler();

        //inizializza la scena
        initScene(primaryStage);

        //Registra classi
        registerClass();

        InputProgram encoding = new ASPInputProgram();
        encoding.addFilesPath("encodings/drop");
        handler.addProgram(encoding);


        //ESEMPIO
        String s = "1";
        cells[ROW_SIZE - 1][0].setText("2");
        cells[ROW_SIZE - 1][1].setText("4");
        cells[ROW_SIZE - 1][2].setText("4");
        cells[ROW_SIZE - 2][2].setText("2");
        cells[ROW_SIZE - 1][3].setText("4");
        cells[ROW_SIZE - 1][4].setText("4");
        cells[ROW_SIZE - 2][4].setText("2");
        //System.out.println(Integer.parseInt(cells[ROW_SIZE - 1][0].getText()));


        // Crea una Timeline per aggiornare le celle con numeri casuali
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), this));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

    private void registerClass() {
        try {
            ASPMapper.getInstance().registerClass(Block.class);
            ASPMapper.getInstance().registerClass(newBlock.class);
        } catch (ObjectNotValidException | IllegalAnnotationException e1) {
            e1.printStackTrace();
        }
    }

    //i have to pass an identical matrix and change the if condition

    private void searchForMatch(Pair<Pair<Integer, Integer>, Integer> p) {
        boolean match_found = true;
        while (match_found) {
            int x, y = 0;
            if ((x = p.getKey().getKey() + 1) < ROW_SIZE &&
                    !(cellIsNull(x, p.getKey().getValue())) &&
                    p.getValue() == Integer.parseInt(cells[x][p.getKey().getValue()].getText())) {

                y = p.getKey().getValue();
                System.out.println("Posso accoppiare giù con valore : " + p.getValue());
                updateMatrix("lower", x, y, p.getValue() * 2);
                continue;
            }

            if ((y = p.getKey().getValue() - 1) >= 0 &&
                    !(cellIsNull(p.getKey().getKey(), y)) &&
                    p.getValue() == Integer.parseInt(cells[p.getKey().getKey()][y].getText())) {
                x = p.getKey().getKey();
                System.out.println("Posso accoppiare sx");
                continue;
            }
            if ((y = p.getKey().getValue() + 1) < COL_SIZE &&
                    !(cellIsNull(p.getKey().getKey(), y)) &&
                    p.getValue() == Integer.parseInt(cells[p.getKey().getKey()][y].getText())) {
                x = p.getKey().getKey();
                System.out.println("Posso accoppiare dx");
                continue;
            }
            match_found = false;

        }
    }

    private void updateMatrix(String typeOfMatch, Integer x, Integer y, Integer newValueBlock) {
        switch (typeOfMatch) {
            case "lower":
                System.out.println("Inserisco in posizione x : " + x + " y : " + y);
                //cells[x][y].setText(String.valueOf(newValueBlock));
                break;
            case "sx":
                break;
            case "dx":
                break;
        }
    }


    private boolean cellIsNull(Integer i, Integer j) {
        return cells[i][j].getText().isEmpty();
    }


    int[][] getMatrix() {

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


    void printMatrix() {
        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < COL_SIZE; j++) {
                if (cells[i][j].getText().isEmpty()) {
                    System.out.print(" ");
                } else {
                    System.out.print(cells[i][j].getText());
                }

            }
            System.out.println();
        }
    }


    /*private void findAndAddAvailablePositionsAndBlock() {
        int valueBlock = getRandom(listRandValueBlock);
        System.out.println("valueBlock : " + valueBlock);
        for(int j = 0; j < COL_SIZE; j++){
            for(int i = ROW_SIZE - 1; i > 0; i--)
            {
                if(cells[i][j].getText().isEmpty())
                {
                    try{
                        variableProgram.addObjectInput(new createBlock(i,j,valueBlock));
                    } catch (Exception e){
                        System.err.println("ADD_CREATEBLOCK_TO_PROGRAM_ERROR");
                        e.printStackTrace();
                    }
                    break;
                }else{
                    try{
                        variableProgram.addObjectInput(new Block(i,j,Integer.parseInt(cells[ROW_SIZE - 1][0].getText())));
                    } catch (Exception e){
                        System.err.println("ADD_EXISTING_BLOCK_TO_PROGRAM_ERROR");
                        e.printStackTrace();
                    }
                }
            }

        }

    }*/


    private void resetGame() {
        System.out.println("Game stop");
    }


    // Metodo per inizializzare la mappa di numeri e colori
    private void initializeColorMap() {
        numberColorMap.put(2, RED);
        numberColorMap.put(4, Color.VIOLET);
        numberColorMap.put(8, Color.BLUE);
        numberColorMap.put(16, Color.GREEN);
        numberColorMap.put(32, Color.ORANGE);
        numberColorMap.put(64, Color.YELLOW);
        numberColorMap.put(128, Color.PINK);
        numberColorMap.put(256, Color.CYAN);
        numberColorMap.put(512, Color.LIGHTGRAY);
    }
}