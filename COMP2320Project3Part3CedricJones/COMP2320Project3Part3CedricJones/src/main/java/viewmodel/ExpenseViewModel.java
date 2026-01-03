package viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import model.Category;
import model.Expense;
import model.Ledger;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * ViewModel for managing expense data.
 * Handles user input and communication between the view and model.
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class ExpenseViewModel {

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField amountField;

    @FXML
    private TextField merchantField;

    @FXML
    private TextField itemField;

    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button removeButton;

    @FXML
    private ListView<Expense> expenseListView;

    @FXML
    private Label errorLabel;

    @FXML
    private MenuBar menuBar;
    private Ledger ledger;
    private ObservableList<Expense> expenses;

    @FXML
    private PieChart expensePieChart;

    /** Handles adding a new expense. */
    @FXML
    public void initialize() {
        this.ledger = new Ledger();
        this.expenses = FXCollections.observableArrayList();

        this.categoryComboBox.getItems().addAll(Category.values());

        this.expenseListView.setItems(this.expenses);

        this.expenseListView.getSelectionModel().selectedItemProperty().addListener(
                (_, _, newValue) -> {
                    if (newValue != null) {
                        this.populateFieldsWithExpense(newValue);
                    }
                }
        );

        if (this.errorLabel != null) {
            this.errorLabel.setVisible(false);
            this.errorLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleAdd() {

        this.hideError();

        if (this.validateInputs()) {
            return;
        }

        try {

            LocalDate date = this.datePicker.getValue();
            double amount = Double.parseDouble(this.amountField.getText().trim());
            String merchant = this.merchantField.getText().trim();
            String item = this.itemField.getText().trim();
            Category category = this.categoryComboBox.getValue();

            Expense expense = new Expense(date, amount, merchant, item, category);
            this.ledger.add(expense);
            this.expenses.add(expense);

            this.clearForm();
            this.updatePieChart();
        } catch (NumberFormatException e) {
            this.showError("Amount must be a valid number");
        }
    }

    @FXML
    private void handleEdit() {
        this.hideError();

        Expense selectedExpense = this.expenseListView.getSelectionModel().getSelectedItem();

        if (selectedExpense == null) {
            this.showError("Please select an expense to edit");
            return;
        }

        if (this.validateInputs()) {
            return;
        }

        try {

            selectedExpense.setDate(this.datePicker.getValue());
            selectedExpense.setAmount(Double.parseDouble(this.amountField.getText().trim()));
            selectedExpense.setMerchant(this.merchantField.getText().trim());
            selectedExpense.setItem(this.itemField.getText().trim());
            selectedExpense.setCategory(this.categoryComboBox.getValue());

            this.expenseListView.refresh();

            this.clearForm();
            this.updatePieChart();
        } catch (NumberFormatException e) {
            this.showError("Amount must be a valid number");
        }
    }

    @FXML
    private void handleRemove() {
        this.hideError();

        Expense selectedExpense = this.expenseListView.getSelectionModel().getSelectedItem();

        if (selectedExpense == null) {
            this.showError("Please select an expense to remove");
            return;
        }

        this.ledger.remove(selectedExpense);
        this.expenses.remove(selectedExpense);
        this.clearForm();
        this.updatePieChart();
    }

    @FXML
    private void handleLoadExpenses() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Expense Data");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Finance Files", "*.fin")
        );

        File file = fileChooser.showOpenDialog(this.datePicker.getScene().getWindow());

        if (file != null) {
            this.loadExpensesFromFile(file);
        }
    }

    @FXML
    private void handleSortByDate() {
        this.expenses.sort(Comparator.comparing(Expense::getDate).reversed());
        this.expenseListView.refresh();
    }

    @FXML
    private void handleSortByPrice() {
        this.expenses.sort(Comparator.comparing(Expense::getAmount).reversed());
        this.expenseListView.refresh();
    }

    @FXML
    private void handleSortByCategoryAndPrice() {
        this.expenses.sort(
                Comparator.comparing(Expense::getCategory)
                        .thenComparing(Comparator.comparing(Expense::getAmount).reversed())
        );
        this.expenseListView.refresh();
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("COMP2320 Fall 2025");
        alert.setHeaderText("Finance Manager v1.0");
        alert.setContentText(
                "Project 3 by Cedric Jones"
        );
        alert.showAndWait();
    }

    private void loadExpensesFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            this.ledger.clear();
            this.expenses.clear();

            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length < 5) {
                    System.out.println("Line " + lineNumber + " is invalid. Insufficient data.");
                    continue;
                }

                try {
                    LocalDate date = LocalDate.parse(parts[0].trim());
                    double amount = Double.parseDouble(parts[1].trim());
                    String merchant = parts[2].trim();
                    String item = parts[3].trim();
                    Category category = Category.valueOf(parts[4].trim().toUpperCase());

                    if (amount < 0) {
                        System.out.println("Line " + lineNumber + " is invalid. Amount cannot be negative.");
                        continue;
                    }

                    if (merchant.isEmpty()) {
                        System.out.println("Line " + lineNumber + " is invalid. Merchant cannot be empty.");
                        continue;
                    }

                    if (item.isEmpty()) {
                        System.out.println("Line " + lineNumber + " is invalid. Item cannot be empty.");
                        continue;
                    }

                    Expense expense = new Expense(date, amount, merchant, item, category);
                    this.ledger.add(expense);
                    this.expenses.add(expense);

                } catch (NumberFormatException e) {
                    System.out.println("Line " + lineNumber + " is invalid. Amount must be a valid number.");
                } catch (DateTimeParseException | IllegalArgumentException e) {
                    System.out.println("Line " + lineNumber + " is invalid. " + e.getMessage());
                }
            }
            this.updatePieChart();

        } catch (IOException e) {
            this.showError("Error loading file: " + e.getMessage());
        }
    }

    /**
     * Validates user inputs.
     * @return true if valid
     */
    private boolean validateInputs() {
        if (this.datePicker.getValue() == null) {
            this.showError("Date cannot be null or empty");
            return true;
        }

        if (this.amountField.getText().trim().isEmpty()) {
            this.showError("Item cannot be null or empty");
            return true;
        }

        try {
            Double.parseDouble(this.amountField.getText().trim());
        } catch (NumberFormatException e) {
            this.showError("Item cannot be null or empty");
            return true;
        }

        if (this.merchantField.getText().trim().isEmpty()) {
            this.showError("Item cannot be null or empty");
            return true;
        }

        if (this.itemField.getText().trim().isEmpty()) {
            this.showError("Item cannot be null or empty");
            return true;
        }

        if (this.categoryComboBox.getValue() == null) {
            this.showError("Category must be selected");
            return true;
        }

        return false;
    }

    private void populateFieldsWithExpense(Expense expense) {
        this.datePicker.setValue(expense.getDate());
        this.amountField.setText(String.valueOf(expense.getAmount()));
        this.merchantField.setText(expense.getMerchant());
        this.itemField.setText(expense.getItem());
        this.categoryComboBox.setValue(expense.getCategory());
    }

    private void clearForm() {
        this.datePicker.setValue(null);
        this.amountField.clear();
        this.merchantField.clear();
        this.itemField.clear();
        this.categoryComboBox.setValue(null);
        this.expenseListView.getSelectionModel().clearSelection();
        this.hideError();
    }

    private void showError(String message) {
        if (this.errorLabel != null) {
            this.errorLabel.setText(message);
            this.errorLabel.setVisible(true);
        }
    }

    private void hideError() {
        if (this.errorLabel != null) {
            this.errorLabel.setVisible(false);
        }
    }

    private void updatePieChart() {
        Map<Category, Double> categoryTotals = new HashMap<>();

        for (Expense expense : this.expenses) {
            Category category = expense.getCategory();
            double amount = expense.getAmount();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<Category, Double> entry : categoryTotals.entrySet()) {
            String label = String.format("%s: $%,.2f", entry.getKey(), entry.getValue());
            pieChartData.add(new PieChart.Data(label, entry.getValue()));
        }

        this.expensePieChart.setData(pieChartData);
        this.expensePieChart.setLegendVisible(true);
    }
}