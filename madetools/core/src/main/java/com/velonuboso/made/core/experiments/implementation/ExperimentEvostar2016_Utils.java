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
import com.velonuboso.made.core.experiments.helper.BehaviourTreePrinter;
import com.velonuboso.made.core.inference.implementation.MonomythReasoner;
import com.velonuboso.made.core.inference.implementation.TermRule;
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
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ExperimentEvostar2016_Utils extends BaseExperiment {

    private static final String ARGUMENT_EXPORT_BEHAVIOUR_TREE = "exportBehaviourTree";
    private static final String ARGUMENT_EXPORT_THEORY = "exportTheory";
    
    private final String EXECUTABLE = "dot";
    private BehaviourTreePrinter printer;

    public ExperimentEvostar2016_Utils() {
        printer = new BehaviourTreePrinter();
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
        if (options.has(ARGUMENT_EXPORT_THEORY)) {
            String outputFileName = options.valueOf(ARGUMENT_EXPORT_THEORY).toString();
            exportTheory(outputFileName); 
            System.exit(0);
        }
        printHelp(parser);
    }

    private void exportBehaviourTree(String outputFileName) {
        
        IBehaviourTreeNode node = getDefaultBehaviourTree();
        String graph = printer.getBehaviourTreeAsDigraph(node);
        
        generateDiagram(graph, outputFileName);
    }

    private void exportTheory(String outputFileName) {
        MonomythReasoner reasoner = new MonomythReasoner();
        System.out.println(reasoner.getMonomythRulesAsString());
        
        String graph = printer.getTheoryAsDigraph(reasoner.getMonomythRules());
        generateDiagram(graph, outputFileName);
    }
    
    private void generateDiagram(String graph, String outputFileName) {
        final String DOT = ".";
        final String DEFAULT_EXTENSION = "jpg";

        try {
            checkExecutableExists(EXECUTABLE);
            
            File digraphTemporalFile = saveDotToTemporalFile(graph);
            if (!outputFileName.contains(DOT)) {
                outputFileName = outputFileName + DOT + DEFAULT_EXTENSION;
            }
            Process dotExecutionProcess = runDot(digraphTemporalFile, outputFileName);
            redirectToSystemOutput(dotExecutionProcess.getInputStream());
            redirectToSystemOutput(dotExecutionProcess.getErrorStream());

            //digraphTemporalFile.delete();

            showMessageSuccess(outputFileName);
        } catch (Exception e) {
            Logger.getLogger(ExperimentEvostar2016_Utils.class.getName()).log(Level.SEVERE, null, e.getMessage());
        }
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

    private IBehaviourTreeNode getDefaultBehaviourTree() {
        ICharacter character = ObjectFactory.createObject(ICharacter.class);
        configureCharacter(character);
        IBehaviourTreeNode node = character.getBehaviourTree();
        return node;
    }

    private void showMessageSuccess(String outputFileName) {
        String messageSuccess = "Output file successfully created: " + outputFileName;
        System.out.println(messageSuccess);
    }

    private Process runDot(File digraphTemporalFile, String outputFileName) throws IOException {
        String extension = printer.getExtensionFromFileName(outputFileName);
        Process dotExecutionProcess = Runtime.getRuntime().exec(EXECUTABLE
                + " -T" + extension + " " + digraphTemporalFile.getAbsolutePath()
                + " -o " + outputFileName);
        return dotExecutionProcess;
    }

    private void configureCharacter(ICharacter character) {
        float[] chromosome = new float[50];
        Arrays.fill(chromosome, 0.5f);
        AbmConfigurationEntity entity = new AbmConfigurationEntity(chromosome);
        character.setAbmConfiguration(entity);
    }

    private File saveDotToTemporalFile(String graph) throws IOException {
        File digraphTemporalFile = File.createTempFile("made_", "_bt.tmp");
        Files.write(Paths.get(digraphTemporalFile.toURI()), graph.getBytes(), StandardOpenOption.WRITE);
        return digraphTemporalFile;
    }

    private void redirectToSystemOutput(InputStream stream) throws IOException {
        final String LINE_SEPARATOR_SYSTEM_PROPERTY = "line.separator";

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty(LINE_SEPARATOR_SYSTEM_PROPERTY));
        }
        String result = builder.toString();

        if (!StringUtils.isBlank(result)) {
            Logger.getLogger(ExperimentEvostar2016_Utils.class.getName()).log(Level.INFO, result);
        }
    }

    private OptionParser buildOptionParser() {
        OptionParser parser = new OptionParser();
        parser.accepts(ARGUMENT_EXPORT_BEHAVIOUR_TREE, "Export behaviour tree to "
                + "the specified file (if none specified, jpg is used)").withRequiredArg().ofType(String.class);
        parser.accepts(ARGUMENT_EXPORT_THEORY, "Export theory to "
                + "the specified file (if none specified, txt is used)").withRequiredArg().ofType(String.class);
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
}
