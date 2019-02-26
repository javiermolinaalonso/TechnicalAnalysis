package com.assets.options.blackscholes;

import net.finmath.functions.AnalyticFormulas;

import static java.lang.Math.*;

public class BlackScholesGreeks {

    private static final double P = 0.2316419;
    private static final double B1 = 0.319381530;
    private static final double B2 = -0.356563782;
    private static final double B3 = 1.781477937;
    private static final double B4 = -1.821255978;
    private static final double B5 = 1.330274429;

    public static double[] calculate(boolean callOption,
                                     double currentValue, double strike, double riskFree, double time, double volatility) {

        double[] p = new double[6];

        double d1 = d1(currentValue, strike, riskFree, time, volatility);
        double d2 = d2(d1, volatility, time);

        double sd1 = standardNormalDistribution(d1);
        double cd1 = cumulativeDistribution(d1, sd1);
        double thetaLeft = -(currentValue * sd1 * volatility) / (2 * sqrt(time));

        if (callOption) {

            double cd2 = cumulativeDistribution(d2);

            // price
            p[0] = currentValue * cd1 - strike * exp(-riskFree * time) * cd2;

            // delta
            p[1] = cd1;

            // theta
            double thetaRight = riskFree * strike * exp(-riskFree * time) * cd2;
            p[4] = thetaLeft - thetaRight;

            // rho
            p[5] = strike * time * exp(-riskFree * time) * cd2;

        } else {

            double pcd1 = cumulativeDistribution(-d1);
            double pcd2 = cumulativeDistribution(-d2);

            // price
            p[0] = strike * exp(-riskFree * time) * pcd2 - currentValue * pcd1;

            // delta
            p[1] = cd1 - 1;

            // theta
            double thetaRight = riskFree * strike * exp(-riskFree * time) * pcd2;
            p[4] = thetaLeft + thetaRight;

            // rho
            p[5] = -strike * time * exp(-riskFree * time) * pcd2;

        }

        // gamma
        p[2] = sd1 / (currentValue * volatility * sqrt(time));

        // vega
        p[3] = currentValue * sd1 * sqrt(time);

        return p;

    }

    private static double d1(double s, double k, double r, double t, double v) {
        double top = log(s / k) + (r + pow(v, 2) / 2) * t;
        double bottom = Math.max(v * sqrt(t), 0.000000001);
        return top / bottom;
    }

    private static double d2(double s, double k, double r, double t, double v) {
        return d1(s, k, r, t, v) - v * sqrt(t);
    }

    private static double d2(double d1, double v, double t) {
        return d1 - Math.max(v * sqrt(t), 0.000000001);
    }

    public static double cumulativeDistribution(double x) {
        return cumulativeDistribution(x, standardNormalDistribution(x));
    }

    public static double cumulativeDistribution(double x, double sdx) {
        double t = 1 / (1 + P * abs(x));
        double t1 = B1 * pow(t, 1);
        double t2 = B2 * pow(t, 2);
        double t3 = B3 * pow(t, 3);
        double t4 = B4 * pow(t, 4);
        double t5 = B5 * pow(t, 5);
        double b = t1 + t2 + t3 + t4 + t5;
        double cd = 1 - sdx * b;
        return x < 0 ? 1 - cd : cd;
    }

    public static double standardNormalDistribution(double x) {
        double top = exp(-0.5 * pow(x, 2));
        double bottom = sqrt(2 * PI);
        return top / bottom;
    }

    public static void main(String[] args) {
        double[] calculate = calculate(true, 45, 46, 0.002d, 0.0466, 0.15d);
        System.out.println(calculate[4]/365d);

    }
}