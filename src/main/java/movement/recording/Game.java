package movement.recording;

import movement.arm.RestClient;

import java.util.ArrayList;

public class Game
{
    private RestClient restClient;

    private ArrayList<IAction> moves;

    public Game(RestClient restClient)
    {
        this.restClient = restClient;
        this.moves = new ArrayList<>();
    }

    public void add(IAction action)
    {
        moves.add(action);
    }

    public void replay()
    {
        String sequence = "";
        if (moves.size() > 0) {
            for (IAction move: moves) {
                sequence += move.getAsString() + "\n";
            }
        }
        this.restClient.post("/sequence", sequence);
    }
}
