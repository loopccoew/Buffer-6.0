import java.util.*;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;




class DecisionTree {
    String label;
    String feature;
    boolean isLeaf;


    Map<String, DecisionTree> children;  // Child nodes for each value of the feature

    public DecisionTree() {
        this.children = new HashMap<>();
        this.isLeaf = false;
    }

    public String getTarget(String[] targetList, int idx) {
        return targetList[idx];
    }

    public static int getCol(String feature, String[] attributes) {
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].equals(feature)) {
                return i;
            }
        }
        return -1;
    }

    // Function to build the decision tree recursively
    public static DecisionTree buildTree(double[][] dataset, List<String> features, String target, String[] targetsList, String[] attributes) {
        // If all data in the dataset have the same label, return a leaf node with that label
        int cols = dataset[0].length;
        if (areAllExamplesSameLabel(dataset, target)) {
            DecisionTree leaf = new DecisionTree();
            leaf.label = targetsList[(int) dataset[0][cols - 1]]; // All rows have the same target label
            leaf.isLeaf = true;
            return leaf;
        }

        // If no features are left, return the most common label in the dataset
        if (features.isEmpty()) {
            DecisionTree leaf = new DecisionTree();
            leaf.label = targetsList[(int) getMostCommonLabel(dataset, target)];
            leaf.isLeaf = true;
            return leaf;
        }

        // Calculate Information Gain for each feature and select the one with the highest gain
        String bestFeature = selectBestFeature(dataset, features, target,attributes);

        // Create the root node for the tree
        DecisionTree node = new DecisionTree();
        node.feature = bestFeature;
        // Get the possible values for the selected best feature
        Set<Double> featureValues = getFeatureValues(dataset, bestFeature,attributes);

        List<String> remainingFeatures = new ArrayList<>(features);
        remainingFeatures.remove(bestFeature);

        // Create child nodes for each value of the best feature
        for (Double value : featureValues) {
            // Filter the dataset to get rows where bestFeature equals value
            double[][] subset = filterDatasetByFeatureValue(dataset, getCol(bestFeature, attributes), value);
            // Recursively build the tree for the subset
            node.children.put(value + "", buildTree(subset, remainingFeatures, target, targetsList, attributes));
        }

        return node;
    }

    // Helper function to check if all examples have the same label
    private static boolean areAllExamplesSameLabel(double[][] dataset, String target) {
        int cols = dataset[0].length;
        double firstLabel = dataset[0][cols - 1];
        for (double[] row : dataset) {
            if (row[cols - 1] != firstLabel) {
                return false;
            }
        }
        return true;
    }

    // Helper function to get the most common label in the dataset
    private static double getMostCommonLabel(double[][] dataset, String target) {
        int cols = dataset[0].length;
        Map<Double, Integer> labelCount = new HashMap<>();
        for (double[] row : dataset) {
            Double label = Double.valueOf(row[cols - 1]);
            labelCount.put(label, Integer.valueOf(labelCount.getOrDefault(label, 0)) + 1);
        }

        // Find the most common label
        return labelCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    // Helper function to calculate the best feature based on Information Gain
    private static String selectBestFeature(double[][] dataset, List<String> features, String target, String[] attributes) {
        String bestFeature = null;
        double maxGain = Double.NEGATIVE_INFINITY;

        for (String feature : features) {
            double gain = calculateInformationGain(dataset, feature, target, attributes);
            if (gain > maxGain) {
                maxGain = gain;
                bestFeature = feature;
            }
        }

        return bestFeature;
    }

    // Helper function to calculate Information Gain for a feature
    private static double calculateInformationGain(double[][] dataset, String feature, String target, String[] attributes) {
        // Calculate the entropy of the dataset before the split (the whole dataset)
        double entropyBefore = calculateEntropy(dataset, target);

        // Get the values for the selected feature
        Set<Double> featureValues = getFeatureValues(dataset, feature, attributes);

        // Calculate the weighted entropy after the split
        double entropyAfter = 0.0;
        for (Double value : featureValues) {
            double[][] subset = filterDatasetByFeatureValue(dataset, getCol(feature, attributes), value);
            double weight = (double) subset.length / dataset.length;
            entropyAfter += weight * calculateEntropy(subset, target);
        }

        // Return the information gain (difference between entropy before and after the split)
        return entropyBefore - entropyAfter;
    }

    // Helper function to calculate entropy for a dataset
    private static double calculateEntropy(double[][] dataset, String target) {
        int cols = dataset[0].length;
        Map<Double, Integer> labelCount = new HashMap<>();
        for (double[] row : dataset) {
            Double label = (Double) row[cols - 1];
            labelCount.put(label, labelCount.getOrDefault(label, 0) + 1);
        }

        double entropy = 0.0;
        int total = dataset.length;
        for (int count : labelCount.values()) {
            double probability = (double) count / total;
            entropy -= probability * Math.log(probability) / Math.log(2);  // Base 2 log for entropy
        }

        return entropy;
    }

    // Helper function to get the possible values for a feature
    private static Set<Double> getFeatureValues(double[][] dataset, String feature, String[] attributes) { //3 hi honi everytime
        Set<Double> values = new HashSet<>();
        for (double[] row : dataset) {
            int col = getCol(feature, attributes);
            values.add(Double.valueOf(row[col]));
        }
        return values;
    }

    private static double[][] filterDatasetByFeatureValue(double[][] dataset, int splitIndex, double value) {

        List<List<Double>> subset = new ArrayList<>();

        for (double[] row : dataset) {

            if (row[splitIndex] == value) {

                List<Double> list = Arrays.stream(row).boxed().collect(Collectors.toList());
                subset.add(list);
            }
        }
        double[][] result = new double[subset.size()][];
        for (int i = 0; i < subset.size(); i++) {
            List<Double> inner = subset.get(i);
            result[i] = new double[inner.size()];
            for (int j = 0; j < inner.size(); j++) {
                result[i][j] = inner.get(j);
            }
        }
        return result;

    }
    public static String treeTraversal(double[] userResponses,DecisionTree root,String[] attributes)
    {
        if(root.isLeaf==true){
            return root.label;
        }

        String feature =root.feature;
        int idx=getCol(feature,attributes);
        double resp=userResponses[idx];
        return treeTraversal(userResponses,root.children.get(""+resp),attributes);

    }

}