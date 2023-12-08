package learning.sentiment.learners;

import learning.classifiers.Knn;
import learning.core.Histogram;

public class Knn10 extends Knn<Histogram<String>,String>  {
    public Knn10() {
        super(10, Histogram::cosineDistance);
    }
}
