package movement.recording;

import movement.arm.Control;

import java.util.ArrayList;

public class Sequence
{
    private Control control;

    private ArrayList<IAction> moves;

    public Sequence(Control control)
    {
        this.control = control;
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
                System.out.println(move.getAsString());
            }
        }
        this.control.sequence(sequence);
    }

    public boolean hasActions()
    {
        return this.moves.size() > 1;
    }
}
