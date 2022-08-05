package com.example.demo.domain.mongo;

import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString
public class HobbyInfo {
    private String name;
    private Set<String> hobbies;

}
