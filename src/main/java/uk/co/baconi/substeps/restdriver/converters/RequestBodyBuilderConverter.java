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

package uk.co.baconi.substeps.restdriver.converters;

import com.technophobia.substeps.model.parameter.Converter;
import uk.co.baconi.substeps.restdriver.builders.RequestBodyBuilder;
import uk.co.baconi.substeps.restdriver.builders.impl.FormRequestBodyBuilder;
import uk.co.baconi.substeps.restdriver.builders.impl.JsonArrayRequestBodyBuilder;
import uk.co.baconi.substeps.restdriver.builders.impl.JsonFromUriRequestBodyBuilder;
import uk.co.baconi.substeps.restdriver.builders.impl.JsonObjectRequestBodyBuilder;

public class RequestBodyBuilderConverter implements Converter<RequestBodyBuilder> {

    @Override
    public boolean canConvert(final Class<?> cls) {
        return cls == RequestBodyBuilder.class;
    }

    @Override
    public RequestBodyBuilder convert(final String value) {

        final RequestBodyBuilder builder;

        if (value == null) {
            builder = new FormRequestBodyBuilder();
        } else {
            switch (value) {
                case "JsonObjectRequestBodyBuilder": {
                    builder = new JsonObjectRequestBodyBuilder();
                    break;
                }
                case "JsonArrayRequestBodyBuilder": {
                    builder = new JsonArrayRequestBodyBuilder();
                    break;
                }
                case "JsonFromUriRequestBodyBuilder": {
                    builder = new JsonFromUriRequestBodyBuilder();
                    break;
                }
                case "FormRequestBodyBuilder": {
                    builder = new FormRequestBodyBuilder();
                    break;
                }
                default: {
                    builder = new FormRequestBodyBuilder();
                    break;
                }
            }
        }

        return builder;
    }

}
