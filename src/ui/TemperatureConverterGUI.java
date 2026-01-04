package ui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.*;
import exception.InvalidTemperatureException;

public class TemperatureConverterGUI extends JFrame {
    
    private JTextField inputField;
    private JComboBox<String> unitComboBox;
    private JTextArea resultArea;
    private JLabel statusLabel; // New: To show save status

    public TemperatureConverterGUI() {
        setTitle("Temperature Converter & Logger");
        setSize(400, 350); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Changed to 5 rows to accommodate the status label
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Input Section
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputField = new JTextField(10);
        String[] units = {"Celsius", "Fahrenheit", "Kelvin"};
        unitComboBox = new JComboBox<>(units);
        JButton convertBtn = new JButton("Convert");
        convertBtn.addActionListener(e -> convert());
        
        inputPanel.add(new JLabel("Value:"));
        inputPanel.add(inputField);
        inputPanel.add(unitComboBox);
        inputPanel.add(convertBtn);
        
        // Results Section
        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // Status Label Section
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(new Color(0, 128, 0)); // Dark green for success
        
        panel.add(new JLabel("Temperature Converter", SwingConstants.CENTER));
        panel.add(inputPanel);
        panel.add(new JLabel("Results:"));
        panel.add(new JScrollPane(resultArea));
        panel.add(statusLabel); // Add the status label to the bottom
        
        add(panel);
        setVisible(true);
    }
    
    private void convert() {
        try {
            double value = Double.parseDouble(inputField.getText().trim());
            String unit = (String) unitComboBox.getSelectedItem();
            
            Temperature temp = null;
            if (unit.equals("Celsius")) temp = new Celsius(value);
            else if (unit.equals("Fahrenheit")) temp = new Fahrenheit(value);
            else if (unit.equals("Kelvin")) temp = new Kelvin(value);
            
            double celsius = (temp instanceof Celsius) ? value : 
                            (temp instanceof Fahrenheit) ? (value - 32) * 5/9 : value - 273.15;
            double fahrenheit = (temp instanceof Fahrenheit) ? value : 
                               (temp instanceof Celsius) ? (value * 9/5) + 32 : (value - 273.15) * 9/5 + 32;
            double kelvin = (temp instanceof Kelvin) ? value : 
                           (temp instanceof Celsius) ? value + 273.15 : (value - 32) * 5/9 + 273.15;
            
            String output = String.format("Input: %.2f %s\nCelsius: %.2f°C\nFahrenheit: %.2f°F\nKelvin: %.2fK", 
                                          value, unit, celsius, fahrenheit, kelvin);
            
            resultArea.setText(output);
            
            // Save and update status
            saveToFile(output);
            
        } catch (NumberFormatException e) {
            statusLabel.setText("<html><font color='red'>Error: Invalid number</font></html>");
        } catch (Exception e) {
            statusLabel.setText("<html><font color='red'>Error: " + e.getMessage() + "</font></html>");
        }
    }

  public void saveToFile(String data) {
    
    File file = new File("src/ui/sap.txt");

    try {
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream fo = new FileOutputStream(file, true)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String entry = "\n--- " + timestamp + " ---\n" + data + "\n";
            
            fo.write(entry.getBytes());
            fo.flush();
            
            statusLabel.setText("✓ Saved to " + file.getPath());
        }
    } catch (IOException e) {
        statusLabel.setText("<html><font color='red'>Save Failed: " + e.getMessage() + "</font></html>");
        e.printStackTrace();
    }
}
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TemperatureConverterGUI());
    }
}