package learning.sentiment.learners;

import learning.core.Histogram;
import learning.decisiontree.RandomForest;
import learning.sentiment.core.SentimentAnalyzer;

public class SentimentForest45 extends RandomForest<Histogram<String>, String, String, Integer> {
    public SentimentForest45() {
        super(45, SentimentAnalyzer::allFeatures, Histogram::getCountFor, i -> i + 1);
    }
}
