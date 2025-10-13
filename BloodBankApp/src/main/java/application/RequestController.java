package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.sql.*;

public class RequestController {

    @FXML private TextField txtRequestID;
    @FXML private TextField txtBloodGroup;
    @FXML private TextField txtVolume;
    @FXML private TextField txtHospitalID;

    @FXML private TableView<Request> requestTable;
    @FXML private TableColumn<Request, Integer> colRequestID;
    @FXML private TableColumn<Request, String> colBloodGroup;
    @FXML private TableColumn<Request, Integer> colVolume;
    @FXML private TableColumn<Request, Integer> colHospitalID;
    @FXML private TableColumn<Request, String> colDate;

    @FXML private TableView<BloodUnit> bloodTable;
    @FXML private TableColumn<BloodUnit, Integer> colBloodID;
    @FXML private TableColumn<BloodUnit, Integer> colDonorID;
    @FXML private TableColumn<BloodUnit, Date> colDonationDate;
    @FXML private TableColumn<BloodUnit, Integer> colBloodVolume;
    @FXML private TableColumn<BloodUnit, String> colStatus;

    private ObservableList<BloodUnit> bloodUnitList = FXCollections.observableArrayList();
    private ObservableList<Request> requestList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colRequestID.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        colBloodGroup.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        colVolume.setCellValueFactory(new PropertyValueFactory<>("volumeRequested"));
        colHospitalID.setCellValueFactory(new PropertyValueFactory<>("hospitalID"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));

        loadRequests();

        colBloodID.setCellValueFactory(new PropertyValueFactory<>("bloodID"));
        colDonorID.setCellValueFactory(new PropertyValueFactory<>("donorID"));
        colDonationDate.setCellValueFactory(new PropertyValueFactory<>("donationDate"));
        colBloodVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        bloodTable.setItems(bloodUnitList);

        requestTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadAvailableBloodUnits(newSelection.getBloodGroup());
            }
        });

        requestTable.setOnMouseClicked(e -> {
            Request selected = requestTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                txtRequestID.setText(String.valueOf(selected.getRequestID()));
                txtBloodGroup.setText(selected.getBloodGroup());
                txtVolume.setText(String.valueOf(selected.getVolumeRequested()));
                txtHospitalID.setText(String.valueOf(selected.getHospitalID()));
            }
        });
    }

    public void loadAvailableBloodUnits(String bloodGroup) {
        bloodUnitList.clear();
        String sql = "SELECT Blood_ID, Donor_ID, Donation_Date, Volume, Status FROM Blood WHERE Status = 'Available' AND Blood_Group = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bloodGroup);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BloodUnit unit = new BloodUnit(
                    rs.getInt("Blood_ID"),
                    rs.getInt("Donor_ID"),
                    rs.getDate("Donation_Date"),
                    rs.getInt("Volume"),
                    rs.getString("Status")
                );
                bloodUnitList.add(unit);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load available blood units.");
        }
    }

    @FXML
    public void handleAddRequest() {
        String sql = "INSERT INTO All_Requests (Request_Date, Blood_Group, Volume_Requested, Hospital_ID) VALUES (CURRENT_DATE, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, txtBloodGroup.getText());
            stmt.setInt(2, Integer.parseInt(txtVolume.getText()));
            stmt.setInt(3, Integer.parseInt(txtHospitalID.getText()));
            stmt.executeUpdate();
            showAlert("Success", "Request added successfully!");
            loadRequests();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    public void loadRequests() {
        requestList.clear();
        String sql = "SELECT * FROM All_Requests ORDER BY Request_ID";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                requestList.add(new Request(
                    rs.getInt("Request_ID"),
                    rs.getDate("Request_Date").toString(),
                    rs.getString("Blood_Group"),
                    rs.getInt("Volume_Requested"),
                    rs.getInt("Hospital_ID")
                ));
            }
            requestTable.setItems(requestList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load requests.");
        }
    }

    @FXML
    public void handleUpdateRequest() {
        if (txtRequestID.getText().isEmpty()) {
            showAlert("Warning", "Please select a record to update.");
            return;
        }
        String sql = "UPDATE All_Requests SET Blood_Group=?, Volume_Requested=?, Hospital_ID=? WHERE Request_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, txtBloodGroup.getText());
            stmt.setInt(2, Integer.parseInt(txtVolume.getText()));
            stmt.setInt(3, Integer.parseInt(txtHospitalID.getText()));
            stmt.setInt(4, Integer.parseInt(txtRequestID.getText()));
            stmt.executeUpdate();
            showAlert("Success", "Request updated successfully!");
            loadRequests();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    public void handleDeleteRequest() {
        if (txtRequestID.getText().isEmpty()) {
            showAlert("Warning", "Please select a record to delete.");
            return;
        }
        String sql = "DELETE FROM All_Requests WHERE Request_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(txtRequestID.getText()));
            stmt.executeUpdate();
            showAlert("Success", "Request deleted successfully!");
            loadRequests();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    public void handleFulfillRequest() {
        Request selectedRequest = requestTable.getSelectionModel().getSelectedItem();
        BloodUnit selectedBlood = bloodTable.getSelectionModel().getSelectedItem();

        if (selectedRequest == null || selectedBlood == null) {
            showAlert("Warning", "Please select both a request and a blood unit.");
            return;
        }

        int requestId = selectedRequest.getRequestID();
        int bloodId = selectedBlood.getBloodID();

        String insertSQL = "INSERT INTO Request_Fulfillment (Request_ID, Blood_ID, Fulfillment_Date) VALUES (?, ?, CURRENT_DATE)";
        String updateBloodSQL = "UPDATE Blood SET Status = 'Used' WHERE Blood_ID = ?";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psInsert = conn.prepareStatement(insertSQL);
                 PreparedStatement psUpdate = conn.prepareStatement(updateBloodSQL)) {

                psInsert.setInt(1, requestId);
                psInsert.setInt(2, bloodId);
                psInsert.executeUpdate();

                psUpdate.setInt(1, bloodId);
                psUpdate.executeUpdate();

                conn.commit();

                showAlert("Success", "Request marked as fulfilled.");
                loadRequests();
                loadAvailableBloodUnits(selectedRequest.getBloodGroup());

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            showAlert("Error", "Error fulfilling request: " + e.getMessage());
        }
    }

    @FXML
    public void clearFields() {
        txtRequestID.clear();
        txtBloodGroup.clear();
        txtVolume.clear();
        txtHospitalID.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
