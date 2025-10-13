package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.input.MouseEvent;

import java.sql.*;
import java.math.BigDecimal;
import java.util.Random;

public class InventoryController {

    @FXML private TableView<ObservableList<String>> bloodTable;
    @FXML private TextField txtBloodID, txtVolume, txtBloodGroup;
    @FXML private DatePicker dateDonation;
    @FXML private ComboBox<String> cmbDonor;

    @FXML
    private void initialize() {
        loadDonors();
        loadBloodInventory();

        // Set row click listener
        bloodTable.setOnMouseClicked(this::handleRowSelect);
    }

    public void loadDonors() {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT Donor_ID, Name FROM Donor")) {

            cmbDonor.getItems().clear();
            while (rs.next()) {
                cmbDonor.getItems().add(rs.getInt("Donor_ID") + " - " + rs.getString("Name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addBloodUnit() {
        if (!validateInput(true)) {
            return;
        }

        try {
            long bloodId;
            if (txtBloodID.getText().isEmpty()) {
                bloodId = 100000 + new Random().nextInt(900000);
            } else {
                bloodId = Long.parseLong(txtBloodID.getText());
            }

            int donorId = Integer.parseInt(cmbDonor.getValue().split(" - ")[0]);

            String sql = "INSERT INTO Blood (Blood_ID, Donation_Date, Volume, Blood_Group, Donor_ID) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, bloodId);
                stmt.setDate(2, java.sql.Date.valueOf(dateDonation.getValue()));
                stmt.setBigDecimal(3, new BigDecimal(txtVolume.getText()));
                stmt.setString(4, txtBloodGroup.getText().trim());
                stmt.setInt(5, donorId);

                stmt.executeUpdate();

                new Alert(Alert.AlertType.INFORMATION, "✅ Blood unit added successfully!").show();
                loadBloodInventory();
                clearFields();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "❌ Error adding blood unit: " + e.getMessage()).show();
        }
    }

    @FXML
    public void updateBloodUnit() {
        if (!validateInput(false)) {
            return;
        }

        try {
            long bloodId = Long.parseLong(txtBloodID.getText());
            int donorId = Integer.parseInt(cmbDonor.getValue().split(" - ")[0]);

            String sql = "UPDATE Blood SET Donation_Date = ?, Volume = ?, Blood_Group = ?, Donor_ID = ? WHERE Blood_ID = ?";

            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setDate(1, java.sql.Date.valueOf(dateDonation.getValue()));
                stmt.setBigDecimal(2, new BigDecimal(txtVolume.getText()));
                stmt.setString(3, txtBloodGroup.getText().trim());
                stmt.setInt(4, donorId);
                stmt.setLong(5, bloodId);

                int affectedRows = stmt.executeUpdate();
                if(affectedRows == 0) {
                    new Alert(Alert.AlertType.WARNING, "⚠ No record found with Blood_ID = " + bloodId).show();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "✅ Blood unit updated successfully!").show();
                    loadBloodInventory();
                    clearFields();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "❌ Error updating blood unit: " + e.getMessage()).show();
        }
    }

    @FXML
    public void deleteBloodUnit() {
        if (txtBloodID.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "⚠ Please select a blood unit to delete").show();
            return;
        }

        try {
            long bloodId = Long.parseLong(txtBloodID.getText());

            String sql = "DELETE FROM Blood WHERE Blood_ID = ?";

            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, bloodId);

                int affectedRows = stmt.executeUpdate();
                if(affectedRows == 0) {
                    new Alert(Alert.AlertType.WARNING, "⚠ No record found with Blood_ID = " + bloodId).show();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "✅ Blood unit deleted successfully!").show();
                    loadBloodInventory();
                    clearFields();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "❌ Error deleting blood unit: " + e.getMessage()).show();
        }
    }

    @FXML
    public void loadBloodInventory() {
        String sql = """
            SELECT b.Blood_ID, b.Donation_Date, b.Volume,
                   b.Blood_Group, d.Name AS Donor_Name
            FROM Blood b
            JOIN Donor d ON b.Donor_ID = d.Donor_ID
            ORDER BY b.Donation_Date DESC
        """;

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            bloodTable.getColumns().clear();

            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                final int colIndex = i - 1;
                TableColumn<ObservableList<String>, String> col =
                        new TableColumn<>(rs.getMetaData().getColumnName(i));
                col.setCellValueFactory(data ->
                        new SimpleStringProperty(data.getValue().get(colIndex)));
                bloodTable.getColumns().add(col);
            }

            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            bloodTable.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "❌ Error loading inventory: " + e.getMessage()).show();
        }
    }

    public void clearFields() {
        txtBloodID.clear();
        txtVolume.clear();
        txtBloodGroup.clear();
        cmbDonor.getSelectionModel().clearSelection();
        dateDonation.setValue(null);
    }

    public boolean validateInput(boolean isAdd) {
        if ((isAdd && !txtBloodID.getText().isEmpty()) || cmbDonor.getValue() == null
                || dateDonation.getValue() == null || txtVolume.getText().isEmpty() || txtBloodGroup.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "⚠ Please fill all fields properly!").show();
            return false;
        }
        try {
            new BigDecimal(txtVolume.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "⚠ Volume must be a valid number!").show();
            return false;
        }
        return true;
    }

    public void handleRowSelect(MouseEvent event) {
        ObservableList<String> selected = bloodTable.getSelectionModel().getSelectedItem();
        if (selected != null && selected.size() >= 5) {
            txtBloodID.setText(selected.get(0));
            dateDonation.setValue(java.time.LocalDate.parse(selected.get(1)));
            txtVolume.setText(selected.get(2));
            txtBloodGroup.setText(selected.get(3));

            // Find donor item in combo box matching donor name
            String donorName = selected.get(4);
            for (String item : cmbDonor.getItems()) {
                if (item.endsWith(" - " + donorName) || item.contains(" - " + donorName)) {
                    cmbDonor.setValue(item);
                    break;
                }
            }
        }
    }
}
