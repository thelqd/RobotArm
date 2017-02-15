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

    private boolean isRunning = false;

    private Control control;

    private RestClient restClient;

    private Game game;

    private Game lastGame;

    private Coordinate currentCoordinate;

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public Response init(@RequestParam(value="key") String key)
    {
        this.restClient = new RestClient(key);
        this.control = new Control(restClient);
        this.isInitialized = true;
        return new Response("Connection initialized");
    }

    @RequestMapping(value = "/move", method = RequestMethod.GET)
    public Response move(
            @RequestParam(value = "direction") String direction,
            @RequestParam(value = "length", required = false) Integer length
    )
    {
        if (this.isInitialized) {
            if (!this.isRunning) {
                this.startArm();
            }
            if(length == null) {
                length = 10;
            }
            if (direction.equals("f")) {
                this.currentCoordinate.setX(
                        this.currentCoordinate.getX() + length
                );
            } else if (direction.equals("b")) {
                this.currentCoordinate.setX(
                        this.currentCoordinate.getX() - length
                );
            } else if (direction.equals("l")) {
                this.currentCoordinate.setY(
                        this.currentCoordinate.getY() + length
                );
            } else if (direction.equals("r")) {
                this.currentCoordinate.setY(
                        this.currentCoordinate.getY() - length
                );
            } else if (direction.equals("u")) {
                this.currentCoordinate.setZ(
                        this.currentCoordinate.getZ() + length
                );
            } else if (direction.equals("d")) {
                this.currentCoordinate.setZ(
                        this.currentCoordinate.getZ() - length
                );
            }
            this.control.move(this.currentCoordinate);
            this.game.add(this.currentCoordinate);
            return this.buildResponse("arm moved");
        }
        return this.initFailed();
    }

    @RequestMapping(value = "/grab", method = RequestMethod.GET)
    public Response grab()
    {
        if (this.isInitialized) {
            if (!this.isRunning) {
                this.startArm();
            }
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
            if (!this.isRunning) {
                this.startArm();
            }
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
            if (!this.isRunning) {
                this.startArm();
            }
            this.control.reset();
            this.game.add(new Reset());
            return this.buildResponse("arm reseted");
        }
        return this.initFailed();
    }

    @RequestMapping(value = "/replay", method = RequestMethod.GET)
    public Response replay()
    {
        if (this.isInitialized) {
            if (this.lastGame.hasActions()) {
                this.lastGame.replay();
                return this.buildResponse("replay started");
            } else {
                return this.buildResponse("No actions in game");
            }
        }
        return this.initFailed();
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public Response start()
    {
        if (this.isInitialized) {
            this.startArm();
        }
        return this.initFailed();
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public Response save()
    {
        if (this.isInitialized) {
            this.lastGame = this.game;
            this.isRunning = false;
            return this.buildResponse("sequence saved");
        }
        return this.initFailed();
    }

    private void startArm()
    {
        this.isRunning = true;
        this.game = new Game(restClient);
        this.control.reset();
        this.currentCoordinate = this.control.getPosition();
        this.game.add(new Reset());
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
