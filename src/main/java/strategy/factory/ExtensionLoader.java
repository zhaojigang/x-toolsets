package strategy.factory;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 扩展类加载器
 *
 * @param <T>
 */
public class ExtensionLoader<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionLoader.class);
    /**
     * 当前扩展加载器处理的扩展接口名
     */
    private String interfaceName;
    /**
     * interfaceName 扩展接口下的所有实现
     */
    private Map<String, ExtensionClass<T>> alias2ExtensionClass;

    public ExtensionLoader(Class<T> interfaceClass) {
        this.interfaceName = interfaceClass.getName();
        this.alias2ExtensionClass = new ConcurrentHashMap<>();
        // 此处只指定了一个 spi 文件存储的路径
        loadFromClassLoader(interfaceClass, Lists.newArrayList("strategy.factory"));
    }

    private void loadFromClassLoader(Class<T> interfaceClass, List<String> scanPaths) {
        Reflections reflections = new Reflections(scanPaths, this.getClass().getClassLoader());
        Set<Class<? extends T>> classes = reflections.getSubTypesOf(interfaceClass);
        for (Class<? extends T> clazz : classes) {
            // 必须具有扩展注解
            Extension extension = clazz.getAnnotation(Extension.class);
            if (extension == null || StringUtils.isBlank(extension.value())) {
                throw new RuntimeException(clazz.getName() + " need @Extension");
            }
            // 创建 ExtensionClass
            ExtensionClass<T> extensionClass = new ExtensionClass<>(clazz);
            alias2ExtensionClass.putIfAbsent(extension.value(), extensionClass);
        }
    }

    public T getExtension(String alias) {
        ExtensionClass<T> extensionClass = alias2ExtensionClass.get(alias);
        if (extensionClass == null) {
            throw new RuntimeException("Not found extension of " + interfaceName + " named: \"" + alias + "\"!");
        }
        return extensionClass.getExtInstance();
    }

    public T getExtension(String alias, Class<?>[] argTypes, Object[] args) {
        ExtensionClass<T> extensionClass = alias2ExtensionClass.get(alias);
        if (extensionClass == null) {
            throw new RuntimeException("Not found extension of " + interfaceName + " named: \"" + alias + "\"!");
        }
        return extensionClass.getExtInstance(argTypes, args);
    }
}
