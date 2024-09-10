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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class RandomMatrixApp extends Application implements EventHandler<ActionEvent> {

    // Costanti per le dimensioni della matrice
    static final int ROW_SIZE = 6;
    static final int COL_SIZE = 5;
    //STOP
    private static boolean running = true;
    //HANDLE
    private static Handler handler;
    // Mappa dei numeri multipli di 2 con i colori associati
    private final Map<Integer, Color> numberColorMap = new HashMap<>();
    //FACT AND RULE
    //InputProgram fixedProgram = new ASPInputProgram();
    InputProgram variableProgram = new ASPInputProgram();

    randBlockValue randValue = new randBlockValue();

    Graphic matrixFx;

    public static void main(String[] args) {
        launch(args);
    }


    /*public void showAllTheMove(ArrayList<int[][]> moves) {
        for (int[][] move : moves) {
            System.out.println("Array:");
            for (int i = 0; i < move.length; i++) {
                for (int j = 0; j < move[i].length; j++) {
                    if(move[i][j] != 0){
                        cells[i][j].setText(String.valueOf(move[i][j]));
                    } else {
                    cells[i][j].setText("");
                    }
                }
            }
        }
    }*/

    private static void createHandler() {
        handler = new DesktopHandler(new DLV2DesktopService("lib/dlv-2.1.1-macos"));
    }

    @Override
    public void handle(ActionEvent event) {
        if (!running) {
            resetGame();
        } else {

            MapPossibleBlockArrayMatrix map = new MapPossibleBlockArrayMatrix(matrixFx.getMatrix());
            map.printMap();

            randValue.print();

            //printMatrix();


            addPossibleBlockToProgram(variableProgram, map);
            addExistingBlock(variableProgram);
            handler.addProgram(variableProgram);


            AnswerSets answerSets = (AnswerSets) handler.startSync();
            AnswerSet optimal = new AnswerSet(null);

            try {

                optimal = answerSets.getOptimalAnswerSets().get(0);

                System.out.println("Found Optimal : " + optimal.toString());

                try {
                    // Hai scelto il blocco ora stampa tutto l'array della nuova mossa

                    //for(scorre l'array di matrix )
                    //for(scorre la matrix
                    //setta la scene con i numeri

                    for (Object obj : optimal.getAtoms()) {

                        if (obj instanceof newBlock) {
                            newBlock b = (newBlock) obj;
                            //System.out.println("newBlock" + "[" + b.getRow() + "]"+ "[" + b.getColumn() + "]" + "[" + b.getValue() + "]" );

                            int[] chosenBlock = new int[]{b.getRow(), b.getColumn(), b.getValue()};
                            //map.printMapBlock(chosenBlock);

                            randValue.addValueToArray(map.returnFinalMatrix(chosenBlock));

                            showAllTheMove(map.getArrayOfMatrixForABlock(chosenBlock));

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                System.out.println("No moves left. AI loose!");
                //System.exit(0);
            }

            variableProgram.clearAll();
        }
    }

    public void showAllTheMove(ArrayList<int[][]> moves) {
        Timeline timeline = new Timeline();
        int delay = 0;

        for (int[][] move : moves) {
            KeyFrame keyFrame = new KeyFrame(Duration.millis(delay), e -> {
                updateLabelsWithMove(move);
            });
            timeline.getKeyFrames().add(keyFrame);
            delay += 300; // Set delay to 500 milliseconds between each matrix
        }
        timeline.play();
    }

    private void updateLabelsWithMove(int[][] move) {
        for (int i = 0; i < move.length; i++) {
            for (int j = 0; j < move[i].length; j++) {
                if (move[i][j] != 0) {
                    matrixFx.setCell(i, j, move[i][j]);
                } else {
                    matrixFx.setCell(i, j, 0); // Clear the cell if the value is 0
                }
            }
        }
    }

    //Passare uno stage anche chiamando la classe
    @Override
    public void start(Stage primaryStage) {

        //Crea handler
        createHandler();

        //inizializza la scena
        //Sostituire con chiamata al costruttore
        matrixFx = new Graphic(primaryStage);

        //Registra classi
        registerClass();

        InputProgram encoding = new ASPInputProgram();
        encoding.addFilesPath("encodings/drop");
        handler.addProgram(encoding);


        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), this));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        System.out.println("timeLine end, allMoves.size() : ");

    }

    private void registerClass() {
        try {
            ASPMapper.getInstance().registerClass(Block.class);
            ASPMapper.getInstance().registerClass(newBlock.class);
        } catch (ObjectNotValidException | IllegalAnnotationException e1) {
            e1.printStackTrace();
        }
    }


    private void resetGame() {
        //POP UP GAME OVER E stoppare la schermata sull'ultima mossa
        System.out.println("Game stop");
    }


    private String getColor(int key) {
        for (int k : numberColorMap.keySet()) {
            if (k == key) {
                System.out.println("Color block : " + numberColorMap.get(k).toString());
                return numberColorMap.get(k).toString();
            }
        }
        return null;
    }


    public void addPossibleBlockToProgram(InputProgram facts, MapPossibleBlockArrayMatrix map) {
        Set<int[]> possBlocks = map.getPossibleBlock();
        for (var pB : possBlocks) {
            try {
                facts.addObjectInput(new Block(pB[0], pB[1], pB[2], map.getPossibleBlockAndScore(pB), map.findIfMatch(pB)));
                System.out.println("newBlock" + "[" + pB[0] + "]" + "[" + pB[1] + "]" + "[" + map.getPossibleBlockAndScore(pB) + "]");

            } catch (Exception e) {
                System.err.println("ERROR ADDING FACTS TO PROGRAM");
                e.printStackTrace();
            }
        }
    }

    public void addExistingBlock(InputProgram facts) {
        int[][] currentMatrix = matrixFx.getMatrix();
        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < COL_SIZE; j++) {
                if (currentMatrix[i][j] != 0) {
                    try {
                        facts.addObjectInput(new ExistingBlock(i, j, currentMatrix[i][j]));
                    } catch (Exception e) {
                        System.err.println("ERROR ADDING EXISTING BLOCK TO PROGRAM");
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}