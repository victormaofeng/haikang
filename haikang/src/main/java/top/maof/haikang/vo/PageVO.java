package top.maof.haikang.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {
    private List<T> list;

    private int count;
}
