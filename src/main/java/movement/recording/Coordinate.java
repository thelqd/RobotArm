package movement.recording;

import org.json.simple.JSONObject;

public class Coordinate implements IAction
{
    private int x;

    private int y;

    private int z;

    public Coordinate(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public JSONObject getJson()
    {
        JSONObject json = new JSONObject();
        json.put("x", new Integer(this.getX()));
        json.put("y", new Integer(this.getY()));
        json.put("z", new Integer(this.getZ()));

        return json;
    }

    @Override
    public String getAsString() {
        return "move " + this.getX() + " " + this.getY() + " " + this.getZ() + " ";
    }
}
