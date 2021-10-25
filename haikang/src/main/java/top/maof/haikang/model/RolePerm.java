package top.maof.haikang.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePerm {
    private Integer id;

    private Integer roleId;

    private Integer permId;

}