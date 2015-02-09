package de.dwslab.ai.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public final class Utils {

    /**
     * Creates an empty file in the default temporary-file directory, using the given prefix and
     * suffix to generate its name. The deleteOnExit property is set by default.
     *
     * @param prefix
     *            The prefix string to be used in generating the file's name; must be at least three
     *            characters long
     * @param suffix
     *            The suffix string to be used in generating the file's name; may be null, in which
     *            case the suffix ".tmp" will be used
     * @return the path to the newly created file that did not exist before this method was invoked
     *
     * @throws IllegalArgumentException
     *             - If the prefix argument contains fewer than three characters
     * @throws IOException
     *             - If a file could not be created
     * @throws SecurityException
     *             - If a security manager exists and its
     *             java.lang.SecurityManager.checkWrite(java.lang.String) method does not allow a
     *             file to be created
     *
     * @see {@link File#createTempFile(String, String)}
     */
    public static File createTempFile(String prefix, String suffix) throws IOException {
        File tempFile = File.createTempFile(prefix, suffix);
        tempFile.deleteOnExit();
        return tempFile;
    }

    /**
     * Creates an empty file in the default temporary-file directory, using the given prefix and
     * suffix to generate its name. The deleteOnExit property is set by default.
     *
     * @param prefix
     *            The prefix string to be used in generating the file's name; must be at least three
     *            characters long
     * @param suffix
     *            The suffix string to be used in generating the file's name; may be null, in which
     *            case the suffix ".tmp" will be used
     * @return the path to the newly created file that did not exist before this method was invoked
     *
     * @throws IllegalArgumentException
     *             - If the prefix argument contains fewer than three characters
     * @throws IOException
     *             - If a file could not be created
     * @throws SecurityException
     *             - If a security manager exists and its
     *             java.lang.SecurityManager.checkWrite(java.lang.String) method does not allow a
     *             file to be created
     * 
     * @see {@link File#createTempFile(String, String)}
     */
    public static Path createTempPath(String prefix, String suffix) throws IOException {
        File tempFile = File.createTempFile(prefix, suffix);
        tempFile.deleteOnExit();
        return tempFile.toPath();
    }

}
