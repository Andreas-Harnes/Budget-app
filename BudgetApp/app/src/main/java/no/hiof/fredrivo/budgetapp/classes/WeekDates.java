package no.hiof.fredrivo.budgetapp.classes;


import java.util.*;


/*
 *
 *
 *       Denne klassen er basert på klassen funnet i kommentar feltet under dette spørsmålet
 *
 *       https://www.codeproject.com/Questions/144162/How-to-show-the-dates-of-the-current-week-in-JAVA
 *
 *       Den har blitt modifisert til å gi outputen vi trenger
 *
 *
 */

public class WeekDates {
    private static Calendar c;
    private static List<String> output;

    public WeekDates()
    {
        c = Calendar.getInstance();
        output =  new ArrayList<>();
    }

    public static List<String> getCalendar()
    {
        //Get current Day of week and Apply suitable offset to bring the new calendar
        //back to the appropriate Monday, i.e. this week or next
        switch (c.get(Calendar.DAY_OF_WEEK))
        {
            case Calendar.SUNDAY:
                c.add(Calendar.DATE, 1);
                break;

            case Calendar.MONDAY:
                //Don't need to do anything on a Monday
                //included only for completeness
                break;

            case Calendar.TUESDAY:
                c.add(Calendar.DATE,-1);
                break;

            case Calendar.WEDNESDAY:
                c.add(Calendar.DATE, -2);
                break;

            case Calendar.THURSDAY:
                c.add(Calendar.DATE,-3);
                break;

            case Calendar.FRIDAY:
                c.add(Calendar.DATE,-4);
                break;

            case Calendar.SATURDAY:
                c.add(Calendar.DATE,2);
                break;

        }

        //Add the Monday to the output
        output.add(c.getTime().toString());
        for (int x = 1; x <7; x++)
        {
            //Add the remaining days to the output
            c.add(Calendar.DATE,1);
            output.add(c.getTime().toString());
        }
        return output;
    }

    public static List<String> getDates(){
        List<String> tempList = getCalendar();
        List<String> weekList = new ArrayList<>();
        int year = c.get(Calendar.YEAR);
        String day = "";

        for (String x : tempList) {
            String month = "";
            switch (x.substring(4,7)){
                case "Jan":  month = "01";
                    break;
                case "Feb":  month = "02";
                    break;
                case "Mar":  month = "03";
                    break;
                case "Apr":  month = "04";
                    break;
                case "May":  month = "05";
                    break;
                case "Jun":  month = "06";
                    break;
                case "Jul":  month = "07";
                    break;
                case "Aug":  month = "08";
                    break;
                case "Sep":  month = "09";
                    break;
                case "Oct":  month = "10";
                    break;
                case "Nov":  month = "11";
                    break;
                case "Des":  month = "12";
                    break;
            }

            if(Integer.parseInt(x.substring(8,10)) < 10){
                day = "0" + x.substring(8,10);
            } else {
                day = x.substring(8,10);
            }

           weekList.add(day + "/" + month + "/" + year);
        }

        // Returnerer en liste med datoer i formatet dd/mm/yyyy
        return weekList;
    }
}