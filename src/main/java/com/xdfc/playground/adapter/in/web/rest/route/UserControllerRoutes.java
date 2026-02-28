package com.xdfc.playground.adapter.in.web.rest.route;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.xdfc.playground.adapter.in.web.rest.route.RouteConstructor.constructRoute;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final public class UserControllerRoutes {
    final public static String USER_ID_SEGMENT = "/{id}";

    final public static String ROOT_ENDPOINT = "/users";
    final public static String USER_BY_ID_ENDPOINT = constructRoute(
        ROOT_ENDPOINT,
        USER_ID_SEGMENT
    );

    public static String getUserByIdEndpoint(String id) {
        return USER_BY_ID_ENDPOINT.replace("{id}", id);
    }
}
