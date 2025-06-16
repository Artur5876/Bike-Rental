//import com.example.Jobs;

import com.example.Jobs;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class EmployeesTable {
    public void employeesTableBuilt(){
        // Table columns generation
        TableColumn<Jobs, Integer> jobIdColumn = new TableColumn<>("Job ID");
        jobIdColumn.setCellValueFactory(new PropertyValueFactory<>("jobId"));

        TableColumn<Jobs, Integer> employerIdColumn = new TableColumn<>("Employer ID");
        employerIdColumn.setCellValueFactory(new PropertyValueFactory<>("employerId"));

        TableColumn<Jobs, String> jobTitleColumn = new TableColumn<>("Job Title");
        jobTitleColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));

        TableColumn<Jobs, String> jobDescriptionColumn = new TableColumn<>("Job Description");
        jobDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("jobDescription"));

        TableColumn<Jobs, String> departmentColumn = new TableColumn<>("Department");
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

        TableColumn<Jobs, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Jobs, String> closingDateColumn = new TableColumn<>("Closing Date");
        closingDateColumn.setCellValueFactory(new PropertyValueFactory<>("closingDate"));

        TableColumn<Jobs, String> postedDateColumn = new TableColumn<>("Posted Date");
        postedDateColumn.setCellValueFactory(new PropertyValueFactory<>("postedDate"));
        //status

        TableColumn<Jobs, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
}