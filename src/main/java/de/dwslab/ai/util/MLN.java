package de.dwslab.ai.util;

import java.math.BigDecimal;

public class MLN {

    public static double logit(double probability) {
        return -Math.log((1 / probability) - 1);
    }

    public static BigDecimal logit(BigDecimal probability) {
        return new BigDecimal(-Math.log((1 / probability.doubleValue()) - 1));
    }
}
