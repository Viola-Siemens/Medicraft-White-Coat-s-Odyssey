package com.hexagram2021.medicraft.common.util;

// Thanks to CB for fitting model.
public final class ElectrocardiogramUtils {
	/**
	 * @param x Yime in a period, inexact [0, 1].
	 * @param alpha For each lead, 0 <= V6 < V1 <= 1.
	 * @param pe Pre-excitation, inexact (0.0, 0.6]. 0 for norm.
	 * @param stAbnormality ST segment abnormality. [-0.8, 0) for depression (e.g. myocardial ischemia), inexact 0 for norm, (0, 0.8] for elevation (e.g. myocardial infarction).
	 * @return Value of ECG function of time x.
	 */
	public static double sinus(double x, double alpha, double pe, double stAbnormality) {
		double softPlusStAbnormality = softPlus(stAbnormality);
		return bellCurve(x, 0.1D, 0.2D, 0.03D, 2) +
				bellCurve(x, 0.05D * alpha - 0.05D - 0.5D * softPlusStAbnormality, 0.315D, 0.015D, 2) +
				bellCurve(x, pe * (0.8D - 0.2D * alpha), 0.32D, 0.015D, 2) +
				bellCurve(x, 1.0D - 0.25D * alpha - 0.5D * stAbnormality, 0.35D, 0.015D, 2) +
				bellCurve(x, -0.1D * stAbnormality  - 0.15D - 0.25D * alpha, 0.38D, 0.017D + 0.1D * stAbnormality * stAbnormality, 2.0D + softPlusStAbnormality) +
				bellCurve(x, 0.1D * stAbnormality, 0.52D, 0.1D, 4) +
				bellCurve(x, -softPlus(-stAbnormality) * 0.2D, 0.58D, 0.12D, 3) +
				bellCurve(x, 0.25D + softPlusStAbnormality * 0.5D, 0.6D - 0.1D * stAbnormality, 0.08D + 0.1D * softPlusStAbnormality, 2.0D + softPlusStAbnormality * 3.0D);
	}

	private static double bellCurve(double x, double a, double b, double s, double k) {
		return a * Math.exp(-Math.pow(Math.abs(x - b) / s, k));
	}

	private static double softPlus(double x) {
		return Math.log(Math.exp(x * 10.0D) + 1.0D) / 10.0D;
	}
}
