package com.xdfc.playground.adapter.in.web.rest.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
// TODO ->> apply hasRole calls
// This is spEL -- a DSL. Super useful for auth and perhaps other cases,
// ex: https://www.baeldung.com/spring-security-method-security. Powerful..
@PreAuthorize("#id == authentication.principal.id")
public @interface IsOwnerOrAdministrator {}
