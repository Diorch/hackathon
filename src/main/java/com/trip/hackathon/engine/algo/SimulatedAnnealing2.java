package com.trip.hackathon.engine.algo;

import java.util.Random;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;


public class SimulatedAnnealing2 {


    // 定义问题的参数
    private static final double T0 = 100; // 初始温度
    private static final double Tf = 0; // 最小温度
    private static final double alpha = 0.99; // 冷却速率
    private static final int maxIter = 1000; // 每个温度下的最大迭代次数

    // 定义状态和能量函数
    private static double x = 0.5;
    private static double y = 0.5;
    private static double energy = calculateEnergy(x, y);

    // 定义随机数生成器
    private static Random rand = new Random();

    // 计算状态的能量
    private static double calculateEnergy(double x, double y) {
        return Math.sin(x) * Math.cos(y) - 0.5 * Math.exp(Math.abs(1 - Math.sqrt(x*x + y*y)/Math.PI));
    }

    // 计算新状态的能量差
    private static double calculateDeltaEnergy(double x, double y, double dx, double dy) {
        double energy1 = calculateEnergy(x, y);
        double energy2 = calculateEnergy(x+dx, y+dy);
        return energy2 - energy1;
    }

    // 随机转移状态
    private static void randomMove() {
        double dx = rand.nextDouble() - 0.5;
        double dy = rand.nextDouble() - 0.5;
        double p = rand.nextDouble();
        double deltaEnergy = calculateDeltaEnergy(x, y, dx, dy);
        if (deltaEnergy < 0 || p < Math.exp(-deltaEnergy/T)) {
            x += dx;
            y += dy;
            energy += deltaEnergy;
        }
    }

    // 定义问题
    private static class Problem extends AbstractDoubleProblem {
        public Problem() {
            setNumberOfVariables(0);
            setNumberOfObjectives(1);
            setNumberOfConstraints(0);
            setName("SimulatedAnnealing");
        }
        @Override
        public void evaluate(DoubleSolution solution) {
            T = T0;
            while (T > Tf) {
                for (int i = 0; i < maxIter; i++) {
                    randomMove();
                }
                T *= alpha;
            }
            solution.setObjective(0, -energy); // 目标函数为最大化能量
        }
        @Override
        public DoubleSolution createSolution() {
            return newDefaultSolution();
        }
    }

    // 定义温度和当前状态
    private static double T;
    private static double bestEnergy = energy;
    private static double bestX = x;
    private static double bestY = y;

    // 主函数
    public static void main(String[] args) {
        Problem problem = new Problem();
        int maxEvaluations = 1;
        int swarmSize = 1;
        int archiveSize = 1;
        double perturbationIndex = 0.5;
        HyperheuristicRunner runner = new HyperheuristicRunner(problem, maxEvaluations, swarmSize, archiveSize, perturbationIndex);
        runner.run();
        DoubleSolution solution = runner.getBestSolution();
        System.out.println("x=" + bestX + ", y=" + bestY + ", energy=" + bestEnergy);
    }

}
