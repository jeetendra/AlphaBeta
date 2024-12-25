package com.jeet.schedular.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("app")
@RequiredArgsConstructor
public class AppProperties {
    private final String name;
}
