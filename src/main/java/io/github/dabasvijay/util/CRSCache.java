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
package io.github.dabasvijay.util;

import io.github.dabasvijay.*;


import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class CRSCache {
    private static CRSFactory crsFactory = new CRSFactory();

    private ConcurrentHashMap<String, CoordinateReferenceSystem> crsCache;
    private ConcurrentHashMap<String, String> epsgCache;

    public CRSCache() {
        crsCache = new ConcurrentHashMap<>();
        epsgCache = new ConcurrentHashMap<>();
    }

    public CRSCache(ConcurrentHashMap<String, CoordinateReferenceSystem> crsCache, ConcurrentHashMap<String, String> epsgCache) {
        this.crsCache = crsCache;
        this.epsgCache = epsgCache;
    }

    public CoordinateReferenceSystem createFromName(String name)
            throws UnsupportedParameterException, InvalidValueException, UnknownAuthorityCodeException {
        CoordinateReferenceSystem res = crsCache.get(name);
        if(res != null) return res;
        return computeIfAbsent(name, crsCache,  crsFactory.createFromName(name));

    }

    public CoordinateReferenceSystem createFromParameters(String name, String paramStr)
            throws UnsupportedParameterException, InvalidValueException {
        String nonNullName = name == null ? "" : name;
        String key = nonNullName + paramStr;
        CoordinateReferenceSystem res = crsCache.get(key);
        if(res != null) return res;
        return computeIfAbsent(key, crsCache,  crsFactory.createFromParameters(name, paramStr));
    }
    private CoordinateReferenceSystem  computeIfAbsent(final String key, ConcurrentHashMap<String, CoordinateReferenceSystem> map, final CoordinateReferenceSystem newValue ) {
        CoordinateReferenceSystem v = map.get(key);
        if (v == null) {
            v = (v = map.putIfAbsent(key, newValue)) == null ? newValue : v;
        }
        return v;
    }

    public CoordinateReferenceSystem createFromParameters(String name, String[] params)
            throws UnsupportedParameterException, InvalidValueException {
        String nonNullName = name == null ? "" : name;
        String key = nonNullName + String.join(" ", params);
        CoordinateReferenceSystem res = crsCache.get(key);
        if(res != null) return res;
        return computeIfAbsent(key, crsCache,  crsFactory.createFromParameters(name, params));

    }

    public String readEpsgFromParameters(String paramStr) {
        String res = epsgCache.get(paramStr);
        if(res != null) return res;
        final String newValue = readWithException(crsFactory, paramStr);
        return computeIfAbsentWithTry(paramStr, epsgCache, newValue);
    }

    private String readWithException(final CRSFactory crsFactory, final String paramStr) {
        try {
            return crsFactory.readEpsgFromParameters(paramStr);
        } catch (IOException e) {
            return null;
        }



    }

    private String  computeIfAbsentWithTry(final String key, ConcurrentHashMap<String, String> map, final String newValue ) {
        String v = map.get(key);
        if (v == null) {
            v = (v = map.putIfAbsent(key, newValue)) == null ? newValue : v;
        }
        return v;
    }

    public String readEpsgFromParameters(String[] params) {
        String paramStr = String.join(" ", params);
        String res = epsgCache.get(paramStr);
        if(res != null) return res;
        final String newValue = readWithException(crsFactory, paramStr);
        return computeIfAbsentWithTry(paramStr, epsgCache, newValue);
    }
}
