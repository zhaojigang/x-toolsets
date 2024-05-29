package collections;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 集合助手
 * @author zhaojigang
 */
public class CollectionHelper {
    /**
     * 从原始列表中截取指定范围的子列表，并转换为新的列表返回。
     */
    public static <T> List<T> subList(List<T> originList, int fromIndex, int toIndex) {
        return Lists.newArrayList(originList.subList(fromIndex, Math.max(toIndex, originList.size())));
    }
}
