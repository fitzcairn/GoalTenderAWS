package com.stevezero.aws.api.apps.goaltender.id.impl;

import com.stevezero.aws.api.exceptions.InvalidApiResourceId;
import com.stevezero.aws.api.id.ApiResourceId;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 * Pojo for a GoalId.  To enforce uniqueness, concat'd with the user id before encoding.
 */
public class GoalId implements ApiResourceId {
  private static final String USER_KEY = "u";
  private static final String GOAL_KEY = "g";

  private final String goalIdString;
  private final UserId userId;

  public GoalId(String goalIdString, UserId userId) {
    this.goalIdString = goalIdString;
    this.userId = userId;
  }

  /**
   * @return the client-generated goal id.
   */
  public String getClientGoalId() {
    return goalIdString;
  }

  /**
   * @param base64IdString is a URL-safe base64 encoded version of:
   *   {
   *     "u": "base64-encoded UserId JSON object",
   *     "g": "goal id generated by the client.",
   *   }
   * @throws InvalidApiResourceId on parse error.
   */
  public GoalId(String base64IdString) throws InvalidApiResourceId {
    try {
      // Decode the string, propagating the exception if something went wrong.
      String jsonIdString = new String(DECODER.decode(base64IdString));
      JSONObject userIdJson = (JSONObject) JSON_PARSER.parse(jsonIdString);

      // Now parse the resulting JSON.
      this.goalIdString = (String)userIdJson.get(GOAL_KEY);
      this.userId = new UserId((String)userIdJson.get(USER_KEY));

    } catch (ParseException | IllegalArgumentException e) {
      throw new InvalidApiResourceId(base64IdString);
    }
  }

  /**
   * @return a unique base64 encoded encoded id string.
   */
  @Override
  public String toBase64String() {
    JSONObject goalIdJson = new JSONObject();
    goalIdJson.put(USER_KEY, userId.toBase64String());
    goalIdJson.put(GOAL_KEY, goalIdString);
    String JSONString = goalIdJson.toJSONString();
    return ENCODER.encodeToString(JSONString.getBytes());
  }

  /**
   * @return the raw goal id as generated by the client.
   */
  public String getGoalIdString() {
    return this.goalIdString;
  }
}