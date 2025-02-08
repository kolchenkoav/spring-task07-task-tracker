package com.example.webfluxexample.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "role")
public class Role {
    @Id
    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Field("authority")
    private RoleType authority;


    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @Field("user")
    private User user;

    public GrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(authority.name());
    }

    public static Role from(RoleType type) {
        var role = new Role();
        role.setAuthority(type);

        return role;
    }
}
