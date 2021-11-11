package top.maof.haikang.service;

import top.maof.haikang.model.Detection;
import top.maof.haikang.vo.DetectDetailVo;
import top.maof.haikang.vo.PageVO;

import java.util.List;

public interface DetectionService {

    int insert(Detection detection);

    int update(Detection detection);

    Detection select(int id);

    List<Detection> selectsByUser(int userId);

    int delete(int id);

    PageVO<Detection> list(int userId, int type, int page, int pageSize);

    PageVO<DetectDetailVo> listDetail(int userId,int type,int page,int pageSize,int id);
}
