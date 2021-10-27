package top.maof.haikang.enums;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * @ClassName AlgorithmType
 * @Description TODO
 * @Author Quentin_zyj
 * @Date 2021/10/27 19:27
 */
public class AlgorithmType {

    /** 检测算法 */
    public static final int DETECTION = 1;
    /** 重试别算法 */
    public static final int RE_ID = 2;

    public static final List<Integer> ALL_TYPES = ImmutableList.of(DETECTION, RE_ID);

    public static boolean isLegal(int type) {
        for (Integer integer : ALL_TYPES) {
            if (integer == type) {
                return true;
            }
        }
        return false;
    }

}
