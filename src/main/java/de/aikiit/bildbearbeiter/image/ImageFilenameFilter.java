/**
Copyright 2011, Aiki IT, FotoRenamer

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package de.aikiit.bildbearbeiter.image;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filter to prevent wrong files from being manipulated by this tool. Currently
 * only files with the JPG-extension are considered.
 *
 * @author hirsch
 * @version 2011-04-02, 13:52
 */
public class ImageFilenameFilter implements FilenameFilter {
    /**
     * This constant defines a JPG file extension for filtering.
     */
    private static final String JPG = ".jpg";

    /**
     * Filter filenames in a directory for images.
     *
     * @param dir  Directory to filter filenames in.
     * @param name Filename to filter.
     * @return Return <code>true</code> when the given File is a directory and
     *         the file is a JPG-picture.
     */
    public final boolean accept(final File dir, final String name) {
        return new File(dir, name).isFile() && name.toLowerCase().endsWith(JPG);
    }
}
