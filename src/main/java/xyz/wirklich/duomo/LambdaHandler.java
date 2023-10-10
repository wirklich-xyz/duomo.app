package xyz.wirklich.duomo;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

    //    private static SpringLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse> httpApiHandler;
    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
    static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(DuomoApplication.class);
        } catch (ContainerInitializationException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    private static final DuomoController rest = new DuomoController();

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("EVENT TYPE: " + input.getClass().toString());
        return handler.proxy(input, context);
    }
}

