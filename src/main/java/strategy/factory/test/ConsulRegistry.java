package strategy.factory.test;

import strategy.factory.Extension;

@Extension("consul")
public class ConsulRegistry implements Registry {
    @Override
    public void register() {
        System.out.println("ConsulRegistry");
    }
}
