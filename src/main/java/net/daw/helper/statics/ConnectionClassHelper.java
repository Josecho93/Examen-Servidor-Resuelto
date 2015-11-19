/*
 * Copyright (c) 2015 by Rafael Angel Aznar Aparici (rafaaznar at gmail dot com)
 * 
 * openAUSIAS: The stunning micro-library that helps you to develop easily 
 *             AJAX web applications by using Java and jQuery
 * openAUSIAS is distributed under the MIT License (MIT)
 * Sources at https://github.com/rafaelaznar/openAUSIAS
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.daw.helper.statics;

public class ConnectionClassHelper {

    public static Boolean getOpenShift() {
        Boolean openshift = false;
        if (System.getenv() != null
                && System.getenv("OPENSHIFT_APP_NAME") != null
                && !System.getenv("OPENSHIFT_APP_NAME").equals("")) {
            openshift = true;
        }
        return openshift;
    }

    public static String getDatabaseName() {
        if (ConnectionClassHelper.getOpenShift()) {
            return System.getenv("OPENSHIFT_APP_NAME");

        } else {
            return "examen-serv-1ev-2015-2016";
            //return "openausiasblog";
            //return "openausias2015";
            //return "ausiasyield2014";
        }
    }

    public static String getDatabaseLogin() {
        if (ConnectionClassHelper.getOpenShift()) {
            //return System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
            return "el username esta en la web de openshift";
        } else {
            return "root";
        }
    }

    public static String getDatabasePassword() {
        if (ConnectionClassHelper.getOpenShift()) {
            //return System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");
            return "la contraseña esta en la web de openshift";
        } else {
            return "bitnami";
        }
    }

    public static String getDatabasePort() {
        if (ConnectionClassHelper.getOpenShift()) {
            //return System.getenv("OPENSHIFT_MYSQL_DB_PORT");
            //return System.getenv("OPENSHIFT_DIY_PORT");
            return "3306";
        } else {
            return "3306";
        }
    }

    public static String getDatabaseHost() {
        if (ConnectionClassHelper.getOpenShift()) {
            //return System.getenv("OPENSHIFT_MYSQL_DB_IP");
            //return System.getenv("OPENSHIFT_DIY_IP");
            return "la ip aparece en la ventana de entrada al phpMyadmin";

        } else {
            return "127.0.0.1";
        }
    }

    public static String getConnectionChain() {
        if (ConnectionClassHelper.getOpenShift()) {
            //return "jdbc:" + System.getenv("OPENSHIFT_MYSQL_DB_URL") + ConnectionClassHelper.getDatabaseName();
            return "jdbc:mysql://" + ConnectionClassHelper.getDatabaseHost() + ":" + ConnectionClassHelper.getDatabasePort() + "/" + ConnectionClassHelper.getDatabaseName();
        } else {
            return "jdbc:mysql://" + ConnectionClassHelper.getDatabaseHost() + ":" + ConnectionClassHelper.getDatabasePort() + "/" + ConnectionClassHelper.getDatabaseName();
        }
    }
    
    
    
}
