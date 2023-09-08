package robosim.ai;

import robosim.core.Action;
import robosim.core.Controller;
import robosim.core.Simulator;
import robosim.reinforcement.QTable;

public class QTableController implements Controller {
    String[] state_array = {"Far", "Close", "Hit"};
    String[] action_array = {"Forward", "Turn"};
    double discount = 0.8;
    int rate_constant = 2;
    int target_visits = 10;
    int reward;
    QTable q = new QTable(state_array.length, action_array.length, 0, target_visits, rate_constant, discount);
    @Override
    public void control(Simulator sim) {
        int newState;
        int newAction;
        //find current state. Known to Qtable as newstate
        if (sim.wasHit()) {
            newState = 2;
            if (action_array[q.getLastAction()] == "Forward") {
                reward = -20;
                System.out.println("hit");
            }
        }
        else if (sim.findClosestProblem() < 5 && !sim.wasHit()) {
            newState = 1;
            System.out.println("Close");
            if (action_array[q.getLastAction()] == "Forward") {
                reward = 1;
            }
            else {reward = 0;}
        }
        else {
            newState = 0;
            if (action_array[q.getLastAction()] == "Forward") {
                reward = 1;
            }
            else {reward = 0;}
        }
        newAction = q.senseActLearn(newState, reward);
        if (action_array[newAction] == "Forward") {
            Action.FORWARD.applyTo(sim);
        }
        else if (action_array[newAction] == "Turn") {
            Action.LEFT.applyTo(sim);
        }
        System.out.println(q);
    }

}
