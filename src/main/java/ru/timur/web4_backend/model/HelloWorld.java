package ru.timur.web4_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class HelloWorld implements Serializable {
    private String message;
}
