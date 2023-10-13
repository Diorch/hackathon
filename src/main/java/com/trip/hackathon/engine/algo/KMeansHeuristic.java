package com.trip.hackathon.engine.algo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

public class KMeansHeuristic {
    
    // 定义问题的参数
    private static final int N = 1000; // 数据点数量
    private static final int K = 3; // 聚类中心数量
    
    // 定义数据点和聚类中心
    private static double[][] data = new double[N][2];
    private static double[][] centers = new double[K][2];
    
    // 定义聚类结果
    private static List<List<Integer>> clusters = new ArrayList<>();
    
    // 初始化数据点和聚类中心
    static {
        Random rand = new Random();
        for (int i = 0; i < N; i++) {
            data[i][0] = rand.nextDouble() * 10;
            data[i][1] = rand.nextDouble() * 10;
        }
        for (int i = 0; i < K; i++) {
            centers[i][0] = rand.nextDouble() * 10;
            centers[i][1] = rand.nextDouble() * 10;
        }
    }
    
    // 计算两个数据点之间的欧氏距离
    private static double distance(int i, int j) {
        double dx = data[i][0] - centers[j][0];
        double dy = data[i][1] - centers[j][1];
        return Math.sqrt(dx*dx + dy*dy);
    }
    
    // 计算每个数据点所属的簇
    private static void assignClusters() {
        clusters.clear();
        for (int i = 0; i < K; i++) {
            clusters.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < N; i++) {
            double minDistance = Double.MAX_VALUE;
            int minCenter = -1;
            for (int j = 0; j < K; j++) {
                double d = distance(i, j);
                if (d < minDistance) {
                    minDistance = d;
                    minCenter = j;
                }
            }
            clusters.get(minCenter).add(i);
        }
    }
    
    // 计算每个簇的平均值，更新聚类中心
    private static boolean updateCenters() {
        boolean changed = false;
        for (int i = 0; i < K; i++) {
            if (!clusters.get(i).isEmpty()) {
                double xSum = 0.0;
                double ySum = 0.0;
                for (int j : clusters.get(i)) {
                    xSum += data[j][0];
                    ySum += data[j][1];
                }
                double xAvg = xSum / clusters.get(i).size();
                double yAvg = ySum / clusters.get(i).size();
                if (xAvg != centers[i][0] || yAvg != centers[i][1]) {
                    centers[i][0] = xAvg;
                    centers[i][1] = yAvg;
                    changed = true;
                }
            }
        }
        return changed;
    }
    
    // 定义K-means问题
    private static class KMeansProblem extends AbstractDoubleProblem {
        public KMeansProblem() {
            setNumberOfVariables(3);
            setNumberOfObjectives(1);
            setNumberOfConstraints(0);
            setName("KMeans");
        }
        @Override
        public void evaluate(DoubleSolution solution) {
            int maxIter = (int) Math.round(solution.getVariableValue(0) * 100);
            centers[0][0] = solution.getVariableValue(1) * 10;
            centers[0][1] = solution.getVariableValue(2) * 10;
            centers[1][0] = solution.getVariableValue(1) * 5;
            centers[1][1] = solution.getVariableValue(2) * 5;
            centers[2][0] = solution.getVariableValue(1) * 2;
            centers[2][1] = solution.getVariableValue(2) * 2;
            for (int i = 0; i < maxIter; i++) {
                assignClusters();
                boolean changed = updateCenters();
                if (!changed) {
                    break;
                }
            }
            double error = 0.0;
            for (int i = 0; i < K; i++) {
                for (int j : clusters.get(i)) {
                    error += distance(j, i);
                }
            }
            solution.setObjective(0, error);
        }
        @Override
        public DoubleSolution createSolution() {
            return newDefaultSolution();
        }
    }
    
    // 主函数
    public static void main(String[] args) {
        KMeansProblem problem = new KMeansProblem();
        int maxEvaluations = 1000;
        int swarmSize = 100;
        int archiveSize = 100;
        double perturbationIndex = 0.5;
        HyperheuristicRunner runner = new HyperheuristicRunner(problem, maxEvaluations, swarmSize, archiveSize, perturbationIndex);
        runner.run();
        DoubleSolution solution = runner.getBestSolution();
        int maxIter = (int) Math.round(solution.getVariableValue(0) * 100);
        centers[0][0] = solution.getVariableValue(1) * 10;
        centers[0][1] = solution.getVariableValue(2) * 10;
        centers[1][0] = solution.getVariableValue(1) * 5;
        centers[1][1] = solution.getVariableValue(2) * 5;
        centers[2][0] = solution.getVariableValue(1) * 2;
        centers[2][1] = solution.getVariableValue(2) * 2;
        for (int i = 0; i < maxIter; i++) {
            assignClusters();
            updateCenters();
        }
        System.out.println("聚类结果：");
        for (int i = 0; i < K; i++) {
            String clusterStr = clusters.get(i).stream().map(j -> "(" + data[j][0] + ", " + data[j][1] + ")").collect(Collectors.joining(", "));
            System.out.println("簇" + (i+1) + ": " + clusterStr);
        }
    }
}