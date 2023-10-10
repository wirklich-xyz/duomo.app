package xyz.wirklich.duomo;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamLambdaHandler implements RequestStreamHandler {

    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
    static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(DuomoApplication.class);
        } catch (ContainerInitializationException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }
    /**
    public StreamLambdaHandler() throws ContainerInitializationException {
        handler = new SpringBootProxyHandlerBuilder()
                .defaultProxy()
                .asyncInit()
                .springBootApplication(SlowApplication.class)
                .buildAndInitialize();
    }*/

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {

        handler.proxyStream(inputStream, outputStream, context);
        outputStream.close();

/*
            JSONParser parser = new JSONParser();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            JSONObject responseJson = new JSONObject();

            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
            DynamoDB dynamoDb = new DynamoDB(client);

            try {
                JSONObject event = (JSONObject) parser.parse(reader);

                if (event.get("body") != null) {
                    Person person = new Person((String) event.get("body"));

                    dynamoDb.getTable(DYNAMODB_TABLE_NAME)
                            .putItem(new PutItemSpec().withItem(new Item().withNumber("id", person.getId())
                                    .withString("name", person.getName())));
                }

                JSONObject responseBody = new JSONObject();
                responseBody.put("message", "New item created");

                JSONObject headerJson = new JSONObject();
                headerJson.put("x-custom-header", "my custom header value");

                responseJson.put("statusCode", 200);
                responseJson.put("headers", headerJson);
                responseJson.put("body", responseBody.toString());

            } catch (ParseException pex) {
                responseJson.put("statusCode", 400);
                responseJson.put("exception", pex);
            }

            OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
            writer.write(responseJson.toString());
            writer.close();
*/
    }
}
