package strategy.factory.test;

import strategy.factory.Extension;

@Extension("custom")
public class CustomLoadBalancer implements LoadBalancer {
    @Override
    public void route() {
        System.out.println("CustomLoadBalancer");
    }
}
