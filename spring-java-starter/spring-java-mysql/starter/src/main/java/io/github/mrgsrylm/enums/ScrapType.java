package io.github.mrgsrylm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScrapType {
    ORGANIC("organic"),
    RECYCLABLE("recyclable");

    private final String value;
}
