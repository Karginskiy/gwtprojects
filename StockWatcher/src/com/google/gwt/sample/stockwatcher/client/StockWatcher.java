package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StockWatcher implements EntryPoint {

    private VerticalPanel mainPanel = new VerticalPanel();
    private FlexTable stockFlexTable = new FlexTable();
    private HorizontalPanel addPanel = new HorizontalPanel();
    private TextBox newSymbolTextBox = new TextBox();
    private Button addStockButton = new Button("Add");
    private Label lastUpdatedLable = new Label();

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

        addStockButton.addClickHandler(event -> {
            addStock();
        });

        newSymbolTextBox.addKeyDownHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                addStock();
            }
        });



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

    }
}
