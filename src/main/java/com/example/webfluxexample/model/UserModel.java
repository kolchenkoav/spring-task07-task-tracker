package com.example.webfluxexample.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserModel {
    private String id;
    private String username;
    private String email;
}
