package learning.classifiers;

import core.Duple;
import learning.core.Classifier;
import learning.core.Histogram;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class NaiveBayes<V,L,F> implements Classifier<V,L> {
    // Each entry represents P(Feature | Label)
    // We want to know P(Label | Features). That means calculating P(Feature | Label) * P(Label) / P(Feature)
    // P(Feature) is invariant to the label and is generally ignored.
    // But we could do what we did with Markov chains, and calculate 1 / sum(P(Feature|Label)) for P(Label) / P(Feature)

    // Each entry represents P(Feature | Label)
    private LinkedHashMap<L,Histogram<F>> featuresByLabel = new LinkedHashMap<>();

    // Each entry represents P(Label)
    private Histogram<L> priors = new Histogram<>();

    // Given a value, this function returns a list of features and counts of those features.
    private Function<V,ArrayList<Duple<F,Integer>>> allFeaturesFrom;

    public NaiveBayes(Function<V,ArrayList<Duple<F,Integer>>> allFeaturesFrom) {
        this.allFeaturesFrom = allFeaturesFrom;
    }

    // In the training process, we accumulate data to calculate P(Feature), P(Label), and P(Feature | Label).
    // For each data item:
    // * Increment the prior count for the item's label.
    // * For each feature in the item (determined by calling allFeaturesFrom on the item's value)
    //   * Increment the feature count for the item's label by the number of appearances of the feature.
    @Override
    public void train(ArrayList<Duple<V, L>> data) {
        for (Duple<V, L> item: data) {
//            System.out.println("Priors: " + priors);
            //Update priors Histogram
            priors.bump(item.getSecond());
//            System.out.println("Label: " + item.getSecond());
//            System.out.println("Value: " + item.getFirst());
//            System.out.println("Features: " + allFeaturesFrom.apply(item.getFirst()));
            //for each feature
            for (Duple<F, Integer> feature: allFeaturesFrom.apply(item.getFirst())) {
                for (int i = 0; i < feature.getSecond(); i++) {
                    if (!featuresByLabel.containsKey(item.getSecond())) {
                        featuresByLabel.put(item.getSecond(), new Histogram<F>());
                    }
                    featuresByLabel.get(item.getSecond()).bump(feature.getFirst());
//                    System.out.println("Features by Label" + featuresByLabel);
                }
            }
        }
        // Your code here
    }

    // To classify:
    // * For each label:
    //   * Calculate the product of P(Label) * (P(Label | Feature) for all features)
    //     * In principle, we should divide by P(Feature). In practice, we don't, because it is the
    //       same value for all labels.
    // * Whichever label produces the highest product is the classification.
    @Override
    public L classify(V value) {
//        System.out.println("Value: " + value);
        L best = null;
        double best_value = 0;
        for (L label: priors) {
//            System.out.println("Label: " + label);
            ArrayList<Duple<F, Integer>> features = allFeaturesFrom.apply(value);
//            System.out.println(features);
            double p_label = priors.getPortionFor(label);
//            System.out.println("Label percentage?: " + p_label);
            double total = 1.0;
            for (Duple<F, Integer> feature: features) {
                // num: get feature + 1
                //den: get total histogram counts + 1
                //convert to doubles
                double numerator = featuresByLabel.get(label).getCountFor(feature.getFirst()) + 1;
                double denominator = featuresByLabel.get(label).getTotalCounts() + 1;
                double p_label_feature = numerator / denominator;
//                System.out.println("Feature: " + feature);
//                System.out.println("feature percentage?: " + p_label_feature);
                total *= p_label_feature;
//                System.out.println("Total: " + total);
            }
            total *= p_label;
//            System.out.println("Final Total: " + total);
            if (total > best_value) {
                best = label;
                best_value = total;

        }
        }
        // Your code here.
//        System.out.println("Returned: " + best);
        return best;
    }
}