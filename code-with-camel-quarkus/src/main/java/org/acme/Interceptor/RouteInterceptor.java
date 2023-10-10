package org.acme.Interceptor;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;


@ApplicationScoped
@Named("routeInterceptor")
public class RouteInterceptor implements Processor {

    private int delayTime;

    public RouteInterceptor(){
        this.delayTime=2000;
    }
    @Override
    public void process(Exchange exchange) throws Exception {
         Thread.sleep(delayTime);
    }
}
