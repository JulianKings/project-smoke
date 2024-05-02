package com.smoke.utilities.classes;

public final class StringUtil
{
    private StringUtil()
    {
    }

    /**
     * Concatenates strings.
     * @param strings strings to be concatenated
     * @return concatenated string
     */
    public static String concat(final String... strings)
    {
        final StringBuilder sbString = new StringBuilder();
        for (final String string : strings)
        {
            sbString.append(string);
        }
        return sbString.toString();
    }

    /**
     * Creates new string builder with size initializated to <code>sizeHint</code>, unless total length of strings is greater than <code>sizeHint</code>.
     * @param sizeHint hint for string builder size allocation
     * @param strings strings to be appended
     * @return created string builder
     */
    public static StringBuilder startAppend(final int sizeHint, final String... strings)
    {
        final int length = getLength(strings);
        final StringBuilder sbString = new StringBuilder(sizeHint > length ? sizeHint : length);
        for (final String string : strings)
        {
            sbString.append(string);
        }
        return sbString;
    }

    /**
     * Appends strings to existing string builder.
     * @param sbString string builder
     * @param strings strings to be appended
     */
    public static void append(final StringBuilder sbString, final String... strings)
    {
        sbString.ensureCapacity(sbString.length() + getLength(strings));

        for (final String string : strings)
        {
            sbString.append(string);
        }
    }

    public static int getLength(final Iterable<String> strings)
    {
        int length = 0;
        for (final String string : strings)
        {
            length += (string == null) ? 4 : string.length();
        }
        return length;
    }

    /**
     * Counts total length of all the strings.
     * @param strings array of strings
     * @return total length of all the strings
     */
    public static int getLength(final String[] strings)
    {
        int length = 0;
        for (final String string : strings)
        {
            length += (string == null) ? 4 : string.length();
        }
        return length;
    }

    public static String getTraceString(StackTraceElement[] trace)
    {
        final StringBuilder sbString = new StringBuilder();
        for (final StackTraceElement element : trace)
        {
            sbString.append(element.toString()).append("\n");
        }
        return sbString.toString();
    }
}
