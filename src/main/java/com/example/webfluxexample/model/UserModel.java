package com.example.webfluxexample.model;

import com.example.webfluxexample.entity.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserModel {
    private String id;
    private String username;
    private String email;

//    public static String from(User user) {
//        return user.getId();
//    }
}
