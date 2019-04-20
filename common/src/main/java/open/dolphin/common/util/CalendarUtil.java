package open.dolphin.common.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 和暦関連
 * 
 * @author masuda, Masuda Naika
 */
public class CalendarUtil {
    
    private static final GregorianCalendar MEIJI_START = new GregorianCalendar(1868, Calendar.JANUARY, 25);
    private static final GregorianCalendar TAISHO_START = new GregorianCalendar(1912, Calendar.JULY, 30);
    private static final GregorianCalendar SHOWA_START = new GregorianCalendar(1926, Calendar.DECEMBER, 2);
    private static final GregorianCalendar HEISEI_START = new GregorianCalendar(1989, Calendar.JANUARY, 8);
    private static final GregorianCalendar REIWA_START = new GregorianCalendar(2019, Calendar.MAY, 1);

    // 漢字、アルファベット１文字、開始日、まで
    private static final JapaneseEra ERA_MEIJI = new JapaneseEra("明治", "M", MEIJI_START,TAISHO_START);
    private static final JapaneseEra ERA_TAISHO = new JapaneseEra("大正", "T", TAISHO_START, SHOWA_START);
    private static final JapaneseEra ERA_SHOWA = new JapaneseEra("昭和", "S", SHOWA_START, HEISEI_START);
    private static final JapaneseEra ERA_HEISEI = new JapaneseEra("平成", "H", HEISEI_START, REIWA_START);
    private static final JapaneseEra ERA_RAIWA = new JapaneseEra("令和", "R", REIWA_START, null);

    private static final JapaneseEra[] JAPANESE_ERAS = {ERA_RAIWA, ERA_HEISEI, ERA_SHOWA, ERA_TAISHO, ERA_MEIJI};

    // Moved from ModelUtil.java
    public static GregorianCalendar getCalendar(String mmlDate) {
        // ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

        int ymd[] = toYmdIntArray(mmlDate);
        return new GregorianCalendar(ymd[0], ymd[1] - 1, ymd[2]);
    }

    // yyyy-MM-dd'T'HH:mm:ss -> H31.04.30
    public static String toNengo(String mmlDate) {
        return toNengo(mmlDate, "%02d-%02d-%02d");
    }
    
    public static String toNengo(String mmlDate, String frmt) {

        int ymd[] = toYmdIntArray(mmlDate);
        GregorianCalendar gc = new GregorianCalendar(ymd[0], ymd[1] - 1, ymd[2]);
        JapaneseEra era = getEraFromCalendar(gc);

        // "GGGGGyy-MM-dd"
        int wYear = ymd[0] - era.getStartDate().get(Calendar.YEAR) + 1;
        String ret = era.getEraAbbr() + String.format(frmt, wYear, ymd[1], ymd[2]);
        return ret;

    }
    
    // Date-> H31.04.30
    public static String toNengo(Date date) {
        return toNengo(date, "%02d-%02d-%02d");
    }
    
    public static String toNengo(Date date, String frmt) {
        
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        int year = gc.get(Calendar.YEAR);
        int month = gc.get(Calendar.MONTH);
        int day = gc.get(Calendar.DAY_OF_MONTH);
        JapaneseEra era = getEraFromCalendar(gc);

        // "GGGGGyy-MM-dd"
        int wYear = year - era.getStartDate().get(Calendar.YEAR) + 1;
        String ret = era.getEraAbbr() + String.format(frmt, wYear, month + 1, day);
        return ret;
    }

    // yyyy-MM-dd'T'HH:mm:ss -> 平成31年4月30日
    public static String toNengoKanji(String mmlDate) {
        return toNengoKanji(mmlDate, "%d年%d月%d日");
    }
    
    public static String toNengoKanji(String mmlDate, String frmt) {
        
        int ymd[] = toYmdIntArray(mmlDate);
        GregorianCalendar gc = new GregorianCalendar(ymd[0], ymd[1] - 1, ymd[2]);
        JapaneseEra era = getEraFromCalendar(gc);

        int wYear = ymd[0] - era.getStartDate().get(Calendar.YEAR) + 1;
        String ret = era.getEraKanji() + String.format(frmt, wYear, ymd[1], ymd[2]);
        return ret;
    }
    
    // Date -> 平成31年4月30日
    public static String toNengoKanji(Date date) {
        return toNengoKanji(date, "%d年%d月%d日");
    }
    
