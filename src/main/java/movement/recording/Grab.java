package movement.recording;

import org.json.simple.JSONObject;

public class Grab implements IAction
{
    private boolean mode;

    public Grab(boolean mdoe)
    {
        this.mode = mdoe;
    }

    public JSONObject getJson()
    {
        JSONObject json = new JSONObject();
        json.put("pumpOn", this.mode);
        return json;
    }
}
