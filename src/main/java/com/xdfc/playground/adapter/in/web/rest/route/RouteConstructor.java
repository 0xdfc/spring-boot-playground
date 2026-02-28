package com.xdfc.playground.adapter.in.web.rest.route;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final public class RouteConstructor {
    public static String constructRoute(@NotNull CharSequence ... elements) {
        return String.join("", elements);
    }
}
