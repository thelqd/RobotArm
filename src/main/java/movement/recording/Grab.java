package movement.recording;

import org.json.simple.JSONObject;

public class Grab implements IAction
{
    private boolean mode;

    public Grab(boolean mode)
    {
        this.mode = mode;
    }

    public JSONObject getJson()
    {
        JSONObject json = new JSONObject();
        json.put("pumpOn", this.mode);
        return json;
    }

    @Override
    public String getAsString() {
        String ret = "pumpOn " + Boolean.toString(mode) + " \n";
        ret += "valueOn " + Boolean.toString(mode) + " ";
        return ret;
    }
}
