package com.nikecow.yahtzee.gui;

import com.nikecow.yahtzee.game.Yahtzee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class Controller
        implements Initializable {

    // The main components
    @FXML
    AnchorPane mainPane;
    @FXML
    VBox mainContent, introContent;
    // The intro buttons
    @FXML
    Button introTestButton, introNormaalButton;
    /// The about components
    @FXML
    ImageView questionMark;
    @FXML
    TextArea aboutInfo;
    @FXML
    Text aboutTitle;
    @FXML
    HBox aboutContent;
    // The game category buttons and fields
    @FXML
    Button categorieEnen, categorieTweeen, categorieDrieen, categorieVieren, categorieVijven, categorieZessen, categorieDriedezelfde, categorieCarre, categorieFullhouse, categorieKleine, categorieGrote, categorieYahtzee, categorieChance;
    @FXML
    TextArea enenField, tweeenField, drieenField, vierenField, vijvenField, zessenField, driedezelfdeField, carreField, fullhouseField, kleineField, groteField, yahtzeeField, chanceField, totaalField;
    @FXML
    ImageView dobbelField1, dobbelField2, dobbelField3, dobbelField4, dobbelField5;
    // The game control buttons
    @FXML
    Button gooiButton, nieuwspelButton;
    // The game info labels
    @FXML
    Text rondeText, worpText;

    private static final Yahtzee YAHTZEE = Main.y;
    private ArrayList<Integer> behoudenWorpen = new ArrayList<>();
    private ArrayList<ImageView> dobbelFields = new ArrayList<>();
    private ArrayList<TextArea> categorieFields = new ArrayList<>();
    private ArrayList<Button> categorieButtons = new ArrayList<>();
    private List<Integer> worpen;
    private Image[] dobbelStenenPictures = new Image[Yahtzee.aantalOgen + 1];

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        initPanes();
        initArrays();
        loadDices();
        resetDobbels();
        setAboutInfo();
        setListeners();
    }


    private void initArrays() {
        dobbelFields.addAll(Arrays.asList(dobbelField1, dobbelField2, dobbelField3, dobbelField4, dobbelField5));
        categorieFields.addAll(Arrays.asList(enenField, tweeenField, drieenField, vierenField, vijvenField, zessenField, driedezelfdeField, carreField, fullhouseField, kleineField, groteField, yahtzeeField, chanceField));
        categorieButtons.addAll(Arrays.asList(categorieEnen, categorieTweeen, categorieDrieen, categorieVieren, categorieVijven, categorieZessen, categorieDriedezelfde, categorieCarre, categorieFullhouse, categorieKleine, categorieGrote, categorieYahtzee, categorieChance));
    }

    private void loadDices() {
        for (int i = 0; i < dobbelStenenPictures.length; i++) {
            dobbelStenenPictures[i] = new Image("/gui/dice/" + i + ".png");
        }
    }

    private void initPanes() {
        mainContent.setVisible(false);
        aboutContent.setVisible(false);
        introContent.setVisible(true);
        questionMark.setVisible(false);
    }

    private void setListeners() {
        introListeners();
        aboutInfoListeners();
        gameCategoryListeners();
        gameControlListeners();
        dobbelFieldListeners();
    }

    private void introListeners() {
        introNormaalButton.setOnAction((event) -> {
            initGamePanes();
        });
        introTestButton.setOnAction((event) -> {
            YAHTZEE.maxAantalWorpen = 999;
            initGamePanes();
        });
    }

    private void initGamePanes() {
        setGameInfo();
        introContent.setVisible(false);
        mainContent.setVisible(true);
        questionMark.setVisible(true);
    }

    private void aboutInfoListeners() {
        questionMark.setOnMouseEntered((event) -> {
            if (mainContent.isVisible()) {
                mainContent.setVisible(false);
                aboutContent.setVisible(true);
            }
        });
        questionMark.setOnMouseExited((event) -> {
            if (!mainContent.isVisible()) {
                mainContent.setVisible(true);
                aboutContent.setVisible(false);
            }
        });
    }

    private void gameControlListeners() {
        gooiButton.setOnAction((event) -> {
            resetHighlights();
            werpDobbelstenen();
            setCategorieTextArea(YAHTZEE.berekenWorp(worpen));
            setHighlightButton();
            setGameInfo();
        });
        nieuwspelButton.setOnAction((event) -> {
            resetFields();
            YAHTZEE.resetGame();
            setGameInfo();
            gooiButton.setDisable(false);
        });
    }

    private void resetHighlights() {
        for (Button b : categorieButtons) {
            b.setId("");
        }
    }

    private void setHighlightButton() {
        List<Integer> selectedCategories = YAHTZEE.getSelectedCategories();
        List<Integer> worpenMinusSelected = new ArrayList<>(YAHTZEE.getScores());

        // Remove the already selected categories from the possible highest score
        for (int i = 0; i < categorieButtons.size(); i++) {
            if (selectedCategories.contains(i)) {
                worpenMinusSelected.set(i, -1);
            }
        }
        int highestAvailableScore = Collections.max(worpenMinusSelected);
        if (highestAvailableScore <= 0) {
            return;
        }
        for (int i = 0; i < categorieFields.size(); i++) {
            TextArea cat = categorieFields.get(i);
            int catText = Integer.parseInt(cat.getText());
            Button catButton = categorieButtons.get(i);
            if (catText == highestAvailableScore && !catButton.isDisabled()) {
                catButton.setId("highlightedButton");
            }
        }
    }

    private void setCategorieTextArea(ArrayList<Integer> scores) {
        for (int i = 0; i < categorieFields.size(); i++) {
            categorieFields.get(i).setText(scores.get(i).toString());
        }
    }

    private void werpDobbelstenen() {
        int huidAantalWorpen = YAHTZEE.getHuidigAantalWorpen();
        worpen = YAHTZEE.gooiDobbelstenen(behoudenWorpen);

        if (huidAantalWorpen < YAHTZEE.maxAantalWorpen) {
            for (int i = 0; i < worpen.size(); i++) {
                dobbelFields.get(i).setImage(dobbelStenenPictures[worpen.get(i)]);
            }
        }
        if (YAHTZEE.getHuidigAantalWorpen() == YAHTZEE.maxAantalWorpen) {
            gooiButton.setDisable(true);
        }
    }

    private void gameCategoryListeners() {
        for (Button categorie : categorieButtons) {
            categorie.setOnAction((event) -> {
                if (YAHTZEE.getHuidigAantalWorpen() < 1) {
                    return;
                }
                categorie.setDisable(true);
                YAHTZEE.resetHuidigAantalWorpen();
                YAHTZEE.addHuidigRonde();
                YAHTZEE.selectCategory(categorieButtons.indexOf(categorie));
                setCategorieTextArea(YAHTZEE.berekenWorp(worpen));
                setGameInfo();
                resetDobbels();
                resetHighlights();
                gooiButton.setDisable(false);
                if (YAHTZEE.getHuidigRonde() == 13) {
                    gooiButton.setDisable(true);
                }
            });
        }
    }

    private void resetFields() {
        resetDobbels();
        resetCategoryFields();
        resetCategoryButtons();
        totaalField.setText("");
    }

    private void resetCategoryFields() {
        for (TextArea field : categorieFields) {
            field.setText("");
        }
    }

    private void resetCategoryButtons() {
        for (Button button : categorieButtons) {
            button.setDisable(false);
        }
        resetHighlights();
    }

    private void resetDobbels() {
        for (int i = 0; i < Yahtzee.aantalStenen; i++) {
            dobbelFields.get(i).setImage(dobbelStenenPictures[0]);
            dobbelFields.get(i).setEffect(null);
        }
        behoudenWorpen.clear();
    }

    private void dobbelFieldListeners() {
        DropShadow drop = new DropShadow();
        for (int i = 0; i < dobbelFields.size(); i++) {
            ImageView textField = dobbelFields.get(i);
            int field = i;
            textField.setOnMouseClicked((event) -> {
                        if (YAHTZEE.getHuidigAantalWorpen() < 1 || YAHTZEE.getHuidigAantalWorpen() == YAHTZEE.maxAantalWorpen) {
                            return;
                        }
                        if (!behoudenWorpen.contains(field)) {
                            behoudenWorpen.add(field);
                            textField.setEffect(drop);
                        } else {
                            behoudenWorpen.remove(new Integer(field));
                            textField.setEffect(null);
                        }
                    }
            );
        }
    }

    private void setGameInfo() {
        String rondeTextString;
        if (YAHTZEE.getHuidigRonde() > 0) {
            totaalField.setText(String.valueOf(YAHTZEE.getTotaal()));
        }
        worpText.setText("Worp: " + String.valueOf(YAHTZEE.getHuidigAantalWorpen()) + "/" + String.valueOf(YAHTZEE.maxAantalWorpen));
        if (YAHTZEE.getHuidigRonde() == Yahtzee.maxAantalRonden) {
            rondeTextString = "Afgelopen";

        } else {
            rondeTextString = "Ronde: " + String.valueOf(YAHTZEE.getHuidigRonde() + 1) + "/" + String.valueOf(Yahtzee.maxAantalRonden);
        }
        rondeText.setText(rondeTextString);
    }

    private void setAboutInfo() {
        aboutContent.setVisible(false);
        aboutTitle.setText(ApplicationInfo.getGlobalVariables("Title"));
        aboutInfo.setText
                (
                        "Version: " + ApplicationInfo.getGlobalVariables("Implementation-Version") + "-" + ApplicationInfo.getGlobalVariables("Change") + "\n\n" +
                                "Built with: " + "\n" +
                                "Java " + ApplicationInfo.getGlobalVariables("Build-Java-Version") + "\n" +
                                "JavaFX " + ApplicationInfo.getGlobalVariables("Gui") + "\n" +
                                "Gradle " + ApplicationInfo.getGlobalVariables("Gradle-Version") + "\n" +
                                "Groovy " + ApplicationInfo.getGlobalVariables("Groovy") + "\n\n" +
                                "By: " + "\n" +
                                ApplicationInfo.getGlobalVariables("Full-Name") + "\n" +
                                ApplicationInfo.getGlobalVariables("Vendor") + "\n" +
                                ApplicationInfo.getGlobalVariables("Build-Date")
                );
    }


}
