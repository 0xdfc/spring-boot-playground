package com.xdfc.playground.adapter.in.web.rest.constant;

import java.math.BigDecimal;

final public class AccountConstants {
    // BigDecimals don't have limits and can grow infinitely
    // by use of RAM. The only curious case is the scale of a
    // number exceeding the 32 bit MAX_INT.
    final public static BigDecimal MaximumAccountBalance = new BigDecimal(
        "999000000000000" // 999T
    );

    final public static BigDecimal AccountStartingBalance = new BigDecimal(
        "100"
    );
}
