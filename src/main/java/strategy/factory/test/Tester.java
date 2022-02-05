package strategy.factory.test;

import strategy.factory.ExtensionLoaderFactory;

public class Tester {
    public static void main(String[] args) {
        ExtensionLoaderFactory.getExtensionLoader(LoadBalancer.class).getExtension("random").route();
        ExtensionLoaderFactory.getExtensionLoader(LoadBalancer.class).getExtension("custom").route();

        ExtensionLoaderFactory.getExtensionLoader(Registry.class).getExtension("consul").register();
        ExtensionLoaderFactory.getExtensionLoader(Registry.class).getExtension("zk").register();
    }
}