    public static String toNengoKanji(Date date, String frmt) {
        
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        int year = gc.get(Calendar.YEAR);
        int month = gc.get(Calendar.MONTH);
        int day = gc.get(Calendar.DAY_OF_MONTH);
        JapaneseEra era = getEraFromCalendar(gc);

        int wYear = year - era.getStartDate().get(Calendar.YEAR) + 1;
        String ret = era.getEraKanji() + String.format(frmt, wYear, month + 1, day);
        return ret;
    }

    private static JapaneseEra getEraFromCalendar(GregorianCalendar gc) {
        for (JapaneseEra era : JAPANESE_ERAS) {
            if (!gc.before(era.getStartDate())) {    // !before =以降
                return era;
            }
        }
        return null;
    }

    private static int[] toYmdIntArray(String mmlDate) {

        int len = mmlDate.length();
        int pos = 0;
        int ymd[] = new int[3];
        int i = 0;
        StringBuilder sb = new StringBuilder();

        while (true) {
            char c = mmlDate.charAt(pos++);
            if (c == 'T') {
                ymd[i] = Integer.parseInt(sb.toString());
                break;
            } else if (c == '-') {
                ymd[i++] = Integer.parseInt(sb.toString());
                if (i == 3) {
                    break;
                }
                sb.setLength(0);
            } else {
                sb.append(c);
            }
            if (pos == len) {
                ymd[i] = Integer.parseInt(sb.toString());
                break;
            }
        }
        return ymd;
    }

    public static Date parseWareki(String wareki) throws ParseException {
        // "Gyy-MM-dd"
        String[] ymd = wareki.split("-");
        return parseWareki(ymd[0], ymd[1], ymd[2]);
    }
    
    public static Date parseWareki(String strYear, String strMonth, String strDay) throws ParseException {

        try {
            JapaneseEra era = getEraFromString(strYear);
            GregorianCalendar gc = (GregorianCalendar) era.getStartDate().clone();

            int warekiYear = Integer.parseInt(strYear.substring(1));
            gc.add(Calendar.YEAR, warekiYear - 1);

            int month = Integer.parseInt(strMonth);
            if (month < 1 || month > 12) {
                throw new NumberFormatException();
            }
            gc.set(Calendar.MONTH, month - 1);

            int day = Integer.parseInt(strDay);
            if (day < 1 || day > gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)) {
                throw new NumberFormatException();
            }
            gc.set(Calendar.DAY_OF_MONTH, day);

            if (gc.before(era.getStartDate())
                    || (era.getEndDate()) != null && !era.getEndDate().after(gc)) {
                throw new NumberFormatException();
            }
            return gc.getTime();
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException ex) {
        }

        throw new ParseException(strYear + '-' + strMonth + '-' + strDay, 0);
        
    }

    private static JapaneseEra getEraFromString(String wareki) {
        wareki = wareki.toUpperCase();
        for (JapaneseEra era : JAPANESE_ERAS) {
            if (wareki.startsWith(era.getEraAbbr())) {
                return era;
            }
        }
        return null;
    }
    
    public static Date parseRezeDate(String rezeDate) throws ParseException {

        try {
            int nengo = Integer.parseInt(rezeDate.substring(0, 1));
            JapaneseEra era = JAPANESE_ERAS[JAPANESE_ERAS.length - nengo];  // 逆順
            GregorianCalendar gc = (GregorianCalendar) era.getStartDate().clone();
            String wYear = rezeDate.substring(1, 3);
            String month = rezeDate.substring(3, 5);
            String day = rezeDate.substring(5, 7);
            gc.add(Calendar.YEAR, Integer.parseInt(wYear) - 1);
            gc.set(Calendar.MONTH, Integer.parseInt(month) - 1);
            gc.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
            return gc.getTime();
        } catch (NumberFormatException | StringIndexOutOfBoundsException | NullPointerException ex) {
        }

        throw new ParseException(rezeDate, 0);

    }

    
    private static class JapaneseEra {

        private final GregorianCalendar startDate;
        private final GregorianCalendar endDate;
        private final String eraKanji;
        private final String eraAbbr;

        private JapaneseEra(String eraKanji, String eraAbbr, GregorianCalendar startDate, GregorianCalendar endDate) {
            this.eraKanji = eraKanji;
            this.eraAbbr = eraAbbr;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        private String getEraKanji() {
            return eraKanji;
        }

        private String getEraAbbr() {
            return eraAbbr;
        }

        private GregorianCalendar getStartDate() {
            return startDate;
        }

        public GregorianCalendar getEndDate() {
            return endDate;
        }

    }

}
