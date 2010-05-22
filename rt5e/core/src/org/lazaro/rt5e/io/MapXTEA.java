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

import org.lazaro.rt5e.Constants;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lazaro
 */
public class MapXTEA {
    private Map<Integer, int[]> xteas = new HashMap<Integer, int[]>();

    public MapXTEA() {
        try {
            if (!loadPackedFile()) {
                createPackedFile();
            }
            System.out.println("Loaded " + xteas.size() + " map XTEA keys");
        } catch (IOException e) {
            System.err.println("Failed to load map xteas!");
            e.printStackTrace();
        }
    }

    public void createPackedFile() throws IOException {
        DataOutputStream output = new DataOutputStream(new FileOutputStream(Constants.MAP_XTEA_FILE));
        File directory = new File(Constants.MAP_XTEA_DIR);
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isFile()) {
                    BufferedReader input = new BufferedReader(new FileReader(
                            file));
                    int id = Integer.parseInt(file.getName().substring(0,
                            file.getName().indexOf(".")));
                    output.writeInt(id);
                    int[] keys = new int[4];
                    for (int i = 0; i < 4; i++) {
                        keys[i] = Integer.parseInt(input.readLine());
                        output.writeInt(keys[i]);
                    }
                    input.close();
                    xteas.put(id, keys);
                }
            }
        }
        output.close();
    }

    public int[] getKey(int region) {
        return xteas.get(region);
    }

    public boolean loadPackedFile() throws IOException {
        File file = new File(Constants.MAP_XTEA_FILE);
        if (!file.exists()) {
            return false;
        }
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        for (int i = 0; i < file.length() / (4 + (4 * 4)); i++) {
            int id = in.readInt();
            int[] key = new int[4];
            for (int i2 = 0; i2 < 4; i2++) {
                key[i2] = in.readInt();
            }
            xteas.put(id, key);
        }
        return true;
    }
}
