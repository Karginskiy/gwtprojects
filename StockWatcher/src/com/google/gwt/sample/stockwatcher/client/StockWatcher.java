package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StockWatcher implements EntryPoint {

    private static final int REFRESH_INTERVAL = 5000; // ms
    private VerticalPanel mainPanel = new VerticalPanel();
    private FlexTable stockFlexTable = new FlexTable();
    private HorizontalPanel addPanel = new HorizontalPanel();
    private TextBox newSymbolTextBox = new TextBox();
    private Button addStockButton = new Button("Add");
    private Label lastUpdatedLable = new Label();
    private ArrayList<String> stocks = new ArrayList<>();

    @Override
    public void onModuleLoad() {
        stockFlexTable.setText(0, 0, "Symbol");
        stockFlexTable.setText(0, 1, "Price");
        stockFlexTable.setText(0, 2, "Change");
        stockFlexTable.setText(0, 3, "Remove");

        addPanel.add(newSymbolTextBox);
        addPanel.add(addStockButton);

        mainPanel.add(stockFlexTable);
        mainPanel.add(addPanel);
        mainPanel.add(lastUpdatedLable);

        RootPanel.get("stockList").add(mainPanel);

        newSymbolTextBox.setFocus(true);

        Timer refreshTimer = new Timer() {
            @Override
            public void run() {
                refreshWatchList();
            }
        };
        refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

        addStockButton.addClickHandler(event -> {
            addStock();
        });

        newSymbolTextBox.addKeyDownHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                addStock();
            }
        });



    }

    private void refreshWatchList() {
        final double MAX_PRICE = 100.0; // $100.00
        final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

        StockPrice[] prices = new StockPrice[stocks.size()];
        for (int i = 0; i < stocks.size(); i++) {
            double price = Random.nextDouble() * MAX_PRICE;
            double change = price * MAX_PRICE_CHANGE
                    * (Random.nextDouble() * 2.0 - 1.0);

            prices[i] = new StockPrice(stocks.get(i), price, change);

        }

        updateTable(prices);

    }

    private void updateTable(StockPrice[] prices) {
        for (int i = 0; i < prices.length; i++) {
            updateTable(prices[i]);
        }

        DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
        lastUpdatedLable.setText("Last update : " + dateTimeFormat.format(new Date()));
    }

    private void updateTable(StockPrice price) {
        if (!stocks.contains(price.getSymbol())) {
            return;
        }

        int row = stocks.indexOf(price.getSymbol()) + 1;

        String priceText = NumberFormat.getFormat("+#,##0.00").format(price.getPrice());
        NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
        String changeText = changeFormat.format(price.getChange());
        String changePercentText = changeFormat.format(price.getChangePercent());

        stockFlexTable.setText(row, 1, priceText);
        stockFlexTable.setText(row, 2, changeText + " (" + changePercentText + "%)");
    }

    public void addStock() {

        final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
        newSymbolTextBox.setFocus(true);

        if (!symbol.matches("^[0-9A-Z\\.]{1,10}$")) {
            Window.alert("'" + symbol + "' is not a valid symbol.");
            newSymbolTextBox.selectAll();
            return;
        }

        newSymbolTextBox.setText("");

        if (stocks.contains(symbol)) {
            return;
        }

        int row = stockFlexTable.getRowCount();
        stocks.add(symbol);
        stockFlexTable.setText(row, 0, symbol);

        Button removeStockButton = new Button("x");
        removeStockButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int removedIndex = stocks.indexOf(symbol);
                stocks.remove(removedIndex);
                stockFlexTable.removeRow(removedIndex + 1);
            }
        });
        stockFlexTable.setWidget(row, 3, removeStockButton);
        // TODO Get the stock price.

    }
}
