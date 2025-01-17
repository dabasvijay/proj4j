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

import java.io.Serializable;

/**
 * An interface for the operation of transforming
 * a {@link ProjCoordinate} from one {@link CoordinateReferenceSystem}
 * into a different one.
 *
 * @author Martin Davis
 * @see CoordinateTransformFactory
 */
public interface CoordinateTransform extends Serializable {

    CoordinateReferenceSystem getSourceCRS();

    CoordinateReferenceSystem getTargetCRS();


    /**
     * Tranforms a coordinate from the source {@link CoordinateReferenceSystem}
     * to the target one.
     *
     * @param src the input coordinate to transform
     * @param tgt the transformed coordinate
     * @return the target coordinate which was passed in
     * @throws Proj4jException if a computation error is encountered
     */
    ProjCoordinate transform(ProjCoordinate src, ProjCoordinate tgt)
            throws Proj4jException;


}
