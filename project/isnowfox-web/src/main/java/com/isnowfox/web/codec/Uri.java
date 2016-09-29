package com.isnowfox.web.codec;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpConstants;
import org.jboss.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * uri信息
 * 这个代码来自 netty.io 的QueryStringDecoder
 */

public final class Uri {
    public static final int DEFAULT_MAX_PARAMS = 1024;
    private Charset charset;
    private String uri;
    private int maxParams;
    private boolean isDir;
    private String path;
    private String fileName;
    private String fileExtensionName;
    private String pattern;
    private Map<String, List<String>> params;
    private int nParams;

//    /**
//     * Creates a new decoder that decodes the specified URI encoded in the
//     * specified charset.
//     */
//    public Uri(String uri, Charset charset) {
//        this(uri, charset, DEFAULT_MAX_PARAMS);
//    }

    /**
     * Creates a new decoder that decodes the specified URI encoded in the
     * specified charset.
     *
     * @param string
     */
    public Uri(String uri, Charset charset, int maxParams, String defaultFileName) {
        reset(uri, charset, maxParams, defaultFileName);
    }


    public void reset(String uri, Charset charset, int maxParams, String defaultFileName) {
        if (uri == null) {
            throw new NullPointerException("uri");
        }
        if (uri.isEmpty()) {
            throw new NullPointerException("uri empty");
        }
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        if (maxParams <= 0) {
            throw new IllegalArgumentException(
                    "maxParams: " + maxParams + " (expected: a positive integer)");
        }

        // http://en.wikipedia.org/wiki/Query_string
        this.uri = uri.replace(';', '&');
        this.charset = charset;
        this.maxParams = maxParams;

        int pathEndPos = uri.indexOf('?');
        if (pathEndPos < 0) {
            path = uri;
        } else {
            path = uri.substring(0, pathEndPos);
        }
        isDir = path.charAt(path.length() - 1) == '/';
        if (isDir) {
            fileName = defaultFileName;
            int extPos = defaultFileName.lastIndexOf('.');
            if (extPos < 0) {
                fileExtensionName = null;
                pattern = defaultFileName;
            } else {
                fileExtensionName = defaultFileName.substring(extPos + 1);
                pattern = defaultFileName.substring(0, extPos);
            }
        } else {
            int namePos = path.lastIndexOf('/');
            if (namePos < 0) {
                fileName = path;
            } else {
                fileName = path.substring(namePos + 1);
            }
            int extPos = path.lastIndexOf('.');
            if (extPos < 0) {
                fileExtensionName = null;
                pattern = path;
            } else {
                fileExtensionName = path.substring(extPos + 1);
                pattern = path.substring(0, extPos);
            }
        }

    }

    /**
     * 是否指定的扩展类型
     *
     * @param suffix
     * @return
     */
    public boolean isExtensionType(String suffix) {
        return StringUtils.isEmpty(suffix) ? StringUtils.isEmpty(fileExtensionName) : suffix.equals(fileExtensionName);
    }

    /**
     * Returns the decoded path string of the URI.
     */
    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileExtensionName() {
        return fileExtensionName;
    }

    /**
     * Returns the decoded key-value parameter pairs of the URI.
     */
    public Map<String, List<String>> getParameters() {
        if (params == null) {
            int pathLength = getPath().length();
            if (uri.length() == pathLength) {
                return Collections.emptyMap();
            }
            decodeParams(uri.substring(pathLength + 1));
        }
        return params;
    }

    public boolean isDir() {
        return isDir;
    }

    private void decodeParams(String s) {
        Map<String, List<String>> params = this.params = new LinkedHashMap<String, List<String>>();
        nParams = 0;
        String name = null;
        int pos = 0; // Beginning of the unprocessed region
        int i;       // End of the unprocessed region
        char c;      // Current character
        for (i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            if (c == '=' && name == null) {
                if (pos != i) {
                    name = decodeComponent(s.substring(pos, i), charset);
                }
                pos = i + 1;
            } else if (c == '&') {
                if (name == null && pos != i) {
                    // We haven't seen an `=' so far but moved forward.
                    // Must be a param of the form '&a&' so add it with
                    // an empty value.
                    if (!addParam(params, decodeComponent(s.substring(pos, i), charset), "")) {
                        return;
                    }
                } else if (name != null) {
                    if (!addParam(params, name, decodeComponent(s.substring(pos, i), charset))) {
                        return;
                    }
                    name = null;
                }
                pos = i + 1;
            }
        }

        if (pos != i) {  // Are there characters we haven't dealt with?
            if (name == null) {     // Yes and we haven't seen any `='.
                addParam(params, decodeComponent(s.substring(pos, i), charset), "");
            } else {                // Yes and this must be the last value.
                addParam(params, name, decodeComponent(s.substring(pos, i), charset));
            }
        } else if (name != null) {  // Have we seen a name without value?
            addParam(params, name, "");
        }
    }

