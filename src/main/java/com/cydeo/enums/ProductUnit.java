package com.cydeo.enums;

public enum ProductUnit {
    LBS("Libre"), GALLON("Gallon"), PCS("Pieces"), KG("Kilogram"), METER("Meter"), INCH("Inch"), FEET("Feet");

    private final String unit;

    ProductUnit(String unit) {
        this.unit = unit;
    }
    public String getUnit() {
        return unit;
    }
}
