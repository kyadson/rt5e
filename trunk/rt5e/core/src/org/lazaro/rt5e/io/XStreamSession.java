/**
 * Copyright (C) 2010 Lazaro Brito
 *
 * This file is part of RT5E.
 *
 * RT5E is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RT5E is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RT5E.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.lazaro.rt5e.io;

import com.thoughtworks.xstream.XStream;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Lazaro
 */
public class XStreamSession {
    private final XStream xStream;

    public XStreamSession() {
        xStream = new XStream();
    }

    /**
     * Loads the class aliases that simplify XML output.
     *
     * @param filePath The path of the XML file which stores the aliases.
     * @throws IOException            Error reading file.
     * @throws ClassNotFoundException Could not find the class specified in the XML file.
     */
    public void loadAliases(String filePath) throws IOException,
            ClassNotFoundException {
        Properties aliases = new Properties();
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream inputStream = new FileInputStream(file);
            aliases.loadFromXML(inputStream);
        }
        for (Enumeration<?> e = aliases.propertyNames(); e.hasMoreElements();) {
            String alias = (String) e.nextElement();
            Class<?> aliasClass = Class.forName((String) aliases.get(alias));
            xStream.alias(alias, aliasClass);
        }
    }

    /**
     * Reads an <code>Object</code> from an XML file.
     *
     * @param filePath The path of the XML file.
     * @return The object.
     * @throws IOException Error reading file.
     */
    public Object readObject(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            InputStream inputStream = new FileInputStream(file);
            if (filePath.endsWith(".gz")) {
                inputStream = new GZIPInputStream(inputStream);
            }
            Object object = xStream.fromXML(inputStream);
            inputStream.close();
            return object;
        }
        return null;
    }

    /**
     * Writes an XML file from an <code>Object</code>.
     *
     * @param object   The object to write.
     * @param filePath The path to the XML file.
     * @throws IOException Error writing file.
     */
    public void writeObject(Object object, String filePath) throws IOException {
        File file = new File(filePath);
        OutputStream outputStream = new FileOutputStream(file);
        if (filePath.endsWith(".gz")) {
            outputStream = new GZIPOutputStream(outputStream);
        }
        xStream.toXML(object, outputStream);
        outputStream.close();
    }
}
