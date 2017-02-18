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

    private Sequence sequence;

    private Sequence lastSequence;

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
            Coordinate currentCoord = new Coordinate(
                    this.currentCoordinate.getX(),
                    this.currentCoordinate.getY(),
                    this.currentCoordinate.getZ()
            );
            if (direction.equals("f")) {
                currentCoord.setX(
                        currentCoord.getX() + length
                );
            } else if (direction.equals("b")) {
                currentCoord.setX(
                        currentCoord.getX() - length
                );
            } else if (direction.equals("l")) {
                currentCoord.setY(
                        currentCoord.getY() - length
                );
            } else if (direction.equals("r")) {
                currentCoord.setY(
                        currentCoord.getY() + length
                );
            } else if (direction.equals("u")) {
                currentCoord.setZ(
                        currentCoord.getZ() + length
                );
            } else if (direction.equals("d")) {
                currentCoord.setZ(
                        currentCoord.getZ() - length
                );
            }
            this.control.move(currentCoord);
            this.sequence.add(currentCoord);
            this.currentCoordinate = currentCoord;
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
            this.sequence.add(new Grab(true));
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
            this.sequence.add(new Grab(false));
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
            this.sequence.add(new Reset());
            return this.buildResponse("arm reseted");
        }
        return this.initFailed();
    }

    @RequestMapping(value = "/replay", method = RequestMethod.GET)
    public Response replay()
    {
        if (this.isInitialized) {
            if (this.lastSequence.hasActions()) {
                this.lastSequence.replay();
                return this.buildResponse("replay started");
            } else {
                return this.buildResponse("No actions in sequence");
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
            this.lastSequence = this.sequence;
            this.isRunning = false;
            return this.buildResponse("sequence saved");
        }
        return this.initFailed();
    }

    private void startArm()
    {
        this.isRunning = true;
        this.sequence = new Sequence(this.control);
        this.control.reset();
        this.currentCoordinate = this.control.getPosition();
        //this.sequence.add(new Reset());
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
