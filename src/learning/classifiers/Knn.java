package learning.classifiers;

import core.Duple;
import learning.core.Classifier;
import learning.core.Histogram;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.function.ToDoubleBiFunction;

// KnnTest.test() should pass once this is finished.
public class Knn<V, L> implements Classifier<V, L> {
    private ArrayList<Duple<V, L>> data = new ArrayList<>();
    private ToDoubleBiFunction<V, V> distance;
    private int k;

    public Knn(int k, ToDoubleBiFunction<V, V> distance) {
        this.k = k;
        this.distance = distance;
    }

    @Override
    public L classify(V value) {
        // TODO: Find the distance from value to each element of data. Use Histogram.getPluralityWinner()
        //  to find the most popular label.
//        k = Math.sqrt(data.size()) / 2;
//        System.out.println(k);
        Histogram<L> label_histogram = new Histogram<>();
        ArrayList<Duple<Double, L>> list = new ArrayList<>();
        for (Duple<V, L> duple: data) {
            //will the value always be a double?
            double difference = distance.applyAsDouble(duple.getFirst(), value);
            //use distance. Don't convert to double
            Duple<Double, L> difference_duple = new Duple<>(difference, duple.getSecond());
            if (list.size() < k) {
                list.add(difference_duple);
            }
            else {
                int biggest_spot = 0;
                int spot = 0;
                for (Duple<Double, L> list_duple: list) {
                    if (list_duple.getFirst() > list.get(biggest_spot).getFirst()) {
                        biggest_spot = spot;
                    }
                    spot += 1;
                }
                if (difference < list.get(biggest_spot).getFirst()) {
                    list.remove(biggest_spot);
                    list.add(difference_duple);
                }
            }
            /*how do I pick which labels to bump?
            How do I know which labels are associated with which value?
            I ended up using an ArrayList, and had to find the biggest value each time I went through the
            for loop, but I am not sure if this was the optimal solution. It works tho
             */
        }
        //bump labels
        for (Duple<Double, L> duple : list) {
            label_histogram.bump(duple.getSecond());
        }
        L node = label_histogram.getPluralityWinner();
//        System.out.println(list);
//        System.out.println(label_histogram);
//        System.out.println(node);
        return node;
    }

    @Override
    public void train(ArrayList<Duple<V, L>> training) {
        // TODO: Add all elements of training to data.
        data.addAll(training);
    }
}
