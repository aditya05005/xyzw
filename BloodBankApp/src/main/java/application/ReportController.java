package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;

import java.sql.*;

public class ReportController {

    @FXML private TableView<ObservableList<String>> reportTable;

    public void loadQuery(String sql) {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            reportTable.getColumns().clear();

            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                final int colIndex = i;
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(rs.getMetaData().getColumnName(colIndex));
                col.setCellValueFactory(data ->
                        new javafx.beans.property.SimpleStringProperty(data.getValue().get(colIndex - 1)));
                reportTable.getColumns().add(col);
            }

            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            reportTable.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading report: " + e.getMessage()).show();
        }
    }

@FXML
public void showTopDonors() {
    String sql = "SELECT d.Donor_ID, d.Name, d.Blood_Group, " +
            "COUNT(b.Blood_ID) AS Total_Donations, SUM(b.Volume) AS Total_Volume_ml " +
            "FROM Donor d INNER JOIN Blood b ON d.Donor_ID = b.Donor_ID " +  // INNER JOIN keeps only donors who donated
            "GROUP BY d.Donor_ID, d.Name, d.Blood_Group " +
            "HAVING SUM(b.Volume) > 0 " +
            "ORDER BY Total_Volume_ml DESC LIMIT 5";
    loadQuery(sql);
}


    @FXML
    public void showPendingRequests() {
        String sql = "SELECT r.Request_ID, r.Request_Date, r.Blood_Group, r.Volume_Requested, h.Name AS Hospital_Name " +
                "FROM All_Requests r JOIN Hospital h ON r.Hospital_ID = h.Hospital_ID " +
                "WHERE r.Request_ID NOT IN (SELECT Request_ID FROM Request_Fulfillment)";
        loadQuery(sql);
    }

    @FXML
    public void showTopHospitals() {
        String sql = "SELECT h.Hospital_ID, h.Name AS Hospital_Name, " +
                "COUNT(r.Request_ID) AS Total_Requests, SUM(r.Volume_Requested) AS Total_Units_Requested " +
                "FROM Hospital h JOIN All_Requests r ON h.Hospital_ID = r.Hospital_ID " +
                "GROUP BY h.Hospital_ID, h.Name " +
                "ORDER BY Total_Units_Requested DESC";
        loadQuery(sql);
    }

    @FXML
    public void showFulfilledRequests() {
        String sql = "SELECT rf.Request_ID, rf.Fulfillment_Date, " +
                "d.Name AS Donor_Name, d.Blood_Group AS Donor_Blood_Group, b.Volume AS Volume_ml, " +
                "h.Name AS Hospital_Name " +
                "FROM Request_Fulfillment rf " +
                "JOIN Blood b ON rf.Blood_ID = b.Blood_ID " +
                "JOIN Donor d ON b.Donor_ID = d.Donor_ID " +
                "JOIN All_Requests r ON rf.Request_ID = r.Request_ID " +
                "JOIN Hospital h ON r.Hospital_ID = h.Hospital_ID " +
                "ORDER BY rf.Fulfillment_Date DESC";
        loadQuery(sql);
    }
}
