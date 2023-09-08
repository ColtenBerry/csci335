package robosim.ai;

import core.Duple;
import robosim.core.*;
import robosim.reinforcement.QTable;

public class QTableDirtController implements Controller {
    String[] state_array = {"Far", "O_Close", "Hit", "Dirt_detected"};
    String[] action_array = {"Forward", "Turn_Left"};
    double discount = 0.5;
    int rate_constant = 2;
    int target_visits = 5;
    int reward;
    QTable q = new QTable(state_array.length, action_array.length, 0, target_visits, rate_constant, discount);
    @Override
    public void control(Simulator sim) {
        int newState = 0;
        int newAction;
        //find current state. Known to Qtable as newstate
        //hit
        if (sim.wasHit()) {
            newState = 2;
            if (action_array[q.getLastAction()] == "Forward") {
                reward = -20;
                System.out.println("hit");
            }
            else {
                reward = 0;
            }
        }
        //close to obstacle
        else if (sim.findClosestProblem() < 5 && !sim.wasHit()) {
            newState = 1;
            System.out.println("Close");
            if (action_array[q.getLastAction()] == "Forward") {
                reward = 1;
            }
            else {reward = 0;}
        }
        else {
            for (Duple<SimObject, Polar> obj : sim.allVisibleObjects()) {
                System.out.println(obj);
                if (obj.getFirst().isVacuumable()) {
                    newState = 3;
                    if (action_array[q.getLastAction()] == "Forward") {
                        reward = 5;
                    }
                    else {
                        reward = 0;
                    }
                }
                else {
                    newState = 0;
                    if (action_array[q.getLastAction()] == "Forward") {
                        reward = 1;
                    }
                    else {
                        reward = 0;
                    }
                }
            }
        }
        newAction = q.senseActLearn(newState, reward);
        if (action_array[newAction] == "Forward") {
            Action.FORWARD.applyTo(sim);
        }
        else if (action_array[newAction] == "Turn_Left") {
            Action.LEFT.applyTo(sim);
        }
//        else if (action_array[newAction] == "Turn_Right") {
//            Action.RIGHT.applyTo(sim);
//        }
        System.out.println(q);
    }

}
