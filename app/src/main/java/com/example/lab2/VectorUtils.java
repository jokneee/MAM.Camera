package com.example.lab2;

public class VectorUtils {
    public static float getAngle(float[] a, float b[]) {
        return (float) Math.acos(
                dotProduct(a, b) / (vectorMagnitude(a) * vectorMagnitude(b))
        );
    }

    private static float dotProduct(float[] a, float[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

    private static float vectorMagnitude(float[] vec) {
        return (float) Math.sqrt(vec[0] * vec[0] + vec[1] * vec[1] + vec[2] * vec[2]);
    }
}
