package lt.praktika.hotelapp.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class AdminUiFactory {

    /** Veiksmų korteles aprašanti kortelė su pavadinimu, paaiškinimu ir mygtuku. */
    public VBox card(String title, String description, Button action) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setStyle("-fx-text-fill: #555;");

        action.setMaxWidth(Double.MAX_VALUE);
        VBox card = new VBox(8, titleLabel, descriptionLabel, action);
        card.setPadding(new Insets(14));
        card.setMinWidth(230);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #d9dee8; -fx-border-radius: 10;");
        VBox.setVgrow(descriptionLabel, Priority.ALWAYS);
        return card;
    }

    public FlowPane actionGrid(Node... cards) {
        FlowPane pane = new FlowPane(14, 14, cards);
        pane.setPadding(new Insets(4));
        pane.setAlignment(Pos.TOP_LEFT);
        return pane;
    }

    public HBox topBar(String title, Button logout) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(12, titleLabel, spacer, logout);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(0, 0, 12, 0));
        return top;
    }

    /** Statistikos kortelė registratoriaus pradiniam ekranui (skaičius + paaiškinimas). */
    public VBox statCard(String title, String value, String accentColor) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #6b7280;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + accentColor + ";");

        VBox card = new VBox(6, titleLabel, valueLabel);
        card.setPadding(new Insets(16));
        card.setMinWidth(200);
        card.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 10;"
                        + "-fx-border-color: #d9dee8;"
                        + "-fx-border-radius: 10;"
        );
        return card;
    }

    /** Pirminis (didelis, mėlynas) veiksmų mygtukas. */
    public Button primaryButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #2563eb;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 8 18 8 18;"
                        + "-fx-background-radius: 6;"
        );
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    /** Antrinis (žalias) veiksmų mygtukas (paprastai naudojamas „Pridėti“). */
    public Button successButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #16a34a;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 8 18 8 18;"
                        + "-fx-background-radius: 6;"
        );
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    /** Įspėjamasis (oranžinis) mygtukas (paprastai naudojamas „Atnaujinti“). */
    public Button warningButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #f59e0b;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 8 18 8 18;"
                        + "-fx-background-radius: 6;"
        );
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    /** Pavojingas (raudonas) mygtukas (paprastai naudojamas „Ištrinti“). */
    public Button dangerButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #dc2626;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 8 18 8 18;"
                        + "-fx-background-radius: 6;"
        );
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    /** Neutralus (pilkas) mygtukas (paprastai naudojamas „Išvalyti“ arba „Grįžti“). */
    public Button neutralButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #6b7280;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 8 18 8 18;"
                        + "-fx-background-radius: 6;"
        );
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }
}
