/*******************************************************************************
 * Copyright 2006, 2017 Jerry Huxtable, Martin Davis
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

/*
 * This file was semi-automatically converted from the public-domain USGS PROJ source.
 */
package io.github.dabasvijay.proj;

import io.github.dabasvijay.ProjCoordinate;
import io.github.dabasvijay.util.ProjectionMath;

public class SinusoidalProjection extends PseudoCylindricalProjection {
	
	public ProjCoordinate project(double lam, double phi, ProjCoordinate xy) {
		xy.x = lam * Math.cos(phi);
		xy.y = phi;
		return xy;
	}

	public ProjCoordinate projectInverse(double x, double y, ProjCoordinate lp) {
		lp.x = x / Math.cos(y);
		lp.y = y;
		return lp;
	}

	public double getWidth(double y) {
		return ProjectionMath.normalizeLongitude(Math.PI) * Math.cos(y);
	}

	public boolean hasInverse() {
		return true;
	}

	public boolean isEqualArea() {
     return true;
	}

	public String toString() {
		return "Sinusoidal";
	}

}
