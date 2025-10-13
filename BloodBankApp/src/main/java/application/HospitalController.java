package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

public class HospitalController {

    @FXML private TextField hospitalIdField;
    @FXML private TextField nameField;
    @FXML private TextField contactField;
    @FXML private TextField locationField;
    @FXML private TextField billingCodeField;

    @FXML private TableView<Hospital> hospitalTable;
    @FXML private TableColumn<Hospital, Integer> hospitalIdColumn;
    @FXML private TableColumn<Hospital, String> nameColumn;
    @FXML private TableColumn<Hospital, String> contactColumn;
    @FXML private TableColumn<Hospital, String> locationColumn;
    @FXML private TableColumn<Hospital, String> billingCodeColumn;

    private final ObservableList<Hospital> hospitalList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        hospitalIdColumn.setCellValueFactory(new PropertyValueFactory<>("hospitalId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        billingCodeColumn.setCellValueFactory(new PropertyValueFactory<>("billingCode"));

        hospitalTable.setItems(hospitalList);

        // Populate fields when selecting a row
        hospitalTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                hospitalIdField.setText(String.valueOf(newSel.getHospitalId()));
                nameField.setText(newSel.getName());
                contactField.setText(newSel.getContact());
                locationField.setText(newSel.getLocation());
                billingCodeField.setText(newSel.getBillingCode());
            }
        });

        loadHospitals();
    }

    @FXML
    private void addHospital() {
        String sql = "INSERT INTO Hospital (Hospital_ID, Name, Contact, Location, Billing_Code) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(hospitalIdField.getText()));
            stmt.setString(2, nameField.getText());
            stmt.setString(3, contactField.getText());
            stmt.setString(4, locationField.getText());
            stmt.setString(5, billingCodeField.getText().isEmpty() ? null : billingCodeField.getText());

            stmt.executeUpdate();
            showAlert("Success", "Hospital added successfully.");
            clearFields();
            loadHospitals();
        } catch (Exception e) {
            showAlert("Error", "Failed to add hospital: " + e.getMessage());
        }
    }

    @FXML
    private void updateHospital() {
        String sql = "UPDATE Hospital SET Name = ?, Contact = ?, Location = ?, Billing_Code = ? WHERE Hospital_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nameField.getText());
            stmt.setString(2, contactField.getText());
            stmt.setString(3, locationField.getText());
            stmt.setString(4, billingCodeField.getText().isEmpty() ? null : billingCodeField.getText());
            stmt.setInt(5, Integer.parseInt(hospitalIdField.getText()));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                showAlert("Success", "Hospital updated successfully.");
                clearFields();
                loadHospitals();
            } else {
                showAlert("Error", "No hospital found with that ID.");
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to update hospital: " + e.getMessage());
        }
    }

    @FXML
    private void deleteHospital() {
        String sql = "DELETE FROM Hospital WHERE Hospital_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(hospitalIdField.getText()));
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                showAlert("Success", "Hospital deleted successfully.");
                clearFields();
                loadHospitals();
            } else {
                showAlert("Error", "No hospital found with that ID.");
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to delete hospital: " + e.getMessage());
        }
    }

    private void loadHospitals() {
        hospitalList.clear();
        String sql = "SELECT * FROM Hospital ORDER BY Hospital_ID";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                hospitalList.add(new Hospital(
                        rs.getInt("Hospital_ID"),
                        rs.getString("Name"),
                        rs.getString("Contact"),
                        rs.getString("Location"),
                        rs.getString("Billing_Code")
                ));
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load hospitals: " + e.getMessage());
        }
    }

    private void clearFields() {
        hospitalIdField.clear();
        nameField.clear();
        contactField.clear();
        locationField.clear();
        billingCodeField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
