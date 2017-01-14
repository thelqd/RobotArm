package movement.recording;

import org.json.simple.JSONObject;

public class Reset implements IAction {

    public JSONObject getJson()
    {
        JSONObject json = new JSONObject();
        json.put("reset", "");
        return json;
    }
}
