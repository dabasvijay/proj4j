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

import java.io.Serializable;

public final class PolarCoordinate implements Serializable {

    public double lam, phi;

    public PolarCoordinate(PolarCoordinate that) {
        this(that.lam, that.phi);
    }

    public PolarCoordinate(double lam, double phi) {
        this.lam = lam;
        this.phi = phi;
    }

    @Override
    public String toString() {
        return String.format("<λ%f, φ%f>", lam, phi);
    }

    @Override
    public int hashCode() {
        return new Double(lam).hashCode() | (17 * new Double(phi).hashCode());
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof PolarCoordinate) {
            PolarCoordinate c = (PolarCoordinate) that;
            return lam == c.lam && phi == c.phi;
        } else {
            return false;
        }
    }
}
