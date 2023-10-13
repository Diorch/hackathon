package com.trip.hackathon.engine.algo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

public class DBSCANHyperheuristic {
    
    // 定义问题的参数
    private static final int N = 1000; // 数据点数量
    private static final double epsilonMin = 0.1; // 邻域半径的最小值
    private static final double epsilonMax = 2.0; // 邻域半径的最大值
    private static final int minPtsMin = 2; // 最小簇大小的最小值
    private static final int minPtsMax = 20; // 最小簇大小的最大值
    
    // 定义数据点
    private static double[][] data = new double[N][2];
   