package top.maof.haikang.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    private Integer id;

    private Integer level;

    private String synopsis;

    private List<Perm> perms;
}