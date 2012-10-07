/**
 * Copyright 2011-2012 the original author or authors.
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
package org.informantproject.core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.informantproject.api.Logger;
import org.informantproject.api.LoggerFactory;
import org.informantproject.core.util.Static;

import com.google.common.base.Strings;
import com.google.common.io.Files;

/**
 * @author Trask Stalnaker
 * @since 0.5
 */
@Static
class DataDir {

    private static final Logger logger = LoggerFactory.getLogger(DataDir.class);

    private static final File BASE_DIR;

    static {
        File baseDir;
        try {
            URL agentJarLocation = DataDir.class.getProtectionDomain().getCodeSource()
                    .getLocation();
            if (agentJarLocation == null) {
                // probably running unit tests, will log warning below in getDataDir() if
                // internal.data.dir is not provided
                baseDir = null;
            } else {
                // by default use the same directory that the agent jar is in
                baseDir = new File(agentJarLocation.toURI()).getParentFile();
            }
        } catch (URISyntaxException e) {
            logger.error(e.getMessage(), e);
            baseDir = new File(".");
        }
        BASE_DIR = baseDir;
    }

    static File getDataDirWithNoWarning(Map<String, String> properties) {
        return getDataDir(properties, true);
    }

    static File getDataDir(Map<String, String> properties) {
        return getDataDir(properties, false);
    }

    private static File getDataDir(Map<String, String> properties, boolean disableWarnings) {
        String internalDataDir = properties.get("internal.data.dir");
        if (internalDataDir != null) {
            // used by unit tests
            return new File(internalDataDir);
        }
        File baseDir = BASE_DIR;
        if (baseDir == null) {
            if (!disableWarnings) {
                logger.warn("could not determine location of informant.jar, using process current"
                        + " directory as the base directory");
            }
            baseDir = new File(".");
        }
        String id = properties.get("id");
        if (Strings.isNullOrEmpty(id)) {
            return baseDir;
        }
        if (!id.matches("[a-zA-Z0-9 -_]+")) {
            if (!disableWarnings) {
                logger.warn("invalid informant.id '{}', id must include only alphanumeric"
                        + " characters, spaces, dashes underscores and forward slashes, proceeding"
                        + " instead with empty id", id);
            }
            return baseDir;
        }
        File dataDir = new File(baseDir, id);
        try {
            Files.createParentDirs(dataDir);
            return dataDir;
        } catch (IOException e) {
            if (!disableWarnings) {
                logger.warn("unable to create directory '{}', writing to base dir instead '{}'",
                        dataDir.getPath(), baseDir.getPath());
            }
            return baseDir;
        }
    }
}