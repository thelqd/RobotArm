package movement.recording;

import org.json.simple.JSONObject;

public interface IAction
{
    /**
     *
     * @return JSONObject
     */
    JSONObject getJson();

    /**
     *
     * @return
     */
    String getAsString();
}
