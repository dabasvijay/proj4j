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
package io.github.dabasvijay;

import io.github.dabasvijay.datum.Datum;
import io.github.dabasvijay.datum.Ellipsoid;
import io.github.dabasvijay.proj.LongLatProjection;
import io.github.dabasvijay.proj.Projection;
import io.github.dabasvijay.units.Unit;
import io.github.dabasvijay.units.Units;

import java.util.Objects;

/**
 * Represents a projected or geodetic geospatial coordinate system,
 * to which coordinates may be referenced.
 * A coordinate system is defined by the following things:
 * <ul>
 * <li>an {@link Ellipsoid} specifies how the shape of the Earth is approximated
 * <li>a {@link Datum} provides the mapping from the ellipsoid to
 * actual locations on the earth
 * <li>a {@link Projection} method maps the ellpsoidal surface to a planar space.
 * (The projection method may be null in the case of geodetic coordinate systems).
 * <li>a {@link Unit} indicates how the ordinate values
 * of coordinates are interpreted
 * </ul>
 *
 * @author Martin Davis
 * @see CRSFactory
 */
// CoordinateReferenceSystem corresponds to the PJ struct from proj.4
public class CoordinateReferenceSystem implements java.io.Serializable {

    // allows specifying transformations which convert to/from Geographic coordinates on the same datum
    public static final CoordinateReferenceSystem CS_GEO = new CoordinateReferenceSystem("CS_GEO", null, null, null);

    //TODO: add metadata like authority, id, name, parameter string, datum, ellipsoid, datum shift parameters

    private String name;
    private String[] params;
    private Datum datum;
    private Projection proj;

    public CoordinateReferenceSystem(String name, String[] params, Datum datum, Projection proj) {
        this.name = name;
        this.params = params;
        this.datum = datum;
        this.proj = proj;

        if (name == null) {
            String projName = "null-proj";
            if (proj != null)
                projName = proj.getName();
            this.name = projName + "-CS";
        }
    }

    public String getName() {
        return name;
    }

    public String[] getParameters() {
        return params;
    }

    public Datum getDatum() {
        return datum;
    }

    public Projection getProjection() {
        return proj;
    }

    public String getParameterString() {
        if (params == null) return "";

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < params.length; i++) {
            buf.append(params[i]);
            buf.append(" ");
        }
        return buf.toString();
    }

    public Boolean isGeographic() {
        return proj.isGeographic();
    }

    /**
     * Creates a geographic (unprojected) {@link CoordinateReferenceSystem}
     * based on the {@link Datum} of this CRS.
     * This is useful for defining {@link CoordinateTransform}s
     * to and from geographic coordinate systems,
     * where no datum transformation is required.
     * The {@link Units} of the geographic CRS are set to {@link Units#DEGREES}.
     *
     * @return a geographic CoordinateReferenceSystem based on the datum of this CRS
     */
    public CoordinateReferenceSystem createGeographic() {
        Datum datum = getDatum();
        Projection geoProj = new LongLatProjection();
        geoProj.setEllipsoid(getProjection().getEllipsoid());
        geoProj.setUnits(Units.DEGREES);
        geoProj.initialize();
        return new CoordinateReferenceSystem("GEO-" + datum.getCode(), null, datum, geoProj);
    }

    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that instanceof CoordinateReferenceSystem) {
            CoordinateReferenceSystem cr = (CoordinateReferenceSystem) that;
            // Projection equality contains Ellipsoid and Unit equality
            return datum.isEqual(cr.getDatum()) && proj.equals(cr.proj);
        }
        return false;
    }

    @Override
	public int hashCode() {
			return Objects.hash(datum, proj);
	}
}