    private boolean addParam(Map<String, List<String>> params, String name, String value) {
        if (nParams >= maxParams) {
            return false;
        }

        List<String> values = params.get(name);
        if (values == null) {
            values = new ArrayList<String>(1);  // Often there's only 1 value.
            params.put(name, values);
        }
        values.add(value);
        nParams++;
        return true;
    }

    /**
     * Decodes a bit of an URL encoded by a browser.
     * <p>
     * This is equivalent to calling {@link #decodeComponent(String, Charset)}
     * with the UTF-8 charset (recommended to comply with RFC 3986, Section 2).
     *
     * @param s The string to decode (can be empty).
     * @return The decoded string, or {@code s} if there's nothing to decode.
     * If the string to decode is {@code null}, returns an empty string.
     * @throws IllegalArgumentException if the string contains a malformed
     *                                  escape sequence.
     */
    public static String decodeComponent(final String s) {
        return decodeComponent(s, HttpConstants.DEFAULT_CHARSET);
    }

    /**
     * Decodes a bit of an URL encoded by a browser.
     * <p>
     * The string is expected to be encoded as per RFC 3986, Section 2.
     * This is the encoding used by JavaScript functions {@code encodeURI}
     * and {@code encodeURIComponent}, but not {@code escape}.  For example
     * in this encoding, &eacute; (in Unicode {@code U+00E9} or in UTF-8
     * {@code 0xC3 0xA9}) is encoded as {@code %C3%A9} or {@code %c3%a9}.
     * <p>
     * This is essentially equivalent to calling
     * {@link URLDecoder#decode(String, String) URLDecoder.decode(s, charset.name())}
     * except that it's over 2x faster and generates less garbage for the GC.
     * Actually this function doesn't allocate any memory if there's nothing
     * to decode, the argument itself is returned.
     *
     * @param s       The string to decode (can be empty).
     * @param charset The charset to use to decode the string (should really
     *                be {@link CharsetUtil#UTF_8}.
     * @return The decoded string, or {@code s} if there's nothing to decode.
     * If the string to decode is {@code null}, returns an empty string.
     * @throws IllegalArgumentException if the string contains a malformed
     *                                  escape sequence.
     */
    @SuppressWarnings("fallthrough")
    public static String decodeComponent(final String s,
                                         final Charset charset) {
        if (s == null) {
            return "";
        }
        final int size = s.length();
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            final char c = s.charAt(i);
            switch (c) {
                case '%':
                    i++;  // We can skip at least one char, e.g. `%%'.
                    // Fall through.
                case '+':
                    modified = true;
                    break;
            }
        }
        if (!modified) {
            return s;
        }
        final byte[] buf = new byte[size];
        int pos = 0;  // position in `buf'.
        for (int i = 0; i < size; i++) {
            char c = s.charAt(i);
            switch (c) {
                case '+':
                    buf[pos++] = ' ';  // "+" -> " "
                    break;
                case '%':
                    if (i == size - 1) {
                        throw new IllegalArgumentException("unterminated escape"
                                + " sequence at end of string: " + s);
                    }
                    c = s.charAt(++i);
                    if (c == '%') {
                        buf[pos++] = '%';  // "%%" -> "%"
                        break;
                    }

                    if (i == size - 1) {
                        throw new IllegalArgumentException("partial escape"
                                + " sequence at end of string: " + s);
                    }
                    c = decodeHexNibble(c);
                    final char c2 = decodeHexNibble(s.charAt(++i));
                    if (c == Character.MAX_VALUE || c2 == Character.MAX_VALUE) {
                        throw new IllegalArgumentException(
                                "invalid escape sequence `%" + s.charAt(i - 1)
                                        + s.charAt(i) + "' at index " + (i - 2)
                                        + " of: " + s);
                    }
                    c = (char) (c * 16 + c2);
                    // Fall through.
                default:
                    buf[pos++] = (byte) c;
                    break;
            }
        }
        try {
            return new String(buf, 0, pos, charset.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("unsupported encoding: " + charset.name(), e);
        }
    }

    /**
     * Helper to decode half of a hexadecimal number from a string.
     *
     * @param c The ASCII character of the hexadecimal number to decode.
     *          Must be in the range {@code [0-9a-fA-F]}.
     * @return The hexadecimal value represented in the ASCII character
     * given, or {@link Character#MAX_VALUE} if the character is invalid.
     */
    private static char decodeHexNibble(final char c) {
        if ('0' <= c && c <= '9') {
            return (char) (c - '0');
        } else if ('a' <= c && c <= 'f') {
            return (char) (c - 'a' + 10);
        } else if ('A' <= c && c <= 'F') {
            return (char) (c - 'A' + 10);
        } else {
            return Character.MAX_VALUE;
        }
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return "Uri [charset=" + charset + ", uri=" + uri + ", maxParams="
                + maxParams + ", isDir=" + isDir + ", path=" + path
                + ", fileName=" + fileName + ", fileExtensionName="
                + fileExtensionName + ", params=" + params + ", nParams="
                + nParams + "]";
    }
}