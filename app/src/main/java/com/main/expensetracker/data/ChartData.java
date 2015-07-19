package com.main.expensetracker.data;

/**
 * Created by MaRoy on 3/19/2015.
 */
public class ChartData {

    private String value;
    private String label;

    public ChartData(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
