package com.example.webfluxexample.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String id;
    private String username;
    private String email;
}
