package currencyconvertor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import org.json.*;

public class CurrencyConvertor extends JFrame implements ActionListener {

    JButton convert, clear;
    JComboBox<String> input, output;
    JTextField amount, display;
    String Currency[] = {  "EUR", "USD", "JPY", "BGN", "CZK", "DKK", "GBP", "HUF", "PLN", "RON",
                            "SEK", "CHF", "ISK", "NOK", "HRK", "RUB", "TRY", "AUD", "BRL", "CAD",
                            "CNY", "HKD", "IDR", "ILS", "INR", "KRW", "MXN", "MYR", "NZD", "PHP",
                            "SGD", "THB", "ZAR"};

    CurrencyConvertor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Currency Convertor");
        setLayout(new GridLayout(7, 1));

        JLabel heading = new JLabel("Currency Convertor");
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setFont(new Font("System", Font.BOLD, 30));
        add(heading);

        JPanel ip = new JPanel(new GridLayout(1, 2));
        JLabel heading1 = new JLabel("Select The Input Currency : ");
        heading1.setHorizontalAlignment(SwingConstants.CENTER);
        heading1.setFont(new Font("System", Font.BOLD, 18));
        ip.add(heading1);
        add(ip);

        JPanel a = new JPanel();
        input = new JComboBox<>(Currency);
        Dimension asize = new Dimension(200, 30);
        input.setPreferredSize(asize);
        input.setBackground(Color.WHITE);
        ip.add(input);
        a.add(ip);
        add(a);

        JPanel ip2 = new JPanel(new GridLayout(1, 2));
        JLabel heading2 = new JLabel("Enter The Amount : ");
        heading2.setHorizontalAlignment(SwingConstants.CENTER);
        heading2.setFont(new Font("System", Font.BOLD, 18));
        ip2.add(heading2);
        add(ip2);

        JPanel b = new JPanel();
        amount = new JTextField();
        amount.setFont(new Font("System", Font.BOLD, 16));
        Dimension bsize = new Dimension(200, 30);
        amount.setPreferredSize(bsize);
        ip2.add(amount);
        b.add(ip2);
        add(b);

        JPanel out = new JPanel(new GridLayout(1, 2));
        JLabel heading3 = new JLabel("Select The Output Currency : ");
        heading3.setHorizontalAlignment(SwingConstants.CENTER);
        heading3.setFont(new Font("System", Font.BOLD, 18));
        out.add(heading3);
        add(out);

        JPanel c = new JPanel();
        output = new JComboBox<>(Currency);
        Dimension csize = new Dimension(200, 30);
        output.setPreferredSize(csize);
        output.setBackground(Color.WHITE);
        out.add(output);
        c.add(out);
        add(c);

        convert = new JButton("Convert");
        JPanel convertPanel = new JPanel();
        Dimension buttonSize = new Dimension(180, 40);
        convert.setPreferredSize(buttonSize);
        convert.addActionListener(this);
        convertPanel.add(convert);
        add(convertPanel);

        JPanel out2 = new JPanel(new GridLayout(1, 2));
        JLabel heading4 = new JLabel("Converted Amount : ");
        heading4.setHorizontalAlignment(SwingConstants.CENTER);
        heading4.setFont(new Font("System", Font.BOLD, 18));
        out2.add(heading4);
        add(out2);

        JPanel d = new JPanel();
        display = new JTextField();
        display.setFont(new Font("System", Font.BOLD, 16));
        Dimension dsize = new Dimension(200, 30);
        display.setPreferredSize(dsize);
        out2.add(display);
        d.add(out2);
        add(d);

        clear = new JButton("Clear");
        JPanel clearPanel = new JPanel();
        Dimension buttonSize1 = new Dimension(180, 40);
        clear.setPreferredSize(buttonSize1);
        clear.addActionListener(this);
        clearPanel.add(clear);
        add(clearPanel);

        setLocation(350, 30);
        setSize(600, 700);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == clear) {
            amount.setText("");
            display.setText("");
        } else if (ae.getSource() == convert) {
            String inputCurrency = (String) input.getSelectedItem();
            String outputCurrency = (String) output.getSelectedItem();
            convertCurrency(amount.getText(), inputCurrency, outputCurrency);
        }
    }

    public void convertCurrency(String Amount, String inputCurrency, String outputCurrency) {
        double amount = Double.parseDouble(Amount);
        double conversionRate = fetchConversionRate(inputCurrency, outputCurrency);
        if (conversionRate != -1) {
            double result = amount * conversionRate;
            display.setText(Double.toString(result));
        } else {
            display.setText("Error fetching conversion rate.");
        }
    }

    public double fetchConversionRate(String input, String output) {
        String apiUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_p8N2TJrRVOMGAPqTKy5N7vaOuVKt0J9QkiIvEAjM&base_currency=" + input;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject json = new JSONObject(response.toString());
                JSONObject data = json.getJSONObject("data");
                double conversionRate = data.getDouble(output);
                return conversionRate;
            } else {
                throw new IOException("Failed to fetch data from API, response code: " + connection.getResponseCode());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return -1; // Return -1 to indicate failure
        }
    }

    public static void main(String[] args) {
        new CurrencyConvertor();
    }
}
