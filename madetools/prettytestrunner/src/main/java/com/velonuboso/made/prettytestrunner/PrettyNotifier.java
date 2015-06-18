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

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class PrettyNotifier extends RunNotifier {

    private int testCounter = 0;
    private Failure failure = null;
    private String testAsRequirement = null;
    private String classAsRequirement = null;

    public static final String TEST_CLASS_BEGIN = "\nRequisites of the %s\n";
    public static final String TEST_OK_TEMPLATE = "%3d: OK   -> %s\n";
    public static final String TEST_FAIL_TEMPLATE = "%3d: FAIL -> %s\n";

    @Override
    public void fireTestStarted(Description description) throws StoppedByUserException {
        super.fireTestStarted(description);
        failure = null;
        setTestAsRequirement(description.getMethodName());
        if (classAsRequirement == null) {
            setClassAsRequirement(description.getTestClass());
            printClassBegin();
        }
        testCounter++;
    }

    @Override
    public void fireTestFailure(Failure failure) {
        super.fireTestFailure(failure);
        this.failure = failure;
    }

    @Override
    public void fireTestFinished(Description description) {
        super.fireTestFinished(description);
        if (failure == null) {
            printTestOk();
        } else {
            printTestFailure();
        }
    }

    private void printTestOk() {
        System.out.format(TEST_OK_TEMPLATE, testCounter, testAsRequirement);
    }

    private void printTestFailure() {
        System.out.format(TEST_FAIL_TEMPLATE, testCounter, testAsRequirement);
    }

    private void printClassBegin() {
        System.out.format(TEST_CLASS_BEGIN, classAsRequirement);
    }

    private void setTestAsRequirement(String methodName) {
        testAsRequirement = removeItUtFromTestName(methodName);
        testAsRequirement = replaceMethodSeparatorInTestName(testAsRequirement);
        testAsRequirement = replaceWordSeparatorInTestName(testAsRequirement);
    }

    private String replaceWordSeparatorInTestName(String methodName) {
        return methodName.replace("_", " ");
    }

    private String replaceMethodSeparatorInTestName(String methodName) {
        return methodName.replace("__", ": ");
    }

    private static String removeItUtFromTestName(String methodName) {
        return methodName.replaceAll("^[IU]T_", "");
    }

    private void setClassAsRequirement(Class testClass) {
        classAsRequirement = testClass.getSimpleName();
        classAsRequirement = removeTestWord(classAsRequirement);
        classAsRequirement = splitCamelCase(classAsRequirement);
    }

    static String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    private String removeTestWord(String testName) {
        return testName.replaceAll("Test$", "");
    }
}
