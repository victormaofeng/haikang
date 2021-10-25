package top.maof.haikang.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Perm {
    private Integer id;

    private String permission;

    private String synopsis;
}