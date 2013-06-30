/*
 * Copyright 2013 Rubén Héctor García <raiben@gmail.com>.
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

package com.velonuboso.basicmade;

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

/**
 *
 * @author Ruben
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
        evalClass.addMethod(
                CtNewMethod.make(
                "public double getWeight(int p, int pm, int a, int am) {"
                + "return ("+condition+")?((double)"+weight+"):0.0;"
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
        while (m.find() && !found){
            found = true;
        }
        return found;
    }
}
