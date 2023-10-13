package com.trip.hackathon.engine.algo;

import java.util.Arrays;
import java.util.Random;

public class AntColonyOptimization {
    
    // 定义问题的参数
    private static final int N = 50; // 城市数量
    private static final double alpha = 1.0; // 信息素重要程度因子
    private static final double beta = 2.0; // 启发式因子
    private static final double rho = 0.5; // 信息素挥发因子
    private static final int antNum = 20; // 蚂蚁数量
    private static final int maxIter = 100; // 最大迭代次数
    
    // 定义城市距离矩阵
    private static double[][] distance = new double[N][N];
    
    // 定义信息素矩阵
    private static double[][] tau = new double[N][N];
    
    // 定义蚂蚁的位置矩阵
    private static int[][] ants = new int[antNum][N];
    
    // 定义每个蚂蚁的路径长度
    private static double[] pathLen = new double[antNum];
    
    // 定义全局最优路径和长度
    private static int[] bestTour = new int[N];
    private static double bestLength = Double.MAX_VALUE;
    
    // 初始化城市距离矩阵和信息素矩阵
    static {
        Random rand = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == j) {
                    distance[i][j] = 0.0;
                    tau[i][j] = 0.0;
                } else {
                    distance[i][j] = rand.nextDouble() * 100;
                    tau[i][j] = 1.0;
                }
            }
        }
    }
    
    // 计算两个城市之间的信息素转移概率
    private static double prob(int i, int j) {
        double numerator = Math.pow(tau[i][j], alpha) * Math.pow(1.0 / distance[i][j], beta);
        double denominator = 0.0;
        for (int k = 0; k < N; k++) {
            if (k != j && ants[0][k] != 1) {
                denominator += Math.pow(tau[i][k], alpha) * Math.pow(1.0 / distance[i][k], beta);
            }
        }
        return numerator / denominator;
    }
    
    // 让所有蚂蚁进行一次完整的遍历
    private static void antTour() {
        Random rand = new Random();
        for (int i = 0; i < antNum; i++) {
            // 随机选择起点城市
            int start = rand.nextInt(N);
            int current = start;
            // 将起点城市标记为已经访问过
            ants[i][start] = 1;
            // 按照信息素转移概率选择下一个城市
            for (int j = 1; j < N; j++) {
                double probSum = 0.0;
                for (int k = 0; k < N; k++) {
                    if (ants[i][k] != 1) {
                        probSum += prob(current, k);
                    }
                }
                double r = rand.nextDouble();
                double probCum = 0.0;
                for (int k = 0; k < N; k++) {
                    if (ants[i][k] != 1) {
                        probCum += prob(current, k) / probSum;
                        if (r <= probCum) {
                            current = k;
                            ants[i][current] = 1;
                            break;
                        }
                    }
                }
            }
            // 计算路径长度
            double len = 0.0;
            for (int j = 0; j < N - 1; j++) {
                len += distance[ants[i][j]][ants[i][j+1]];
            }
            len += distance[ants[i][N-1]][ants[i][0]];
            pathLen[i] = len;
            // 更新全局最优路径和长度
            if (len < bestLength) {
                bestLength = len;
                for (int j = 0; j < N; j++) {
                    bestTour[j] = ants[i][j];
                }
            }
            // 重置蚂蚁的位置
            for (int j = 0; j < N; j++) {
                ants[i][j] = 0;
            }
        }
    }
    
    // 更新信息素矩阵
    private static void updateTau() {
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                double deltaTau = 0.0;
                for (int k = 0; k < antNum; k++) {
                    deltaTau += 1.0 / pathLen[k] * (ants[k][i] == 1 && ants[k][j] == 1 ? 1.0 : 0.0);
                }
                tau[i][j] = (1.0 - rho) * tau[i][j] + deltaTau;
                tau[j][i] = tau[i][j];
            }
        }
    }
    
    // 主函数
    public static void main(String[] args) {
        for (int i = 0; i < maxIter; i++) {
            antTour();
            updateTau();
        }
        System.out.println("找到最短路径长度：" + bestLength);
        System.out.println("最优路径： " + Arrays.toString(bestTour));
    }
}