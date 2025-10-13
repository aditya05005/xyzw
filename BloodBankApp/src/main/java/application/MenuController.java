package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;


public class MenuController {

    private void openScreen(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(loader.load()));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openDonorScreen(ActionEvent event) {
        openScreen("donor.fxml", "Donor Management");
    }

    @FXML
    private void openRequestScreen(ActionEvent event) {
        openScreen("request.fxml", "Requests Management");
    }

    @FXML
    private void openReportScreen(ActionEvent event) {
        openScreen("report.fxml", "Reports");
    }

	@FXML
private void openHospitalScreen() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hospital.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Hospital Management");
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    @FXML
    private void openInventory() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("inventory.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Blood Inventory Management");
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}


}
