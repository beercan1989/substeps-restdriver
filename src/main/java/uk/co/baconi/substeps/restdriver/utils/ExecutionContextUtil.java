/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package uk.co.baconi.substeps.restdriver.utils;

import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.runner.ExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExecutionContextUtil {

    private ExecutionContextUtil() {
    }

    @SuppressWarnings("unchecked")
    public static <A> List<A> getList(final Scope scope, final String key) {
        final Object result = ExecutionContext.get(scope, key);

        if (result instanceof List) {
            return (List<A>) result;
        } else {
            return new ArrayList<>();
        }
    }

    public static <A> Optional<A> get(final Scope scope, final String key, final Class<A> expectedType) {
        final Object result = ExecutionContext.get(scope, key);

        if (result != null && expectedType.isInstance(result)) {
            return Optional.of(expectedType.cast(result));
        } else {
            return Optional.empty();
        }
    }

    public static <A> void put(final Scope scope, final String key, final A value) {
        ExecutionContext.put(scope, key, value);
    }

    public static void clear(final Scope scope) {
        ExecutionContext.clear(scope);
    }

}