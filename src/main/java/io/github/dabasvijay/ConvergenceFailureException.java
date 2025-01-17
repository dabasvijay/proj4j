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
 *******************************************************************************/

package io.github.dabasvijay;

/**
 * Signals that an interative mathematical algorithm has failed to converge.
 * This is usually due to values exceeding the
 * allowable bounds for the computation in which they are being used.
 *
 * @author mbdavis
 */
public class ConvergenceFailureException extends Proj4jException {

    public ConvergenceFailureException(String message) {
        super(message);
    }

}
