/*
 * Decompiled with CFR 0_114.
 */
package lu.tudor.santec.i18n;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translatrix {
    private static Locale m_Locale = Locale.getDefault();
    private static Hashtable m_Translations = new Hashtable();
    private static Vector m_Bundles = new Vector();
    private static Vector m_SupportedLocales = new Vector();
    private static Pattern c_KeyPattern = Pattern.compile("^language_(\\d)$", 2);
    private static Pattern c_LocalePattern = Pattern.compile("^([a-zA-Z]{2})_([a-zA-Z]{2})$", 2);
    private static Pattern c_PlaceHolderPattern = Pattern.compile("(\\$)(\\d+)", 2);

    private static boolean loadBundle(String string, Locale locale) {
        ResourceBundle resourceBundle = null;
        boolean bl = false;
        try {
            resourceBundle = locale == null ? ResourceBundle.getBundle(string) : ResourceBundle.getBundle(string, locale);
            if (resourceBundle != null) {
                Enumeration<String> enumeration = resourceBundle.getKeys();
                while (enumeration.hasMoreElements()) {
                    String string2 = enumeration.nextElement();
                    m_Translations.put(string2, resourceBundle.getString(string2));
                }
                bl = true;
            }
        }
        catch (MissingResourceException var6_6) {
            Translatrix.logException("MissingResourceException while loading language file", var6_6);
        }
        return bl;
    }

    private static void logException(String string, Exception exception) {
        System.err.println(string);
        exception.printStackTrace(System.err);
    }

    private static boolean isSupportedLocale(Locale locale) {
        boolean bl = false;
        for (int i = 0; i < m_SupportedLocales.size(); ++i) {
            Locale locale2 = (Locale)m_SupportedLocales.elementAt(i);
            if (!locale.equals(locale2)) continue;
            bl = true;
        }
        return bl;
    }

    public static void setLocale(Locale locale) {
        if (Translatrix.isSupportedLocale(locale)) {
            m_Locale = locale;
            m_Translations = new Hashtable();
            for (int i = 0; i < m_Bundles.size(); ++i) {
                String string = (String)m_Bundles.elementAt(i);
                Translatrix.loadBundle(string, m_Locale);
            }
        } else {
            System.err.println("Unsupported Locale " + locale.toString() + " specified in call to setLocale()");
        }
    }

    public static void setLocale(String string) {
        Matcher matcher = c_LocalePattern.matcher(string);
        if (matcher.matches()) {
            Translatrix.setLocale(new Locale(matcher.group(1), matcher.group(2)));
        }
    }

    public static Locale getLocale() {
        return m_Locale;
    }

    public static Locale getDefaultLocale() {
        if (m_SupportedLocales != null && m_SupportedLocales.size() > 0) {
            return (Locale)m_SupportedLocales.elementAt(0);
        }
        return Locale.getDefault();
    }

    public static void addBundle(String string) {
        if (Translatrix.loadBundle(string, m_Locale)) {
            m_Bundles.addElement(string);
        }
    }

    public static void loadSupportedLocales(String string) {
        ResourceBundle resourceBundle;
        try {
            resourceBundle = ResourceBundle.getBundle(string);
        }
        catch (Exception var8_2) {
            Translatrix.logException("Failed to load supportedLocales file", var8_2);
            return;
        }
        if (resourceBundle != null) {
            Integer n;
            Hashtable<Integer, Locale> hashtable = new Hashtable<Integer, Locale>();
            Enumeration<String> enumeration = resourceBundle.getKeys();
            while (enumeration.hasMoreElements()) {
                String string2 = enumeration.nextElement();
                Matcher matcher = c_KeyPattern.matcher(string2);
                if (!matcher.matches()) continue;
                n = new Integer(matcher.group(1));
                matcher = c_LocalePattern.matcher(resourceBundle.getString(string2));
                if (!matcher.matches()) continue;
                hashtable.put(n, new Locale(matcher.group(1), matcher.group(2)));
            }
            if (hashtable.size() > 0) {
                m_SupportedLocales = new Vector();
                for (int i = 0; i < hashtable.size(); ++i) {
                    n = new Integer(i);
                    if (!hashtable.containsKey(n)) continue;
                    m_SupportedLocales.add((Locale)hashtable.get(n));
                }
            }
        }
    }

    public static Vector getSupportedLocales() {
        return m_SupportedLocales;
    }

    public static Vector getBundles() {
        return m_Bundles;
    }

    public static Vector getTranslationKeys() {
        Vector vector = new Vector();
        Enumeration enumeration = m_Translations.keys();
        while (enumeration.hasMoreElements()) {
            vector.add(enumeration.nextElement());
        }
        return vector;
    }

    public static String getTranslationString(String string) {
        String string2 = string;
        if (m_Translations != null && (string2 = (String)m_Translations.get(string)) == null) {
            string2 = string;
        }
        return string2;
    }

    public static String getTranslationString(String string, String[] arrstring) {
        String string2 = string;
        if (m_Translations != null) {
            string2 = (String)m_Translations.get(string);
            if (string2 != null) {
                if (arrstring != null && arrstring.length > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    Matcher matcher = c_PlaceHolderPattern.matcher(string2);
                    while (matcher.find()) {
                        int n = new Integer(matcher.group(2));
                        if (n < 0 || n >= arrstring.length) continue;
                        matcher.appendReplacement(stringBuffer, arrstring[n]);
                    }
                    matcher.appendTail(stringBuffer);
                    string2 = stringBuffer.toString();
                }
            } else {
                string2 = string;
            }
        }
        return string2;
    }
}

