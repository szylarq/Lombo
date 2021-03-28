package lombo.utils;

import lombo.model.ContractLength;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtils {

    public static final int CONTRACT_DATE_TO_THRESHOLD = 3;

    public static String getDayOfWeekString(LocalDate date){

        String dayOfWeekName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());

        return dayOfWeekName.substring(0, 1).toUpperCase() + dayOfWeekName.substring(1) + ", " +
                toPolishDateFormat(date);
    }

    public static String toPolishDateFormat(LocalDate dateToFormat){

        return dateToFormat.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static boolean isDateBetweenTwoDates(LocalDate firstDate, LocalDate secondDate, LocalDate dateToCheck){

        if(firstDate == null && secondDate != null){

            return dateToCheck.compareTo(secondDate) <= 0;
        }
        else if(firstDate != null && secondDate == null){

            return dateToCheck.compareTo(firstDate) >= 0;
        }
        else {

            return (dateToCheck.compareTo(firstDate) >= 0 && dateToCheck.compareTo(secondDate) <= 0);
        }
    }

    public static String convertPolishDateFormatToLocalDateFormat(String stringInPolishFormat){

        String[] dateElements = stringInPolishFormat.split("\\.");

        return dateElements[2] + "-" + dateElements[1] + "-" + dateElements[0];

    }

    public static LocalDate calculateDateTo(ContractLength length, LocalDate dateFrom){

        if(length == null || dateFrom == null){

            return null;
        }
        else {

            LocalDate dateTo = null;

            switch(length.getLength()){

                case "1 tydzień":
                    dateTo = dateFrom.plusDays(7);
                    break;

                case "2 tygodnie":
                    dateTo = dateFrom.plusDays(14);
                    break;

                case "3 tygodnie":
                    dateTo = dateFrom.plusDays(21);
                    break;

                case "1 miesiąc":
                    dateTo = dateFrom.plusMonths(1);
            }

            return dateTo;
        }
    }

    public static boolean doesDateCrossThreshold(LocalDate date, int daysThreshold){

        LocalDate today = LocalDate.now();

        return date != null && (today.compareTo(date) <= 0 && today.compareTo(date.minusDays(daysThreshold)) >= 0);
    }
}

