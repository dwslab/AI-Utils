package de.dwslab.ai.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

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

    /**
     * Read all lines from an {@code InputStream} as a {@code Stream}. Bytes from the input stream
     * are
     * decoded into characters using the {@link StandardCharsets#UTF_8 UTF-8} {@link Charset
     * charset}.
     *
     * <p>
     * This method works as if invoking it were equivalent to evaluating the expression:
     *
     * <pre>
     * {@code
     * Utils.lines(path, StandardCharsets.UTF_8)
     * }
     * </pre>
     *
     * @param in
     *            the input stream
     *
     * @return the lines from the input stream as a {@code Stream}
     *
     * @throws IOException
     *             if an I/O error occurs opening the file
     * @throws SecurityException
     *             In the case of the default provider, and a security manager is
     *             installed, the {@link SecurityManager#checkRead(String) checkRead} method is
     *             invoked to check read access to the file.
     *
     * @see Files#readAllLines(Path)
     */
    public static Stream<String> lines(InputStream in) {
        return lines(in, StandardCharsets.UTF_8);
    }

    /**
     * Read all lines lazily from an {@code InputStream} as a {@code Stream}.
     *
     * <p>
     * Bytes from the file are decoded into characters using the specified charset and the same line
     * terminators as specified by {@link Files#readAllLines(Path, Charset)} are supported.
     *
     * <p>
     * After this method returns, then any subsequent I/O exception that occurs while reading from
     * the file or when a malformed or unmappable byte sequence is read, is wrapped in an
     * {@link UncheckedIOException} that will be thrown from the {@link java.util.stream.Stream}
     * method that caused the read to take place. In case an {@code IOException} is thrown when
     * closing the file, it is also wrapped as an {@code UncheckedIOException}.
     *
     * <p>
     * The returned stream encapsulates a {@link Reader}. If timely disposal of file system
     * resources is required, the try-with-resources construct should be used to ensure that the
     * stream's {@link Stream#close close} method is invoked after the stream operations are
     * completed.
     *
     *
     * @param in
     *            the input stream
     * @param cs
     *            the charset to use for decoding
     *
     * @return the lines from the file as a {@code Stream}
     *
     * @throws SecurityException
     *             In the case of the default provider, and a security manager is
     *             installed, the {@link SecurityManager#checkRead(String) checkRead} method is
     *             invoked to check read access to the file.
     *
     * @see Files#readAllLines(Path, Charset)
     * @see Files#newBufferedReader(Path, Charset)
     * @see java.io.BufferedReader#lines()
     */
    @SuppressWarnings("resource")
    public static Stream<String> lines(InputStream in, Charset cs) {
        InputStreamReader reader = new InputStreamReader(in, cs);
        BufferedReader br = new BufferedReader(reader);
        try {
            return br.lines().onClose(asUncheckedRunnable(br));
        } catch (Error | RuntimeException e) {
            try {
                br.close();
            } catch (IOException ex) {
                try {
                    e.addSuppressed(ex);
                } catch (Throwable ignore) {
                    // ignore
                }
            }
            throw e;
        }
    }

    /**
     * Convert a Closeable to a Runnable by converting checked IOException
     * to UncheckedIOException.
     *
     * Copied from {@link Files#asUncheckedRunnable}
     *
     * @see Files#asUncheckedRunnable
     */
    private static Runnable asUncheckedRunnable(Closeable c) {
        return () -> {
            try {
                c.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }

}
