package strategy.factory.test;

import strategy.factory.Extension;

@Extension("random")
public class RandomLoadBalancer implements LoadBalancer {
    @Override
    public void route() {
        System.out.println("RandomLoadBalancer");
    }
}
