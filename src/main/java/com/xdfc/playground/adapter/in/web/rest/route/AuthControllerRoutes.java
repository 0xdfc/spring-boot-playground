package com.xdfc.playground.adapter.in.web.rest.route;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.xdfc.playground.adapter.in.web.rest.route.RouteConstructor.constructRoute;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final public class AuthControllerRoutes {
    final public static String REGISTRATION_SEGMENT = "/registration";
    final public static String LOGIN_SEGMENT = "/login";
    final public static String FORGOT_PASSWORD_SEGMENT = "/forgot/password";
    final public static String RESET_PASSWORD_SEGMENT = "/reset/password";

    final public static String ROOT_ENDPOINT = "/auth/manage";
    final public static String REGISTRATION_ENDPOINT = constructRoute(
        ROOT_ENDPOINT,
        REGISTRATION_SEGMENT
    );
    final public static String LOGIN_ENDPOINT = constructRoute(
        ROOT_ENDPOINT,
        LOGIN_SEGMENT
    );
}
