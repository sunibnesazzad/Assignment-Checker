/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignmentchecker;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sazzad
 */
public class FrontPageController implements Initializable {

    Stage window;

    @FXML
    private Button directoryButton;
    public static Set<String> files = new HashSet<>();
    @FXML
    private BorderPane mainLayout;
    GridPane gridPane = new GridPane();
    public Set<String> nameList = new HashSet<String>();

    @FXML
    private Button wholeButton;

    Service service = new Service();

    @FXML
    private MenuBar menuBar;
    
    @FXML
    private VBox fileList;

    @FXML
    private void home(ActionEvent event) throws IOException {

        Stage stage;
        Parent root;

        stage = (Stage) menuBar.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("fontPage.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void selectDirectory(ActionEvent event) {
        DirectoryChooser directory = new DirectoryChooser();
        directory.setTitle("SELECT FOLDER");
//      directory.setInitialDirectory(new File("C:\\Users\\tesla\\Desktop"));
        File selectedDirectory = directory.showDialog(window);
//                System.out.println(selectedDirectory);

        if (selectedDirectory == null) {
            return;
        }
        File[] fileArray = selectedDirectory.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".c");
            }
        });
            
        
        
        for (File file : fileArray) {
            
            
            if (file.isFile()) {
                files.add(file.getAbsolutePath());
               // System.out.println(file.getAbsolutePath());
                C_File cFile = new C_File(file.toString());
//                System.out.println(cFile.getName());
                nameList.add(cFile.getName());
            }
        }

        wholeButton.setVisible(true);
        
        
        
       
        fileList.setSpacing(5);
        fileList.getChildren().add(new Label("Total Files To Compare"));
        for(String s: nameList){
            System.out.println(s);
            Label label = new Label(s);
            fileList.getChildren().add(label);
        }
        

    }

    @FXML
    private void wholeComparison(ActionEvent event) {
        
        mainLayout.getChildren().remove(fileList);

        ScrollPane sp = new ScrollPane();
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        

        sp.setPadding(new Insets(10, 10, 10, 10));
        GridPane gridPane = new GridPane();
        
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        sp.setContent(gridPane);

        int row = 0;
        int col = 1;

        for (String s2 : nameList) {

            Label horizontalLabel = new Label(s2);
            horizontalLabel.setPadding(new Insets(5, 5, 5, 5));
            gridPane.add(horizontalLabel, col++, row);

        }

        List<Each> comp = service.getComparison();
        row = 1;

        Map<String, String> map = new HashMap<>();

        for (String s1 : nameList) {

            Label verticalLabel = new Label(s1);
            verticalLabel.setPadding(new Insets(5, 5, 5, 5));
            gridPane.add(verticalLabel, 0, row);

            col = 1;
            for (String s2 : nameList) {

                //   gridPane.add(new Label(s1), col++, row);
                boolean flag = false;
                for (Each c : comp) {

                    if (c.getFileName2().equalsIgnoreCase(s2) && c.getFileName1().equalsIgnoreCase(s1)) {

                        String s = "";
                        s += c.getPath1() + "####" + c.getPath2();

                        double val = c.getPercentage();
//                        System.out.println(s+"----->"+val);
                        Button button = new Button(String.format("%.2f", val));
                        button.setId(s);
                        // button.setPadding(new Insets(5, 5, 5, 5));
                        button.setPrefWidth(100);

                        if (val <= 40) {
                            button.setStyle("-fx-background-color:green;"
                                    + "-fx-text-fill:#FFFFFF;");

                        } else if (val > 40 && val <= 60) {
                            button.setStyle("-fx-background-color:yellow;");
                        } 
                        else if (val > 60& val <= 80) {
                            button.setStyle("-fx-background-color:orange;");
                        }
                        
                        else if (val >= 80) {
                            button.setStyle("-fx-background-color:red;");
                        }

                        button.setOnAction(e -> {

                            String paths[] = button.getId().split("####");
//                            System.err.println(paths[0]);
//                            System.err.println(paths[1]);
                           // CodeMapping codeMapping = new CodeMapping();
                           // showCodeMapping(codeMapping.getOneByOneResult(paths[0].trim(), paths[0].trim()));

                        });

                        gridPane.add(button, col++, row);
                        flag = true;
                    }
                }
                if (!flag) {
                    Label emptyLabel = new Label(" ");
                    emptyLabel.setPadding(new Insets(5, 5, 5, 5));
                    gridPane.add(emptyLabel, col++, row);
                }

            }

            row++;

        }

        System.err.println();

        mainLayout.setCenter(sp);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        wholeButton.setVisible(false);

    }

    private void showCodeMapping(HashMap<String, Double> oneByOneResult) {

        System.err.println("ok");

        ScrollPane sp = new ScrollPane();
        sp.setPadding(new Insets(10, 10, 10, 10));

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        vBox.setSpacing(10);

        for (Map.Entry<String, Double> entry : oneByOneResult.entrySet()) {

            HBox hBox = new HBox();
            hBox.setSpacing(10);

            Label key = new Label(entry.getKey());
            Label value = new Label(String.format("%.2f", (entry.getValue() * 100)));
            value.setTextFill(Color.RED);

            hBox.getChildren().addAll(key, value);
            vBox.getChildren().add(hBox);

        }

        sp.setContent(vBox);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(sp, 600, 400);
        stage.setScene(scene);
        stage.showAndWait();

    } 
    
}
