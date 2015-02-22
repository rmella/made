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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;
import javax.naming.Binding;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.apache.commons.math3.analysis.function.Gaussian;

/**
 *
 * @author raiben@gmail.com
 */
public class MadePattern {

    private String label;
    private String regexp;
    private String condition;
    private String weight;
    private Pattern patt;
    private Object evaluator;
    private Method evaluatorMethod;

    public MadePattern(String label, String regexp, String weight, String condition) 
            throws Exception {
        this.label = label;
        this.regexp = regexp;
        this.weight = weight;
        this.condition = condition;
        patt = Pattern.compile(regexp);
        
        ClassPool pool = ClassPool.getDefault();
        CtClass evalClass = pool.makeClass("Eval"+label);

        String weight2 = weight.replace("gaussian", "com.velonuboso.made.core.MadePattern.gaussian");

        evalClass.addMethod(
                CtNewMethod.make(
                "public double getWeight(int p, int pm, int a, int am) {"
                + "return ("+condition+")?((double)"+weight2+"):0.0;"
                + "}", evalClass));
        Class clazz = evalClass.toClass();
         evaluator = clazz.newInstance();
         
         Class[] formalParams = new Class[] { int.class, int.class, int.class, int.class };
         evaluatorMethod = clazz.getDeclaredMethod("getWeight", formalParams);
    }

    public String getLabel() {
        return label;
    }

    public double getWeight(int p, int pm, int a, int am){
        try {
            return (Double) evaluatorMethod.invoke(evaluator, p, pm, a, am);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MadePattern.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MadePattern.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(MadePattern.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public String getRegexp() {
        return regexp;
    }
    
    public boolean evaluate(String log){
        Matcher m = patt.matcher(log);
        boolean found = false;
        //System.out.println("last pattern: "+this.regexp);
        while (m.find() && !found){
            found = true;
        }
        //System.out.println("end..." + found);
        return found;
    }

    public static double gaussian (double value, double target, double amplitude){
        Gaussian g = new Gaussian();
        return g.value(3*(target-value)/amplitude);
    }

}
