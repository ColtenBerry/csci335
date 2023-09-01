package learning.markov;

import learning.core.Histogram;

import java.util.*;

public class MarkovChain<L,S> {
    private LinkedHashMap<L, HashMap<Optional<S>, Histogram<S>>> label2symbol2symbol = new LinkedHashMap<>();

    public Set<L> allLabels() {return label2symbol2symbol.keySet();}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (L language: label2symbol2symbol.keySet()) {
            sb.append(language);
            sb.append('\n');
            for (Map.Entry<Optional<S>, Histogram<S>> entry: label2symbol2symbol.get(language).entrySet()) {
                sb.append("    ");
                sb.append(entry.getKey());
                sb.append(":");
                sb.append(entry.getValue().toString());
                sb.append('\n');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // Increase the count for the transition from prev to next.
    // Should pass SimpleMarkovTest.testCreateChains().
    public void count(Optional<S> prev, L label, S next) {
        // TODO: YOUR CODE HERE
        if (label2symbol2symbol.get(label) == null) {
            HashMap<Optional<S>, Histogram<S>> hashmap = new HashMap<>();
            Histogram<S> histogram = new Histogram<>();
            hashmap.put(prev, histogram);
            histogram.bump(next);
            label2symbol2symbol.put(label, hashmap);
        }
        else if (label2symbol2symbol.get(label).get(prev) == null) {
            Histogram<S> histogram = new Histogram<>();
            label2symbol2symbol.get(label).put(prev, histogram);
            label2symbol2symbol.get(label).get(prev).bump(next);
        }
        else {
            label2symbol2symbol.get(label).get(prev).bump(next);
        }

    }

    // Returns P(sequence | label)
    // Should pass SimpleMarkovTest.testSourceProbabilities() and MajorMarkovTest.phraseTest()
    //
    // HINT: Be sure to add 1 to both the numerator and denominator when finding the probability of a
    // transition. This helps avoid sending the probability to zero.
    public double probability(ArrayList<S> sequence, L label) {
        double denominator;
        double numerator;
        double portion = 1.0;
        double new_portion;
//        System.out.println(label2symbol2symbol);
//        System.out.println(label);
        S prev = null;
        for (S symbol: sequence) {
//            System.out.println(prev);
//            System.out.println(symbol);
            /*I believe the following line does not work because symbol is S, while I need Optional<S>
            fixed by Optional.of()
            */
            //seems to be a problem with f and g in english
            if (prev == null) {
//                System.out.println("null");
                prev = symbol;
            }
            else if (label2symbol2symbol.get(label).get(Optional.of(prev)) == null) {
//                System.out.println("null i guess-------------------------------------------");
                prev = symbol;
            }
            else {
                numerator = label2symbol2symbol.get(label).get(Optional.of(prev)).getCountFor(symbol) + 1;
//                System.out.println(numerator);
                denominator = label2symbol2symbol.get(label).get(Optional.of(prev)).getTotalCounts() + 1;
//                System.out.println(denominator);
                new_portion = numerator / denominator;
                portion *= new_portion;
                prev = symbol;
            }
        }
//        System.out.println(portion);
        return portion;
    }

    // Return a map from each label to P(label | sequence).
    // Should pass MajorMarkovTest.testSentenceDistributions()
    public LinkedHashMap<L,Double> labelDistribution(ArrayList<S> sequence) {
        // TODO: YOUR CODE HERE
        LinkedHashMap<L, Double> hashMap = new LinkedHashMap<>();
        double newPortion;
        double total = 0.0;
        double percentage;
        for (L label: label2symbol2symbol.keySet()) {
//            System.out.println(label);
            newPortion = probability(sequence, label);
            total += newPortion;
            hashMap.put(label, newPortion);
        }
        for (L label: label2symbol2symbol.keySet()) {
            percentage = hashMap.get(label) / total;
            hashMap.replace(label, hashMap.get(label), percentage);
        }
        return hashMap;
    }

    // Calls labelDistribution(). Returns the label with highest probability.
    // Should pass MajorMarkovTest.bestChainTest()
    public L bestMatchingChain(ArrayList<S> sequence) {
        // TODO: YOUR CODE HERE
        LinkedHashMap<L, Double> hashmap = labelDistribution(sequence);
        L highest = null;
        L challenger;
        for (L label: hashmap.keySet()) {
            challenger = label;
            if (highest == null) {
                highest = challenger;
            }
            else if (hashmap.get(challenger) > hashmap.get(highest)) {
                highest = challenger;
            }
        }
        return highest;
    }
}
