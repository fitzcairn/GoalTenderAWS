package com.stevezero.aws.api.apps.goaltender.id.impl;

import com.stevezero.aws.api.exceptions.InvalidResourceIdException;
import com.stevezero.aws.api.id.ApiResourceId;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 * Pojo for a GoalId.  To enforce uniqueness, concat'd with the user id before encoding.
 */
public class GoalId implements ApiResourceId {
  private static final String USER_KEY = "u";
  private static final String GOAL_KEY = "g";

  private final String goalId;
  private final String userBase64String;

  public GoalId(String goalId, String userBase64String) {
    this.goalId = goalId;
    this.userBase64String = userBase64String;
  }

  /**
   * @param base64IdString is a URL-safe base64 encoded version of:
   *   {
   *     "u": "base64-encoded UserId JSON object",
   *     "g": "goal id generated by the client.",
   *   }
   * @throws InvalidResourceIdException on parse error.
   */
  public GoalId(String base64IdString) throws InvalidResourceIdException {
    try {
      // Decode the string, propagating the exception if something went wrong.
      String jsonIdString = new String(DECODER.decode(base64IdString));
      JSONObject userIdJson = (JSONObject) JSON_PARSER.parse(jsonIdString);

      // Now parse the resulting JSON.
      this.goalId = (String)userIdJson.get(GOAL_KEY);
      this.userBase64String = (String)userIdJson.get(USER_KEY);

    } catch (ParseException | IllegalArgumentException e) {
      throw new InvalidResourceIdException(base64IdString);
    }
  }

  /**
   * @return a unique base64 encoded encoded id string.
   */
  @Override
  public String toBase64String() {
    JSONObject goalIdJson = new JSONObject();
    goalIdJson.put(USER_KEY, userBase64String);
    goalIdJson.put(GOAL_KEY, goalId);
    String JSONString = goalIdJson.toJSONString();
    return ENCODER.encodeToString(JSONString.getBytes());
  }

  /**
   * @return the raw goal id as generated by the client.
   */
  public String getGoalIdString() {
    return this.goalId;
  }
}