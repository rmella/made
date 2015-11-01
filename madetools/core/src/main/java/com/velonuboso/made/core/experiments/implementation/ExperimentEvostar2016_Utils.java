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
package com.velonuboso.made.core.experiments.implementation;

import com.velonuboso.made.core.abm.api.IBehaviourTreeNode;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.TempFile;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ExperimentEvostar2016_Utils extends BaseExperiment {

    private static final String ARGUMENT_EXPORT_BEHAVIOUR_TREE = "exportBehaviourTree";
    private static final String DEFAULT_EXTENSION = "jpg";

    public ExperimentEvostar2016_Utils() {
    }

    @Override
    public String getDescription() {
        return "Utilities for EvoGames 2016";
    }

    @Override
    public void run(String[] arguments) {

        OptionParser parser = buildOptionParser();

        OptionSet options = parser.parse(arguments);

        if (options.has(ARGUMENT_EXPORT_BEHAVIOUR_TREE)) {
            String outputFileName = options.valueOf(ARGUMENT_EXPORT_BEHAVIOUR_TREE).toString();
            exportBehaviourTree(outputFileName);
            System.exit(0);
        }

        printHelp(parser);
    }

    private void exportBehaviourTree(String outputFileName) {
        final String EXECUTABLE = "dot";

        checkExecutableExists(EXECUTABLE);

        ICharacter character = ObjectFactory.createObject(ICharacter.class);

        float[] chromosome = new float[50];
        Arrays.fill(chromosome, 0.5f);
        AbmConfigurationEntity entity = new AbmConfigurationEntity(chromosome);
        character.setAbmConfiguration(entity);

        IBehaviourTreeNode node = character.getBehaviourTree();
        String graph = getBehaviourTreeAsDigraph(node);
        try {
            File digraphTemporalFile = File.createTempFile("made_", "_bt.tmp");
            Files.write(Paths.get(digraphTemporalFile.toURI()), graph.getBytes(), StandardOpenOption.WRITE);

            if (!outputFileName.contains(".")){
                outputFileName = outputFileName+"."+ DEFAULT_EXTENSION;
            }
            String extension = getExtensionFromFileName (outputFileName);
            
            Process dotExecutionProcess = Runtime.getRuntime().exec(EXECUTABLE
                    + " -T"+extension+" " + digraphTemporalFile.getAbsolutePath()
                    + " -o " + outputFileName);

            redirectToSystemOutput(dotExecutionProcess.getInputStream());
            redirectToSystemOutput(dotExecutionProcess.getErrorStream());

            digraphTemporalFile.delete();
        } catch (Exception e) {
            Logger.getLogger(ExperimentEvostar2016_Utils.class.getName()).log(Level.SEVERE, null, e.getMessage());
        }
    }

    private void redirectToSystemOutput(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        String result = builder.toString();

        Logger.getLogger(ExperimentEvostar2016_Utils.class.getName()).log(Level.INFO, result);
    }

    private OptionParser buildOptionParser() {
        OptionParser parser = new OptionParser();
        parser.accepts(ARGUMENT_EXPORT_BEHAVIOUR_TREE, "Export behaviour tree to pdf in "
                + "the specified file (pdf extension added automatically)").withRequiredArg().ofType(String.class);
        parser.allowsUnrecognizedOptions();
        return parser;
    }

    private void printHelp(OptionParser parser) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            parser.printHelpOn(byteArrayOutputStream);
            System.out.println(byteArrayOutputStream.toString());
        } catch (IOException ex) {
            Logger.getLogger(ExperimentEvostar2016_Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getBehaviourTreeAsDigraph(IBehaviourTreeNode node) {
        StringBuilder builder = new StringBuilder();

        builder.append("digraph bt{\n");
        includeGeneralPropertieForGraph(builder);
        recursivePrintChildren(node, builder);
        builder.append("}");
        return builder.toString();
    }

    private void checkExecutableExists(String executableName) {
        boolean existsInPath = Stream.of(System.getenv("PATH").split(Pattern.quote(File.pathSeparator)))
                .map(Paths::get)
                .anyMatch(path -> Files.exists(path.resolve(executableName)) || Files.exists(path.resolve(executableName + ".exe")));
        if (!existsInPath) {
            Logger.getLogger(ExperimentEvostar2016_Utils.class.getName()).log(Level.SEVERE,
                    "Could not find " + executableName + " in the path");
        }
    }

    private void recursivePrintChildren(IBehaviourTreeNode parent, StringBuilder builder) {
        if (parent.getChildren() == null) {
            return;
        }
        parent.getChildren().
                forEach(child -> {
                    printChild(parent, child, builder);
                    recursivePrintChildren(child, builder);
                });
    }

    private void printChild(IBehaviourTreeNode parent, IBehaviourTreeNode child, StringBuilder builder) {
        String parentName = getNodeName(parent);
        String parentLabel = convertClassNameToReadableFormat(parent.getAction().getClass().getSimpleName());
        if (parentLabel.contains("lambda")) {
            parentLabel = "Root node";
            builder.append("node_" + parentName + " [shape=circle];\n");
        }

        String childClass = child.getAction().getClass().getSimpleName();
        boolean isCondition = childClass.contains("Condition");

        String childLabel = convertClassNameToReadableFormat(childClass);
        String childName = getNodeName(child);

        if (isCondition) {
            builder.append("node_" + childName + " [shape=oval, height=1.1];\n");
        }

        builder.append("{node_" + parentName + " [label=\"" + parentLabel + "\"]} "
                + "-> {node_" + childName + " [label=\"" + childLabel + "\"]};\n");
    }

    static String convertClassNameToReadableFormat(String s) {
        String finalText = s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
        finalText = finalText.replaceAll("(Condition |Strategy )", "");
        finalText = finalText.toLowerCase();
        finalText = finalText.replace("friend similarity", "friend's similarity");
        finalText = finalText.replace("enemy similarity", "enemy's similarity");
        finalText = finalText.replace(" ", "\n");
        return finalText;
    }

    private void includeGeneralPropertieForGraph(StringBuilder builder) {
        builder.append("graph [pad=\".2\", ranksep=\"0.5\", nodesep=\"0.25\", rankdir=LR, ordering=out, splines=ortho];\n"
                + "node [fontname=\"FreeSans\",fontsize=\"16\",shape=box,width=1.1, height=1.1 margin=0.1, style=rounded];\n"
                + "edge [fontname=\"FreeSans\",fontsize=\"12\",labelfontname=\"FreeSans\",labelfontsize=\"10\"]\n;");
    }

    private String getNodeName(IBehaviourTreeNode parent) {
        return Integer.toString(System.identityHashCode(parent));
    }

    private String getExtensionFromFileName(String outputFileName) {
        return FilenameUtils.getExtension(outputFileName).toLowerCase();
    }
}
