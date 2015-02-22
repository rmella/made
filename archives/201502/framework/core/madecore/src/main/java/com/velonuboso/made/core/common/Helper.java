/*
 * Copyright 2013 Rubén Héctor García <raiben@gmail.com>.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.velonuboso.made.core.common;

import com.velonuboso.made.core.interfaces.Archetype;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.jgap.Gene;
import org.jgap.IChromosome;

/**
 * Provides different static methods used by the application.
 *
 * @author raiben@gmail.com
 * @author M. Jessup (http://stackoverflow.com/users/294738/m-jessup)
 */

public final class Helper {

    public static ArrayList chromosomeToArray(IChromosome bestSolutionSoFar) {
        ArrayList ret = new ArrayList();

        for (Gene g : bestSolutionSoFar.getGenes()) {
            ret.add(g.getAllele());
        }

        return ret;
    }

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
            final int generation, final IChromosome solution, final double average) {
        StringBuilder str = new StringBuilder();
        str.append("gen = ");
        str.append(String.format(FIXED_INTEGER_ITERATION_FORMAT, generation));
        str.append(",avg = ");
        str.append(String.format(FIXED_DECIMAL_FITNESS_FORMAT,
                average));
        str.append(",best = ");
        str.append(String.format(FIXED_DECIMAL_FITNESS_FORMAT,
                solution.getFitnessValue()));
        str.append(",chrom = {");
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
    public static String executionsToString(final int j, final double mean,
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
    public static double getMin(double[] data, int length) {
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
    public static double getMax(final double[] data, int length) {
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

    public static double getGaussian(float ratio, Float from, Float to) {
        Gaussian gaussian = new Gaussian();
        if (from == null && to == null) {
            return 1;
        } else if (from == null) {
            from = 1 - to;
        } else if (to == null) {
            to = 2 - from;
        }
        float target = (from + to) / 2f;
        float amplitude = to - from;
        return gaussian.value((ratio - target) / amplitude) / 0.40;
    }

    /**
     * Modification by rhgarcia from the implementation of M. Jessup 
     * at StackOverflow.
     * http://stackoverflow.com/users/294738/m-jessup
     */
    static class Node {

        public final Class c;
        public final HashSet<Edge> inEdges;
        public final HashSet<Edge> outEdges;

        public Node(Class c) {
            this.c = c;
            inEdges = new HashSet<Edge>();
            outEdges = new HashSet<Edge>();
        }

        public Node addEdge(Node node) {
            Edge e = new Edge(this, node);
            outEdges.add(e);
            node.inEdges.add(e);
            return this;
        }

        public String toString() {
            return c.getSimpleName();
        }

        public Class getC() {
            return c;
        }

    }

    /**
     * Implementation of M. Jessup 
     * at StackOverflow.
     * http://stackoverflow.com/users/294738/m-jessup
     */
    static class Edge {

        public final Node from;
        public final Node to;

        public Edge(Node from, Node to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object obj) {
            Edge e = (Edge) obj;
            return e.from == from && e.to == to;
        }
    }

    /**
     * Modification by rhgarcia from the implementation of M. Jessup 
     * at StackOverflow.
     * http://stackoverflow.com/users/294738/m-jessup
     */
    public static ArrayList<Class> topologicalOrder(ArrayList<Class> classes) throws Exception {

        Node[] allNodes = new Node[classes.size()];
        HashMap<String, Node> nodes = new HashMap<String, Node>();

        for (int i = 0; i < classes.size(); i++) {
            Class c = classes.get(i);
            Node n = new Node(c);
            allNodes[i] = n;
            nodes.put(c.getSimpleName(), n);
        }

        for (int i = 0; i < allNodes.length; i++) {
            Node n = allNodes[i];
            Class c = n.getC();
            Archetype a = (Archetype) c.getConstructors()[0].newInstance();
            ArrayList<Class> dependencies = a.getDependencies();
            for (int j = 0; j < dependencies.size(); j++) {
                Class dep = dependencies.get(j);
                Node nAux = nodes.get(dep.getSimpleName());
                if (nAux != null) {
                    nAux.addEdge(n);
                }
            }
        }

        //L <- Empty list that will contain the sorted elements
        ArrayList<Node> L = new ArrayList<Node>();

        //S <- Set of all nodes with no incoming edges
        HashSet<Node> S = new HashSet<Node>();
        for (Node n : allNodes) {
            if (n.inEdges.size() == 0) {
                S.add(n);
            }
        }

        //while S is non-empty do
        while (!S.isEmpty()) {
            //remove a node n from S
            Node n = S.iterator().next();
            S.remove(n);

            //insert n into L
            L.add(n);

            //for each node m with an edge e from n to m do
            for (Iterator<Edge> it = n.outEdges.iterator(); it.hasNext();) {
                //remove edge e from the graph
                Edge e = it.next();
                Node m = e.to;
                it.remove();//Remove edge from n
                m.inEdges.remove(e);//Remove edge from m

                //if m has no other incoming edges then insert m into S
                if (m.inEdges.isEmpty()) {
                    S.add(m);
                }
            }
        }
        //Check to see if all edges are removed
        boolean cycle = false;
        for (Node n : allNodes) {
            if (!n.inEdges.isEmpty()) {
                cycle = true;
                break;
            }
        }
        if (cycle) {
            throw new Exception("Cycle present, topological sort not possible");
        }
        System.out.println("Topological Sort: " + Arrays.toString(L.toArray()));

        ArrayList<Class> ret = new ArrayList<Class>();
        for (Node n : L) {
            ret.add(n.getC());
        }
        return ret;
    }
}
