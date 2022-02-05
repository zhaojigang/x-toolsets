package strategy.factory.test;

import strategy.factory.Extension;

@Extension("zk")
public class ZkRegistry implements Registry {
    @Override
    public void register() {
        System.out.println("ZkRegistry");
    }
}
