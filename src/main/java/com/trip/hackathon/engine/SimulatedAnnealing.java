package com.trip.hackathon.engine;

import java.util.Random;

public class SimulatedAnnealing {


        // 定义要求解的目标函数
        public static double objectiveFunction(double x, double y) {
            return Math.sin(5 * Math.PI * x) * Math.sin(5 * Math.PI * y);
        }

        // 定义模拟退火算法的主函数
        public static void main(String[] args) {
            double t = 1000; // 初始温度
            double alpha = 0.95; // 降温速率
            double minT = 1e-8; // 终止温度
            double x = 0.5, y = 0.5; // 初始状态
            double bestX = x, bestY = y; // 记录最优解

            Random rand = new Random(); // 创建随机数生成器

            while (t > minT) {
                // 生成新状态
                double newX = x + rand.nextDouble() * 0.1 - 0.05;
                double newY = y + rand.nextDouble() * 0.1 - 0.05;

                // 计算新状态的目标函数值和当前状态的目标函数值
                double delta = objectiveFunction(newX, newY) - objectiveFunction(x, y);

                if (delta > 0) {
                    // 如果新状态更好，则接受新状态
                    x = newX;
                    y = newY;
                    if (objectiveFunction(x, y) > objectiveFunction(bestX, bestY)) {
                        // 记录最优解
                        bestX = x;
                        bestY = y;
                    }
                } else {
                    // 如果新状态更差，则以一定概率接受新状态
                    double p = Math.exp(delta / t);
                    if (rand.nextDouble() < p) {
                        x = newX;
                        y = newY;
                    }
                }

                // 降温
                t *= alpha;
            }

            System.out.printf("找到最大值：%.4f，最优解为：(%.4f, %.4f)\n", objectiveFunction(bestX, bestY), bestX, bestY);
        }

}
