package com.assets.options.blackscholes;

import net.finmath.functions.NormalDistribution;

import static java.lang.Math.*;

public class BlackScholesGreeks {

    public static double[] calculate(boolean callOption,
                                     double currentValue, double strike, double riskFree, double time, double volatility) {

        double[] p = new double[6];

        double d1 = d1(currentValue, strike, riskFree, time, volatility);
        double d2 = d2(d1, volatility, time);

        double sd1 = standardNormalDistribution(d1);
        double cd1 = NormalDistribution.cumulativeDistribution(d1);
        double thetaLeft = -(currentValue * sd1 * volatility) / (2 * sqrt(time));

        if (callOption) {

            double cd2 = NormalDistribution.cumulativeDistribution(d2);

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

            double pcd1 = NormalDistribution.cumulativeDistribution(-d1);
            double pcd2 = NormalDistribution.cumulativeDistribution(-d2);

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

    private static double d2(double d1, double v, double t) {
        return d1 - Math.max(v * sqrt(t), 0.000000001);
    }

    private static double standardNormalDistribution(double x) {
        double top = exp(-0.5 * pow(x, 2));
        double bottom = sqrt(2 * PI);
        return top / bottom;
    }

    public static void main(String[] args) {
        double[] calculate = calculate(true, 45, 46, 0.002d, 0.0466, 0.15d);
        System.out.println(calculate[4]/365d);

    }
}