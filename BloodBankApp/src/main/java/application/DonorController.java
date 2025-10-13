package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.input.MouseEvent;
import java.sql.*;
import java.time.LocalDate;

public class DonorController {
    @FXML private TextField txtName;
    @FXML private ComboBox<String> cmbGender;
    @FXML private DatePicker dateDOB;
    @FXML private TextField txtPhone;
    @FXML private TextField txtApartment;
    @FXML private TextField txtStreet;
    @FXML private TextField txtCity;
    @FXML private ComboBox<String> cmbBloodGroup;
    @FXML private TableView<ObservableList<String>> donorTable;
    @FXML private TextField txtSearchId;
    @FXML private TextField txtSearchName;

    private Integer currentDonorId = null;

    @FXML
    private void initialize() {
        cmbGender.getItems().addAll("Male", "Female", "Other");
        cmbBloodGroup.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        donorTable.setOnMouseClicked(this::onTableClick);
        refreshDonors();
    }

    @FXML
    private void handleAddDonor() {
        String sql = "INSERT INTO Donor (Name, Gender, DOB, Phone_Number, Apartment_Number, Street, City, Blood_Group) " +
                   "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, txtName.getText());
            stmt.setString(2, cmbGender.getValue());
            stmt.setDate(3, java.sql.Date.valueOf(dateDOB.getValue()));
            stmt.setString(4, txtPhone.getText());
            stmt.setString(5, txtApartment.getText());
            stmt.setString(6, txtStreet.getText());
            stmt.setString(7, txtCity.getText());
            stmt.setString(8, cmbBloodGroup.getValue());
            stmt.executeUpdate();
            alertInfo("Donor added successfully!");
            clearForm();
            refreshDonors();
        } catch (Exception e) {
            alertError("Error adding donor: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateDonor() {
        if (currentDonorId == null) {
            alertWarn("Select a donor row to update.");
            return;
        }
        String sql = "UPDATE Donor SET Name=?, Gender=?, DOB=?, Phone_Number=?, Apartment_Number=?, Street=?, City=?, Blood_Group=? WHERE Donor_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, txtName.getText());
            stmt.setString(2, cmbGender.getValue());
            stmt.setDate(3, java.sql.Date.valueOf(dateDOB.getValue()));
            stmt.setString(4, txtPhone.getText());
            stmt.setString(5, txtApartment.getText());
            stmt.setString(6, txtStreet.getText());
            stmt.setString(7, txtCity.getText());
            stmt.setString(8, cmbBloodGroup.getValue());
            stmt.setInt(9, currentDonorId);
            int n = stmt.executeUpdate();
            if (n > 0) {
                alertInfo("Donor updated successfully!");
                clearForm();
                refreshDonors();
            } else {
                alertError("No donor was updated.");
            }
        } catch (Exception e) {
            alertError("Error updating donor: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteDonor() {
        ObservableList<String> row = donorTable.getSelectionModel().getSelectedItem();
        if (row == null) { alertWarn("Select a donor to delete."); return; }
        int donorId = Integer.parseInt(row.get(0));
        String sql = "DELETE FROM Donor WHERE Donor_ID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, donorId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                alertInfo("Donor deleted successfully!");
                clearForm();
                refreshDonors();
            } else {
                alertError("Failed to delete donor.");
            }
        } catch (Exception e) {
            alertError("Error deleting donor: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearchDonor() {
        String sql = "SELECT Donor_ID, Name, Gender, DOB, Phone_Number, City, Blood_Group FROM Donor WHERE 1=1";
        if (!txtSearchId.getText().isEmpty())
            sql += " AND Donor_ID = " + txtSearchId.getText();
        if (!txtSearchName.getText().isEmpty())
            sql += " AND Name LIKE '%" + txtSearchName.getText().replace("'", "''") + "%'";
        loadDonors(sql);
    }

    @FXML
    private void refreshDonors() {
        loadDonors("SELECT Donor_ID, Name, Gender, DOB, Phone_Number, City, Blood_Group FROM Donor");
        currentDonorId = null;
    }

    private void loadDonors(String sql) {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            donorTable.getColumns().clear();
            donorTable.getItems().clear();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                final int colIdx = i - 1;
                TableColumn<ObservableList<String>, String> col =
                    new TableColumn<>(rs.getMetaData().getColumnName(i));
                col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(colIdx)));
                donorTable.getColumns().add(col);
            }
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
                    row.add(rs.getString(i));
                data.add(row);
            }
            donorTable.setItems(data);
        } catch (Exception e) {
            alertError("Error loading donors: " + e.getMessage());
        }
    }

    private void onTableClick(MouseEvent evt) {
        ObservableList<String> row = donorTable.getSelectionModel().getSelectedItem();
        if (row != null) {
            currentDonorId = Integer.parseInt(row.get(0));
            txtName.setText(row.get(1));
            cmbGender.setValue(row.get(2));
            dateDOB.setValue(LocalDate.parse(row.get(3)));
            txtPhone.setText(row.get(4));
            txtCity.setText(row.get(5));
            cmbBloodGroup.setValue(row.get(6));
            // Apartment/Street optional: load with another query if needed
        }
    }

    private void clearForm() {
        txtName.clear(); cmbGender.setValue(null); dateDOB.setValue(null); txtPhone.clear();
        txtApartment.clear(); txtStreet.clear(); txtCity.clear(); cmbBloodGroup.setValue(null);
        currentDonorId = null;
    }

    private void alertInfo(String msg) { new Alert(Alert.AlertType.INFORMATION, msg).show(); }
    private void alertError(String msg) { new Alert(Alert.AlertType.ERROR, msg).show(); }
    private void alertWarn(String msg) { new Alert(Alert.AlertType.WARNING, msg).show(); }
}
