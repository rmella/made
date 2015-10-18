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
package com.velonuboso.made.core.ec.implementation.listeners;

import com.velonuboso.made.core.common.api.IGlobalConfigurationFactory;
import com.velonuboso.made.core.common.entity.CommonAbmConfiguration;
import com.velonuboso.made.core.common.entity.CommonEcConfiguration;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.ec.api.IGeneticAlgorithmListener;
import com.velonuboso.made.core.ec.api.IIndividual;
import com.velonuboso.made.core.ec.entity.Fitness;
import com.velonuboso.made.core.ec.entity.TrialInformation;
import com.velonuboso.made.core.experiments.api.IExperiment;
import com.velonuboso.made.core.inference.entity.Trope;
import com.velonuboso.made.core.inference.entity.WorldDeductions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ExcelWriterGeneticAlgorithmListener implements IGeneticAlgorithmListener {

    private boolean headerprinted = false;
    private String outputFilePath = null;
    private IExperiment experiment = null;
    ConsoleWriterGeneticAlgorithmListener consoleListener;

    public ExcelWriterGeneticAlgorithmListener() {
        consoleListener = new ConsoleWriterGeneticAlgorithmListener();
    }

    @Override
    public void notifyNewExperimentExecuting(IExperiment experiment) {
        consoleListener.notifyNewExperimentExecuting(experiment);

        this.experiment = experiment;
        outputFilePath = buildTargetFileName();
        copyBaseFileToTarget();
        insertInfoIntoExcel();
    }

    @Override
    public void notifyIterationSummary(int iteration, IIndividual bestIndividualEver, float populationAverage, float populationStandardDeviation) {
        consoleListener.notifyIterationSummary(iteration, bestIndividualEver,
                populationAverage, populationStandardDeviation);

        if (!headerprinted) {
            headerprinted = true;
            printHeader(bestIndividualEver);
        }
        printLine(iteration, populationAverage, populationStandardDeviation, bestIndividualEver);
    }

    @Override
    public void notifyTrialExecuted(WorldDeductions deductions) {
        consoleListener.notifyTrialExecuted(deductions);
    }

    @Override
    public void notifyIndividualEvaluation(Fitness fitness) {
        consoleListener.notifyIndividualEvaluation(fitness);
    }

    private String buildTargetFileName() {
        String SEPARATOR = "_";
        String OUTPUT_PATH = "data/";

        IGlobalConfigurationFactory globalConfigurationFactory
                = ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        CommonEcConfiguration ecConfig = globalConfigurationFactory.getCommonEcConfiguration();
        String currentTrope = ecConfig.PROMOTE_TROPE==null? "ALL": ecConfig.PROMOTE_TROPE.name();

        String suffix = new SimpleDateFormat("yyyyMMddHHmmss.S").format(new Date());
        String fileName = "experiment" + SEPARATOR + experiment.getCodeName() 
                + SEPARATOR + currentTrope + SEPARATOR + suffix + ".xlsx";
        return OUTPUT_PATH + fileName;
    }

    private void copyBaseFileToTarget() throws RuntimeException {
        try {
            File target = new File(outputFilePath);
            URL inputUrl = getClass().getResource("/ExperimentSummary.xlsx");
            FileUtils.copyURLToFile(inputUrl, target);
        } catch (IOException ex) {
            Logger.getLogger(ExcelWriterGeneticAlgorithmListener.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    private void printLine(int iteration, float populationAverage, float populationStandardDeviation, IIndividual bestIndividualEver) {
        FileInputStream input_document = null;
        try {
            input_document = new FileInputStream(new File(outputFilePath));
            XSSFWorkbook report = new XSSFWorkbook(input_document);
            input_document.close();

            writeInfo(report, 1, iteration + 2, 0, iteration, null);
            writeInfo(report, 1, iteration + 2, 1, bestIndividualEver.getCurrentFitness().getValue().getAverage(), null);
            writeInfo(report, 1, iteration + 2, 2, populationAverage, null);
            writeInfo(report, 1, iteration + 2, 3, populationStandardDeviation, null);
            writeInfo(report, 1, iteration + 2, 4, bestIndividualEver.getCurrentFitness().getValue().getAverage(), null);
            writeInfo(report, 1, iteration + 2, 5, bestIndividualEver.getCurrentFitness().getValue().getStandardDeviation(), null);
            writeInfo(report, 1, iteration + 2, 6, bestIndividualEver.getCurrentFitness().getValue().getNumberOfTrials(), null);
            writeInfo(report, 1, iteration + 2, 7, Arrays.deepToString(bestIndividualEver.getGenes()), null);

            HashMap<String, TrialInformation> extraMeasures = bestIndividualEver.getCurrentFitness().getExtraMeasures();
            List<String> extraTags = extraMeasures.keySet().stream().sorted().collect(Collectors.toList());
            for (int i = 0; i < extraTags.size(); i++) {
                String tag = extraTags.get(i);
                writeInfo(report, 1, iteration + 2, 8 + (i * 2), extraMeasures.get(tag).getAverage(), null);
                writeInfo(report, 1, iteration + 2, 8 + (i * 2) + 1, extraMeasures.get(tag).getStandardDeviation(), null);
            }

            writeInfo(report, 2, iteration + 1, 0, iteration, null);
            writeInfo(report, 2, iteration + 1, 1, bestIndividualEver.getCurrentFitness().getValue().getAverage(), null);
            writeInfo(report, 2, iteration + 1, 2, populationAverage, null);

            saveChanges(report);
        } catch (Exception ex) {
            Logger.getLogger(ExcelWriterGeneticAlgorithmListener.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } finally {
            try {
                input_document.close();
            } catch (IOException ex) {
                Logger.getLogger(ExcelWriterGeneticAlgorithmListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void printHeader(IIndividual bestIndividualEver) {
        FileInputStream input_document = null;
        try {
            input_document = new FileInputStream(new File(outputFilePath));
            XSSFWorkbook report = new XSSFWorkbook(input_document);
            input_document.close();

            XSSFSheet experimentInfoSheet = report.getSheetAt(1);
            CellStyle titleStyle = experimentInfoSheet.getRow(0).getCell(3).getCellStyle();
            CellStyle subtitleStyle = experimentInfoSheet.getRow(1).getCell(3).getCellStyle();

            HashMap<String, TrialInformation> extraMeasures = bestIndividualEver.getCurrentFitness().getExtraMeasures();
            List<String> tags = extraMeasures.keySet().stream().sorted().collect(Collectors.toList());
            for (int i = 0; i < tags.size(); i++) {
                String tag = tags.get(i);
                writeInfo(report, 1, 0, 8 + (i * 2), tag.toLowerCase(), titleStyle);

                writeInfo(report, 1, 1, 8 + (i * 2), "Trials average", subtitleStyle);
                writeInfo(report, 1, 1, 8 + (i * 2) + 1, "Trials Std. Dev.", subtitleStyle);
            }

            saveChanges(report);
        } catch (Exception ex) {
            Logger.getLogger(ExcelWriterGeneticAlgorithmListener.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } finally {
            try {
                input_document.close();
            } catch (IOException ex) {
                Logger.getLogger(ExcelWriterGeneticAlgorithmListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void insertInfoIntoExcel() {
        FileInputStream input_document = null;
        try {
            input_document = new FileInputStream(new File(outputFilePath));
            XSSFWorkbook report = new XSSFWorkbook(input_document);
            input_document.close();
            CellStyle defaultStyle = getDefaultCellStyle(report);
            writeInfo(report, 0, 1, 1, experiment.getCodeName(), defaultStyle);
            writeInfo(report, 0, 1, 2, experiment.getDescription(), defaultStyle);
            writeInfo(report, 0, 1, 3, new Date(), defaultStyle);
            writeInfo(report, 0, 1, 5, this.getClass().getPackage().getImplementationVersion(), defaultStyle);

            writeEcConfig(report);

            saveChanges(report);
        } catch (Exception ex) {
            Logger.getLogger(ExcelWriterGeneticAlgorithmListener.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } finally {
            try {
                input_document.close();
            } catch (IOException ex) {
                Logger.getLogger(ExcelWriterGeneticAlgorithmListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void writeInfo(XSSFWorkbook report, int sheetIndex, int rowIndex, int cellIndex, Object value, CellStyle alternativeStyle) {
        XSSFSheet experimentInfoSheet = report.getSheetAt(sheetIndex);
        XSSFRow row = experimentInfoSheet.getRow(rowIndex);
        if (row == null) {
            row = experimentInfoSheet.createRow(rowIndex);
        }

        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            cell = experimentInfoSheet.getRow(rowIndex).createCell(cellIndex);
            if (alternativeStyle == null) {
                cell.getCellStyle().setWrapText(true);
                report.getFontAt(cell.getCellStyle().getFontIndex()).setFontHeightInPoints((short) 10);
            } else {
                cell.setCellStyle(alternativeStyle);
            }
        }
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double || value instanceof Float || value instanceof Integer) {
            cell.setCellValue(Double.parseDouble(value.toString()));
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Trope) {
            String tropeName = ((Trope) value).name();
            cell.setCellValue(tropeName);
        }
    }

    private void saveChanges(XSSFWorkbook report) {
        FileOutputStream output_file = null;
        try {
            output_file = new FileOutputStream(new File(outputFilePath));
            report.write(output_file);
            output_file.close();
        } catch (Exception ex) {
            Logger.getLogger(ExcelWriterGeneticAlgorithmListener.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } finally {
            try {
                output_file.close();
            } catch (IOException ex) {
                Logger.getLogger(ExcelWriterGeneticAlgorithmListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private CellStyle getDefaultCellStyle(XSSFWorkbook report) {
        XSSFSheet experimentInfoSheet = report.getSheetAt(0);
        return experimentInfoSheet.getRow(1).getCell(0).getCellStyle();
    }

    private void writeEcConfig(XSSFWorkbook report) throws Exception {
        XSSFSheet experimentInfoSheet = report.getSheetAt(0);
        CellStyle titleStyle = experimentInfoSheet.getRow(3).getCell(0).getCellStyle();
        CellStyle valueStyle = getDefaultCellStyle(report);

        IGlobalConfigurationFactory globalConfigurationFactory
                = ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        CommonEcConfiguration ecConfig = globalConfigurationFactory.getCommonEcConfiguration();
        CommonAbmConfiguration abmConfig = globalConfigurationFactory.getCommonAbmConfiguration();

        int currentRow = 3;
        Field[] fields = ecConfig.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            writeInfo(report, 0, currentRow, i, fields[i].getName().toLowerCase().replace("_", " "), titleStyle);
            writeInfo(report, 0, currentRow + 1, i, fields[i].get(ecConfig), valueStyle);
        }

        currentRow = 6;
        fields = abmConfig.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            writeInfo(report, 0, currentRow, i, fields[i].getName().toLowerCase().replace("_", " "), titleStyle);
            writeInfo(report, 0, currentRow + 1, i, fields[i].get(abmConfig), valueStyle);
        }
    }
}
