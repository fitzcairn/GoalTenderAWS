package com.stevezero.aws.goaltender.user.methods;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.stevezero.aws.goaltender.ApiGatewayProxyResponse;
import com.stevezero.aws.goaltender.StatusCode;
import com.stevezero.aws.goaltender.user.data.UserId;

/**
 * Look up user and return information, or not found.
 */
public class GetUser {

  public ApiGatewayProxyResponse handle(UserId id, Context context) throws Exception {
    ApiGatewayProxyResponse.Builder responseBuilder = new ApiGatewayProxyResponse.Builder();
    LambdaLogger logger = context.getLogger();

    try {
      logger.log("Handling request in GetUser.");

      // TODO: actual logic. :)

      responseBuilder
          .withBody(id.toString())
          .withStatusCode(StatusCode.OK);

    } catch(Exception e) {
      responseBuilder
          .withStatusCode(StatusCode.SERVER_ERROR)
          .withBody(e.toString());
    }

    return responseBuilder.build();
  }
}
