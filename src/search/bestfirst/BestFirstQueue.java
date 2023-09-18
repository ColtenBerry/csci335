package search.bestfirst;

import maze.core.MazeExplorer;
import search.SearchNode;
import search.SearchQueue;

import java.awt.*;
import java.util.*;
import java.util.function.ToIntFunction;

public class BestFirstQueue<T> implements SearchQueue<T> {
    // TODO: Implement this class
    // HINT: Use java.util.PriorityQueue. It will really help you.
    //priorityQueue --> lowest value comes to top
    PriorityQueue<SearchNode<T>> queue = new PriorityQueue<>(new Comparator<SearchNode<T>>() {
        @Override
        public int compare(SearchNode<T> o1, SearchNode<T> o2) {
            //get depth + heuristic.applyasint(getvalue)
            int calc1 = o1.getDepth() + heuristic.applyAsInt(o1.getValue());
            int calc2 = o2.getDepth() + heuristic.applyAsInt(o2.getValue());
            if (calc1 < calc2) {
                return -1;
            }
            else if (calc1 > calc2) {
                return 1;
            }
            else {
                return 0;
            }
        }
    });
    HashMap<T, Integer> visited = new HashMap<>();
    ToIntFunction<T> heuristic;
    //heuristic estimates how many nodes are left until the search ends
    //typically, a good guess is an underestimate. Overestimating will cause the best solution to fall deeper into the heap
    //


    public BestFirstQueue(ToIntFunction<T> heuristic) {
        // TODO: Your code here
        this.heuristic = heuristic;

    }

    @Override
    public void enqueue(SearchNode<T> node) {
        // TODO: Your code here
        int result_int;
        if (!visited.containsKey(node.getValue())) {
            visited.put(node.getValue(), heuristic.applyAsInt(node.getValue()) + node.getDepth());
            queue.add(node);
        }
        else {
            result_int = queue.comparator().compare(node, node);
            if (result_int > 0) {
                queue.remove(visited.get(node));
                queue.add(node);
            }
        }
    }

    @Override
    public Optional<SearchNode<T>> dequeue() {
        // TODO: Your code here
        if (queue.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(queue.remove());
        }
    }
}
