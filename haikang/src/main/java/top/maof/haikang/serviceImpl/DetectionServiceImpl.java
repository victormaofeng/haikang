package top.maof.haikang.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.maof.haikang.mapper.DetectionMapper;
import top.maof.haikang.model.Detection;
import top.maof.haikang.service.DetectionService;
import top.maof.haikang.vo.PageVO;

import java.util.List;

@Service
public class DetectionServiceImpl implements DetectionService {

    @Autowired
    DetectionMapper detectionMapper;

    /**
     * 返回值要求是新插入数据的id,而不是纪录改变值1
     *
     * @author 毛逢
     * @date: 2021/9/17 21:10
     */
    @Override
    public int insert(Detection detection) {
        return detectionMapper.insert(detection) == 0 ? 0 : detection.getId();
    }

    /**
     * 以id为条件,所有列更新
     *
     * @author 毛逢
     * @date: 2021/9/17 21:11
     */
    @Override
    public int update(Detection detection) {
        return detectionMapper.update(detection);
    }

    /**
     * id查询单条纪录
     *
     * @author 毛逢
     * @date: 2021/9/17 21:12
     */
    @Override
    public Detection select(int id) {
        return detectionMapper.select(id);
    }

    /**
     * 查询用户id做过的的所有检测纪录
     *
     * @author 毛逢
     * @date: 2021/9/17 21:12
     */
    @Override
    public List<Detection> selectsByUser(int userId) {
        return detectionMapper.selectsByUser(userId);
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @Override
    public int delete(int id) {
        return detectionMapper.delete(id);
    }


    /**
     * type 0 不限, 1 检测, 2 重识别
     */
    @Override
    public PageVO<Detection> list(int userId, int type, int page, int pageSize) {
        PageVO<Detection> pageVO = new PageVO<>();
        int length = 0;
        List<Detection> detections = null;
        switch (type) {
            // 类型不限
            case 0:
                detections = detectionMapper.selectsByUserPage(userId, (page - 1) * pageSize, pageSize);
                length = detectionMapper.selectsCountByUser(userId);
                break;
            // 检测纪录
            case 1:
                detections = detectionMapper.selectsByUserAndType(userId, true, (page - 1) * pageSize,
                        pageSize);
                length = detectionMapper.selectsCountByUserAndType(userId, true);
                break;
            // 重识别纪录
            case 2:
                detections = detectionMapper.selectsByUserAndType(userId, false, (page - 1) * pageSize,
                        pageSize);
                length = detectionMapper.selectsCountByUserAndType(userId, false);
                break;
        }
        pageVO.setCount(length);
        pageVO.setList(detections);
        return pageVO;
    }
}
