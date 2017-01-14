package movement.arm;

import movement.recording.Coordinate;
import org.springframework.http.HttpStatus;

public class Control
{
    RestClient restClient;

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
        this.restClient.post("/reset", "[]");
        return this.restClient.getStatus();
    }

    public HttpStatus grab()
    {
        this.restClient.post("/grabOn", "[true]");
        return this.restClient.getStatus();
    }

    public HttpStatus release()
    {
        this.restClient.post("/grabOn", "[false]");
        return this.restClient.getStatus();
    }

    public HttpStatus sequence(String seq)
    {
        this.restClient.post("/sequence", seq);
        return this.restClient.getStatus();
    }
}
