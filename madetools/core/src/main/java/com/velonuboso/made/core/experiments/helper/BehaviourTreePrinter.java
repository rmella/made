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

import com.velonuboso.made.core.abm.api.IBehaviourTreeNode;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class BehaviourTreePrinter {

    IBehaviourTreeNode node;
    StringBuilder builder;
    
    public BehaviourTreePrinter() {
        builder = new StringBuilder();
    }

    public String getBehaviourTreeAsDigraph(IBehaviourTreeNode node) {
        this.node = node;
        builder.append("digraph bt{\n");
        includeGeneralPropertieForGraph();
        recursivePrintChildren(node);
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

    private void includeGeneralPropertieForGraph() {
        builder.append("graph [pad=\".2\", ranksep=\"0.5\", nodesep=\"0.25\", rankdir=LR, ordering=out, splines=ortho];\n"
                + "node [fontname=\"FreeSans\",fontsize=\"16\",shape=box,width=1.1, height=1.1 margin=0.1, style=rounded];\n"
                + "edge [fontname=\"FreeSans\",fontsize=\"12\",labelfontname=\"FreeSans\",labelfontsize=\"10\"]\n;");
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
            )," ");
    }
    
    public static String getExtensionFromFileName(String outputFileName) {
        return FilenameUtils.getExtension(outputFileName).toLowerCase();
    }
}
