package strategy.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用策略工厂
 */
public class ExtensionLoaderFactory {
    /**
     * key: 策略接口 Class
     * value: 策略接口对应的 ExtensionLoader（单例，每一个策略接口有一个 ExtensionLoader）
     */
    private static final Map<Class<?>, ExtensionLoader<?>> LOADER_MAP = new ConcurrentHashMap<>();

    /**
     * 获取或创建 clazz 策略接口的 ExtensionLoader
     *
     * @param clazz 策略接口
     * @param <T>
     * @return clazz 策略接口的 ExtensionLoader
     */
    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> clazz) {
        ExtensionLoader<T> loader = (ExtensionLoader<T>) LOADER_MAP.get(clazz);
        if (loader == null) {
            synchronized (ExtensionLoaderFactory.class) {
                if (loader == null) {
                    loader = new ExtensionLoader<>(clazz);
                    LOADER_MAP.put(clazz, loader);
                }
            }
        }
        return loader;
    }
}
