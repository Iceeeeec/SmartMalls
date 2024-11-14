package com.hsasys;

import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
@CrossOrigin
public class DecisionTree {
    public static class TreeNode {
        public String label;
        HashMap<Object, TreeNode> children;

        public TreeNode(String label) {
            this.label = label;
            this.children = new HashMap<>();
        }
    }
//featLabels 构建的决策树的根节点
    public static void main(String[] args) {
        ArrayList<Object[]> dataSet = createDataSet();
        ArrayList<String> labels = createLabels();
        ArrayList<String> featLabels = new ArrayList<>();
        TreeNode decisionTree = createTree(dataSet, labels, featLabels);
        System.out.println(decisionTree);
        System.out.println(decisionTree.label);
    }

    public static ArrayList<Object[]> createDataSet() {
        ArrayList<Object[]> dataSet = new ArrayList<>();
        dataSet.add(new Object[]{0, 0, 0, 0, "no"});
        dataSet.add(new Object[]{0, 0, 0, 1, "no"});
        dataSet.add(new Object[]{0, 1, 0, 1, "yes"});
        dataSet.add(new Object[]{0, 1, 1, 0, "yes"});
        dataSet.add(new Object[]{0, 0, 0, 0, "no"});
        dataSet.add(new Object[]{1, 0, 0, 0, "no"});
        dataSet.add(new Object[]{1, 0, 0, 1, "no"});
        dataSet.add(new Object[]{1, 1, 1, 1, "yes"});
        dataSet.add(new Object[]{1, 0, 1, 2, "yes"});
        dataSet.add(new Object[]{1, 0, 1, 2, "yes"});
        dataSet.add(new Object[]{2, 0, 1, 2, "yes"});
        dataSet.add(new Object[]{2, 0, 1, 1, "yes"});
        dataSet.add(new Object[]{2, 1, 0, 1, "yes"});
        dataSet.add(new Object[]{2, 1, 0, 2, "yes"});
        dataSet.add(new Object[]{2, 0, 0, 0, "no"});
        return dataSet;
    }

    public static ArrayList<String> createLabels() {
        ArrayList<String> labels = new ArrayList<>();
        labels.add("水果");
        labels.add("甜品");
        labels.add("烧烤");
        labels.add("零食");
        labels.add("爱运动");
        return labels;
    }

    public static TreeNode createTree(ArrayList<Object[]> dataSet, ArrayList<String> labels, ArrayList<String> featLabels) {
        ArrayList<Object> classList = new ArrayList<>();
        for (Object[] example : dataSet) {
            classList.add(example[example.length - 1]);
        }
        if (classList.stream().distinct().count() == 1) {
            return new TreeNode((String) classList.get(0));
        }
        if (dataSet.get(0).length == 1) {
            return new TreeNode(majorityCnt(classList));
        }
        int bestFeat = chooseBestFeatureToSplit(dataSet);
        String bestLabel = labels.get(bestFeat);
        featLabels.add(bestLabel);
        TreeNode myTree = new TreeNode(bestLabel);

        labels.remove(bestFeat);
        HashSet<Object> featValues = new HashSet<>();
        for (Object[] example : dataSet) {
            featValues.add(example[bestFeat]);
        }
        for (Object value : featValues) {
            ArrayList<String> subLabels = new ArrayList<>(labels);
            myTree.children.put(value, createTree(splitDataSet(dataSet, bestFeat, value), subLabels, featLabels));
        }
        return myTree;
    }
    public static String majorityCnt(ArrayList<Object> classList) {
        HashMap<Object, Integer> classCount = new HashMap<>();
        for (Object vote : classList) {
            classCount.put(vote, classCount.getOrDefault(vote, 0) + 1);
        }
        Object majority = null;
        int maxCount = Integer.MIN_VALUE;
        for (Object key : classCount.keySet()) {
            int count = classCount.get(key);
            if (count > maxCount) {
                maxCount = count;
                majority = key;
            }
        }
        assert majority != null;
        return (String) majority;
    }
    public static int chooseBestFeatureToSplit(ArrayList<Object[]> dataSet) {
        int numFeatures = dataSet.get(0).length - 1;
        double baseEntropy = calcShannonEnt(dataSet);
        double bestInfoGain = 0;
        int bestFeature = -1;
        for (int i = 0; i < numFeatures; i++) {
            ArrayList<Object> featList = new ArrayList<>();
            for (Object[] example : dataSet) {
                featList.add(example[i]);
            }
            HashSet<Object> uniqueVals = new HashSet<>(featList);
            double newEntropy = 0;
            for (Object val : uniqueVals) {
                ArrayList<Object[]> subDataSet = splitDataSet(dataSet, i, val);
                double prop = (double) subDataSet.size() / dataSet.size();
                newEntropy += prop * calcShannonEnt(subDataSet);
            }
            double infoGain = baseEntropy - newEntropy;
            if (infoGain > bestInfoGain) {
                bestInfoGain = infoGain;
                bestFeature = i;
            }
        }
        return bestFeature;
    }
    public static ArrayList<Object[]> splitDataSet(ArrayList<Object[]> dataSet, int axis, Object val) {
        ArrayList<Object[]> retDataSet = new ArrayList<>();
        for (Object[] feature : dataSet) {
            if (feature[axis].equals(val)) {
                Object[] reducedFeatVec = new Object[feature.length - 1];
                System.arraycopy(feature, 0, reducedFeatVec, 0, axis);
                System.arraycopy(feature, axis + 1, reducedFeatVec, axis, feature.length - axis - 1);
                retDataSet.add(reducedFeatVec);
            }
        }
        return retDataSet;
    }
    public static double calcShannonEnt(ArrayList<Object[]> dataSet) {
        int numExamples = dataSet.size();
        HashMap<Object, Integer> labelsCount = new HashMap<>();

        for (Object[] featVec : dataSet) {
            Object label = featVec[featVec.length - 1];
            labelsCount.put(label, labelsCount.getOrDefault(label, 0) + 1);
        }
        double shannonEnt = 0;
        for (int count : labelsCount.values()) {
            double prop = (double) count / numExamples;
            shannonEnt -= prop * log2(prop);
        }
        return shannonEnt;
    }
    public static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }
}
