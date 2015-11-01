/*
 * Copyright (C) 2015 Rubén Héctor García (raiben@gmail.com)
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
package com.velonuboso.made.core.experiments.helper;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import com.velonuboso.made.core.abm.api.IBehaviourTreeNode;
import com.velonuboso.made.core.inference.entity.Trope;
import com.velonuboso.made.core.inference.implementation.TermRule;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class BehaviourTreePrinter {

    IBehaviourTreeNode node;
    TermRule[] termRules;
    StringBuilder builder;
    HashSet<String> blackListWords;
    HashMap<String, Boolean> nodes;

    public BehaviourTreePrinter() {
        builder = new StringBuilder();
        initializeBlackListPredicates();
        nodes = new HashMap<>();
    }

    private void initializeBlackListPredicates() {
        String[] list = new String[]{
            "length", "findall", "not", "div", "ceiling", "element", "colorSpot", "colorSpotAppears"
        };
        blackListWords = new HashSet<>(Arrays.asList(list));
    }

    public String getBehaviourTreeAsDigraph(IBehaviourTreeNode node) {
        this.node = node;
        builder.append("digraph bt{\n");
        includeGeneralPropertieForBehaviourTreeGraph();
        recursivePrintChildren(node);
        builder.append("}");
        return builder.toString();
    }

    public String getTheoryAsDigraph(TermRule[] monomythRules) {
        builder.append("digraph bt{\n");
        includeGeneralPropertieForPredicatesDependency();
        recursivePrintDependencies(monomythRules);
        printNodesStyle();
        builder.append("}");
        return builder.toString();
    }

    private void recursivePrintChildren(IBehaviourTreeNode parent) {
        if (parent.getChildren() == null) {
            return;
        }
        parent.getChildren().
                forEach(child -> {
                    printChild(parent, child);
                    recursivePrintChildren(child);
                });
    }

    private void printChild(IBehaviourTreeNode parent, IBehaviourTreeNode child) {
        final String SUBSTRING_IN_CONDITION = "Condition";
        final String SUBSTRING_PRESENT_IN_LAMBDA_NODE_NAME = "lambda";
        final String BASE_NODE_NAME = "Root node";

        String parentName = getNodeName(parent);
        String parentLabel = convertClassNameToReadableFormat(parent.getAction().getClass().getSimpleName());
        if (parentLabel.contains(SUBSTRING_PRESENT_IN_LAMBDA_NODE_NAME)) {
            parentLabel = BASE_NODE_NAME;
            builder.append("node_" + parentName + " [shape=circle];\n");
        }

        String childClass = child.getAction().getClass().getSimpleName();
        boolean isCondition = childClass.contains(SUBSTRING_IN_CONDITION);

        String childLabel = convertClassNameToReadableFormat(childClass);
        String childName = getNodeName(child);

        if (isCondition) {
            builder.append("node_" + childName + " [shape=oval, height=1.1];\n");
        }

        builder.append("{node_" + parentName + " [label=\"" + parentLabel + "\"]} "
                + "-> {node_" + childName + " [label=\"" + childLabel + "\"]};\n");
    }

    private void includeGeneralPropertieForBehaviourTreeGraph() {
        builder.append("graph [pad=\".2\", ranksep=\"0.5\", nodesep=\"0.25\", rankdir=LR, ordering=out, splines=ortho];\n"
                + "node [fontname=\"FreeSans\",fontsize=\"16\",shape=box,width=1.1, height=1.1 margin=0.1, style=rounded];\n"
                + "edge [fontname=\"FreeSans\",fontsize=\"12\",labelfontname=\"FreeSans\",labelfontsize=\"10\"]\n;");
    }

    private void includeGeneralPropertieForPredicatesDependency() {
        builder.append("graph [pad=\".1\", ranksep=\"0.2\", rankdir=LR];\n"
                + "node [fontname=\"FreeSans\",fontsize=\"16\",shape=rectangle,width=1.1, margin=0.1, style=rounded];\n"
                + "edge [color=\"#999999\", fontname=\"FreeSans\",fontsize=\"12\",labelfontname=\"FreeSans\",labelfontsize=\"10\"];\n");
    }

    private static String convertClassNameToReadableFormat(String text) {
        String finalText = splitByCamelCase(text);
        finalText = removePrefix(finalText);
        finalText = finalText.toLowerCase();
        finalText = addGenitives(finalText);
        finalText = convertToMultiLine(finalText);
        return finalText;
    }

    private static String removePrefix(String finalText) {
        return finalText.replaceAll("(Condition |Strategy )", "");
    }

    private static String convertToMultiLine(String finalText) {
        return finalText.replace(" ", "\n");
    }

    private static String addGenitives(String finalText) {
        String replacedFinalText = finalText.replace("friend similarity", "friend's similarity");
        replacedFinalText = replacedFinalText.replace("enemy similarity", "enemy's similarity");
        return replacedFinalText;
    }

    private String getNodeName(IBehaviourTreeNode parent) {
        return Integer.toString(System.identityHashCode(parent));
    }

    private static String splitByCamelCase(String text) {
        return text.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ), " ");
    }

    public static String getExtensionFromFileName(String outputFileName) {
        return FilenameUtils.getExtension(outputFileName).toLowerCase();
    }

    private void recursivePrintDependencies(TermRule[] monomythRules) {
        Arrays.stream(monomythRules).
                forEach(termRule -> {
                    printTermRule(termRule);
                });
    }

    private void printTermRule(TermRule termRule) {
        String sourceTerm = termRule.getThenTerm().toString();

        String source = "Unknown";
        String sourceLabel = "Unknown";

        Pattern pattern = Pattern.compile("[A-Za-z0-9_]+\\s*\\(");

        Matcher matcher = pattern.matcher(sourceTerm);
        if (matcher.find()) {
            source = matcher.group();
            source = source.substring(0, source.length() - 1).trim();
            addToNodes(source);
            setNodeSource(source);
            sourceLabel = convertClassNameToReadableFormat(source);
        }

        for (Term term : termRule.getIfTerms()) {
            String targetTerm = term.toString();
            matcher = pattern.matcher(targetTerm);
            while (matcher.find()) {
                String target = matcher.group();
                target = target.substring(0, target.length() - 1).trim();
                String targetLabel = convertClassNameToReadableFormat(target);

                addToNodes(target);

                if (!blackListWords.contains(target) && !blackListWords.contains(source)) {
                    //builder.append(target + "-> "+ source+";\n");
                    builder.append("{" + target + " [label=\"" + targetLabel + "\"]} -> {"
                            + source + " [label=\"" + sourceLabel + "\"]};\n");
                }
            }
        }
    }

    private void setNodeSource(String nodeSource) {
        nodes.put(nodeSource, Boolean.TRUE);
    }

    private void addToNodes(String node) {
        if (!nodes.containsKey(node)) {
            nodes.put(node, Boolean.FALSE);
        }
    }

    private void printNodesStyle() {
        nodes.keySet().stream()
                .filter(node -> !blackListWords.contains(node))
                .forEach(node -> {
                    boolean nodeIsAnArchetype = Arrays.stream(Trope.getTropesInFromMonomyth()).anyMatch(trope -> trope.toString().toLowerCase().equals(node));
                    
                    String color = nodes.get(node) ? nodeIsAnArchetype? "#CCCCCC": "#EEEEEE" : "#FFFFFF";
                    String shape = nodeIsAnArchetype ? "ellipse" : "rectangle";
                    
                    builder.append(node + " [style=\"rounded, filled\", shape=" + shape + ", fillcolor = \"" + color + "\"];\n");
                });
        /*
         builder.append("subgraph cluster_abm {\n");
         nodes.keySet().stream()
         .filter(node -> !blackListWords.contains(node))
         .filter(node -> !nodes.get(node))
         .forEach(node -> {
         builder.append(node + ";\n");
         });
         builder.append("}\n");
        
         builder.append("subgraph cluster_base {\n");
         nodes.keySet().stream()
         .filter(node -> !blackListWords.contains(node))
         .filter(node -> nodes.get(node))
         .forEach(node -> {
         builder.append(node + ";\n");
         });
         builder.append("}\n");
         */
    }

}
