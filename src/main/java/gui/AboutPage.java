package gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

// This class represents the About page in the application
// It displays general information about the app and its features
public class AboutPage extends VBox {
        public AboutPage() {
                setSpacing(12);
                setPadding(new Insets(20));

                // Title
                Label title = new Label("SpendSense Budget Planner");
                title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

                Label subtitle = new Label("Personal Finance & Budget Tracking System");
                subtitle.setStyle("-fx-font-size:  14px; -fx-text-fill: #666666;");

                // App Description
                Label descriptionHeader = new Label("Overview");
                descriptionHeader.setStyle("-fx-font-size: 16px;  -fx-font-weight: bold;");

                Label description = new Label(
                                "SpendSense is a JavaFX based application designed to help users track income and expenses, manage spending categories, "
                                                +
                                                "and monitor budgets");
                description.setWrapText(true);

                // Features
                Label featuresHeader = new Label("Key Features");
                featuresHeader.setStyle("-fx-font-size: 16px;  -fx-font-weight: bold;");

                Label features = new Label(
                                "1. Transaction Tracking by Category\n" +
                                                "2. Monthly Budget Management\n" +
                                                "3. Spending Analytics Dashboard\n" +
                                                "4. Global Transaction Search\n" +
                                                "5. Budget Limit Notifications\n" +
                                                "6. Multi-page navigation UI");
                features.setWrapText(true);

                // Add all UI components to the page layout
                getChildren().addAll(
                                title,
                                subtitle,

                                new Separator(),

                                descriptionHeader,
                                description,

                                new Separator(),

                                featuresHeader,
                                features);
        }
}
