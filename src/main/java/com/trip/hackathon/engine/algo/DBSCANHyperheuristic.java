package com.trip.hackathon.engine.algo;

import org.uma.jmetal.problem.doubleproblem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DBSCANHyperheuristic {

    // 定义问题的参数
    private static final int N = 1000; // 数据点数量
    private static final double epsilonMin = 0.1; // 邻域半径的最小值
    private static final double epsilonMax = 2.0; // 邻域半径的最大值
    private static final int minPtsMin = 2; // 最小簇大小的最小值
    private static final int minPtsMax = 20; // 最小簇大小的最大值

    // 定义数据点
    private static double[][] data = new double[N][2];

    // 定义聚类结果
    private static List<List<Integer>> clusters = new ArrayList<>();

    // 初始化数据点
    static {
        Random rand = new Random();
        for (int i = 0; i < N; i++) {
            data[i][0] = rand.nextDouble() * 10;
            data[i][1] = rand.nextDouble() * 10;
        }
    }

    // 计算两个数据点之间的欧氏距离
    private static double distance(int i, int j) {
        double dx = data[i][0] - data[j][0];
        double dy = data[i][1] - data[j][1];
        return Math.sqrt(dx * dx + dy * dy);
    }

    // 执行DBSCAN算法
    private static void dbscan(double epsilon, int minPts) {
        int[] visited = new int[N];
        for (int i = 0; i < N; i++) {
            if (visited[i] == 0) {
                visited[i] = 1;
                List<Integer> neighborPts = regionQuery(i, epsilon);
                if (neighborPts.size() < minPts) {
                    visited[i] = 2;
                }
                else {
                    List<Integer> cluster = new ArrayList<>();
                    cluster.add(i);
                    clusters.add(cluster);
                    expandCluster(i, neighborPts, cluster, visited, epsilon, minPts);
                }
            }
        }
    }

    // 计算数据点i的邻域
    private static List<Integer> regionQuery(int i, double epsilon) {
        List<Integer> neighborPts = new ArrayList<>();
        for (int j = 0; j < N; j++) {
            if (i != j && distance(i, j) <= epsilon) {
                neighborPts.add(j);
            }
        }
        return neighborPts;
    }

    // 将当前数据点的簇扩展到其邻域
    private static void expandCluster(int i, List<Integer> neighborPts, List<Integer> cluster, int[] visited,
            double epsilon, int minPts) {
        for (int j : neighborPts) {
            if (visited[j] == 0) {
                visited[j] = 1;
                List<Integer> neighborPts2 = regionQuery(j, epsilon);
                if (neighborPts2.size() >= minPts) {
                    neighborPts.addAll(neighborPts2);
                }
            }
            if (visited[j] != 2 && !cluster.contains(j)) {
                cluster.add(j);
                clusters.get(clusters.size() - 1).add(j);
            }
        }
    }

    // 定义DBSCAN问题
    private static class DBSCANProblem extends AbstractDoubleProblem {
        public DBSCANProblem() {
            setNumberOfVariables(2);
            setNumberOfObjectives(2);
            setNumberOfConstraints(0);
            setName("DBSCAN");
        }

        @Override
        public void evaluate(DoubleSolution solution) {
            double epsilon = solution.getVariableValue(0) * (epsilonMax - epsilonMin) + epsilonMin;
            int minPts = (int) Math.round(solution.getVariableValue(1) * (minPtsMax - minPtsMin)) + minPtsMin;
            clusters.clear();
            dbscan(epsilon, minPts);
            double numClusters = (double) clusters.size();
            double clusterSize = clusters.stream().mapToDouble(cluster -> (double) cluster.size()).average()
                    .orElse(0.0);
            solution.setObjective(0, numClusters);
            solution.setObjective(1, -clusterSize);
        }

        @Override
        public DoubleSolution createSolution() {
            return new DefaultSolution();
        }
    }

    // 主函数
    public static void main(String[] args) {
        DBSCANProblem problem = new DBSCANProblem();
        int maxEvaluations = 1000;
        int swarmSize = 100;
        int archiveSize = 100;
        double perturbationIndex = 0.5;
        HyperheuristicRunner runner = new HyperheuristicRunner(problem, maxEvaluations, swarmSize, archiveSize, perturbationIndex);
        runner.run();
        DoubleSolution solution = runner.getBestSolution();
        double epsilon = solution.getVariableValue(0) * (epsilonMax - epsilonMin) + epsilonMin;
        int minPts = (int) Math.round(solution.getVariableValue(1) * (minPtsMax - minPtsMin)) + minPtsMin;
        clusters.clear();
        dbscan(epsilon, minPts);
        System.out.println("聚类结果：");
        for (int i = 0; i < clusters.size(); i++) {
            String clusterStr = clusters.get(i).stream().map(j -> "(" + data[j][0] + ", " + data[j][1] + ")").collect(Collectors.joining(", "));
            System.out.println("簇" + (i + 1) + ": " + clusterStr);
        }
    }
}