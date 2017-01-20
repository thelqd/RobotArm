package movement;

import movement.arm.*;
import movement.recording.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArmController {

    private boolean isInitialized = false;

    private Control control;

    private RestClient restClient;

    private Game game;

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public Response init(@RequestParam(value="key") String key)
    {
        this.restClient = new RestClient(key);
        this.control = new Control(restClient);
        this.game = new Game(restClient);
        this.isInitialized = true;
        this.game.add(new Reset());
        return new Response("Connection initialized");
    }

    @RequestMapping(value = "/move", method = RequestMethod.GET)
    public Response move(
            @RequestParam(value = "x") int x,
            @RequestParam(value = "y") int y,
            @RequestParam(value = "z") int z)
    {
        if (this.isInitialized) {
            Coordinate coordinate = new Coordinate(x, y, z);
            this.control.move(coordinate);
            this.game.add(coordinate);
            return this.buildResponse("arm moved");
        }
        return this.initFailed();
    }

    @RequestMapping(value = "/grab", method = RequestMethod.GET)
    public Response grab()
    {
        if (this.isInitialized) {
            this.control.grab();
            this.game.add(new Grab(true));
            return this.buildResponse("arm grabbing");
        }
        return this.initFailed();
    }

    @RequestMapping(value = "/release", method = RequestMethod.GET)
    public Response release()
    {
        if (this.isInitialized) {
            this.control.release();
            this.game.add(new Grab(false));
            return this.buildResponse("arm released");
        }
        return this.initFailed();
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public Response reset()
    {
        if (this.isInitialized) {
            this.control.reset();
            this.game.add(new Reset());
            return this.buildResponse("arm reseted");
        }
        return this.initFailed();
    }

    @RequestMapping(value = "/erplay", method = RequestMethod.GET)
    public Response replay()
    {
        if (this.isInitialized) {
            this.game.replay();
            return this.buildResponse("replay started");
        }
        return this.initFailed();
    }

    private Response initFailed()
    {
        return this.buildResponse("Service not initialized. Call /init with a valid key");
    }

    private Response buildResponse(String text)
    {
        return new Response(text);
    }
}
