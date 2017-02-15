package movement.arm;

import movement.recording.Coordinate;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;

public class Control
{
    RestClient restClient;

    String response;

    public Control(RestClient restClient)
    {
        this.restClient = restClient;
    }

    public HttpStatus move(Coordinate coordinate)
    {
        this.restClient.put("/positionXYZ", coordinate.getJson().toString());
        return this.restClient.getStatus();
    }

    public HttpStatus reset()
    {
        this.response = this.restClient.post("/reset", "");
        System.out.println(this.response);
        return this.restClient.getStatus();
    }

    public HttpStatus grab()
    {
        this.restClient.post("/grabOn", "true");
        return this.restClient.getStatus();
    }

    public HttpStatus release()
    {
        this.restClient.post("/grabOn", "false");
        return this.restClient.getStatus();
    }

    public HttpStatus sequence(String seq)
    {
        this.response = this.restClient.post("/sequence", seq);
        System.out.println(this.response);
        return this.restClient.getStatus();
    }

    public Coordinate getPosition()
    {
        try {
            String json = this.restClient.get("/positionXYZ");
            JSONObject cords = (JSONObject) new JSONParser().parse(json);
            return new Coordinate(
                    (int)cords.get("x"),
                    (int)cords.get("y"),
                    (int)cords.get("z")
            );
        } catch (Exception e) {
            return new Coordinate(200, 200, 50);
        }
    }
}
