/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.informant.container.trace;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import checkers.nullness.quals.Nullable;

/**
 * @author Trask Stalnaker
 * @since 0.5
 */
public interface TraceService {

    @Nullable
    Trace getLastTrace() throws Exception;

    @Nullable
    Trace getLastTraceSummary() throws Exception;

    @Nullable
    Trace getActiveTrace(int timeout, TimeUnit unit) throws Exception;

    @Nullable
    Trace getActiveTraceSummary(int timeout, TimeUnit unit) throws Exception;

    int getNumPendingCompleteTraces() throws Exception;

    long getNumStoredSnapshots() throws Exception;

    InputStream getTraceExport(String string) throws Exception;
}
