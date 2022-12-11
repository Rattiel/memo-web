package com.jongil.memo.global.config.security;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Ownable {
    @JsonIgnore
    String getPrincipalName();
}
