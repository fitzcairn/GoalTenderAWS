package com.stevezero.aws.api.apps.goaltender.service.method.impl;

import com.amazonaws.services.lambda.runtime.Context;
import com.stevezero.aws.api.apps.goaltender.id.impl.GoalId;
import com.stevezero.aws.api.apps.goaltender.resource.GoalTenderResourceType;
import com.stevezero.aws.api.apps.goaltender.resource.impl.GoalResource;
import com.stevezero.aws.api.apps.goaltender.service.method.GoalTenderApiMethodHandler;
import com.stevezero.aws.api.apps.goaltender.storage.items.impl.GoalItem;
import com.stevezero.aws.api.apps.goaltender.storage.service.impl.GoalStorageService;
import com.stevezero.aws.api.exceptions.ApiException;
import com.stevezero.aws.api.http.StatusCode;
import com.stevezero.aws.api.id.ApiResourceId;
import com.stevezero.aws.api.service.ApiGatewayProxyRequest;
import com.stevezero.aws.api.service.ApiGatewayProxyResponse;
import com.stevezero.aws.api.service.ApiPath;
import com.stevezero.aws.api.storage.service.StorageService;

/**
 * Handler for GET /user/{ID}
 * Look up user and return information, or not found.
 */
public class GoalsGetHandler extends GoalTenderApiMethodHandler {
  public GoalsGetHandler() {
    super(new GoalStorageService());
  }

  // For testing only.
  GoalsGetHandler(StorageService storageService) {
    super(storageService);
  }

  @Override
  public ApiGatewayProxyResponse handleRequest(ApiGatewayProxyRequest request,
                                               ApiPath parsedApiPath,
                                               Context context) throws ApiException {
    ApiGatewayProxyResponse.Builder responseBuilder = new ApiGatewayProxyResponse.Builder();

    // Pull out the ID.
    ApiResourceId goalId = new GoalId(parsedApiPath.getIdFor(GoalTenderResourceType.GOAL),
        parsedApiPath.getIdFor(GoalTenderResourceType.USER));

    // Attempt to fetch from storage.
    GoalItem goalItem = (GoalItem)(storageService.get(goalId));

    if (goalItem == null) {
      responseBuilder
          .withStatusCode(StatusCode.NOT_FOUND);
    } else {
      // Re-encode into an API resource.
      GoalResource goalResource = new GoalResource(goalItem);

      responseBuilder
          .withBody(goalResource.toJsonString())
          .withStatusCode(StatusCode.OK);
    }

    return responseBuilder.build();
  }
}
