/*******************************************************************************
 * Copyright 2009, 2017 Martin Davis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.dabasvijay.io;

import io.github.dabasvijay.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Proj4FileReader {

    public Proj4FileReader() {
        super();
    }

    public String[] readParametersFromFile(String authorityCode, String name)
            throws IOException {
        // TODO: read comment preceding CS string as CS description
        // TODO: use simpler parser than StreamTokenizer for speed and flexibility
        // TODO: parse CSes line-at-a-time (this allows preserving CS param string for later access)

        String filename = "proj4/nad/" + authorityCode.toLowerCase();
        InputStream inStr = Proj4FileReader.class.getClassLoader().getResourceAsStream(filename);
        if (inStr == null) {
            throw new IllegalStateException("Unable to access CRS file: " + filename);
        }
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inStr));
        String[] args;
        try {
            args = readFile(reader, name);
        } finally {
            if (reader != null)
                reader.close();
        }
        return args;
    }

    private StreamTokenizer createTokenizer(BufferedReader reader) {
        StreamTokenizer t = new StreamTokenizer(reader);
        t.commentChar('#');
        t.ordinaryChars('0', '9');
        t.ordinaryChars('.', '.');
        t.ordinaryChars('-', '-');
        t.ordinaryChars('+', '+');
        t.wordChars('0', '9');
        t.wordChars('\'', '\'');
        t.wordChars('"', '"');
        t.wordChars('_', '_');
        t.wordChars('.', '.');
        t.wordChars('-', '-');
        t.wordChars('+', '+');
        t.wordChars(',', ',');
        t.wordChars('@', '@');
        return t;
    }

    private String[] readFile(BufferedReader reader, String name) throws IOException {
        StreamTokenizer t = createTokenizer(reader);

        t.nextToken();
        while (t.ttype == '<') {
            Pair<String, List> pair = parseTokenizer(t);
            String crsName = pair.fst();
            List v = pair.snd();

            // found requested CRS?
            if (crsName.equals(name)) {
                String[] args = (String[]) v.toArray(new String[0]);
                return args;
            }
        }
        return null;
    }

    private static void addParam(List v, String key, String value) {
        String plusKey = key;
        if (!key.startsWith("+"))
            plusKey = "+" + key;

        if (value != null)
            v.add(plusKey + "=" + value);
        else
            v.add(plusKey);
    }

    /**
     * Gets the list of PROJ.4 parameters which define
     * the coordinate system specified by <tt>name</tt>.
     *
     * @param crsName the name of the coordinate system
     * @return the PROJ.4 projection parameters which define the coordinate system
     */
    public String[] getParameters(String crsName) {
        try {
            int p = crsName.indexOf(':');
            if (p >= 0) {
                String auth = crsName.substring(0, p);
                String id = crsName.substring(p + 1);
                return readParametersFromFile(auth, id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readEpsgCodeFromFile(String[] params) throws IOException {
        InputStream inStr = Proj4FileReader.class.getClassLoader().getResourceAsStream("proj4/nad/epsg");

        if (inStr == null) {
            throw new IllegalStateException("Unable to access CRS file: EPSG");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStr));

        StreamTokenizer t = createTokenizer(reader);

        t.nextToken();
        while (t.ttype == '<') {
            Pair<String, List> pair = parseTokenizer(t);
            String crsName = pair.fst();
            List v = pair.snd();

            String[] paramsParsed = (String[]) v.toArray(new String[0]);

            if(Arrays.equals(params, paramsParsed)) return crsName;
        }
        return null;
    }

    private static Pair<String, List> parseTokenizer(StreamTokenizer t) throws IOException {
        t.nextToken();
        if (t.ttype != StreamTokenizer.TT_WORD)
            throw new IOException(t.lineno() + ": Word expected after '<'");
        String crsName = t.sval;
        t.nextToken();
        if (t.ttype != '>')
            throw new IOException(t.lineno() + ": '>' expected");
        t.nextToken();
        List v = new ArrayList();

        while (t.ttype != '<') {
            if (t.ttype == '+')
                t.nextToken();
            if (t.ttype != StreamTokenizer.TT_WORD)
                throw new IOException(t.lineno() + ": Word expected after '+'");
            String key = t.sval;
            t.nextToken();


            // parse =arg, if any
            if (t.ttype == '=') {
                t.nextToken();
                //Removed check to allow for proj4 hack +nadgrids=@null
                //if ( t.ttype != StreamTokenizer.TT_WORD )
                //  throw new IOException( t.lineno()+": Value expected after '='" );
                String value = t.sval;
                t.nextToken();
                addParam(v, key, value);
            } else {
                // add param with no value
                addParam(v, key, null);
            }
        }
        t.nextToken();
        if (t.ttype != '>')
            throw new IOException(t.lineno() + ": '<>' expected");
        t.nextToken();

        return Pair.create(crsName, v);
    }
}
