package top.maof.haikang.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;

    private String username;

    @JsonIgnore
    private String password;

    private String nickname;

    private String salt;

    private String email;

    private String phoneNumber;

    private List<Role> roles;
}