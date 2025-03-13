package com.gustavo.autocontrato.exception.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFoundException extends UsernameNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
