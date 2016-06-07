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

    private ArrayList<Integer> behoudenWorpen = new ArrayList<>();
    private ArrayList<ImageView> dobbelFields = new ArrayList<>();
    private ArrayList<TextArea> categorieFields = new ArrayList<>();
    private ArrayList<Button> categorieButtons = new ArrayList<>();
    private List<Integer> worpen;
    private Image[] dobbelStenenPictures = new Image[Yahtzee.aantalOgen + 1];

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        setSize();
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
            Main.y.maxAantalWorpen = 999;
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
            setCategorieTextArea(Main.y.berekenWorp(worpen));
            setHighlightButton();
            setGameInfo();
        });
        nieuwspelButton.setOnAction((event) -> {
            resetFields();
            Main.y.resetGame();
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
        List<Integer> selectedCategories = Main.y.getSelectedCategories();
        List<Integer> worpenMinusSelected = new ArrayList<>(Main.y.getScores());

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
        int huidAantalWorpen = Main.y.getHuidigAantalWorpen();
        worpen = Main.y.gooiDobbelstenen(behoudenWorpen);

        if (huidAantalWorpen < Main.y.maxAantalWorpen) {
            for (int i = 0; i < worpen.size(); i++) {
                dobbelFields.get(i).setImage(dobbelStenenPictures[worpen.get(i)]);
            }
        }
        if (Main.y.getHuidigAantalWorpen() == Main.y.maxAantalWorpen) {
            gooiButton.setDisable(true);
        }
    }

    private void gameCategoryListeners() {
        for (Button categorie : categorieButtons) {
            categorie.setOnAction((event) -> {
                if (Main.y.getHuidigAantalWorpen() < 1) {
                    return;
                }
                categorie.setDisable(true);
                Main.y.resetHuidigAantalWorpen();
                Main.y.addHuidigRonde();
                Main.y.selectCategory(categorieButtons.indexOf(categorie));
                setCategorieTextArea(Main.y.berekenWorp(worpen));
                setGameInfo();
                resetDobbels();
                resetHighlights();
                gooiButton.setDisable(false);
                if (Main.y.getHuidigRonde() == 13) {
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
                        if (Main.y.getHuidigAantalWorpen() < 1 || Main.y.getHuidigAantalWorpen() == Main.y.maxAantalWorpen) {
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
        if (Main.y.getHuidigRonde() > 0) {
            totaalField.setText(String.valueOf(Main.y.getTotaal()));
        }
        worpText.setText("Worp: " + String.valueOf(Main.y.getHuidigAantalWorpen()) + "/" + String.valueOf(Main.y.maxAantalWorpen));
        if (Main.y.getHuidigRonde() == Yahtzee.maxAantalRonden) {
            rondeTextString = "Afgelopen";

        } else {
            rondeTextString = "Ronde: " + String.valueOf(Main.y.getHuidigRonde() + 1) + "/" + String.valueOf(Yahtzee.maxAantalRonden);
        }
        rondeText.setText(rondeTextString);
    }


    private void setSize() {
        Main.prefWidth = mainPane.getPrefWidth();
        Main.prefHeight = mainPane.getPrefHeight();

    }

    private void setAboutInfo() {
        aboutContent.setVisible(false);
        aboutTitle.setText(ApplicationInfo.getGlobalVariables("Title"));
        aboutInfo.setText
                (
                        "Version: " + ApplicationInfo.getGlobalVariables("Version") + "\n\n" +
                                "Built with: " + "\n" +
                                "Java " + ApplicationInfo.getGlobalVariables("Java") + "\n" +
                                "JavaFX " + ApplicationInfo.getGlobalVariables("Gui") + "\n" +
                                "Gradle " + ApplicationInfo.getGlobalVariables("Gradle") + "\n" +
                                "Groovy " + ApplicationInfo.getGlobalVariables("Groovy") + "\n\n" +
                                "By: " + "\n" +
                                ApplicationInfo.getGlobalVariables("Creator") + "\n" +
                                ApplicationInfo.getGlobalVariables("Vendor") + "\n" +
                                ApplicationInfo.getGlobalVariables("Date")
                );
    }


}
