/*
 * Copyright 2013 Ruben.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.velonuboso.made.prototype;

import java.util.Arrays;
import org.jgap.Gene;
import org.jgap.IChromosome;

/**
 * Provides different static methods used by the application.
 *
 * @author Ruben
 */
public final class Helper {

    /**
     * no constructor needed.
     */
    private Helper() {
    }
    /**
     * default decimal format for the fitness.
     */
    private static final String FIXED_DECIMAL_FITNESS_FORMAT = "%03.3f";
    /**
     * default decimal format for the chromosome representation.
     */
    private static final String FIXED_DECIMAL_ALLELE_FORMAT = "%.3f";
    /**
     * default decimal format for the integer values with left zeroes.
     */
    private static final String FIXED_INTEGER_ITERATION_FORMAT = "%04d";
    /**
     * default decimal format for the integer values with left zeroes.
     */
    private static final String FIXED_INTEGER_MS_FORMAT = "%08.3f";
    /**
     * Miliseconds in a second.
     */
    private static final double MS_IN_A_SECOND = 1000.0;

    /**
     * returns a String with the chromosome values as a comma separated value.
     *
     * @param ic the Chromosome to print
     * @return comma-separated values
     */
    public static String chromosomeToString(final IChromosome ic) {
        StringBuilder str = new StringBuilder();
        Gene[] genes = ic.getGenes();
        boolean first = true;
        for (Gene gene : genes) {
            if (!first) {
                str.append(";");
            }
            str.append(String.format(FIXED_DECIMAL_ALLELE_FORMAT,
                    (Double) gene.getAllele()));
            first = false;
        }
        return str.toString();
    }

    /**
     * returns the chromosome, fitness and generation of the chromosome in a
     * pretty way.
     *
     * @param generation the ordinal number that represents the generation
     * @param solution the chromosome to show
     * @return a pretty formatted string
     */
    public static String chromosomeAndGenerationToString(
            final int generation, final IChromosome solution) {
        StringBuilder str = new StringBuilder();
        str.append("g = ");
        str.append(String.format(FIXED_INTEGER_ITERATION_FORMAT, generation));
        str.append(",v = ");
        str.append(String.format(FIXED_DECIMAL_FITNESS_FORMAT,
                solution.getFitnessValue()));
        str.append(",c = {");
        str.append(chromosomeToString(solution));
        str.append("}");
        return str.toString();
    }

    /**
     * returns the execution time in a pretty way.
     *
     * @param t0 the first captured time in ms
     * @param t1 the secondly captured time in ms
     * @return a pretty formated string
     */
    public static String executionTime(final long t0, final long t1) {
        double timeInSeconds = (t1 - t0) / MS_IN_A_SECOND;
        return "Execution Time = "
                + String.format(FIXED_INTEGER_MS_FORMAT, timeInSeconds)
                + "ms;";
    }

    /**
     * returns the message in a pretty format.
     *
     * @param msg the message to show
     * @return the pretty printed message
     */
    public static String consoleMsg(final String msg) {
        return "--- " + msg + " ---";
    }

    /**
     * returns the executions line in a pretty format.
     *
     * @param j index
     * @param mean mean
     * @param var variance
     * @param error error
     * @param relError relative error
     * @return the message in a pretty format
     */
    static String executionsToString(final int j, final double mean,
            final double var, final double error,
            final double relError) {
        StringBuilder str = new StringBuilder();
        str.append("ex = ");
        str.append(String.format(FIXED_INTEGER_ITERATION_FORMAT, j));
        str.append(", mean = ");
        str.append(String.format(FIXED_DECIMAL_FITNESS_FORMAT,
                mean));
        str.append(", var = ");
        str.append(String.format(FIXED_DECIMAL_FITNESS_FORMAT,
                var));
        str.append(", err = ");
        str.append(String.format(FIXED_DECIMAL_FITNESS_FORMAT,
                error));
        str.append(", relErr = ");
        str.append(String.format(FIXED_DECIMAL_FITNESS_FORMAT,
                relError));
        return str.toString();
    }

    /**
     * calculates the mean of a number array
     *
     * @param data the array of data
     * @param length the searchable length
     * @return the mean of a number array. 0 on zero length
     */
    public static double getMean(double[] data, int length) {
        if (length > data.length) {
            length = data.length;
        }
        if (length == 0) {
            return 0;
        }
        double sum = 0.0;
        for (int i = 0; i < length; i++) {
            double a = data[i];
            sum += a;
        }
        return sum / length;
    }

    /**
     * calculates the variance of a number array
     *
     * @param data the array of data
     * @param length the searchable length
     * @return the variance of a number array.
     */
    public static double getVariance(double[] data, int length) {
        if (length > data.length) {
            length = data.length;
        }
        if (length == 0) {
            return 0;
        }
        double mean = getMean(data, length);
        double temp = 0;
        for (int i = 0; i < length; i++) {
            double a = data[i];
            temp += (mean - a) * (mean - a);
        }
        return temp / length;
    }

    /**
     * calculates the standard deviation of a number array
     *
     * @param data the array of data
     * @param length the searchable length
     * @return the standard deviation of a number array. 0 on zero length
     */
    public static double getStdDev(double[] data, int length) {
        return Math.sqrt(getVariance(data, length));
    }

    /**
     * calculates the median of a number array
     *
     * @param data the array of data
     * @param length the searchable length
     * @return the median of a number array. 0 on zero length
     */
    public static double median(double[] data, int length) {
        if (length > data.length) {
            length = data.length;
        }
        if (length == 0) {
            return 0;
        }
        double[] b = new double[length];
        System.arraycopy(data, 0, b, 0, length);
        Arrays.sort(b);

        if (length % 2 == 0) {
            return (b[(b.length / 2) - 1] + b[b.length / 2]) / 2.0;
        } else {
            return b[b.length / 2];
        }
    }

    /**
     * returns the minimum value os a given array and size.
     *
     * @param data the array of data
     * @param length the searchable length
     * @return the minimum value. Double.MAX_VALUE on zero length
     */
    static double getMin(double[] data, int length) {
        if (length > data.length) {
            length = data.length;
        }
        if (length == 0) {
            return Double.MAX_VALUE;
        }
        double min = data[0];
        for (int i = 1; i < length; i++) {
            double a = data[i];
            if (a < min) {
                min = a;
            }
        }
        return min;
    }

    /**
     * returns the maximun value os a given array and size.
     *
     * @param data the array of data
     * @param length the searchable length
     * @return the maximum value. Double.MIN_VALUE on zero length
     */
    static double getMax(final double[] data, int length) {
        if (length > data.length) {
            length = data.length;
        }
        if (length == 0) {
            return Double.MIN_VALUE;
        }
        double max = data[0];
        for (int i = 1; i < length; i++) {
            double a = data[i];
            if (a > max) {
                max = a;
            }
        }
        return max;
    }
}
