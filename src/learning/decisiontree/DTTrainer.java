package learning.decisiontree;

import core.Duple;
import learning.core.Histogram;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DTTrainer<V,L, F, FV extends Comparable<FV>> {
	private ArrayList<Duple<V,L>> baseData;
	private boolean restrictFeatures;
	private Function<ArrayList<Duple<V,L>>, ArrayList<Duple<F,FV>>> allFeatures;
	private BiFunction<V,F,FV> getFeatureValue;
	private Function<FV,FV> successor;
	
	public DTTrainer(ArrayList<Duple<V, L>> data, Function<ArrayList<Duple<V, L>>, ArrayList<Duple<F,FV>>> allFeatures,
					 boolean restrictFeatures, BiFunction<V,F,FV> getFeatureValue, Function<FV,FV> successor) {
		baseData = data;
		this.restrictFeatures = restrictFeatures;
		this.allFeatures = allFeatures;
		this.getFeatureValue = getFeatureValue;
		this.successor = successor;
	}
	
	public DTTrainer(ArrayList<Duple<V, L>> data, Function<ArrayList<Duple<V,L>>, ArrayList<Duple<F,FV>>> allFeatures,
					 BiFunction<V,F,FV> getFeatureValue, Function<FV,FV> successor) {
		this(data, allFeatures, false, getFeatureValue, successor);
	}

	// TODO: Call allFeatures.apply() to get the feature list. Then shuffle the list, retaining
	//  only targetNumber features. Should pass DTTest.testReduced().
	public static <V,L, F, FV  extends Comparable<FV>> ArrayList<Duple<F,FV>>
	reducedFeatures(ArrayList<Duple<V,L>> data, Function<ArrayList<Duple<V, L>>, ArrayList<Duple<F,FV>>> allFeatures,
					int targetNumber) {
		ArrayList<Duple<F, FV>> feature_list = allFeatures.apply(data);
		Collections.shuffle(feature_list);
		while (feature_list.size() > targetNumber) {
			feature_list.remove(0);
		}
		return feature_list;
    }
	
	public DecisionTree<V,L,F,FV> train() {
		return train(baseData);
	}

	public static <V,L> int numLabels(ArrayList<Duple<V,L>> data) {
		return data.stream().map(Duple::getSecond).collect(Collectors.toUnmodifiableSet()).size();
	}
	
	private DecisionTree<V,L,F,FV> train(ArrayList<Duple<V,L>> data) {
		// TODO: Implement the decision tree learning algorithm
		int targetNumber = data.size();
//		System.out.println(allFeatures.apply(data));
//		System.out.println(getFeatureValue.apply(value, feature));
//		System.out.println(successor.apply(feature_value));
//		System.out.println(baseData);
		ArrayList<Duple<F, FV>> features;
		if (numLabels(data) == 1) {
			// TODO: Return a leaf node consisting of the only label in data
			DTLeaf<V, L, F, FV> leaf = new DTLeaf<>(data.get(0).getSecond());
			return leaf;
		}
		else {
			if (!restrictFeatures) {
				features = allFeatures.apply(data);
			}
			else {
				features = reducedFeatures(data, allFeatures, targetNumber);
			}
			double highest_gain = Double.NEGATIVE_INFINITY;
			Duple<F, FV> highest_gain_feature = null;
			Duple<ArrayList<Duple<V, L>>, ArrayList<Duple<V, L>>> best_split_data = null;
			for (Duple<F, FV> duple: features) {
//				System.out.println(duple);
				Duple<ArrayList<Duple<V, L>>, ArrayList<Duple<V, L>>> split_data = splitOn(data, duple.getFirst(), duple.getSecond(), getFeatureValue);
				ArrayList<Duple<V, L>> list1 = split_data.getFirst();
				ArrayList<Duple<V, L>> list2 = split_data.getSecond();
				double gain_value = gain(data, list1, list2);
				if (gain_value > highest_gain) {
					highest_gain = gain_value;
					highest_gain_feature = duple;
					best_split_data = split_data;
				}
//				System.out.println("Gain Value: " + gain_value);

				//where do recursively created left/right nodes come from?
//				DTInterior<V, L, F, FV> interior = new DTInterior<>(duple.getFirst(), duple.getSecond(), left, right, getFeatureValue, successor);
			}

			// if the best-first size is zero:
			// * Create a histogram of the labels in the other side
			// * Use getPluralityFor() to find the leaf label
			// if the best-second size is zero:
			// * Do the same thing with the first side.
//			if (best_split_data.getFirst() == null) {
//				System.out.println("split is null");
//			}
			if (best_split_data.getFirst().size() == 0) {
//				System.out.println("Thing is activated");
				Histogram<L> gram = new Histogram<L>();
				for (Duple<V, L> duple: best_split_data.getSecond()) {
					gram.bump(duple.getSecond());
				}
				L label = gram.getPluralityWinner();
				DTLeaf<V, L, F, FV> leaf = new DTLeaf<>(label);
				return leaf;
			}
			else if (best_split_data.getSecond().size() == 0) {
				Histogram<L> gram = new Histogram<>();
				for (Duple<V, L> duple: best_split_data.getFirst()) {
					gram.bump(duple.getSecond());
				}
				L label = gram.getPluralityWinner();
				DTLeaf<V, L, F, FV> leaf = new DTLeaf<>(label);
				return leaf;
			}
			DecisionTree<V,L,F,FV> leftTree = train(best_split_data.getFirst());
			DecisionTree<V,L,F,FV> rightTree = train(best_split_data.getSecond());
			DTInterior<V, L, F, FV> interior = new DTInterior<>(highest_gain_feature.getFirst(), highest_gain_feature.getSecond(), leftTree, rightTree, getFeatureValue, successor);
			// TODO: Return an interior node.
			//  If restrictFeatures is false, call allFeatures.apply() to get a complete list
			//  of features and values, all of which you should consider when splitting.
			//  If restrictFeatures is true, call reducedFeatures() to get sqrt(# features)
			//  of possible features/values as candidates for the split. In either case,
			//  for each feature/value combination, use the splitOn() function to break the
			//  data into two parts. Then use gain() on each split to figure out which
			//  feature/value combination has the highest gain. Use that combination, as
			//  well as recursively created left and right nodes, to create the new
			//  interior node.
			//  Note: It is possible for the split to fail; that is, you can have a split
			//  in which one branch has zero elements. In this case, return a leaf node
			//  containing the most popular label in the branch that has elements.
			return interior;
		}		
	}

	public static <V,L> L mostPopularLabelFrom(ArrayList<Duple<V, L>> data) {
		Histogram<L> h = new Histogram<>();
		for (Duple<V,L> datum: data) {
			h.bump(datum.getSecond());
		}
		return h.getPluralityWinner();
	}

	// TODO: Generates a new data set by sampling randomly with replacement. It should return
	//    an `ArrayList` that is the same length as `data`, where each element is selected randomly
	//    from `data`. Should pass `DTTest.testResample()`.
	public static <V,L> ArrayList<Duple<V,L>> resample(ArrayList<Duple<V,L>> data) {
		//wouldn't this mean that its just data reordered?
		//new arraylist, fill randomly. Can use inputs more than once.
		ArrayList<Duple<V, L>> newList = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < data.size(); i++) {
			Duple<V, L> obj = data.get(random.nextInt(data.size()));
			newList.add(obj);
		}
		return newList;
	}

	public static <V,L> double getGini(ArrayList<Duple<V,L>> data) {
		// TODO: Calculate the Gini coefficient:
		//  For each label, calculate its portion of the whole (p_i).
		//  Use of a Histogram<L> for this purpose is recommended.
		//  Gini coefficient is 1 - sum(for all labels i, p_i^2)
		//  Should pass DTTest.testGini().
		Histogram<L> label_histogram = new Histogram<>();
		ArrayList<L> label_list = new ArrayList<>();
		double result = 0;
		for (Duple<V, L> duple: data) {
			L label = duple.getSecond();
			label_histogram.bump(label);
			if (!label_list.contains(label)) {
				label_list.add(label);
			}
		}
		for (L label: label_list) {
			result += (label_histogram.getPortionFor(label) * label_histogram.getPortionFor(label));
		}

		return 1 - result;
	}

	public static <V,L> double gain(ArrayList<Duple<V,L>> parent, ArrayList<Duple<V,L>> child1,
									ArrayList<Duple<V,L>> child2) {
		// TODO: Calculate the gain of the split. Add the gini values for the children.
		//  Subtract that sum from the gini value for the parent. Should pass DTTest.testGain().
		double p = getGini(parent);
		double c1 = getGini(child1);
		double c2 = getGini(child2);
		return p - (c1 + c2);
	}

	public static <V,L, F, FV  extends Comparable<FV>> Duple<ArrayList<Duple<V,L>>,ArrayList<Duple<V,L>>> splitOn
			(ArrayList<Duple<V,L>> data, F feature, FV featureValue, BiFunction<V,F,FV> getFeatureValue) {
		// TODO:
		//  Returns a duple of two new lists of training data.
		//  The first returned list should be everything from this set for which
		//  feature has a value less than or equal to featureValue. The second
		//  returned list should be everything else from this list.
		//  Should pass DTTest.testSplit().
		ArrayList<Duple<V, L>> list1 = new ArrayList<>();
		ArrayList<Duple<V, L>> list2 = new ArrayList<>();
		for (Duple<V, L> item: data) {
			FV fv = getFeatureValue.apply(item.getFirst(), feature);
			double result = featureValue.compareTo(fv);
			if (result >= 0) {
				list1.add(item);
			}
			else {
				list2.add(item);
			}
		}
		Duple<ArrayList<Duple<V, L>>, ArrayList<Duple<V, L>>> thing = new Duple<>(list1, list2);
		return thing;
	}
}
