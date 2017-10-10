package com.test.selenium.testscript;

import java.math.BigDecimal;
import java.math.RoundingMode;

class Point {

    private final BigDecimal x;
    private final BigDecimal y;

    Point(BigDecimal x, BigDecimal y) {
        this.x = x.setScale(0, RoundingMode.HALF_UP);
        this.y = y.setScale(0, RoundingMode.HALF_UP);
    }

    @Override
    public int hashCode() {
        return this.x.hashCode() + this.y.hashCode();
    }

    public int getX() {
        return x.intValue();
    }

    public int getY() {
        return y.intValue();
    }
}
