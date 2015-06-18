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
package com.velonuboso.made.prettytestrunner;

import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class PrettyTestRunner {

    private List<Class> testClasses;

    public static void main(String args[]) throws IOException {
        PrettyTestRunner runner = new PrettyTestRunner();
        runner.run();
    }

    private void initializeArrayOfTestClasses() {
        testClasses = new ArrayList<>();
    }

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private void includeTestClassesFromClassPath() {
        final String PACKAGE_NAME = "com.velonuboso.made";
        final String CLASS_TEST_ENDING = "test";
        
        try {
            ClassLoader loader = getClassLoader();
            ClassPath classpath = ClassPath.from(loader);
            for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClassesRecursive(PACKAGE_NAME)) {
                if (classInfo.getName().toLowerCase().endsWith(CLASS_TEST_ENDING)) {
                    testClasses.add(loader.loadClass(classInfo.getName()));
                }
            }
        } catch (IOException | ClassNotFoundException exception) {
            logException(exception);
        }
    }

    private void run() {
        initializeArrayOfTestClasses();
        includeTestClassesFromClassPath();
        runTestClasses();
    }

    private void runTestClasses() {
        for (Class testClass : testClasses) {
            runSingleTestClass(testClass);
        }
    }

    private void runSingleTestClass(Class testClass) {
        try {
            BlockJUnit4ClassRunner runner = new BlockJUnit4ClassRunner(testClass);
            runner.run(new PrettyNotifier());
        } catch (InitializationError ex) {
            logException(ex);
        }
    }

    private void logException(Exception ex) {
        Logger.getLogger(PrettyTestRunner.class.getName()).log(Level.SEVERE, null, ex);
    }
}
