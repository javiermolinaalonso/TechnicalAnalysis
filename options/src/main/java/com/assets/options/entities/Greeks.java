package com.assets.options.entities;

public class Greeks {

    private final double delta;
    private final double gamma;
    private final double vega;
    private final double theta;
    private final double rho;

    public Greeks(double delta, double gamma, double vega, double theta, double rho) {
        this.delta = delta;
        this.gamma = gamma;
        this.vega = vega;
        this.theta = theta;
        this.rho = rho;
    }

    public double getDelta() {
        return delta;
    }

    public double getGamma() {
        return gamma;
    }

    public double getVega() {
        return vega;
    }

    public double getTheta() {
        return theta;
    }

    public double getRho() {
        return rho;
    }

    @Override
    public String toString() {
        return String.format("Greeks: [%.4f, %.4f, %.4f, %.4f, %.4f]", delta, gamma, theta, vega, rho);
    }
}
