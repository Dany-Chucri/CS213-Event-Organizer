package eventorg;

import java.util.Calendar;
import java.util.StringTokenizer;

/**
 An event's Date information, including their department and email.
 Check's the validity of an event's date to make sure it is in the future but not by more than 6 months.
 @author Dany Chucri, Madhur Nutulapati
 */
public class Date implements Comparable<Date> {
    private final int year;
    private final int month;
    private final int day;

    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;
    public static final int THIS_YEAR = 2023;
    public static final int FIRST_MONTH = 1;
    public static final int LAST_MONTH = 12;
    public static final int FIRST_DAY = 1;
    public static final int NEXT_YEAR = 2024;
    public static final int DAYS_FEB = 28;
    public static final int EVEN_DAYS_MONTH = 30;
    public static final int ODD_DAYS_MONTH = 31;
    public static final int INVALID_DATE = 1;
    public static final int PAST_DATE = 2;
    public static final int TOO_DISTANT_DATE = 3;

    /**
     Creates an instance of Date.
     @param date A date String in the form of "xx/xx/xxxx".
     */
    public Date(String date){
        StringTokenizer part = new StringTokenizer(date,"/");
        this.month= Integer.parseInt(part.nextToken());
//        System.out.println(this.month);
        this.day = Integer.parseInt(part.nextToken());
//        System.out.println(this.day);
        this.year = Integer.parseInt(part.nextToken());
//        System.out.println(this.year);

    }

    /**
     Creates an instance of Date using the current date.
     */
    public Date(){
        Calendar currInfo = Calendar.getInstance();
        this.year = currInfo.get(Calendar.YEAR);
        this.month = currInfo.get(Calendar.MONTH)+1;
        this.day= currInfo.get(Calendar.DAY_OF_MONTH);
    }

    /**
     Basic getter for a Date's day.
     @return An integer representing the Date's day.
     */
    public int getDay() {
        return day;
    }

    /**
     Basic getter for a Date's month.
     @return An integer representing the Date's month.
     */
    public int getMonth() {
        return month;
    }

    /**
     Basic getter for a Date's year.
     @return An integer representing the Date's year.
     */
    public int getYear() {
        return year;
    }


    /**
     * {@inheritDoc}
     * Represents Date in the form of "xx/xx/xxxx".
     * @return A String in the above form.
     */
    @Override
    public String toString(){
        return this.getMonth() + "/" + this.getDay() + "/" + this.getYear();
    }

    /**
     * {@inheritDoc}
     * Compares 2 Date objects based on which comes earlier.
     * @param otherDate The Second date to be compared to.
     * @return An integer using the same criterion as Object's compareTo().
     */
    @Override
    public int compareTo(Date otherDate) {
        int yearComp = Integer.compare(this.year, otherDate.year);
        if (yearComp != 0) {
            return yearComp;
        }
        int monthComp= Integer.compare(this.month, otherDate.month);
        if (monthComp != 0) {
            return monthComp;
        }
        return Integer.compare(this.day, otherDate.day);
    }

    /**
     * Checks the validity of a given Date object.
     * The Date must follow the Gregorian calendar.
     * A given event cannot be more than 6 months into the future.
     * @return 0 for a valid date, 1 for an invalid date, 2 for non-future (past) date, 3 for a date more than 6 months away.
     */
    public int isValid(){

        if (this.year < THIS_YEAR || this.month < FIRST_MONTH || this.month > LAST_MONTH || this.day < FIRST_DAY || this.year > NEXT_YEAR)
            return 1;

        int[] dayMonth = {0, ODD_DAYS_MONTH, DAYS_FEB + isLeapYear(),
                ODD_DAYS_MONTH, EVEN_DAYS_MONTH, ODD_DAYS_MONTH,
                EVEN_DAYS_MONTH, ODD_DAYS_MONTH, ODD_DAYS_MONTH,
                EVEN_DAYS_MONTH, ODD_DAYS_MONTH, EVEN_DAYS_MONTH,
                ODD_DAYS_MONTH};
        if (!checkDate() && this.day > dayMonth[month]){
            return INVALID_DATE;
        }
        if (!checkDate())
            return PAST_DATE;

        Calendar eDate = Calendar.getInstance();
        Calendar specificDate = Calendar.getInstance();
        specificDate.set(Calendar.YEAR, this.year);
        specificDate.set(Calendar.MONTH, (this.month)-1);
        specificDate.set(Calendar.DAY_OF_MONTH, this.day);
        eDate.add(Calendar.MONTH,6);

        if(!eDate.after(specificDate)){ //6 months or more error statement
            return TOO_DISTANT_DATE;
        }

        if(this.day <= dayMonth[month])
            return 0;
        else{
            return INVALID_DATE;
        }
    }

    /**
     * Used by isValid() to ensure the Date is a future date.
     * @return True if the Date is a future date, otherwise false.
     */
    private boolean checkDate(){
        Date currDate = new Date();

        if (this.year > currDate.year) {
            return true;
        } else {
            if (this.year == currDate.year) {
                if (this.month > currDate.month) {
                    return true;
                } else {
                    if (this.month == currDate.month) {
                        return this.day > currDate.day;
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
    }

    /**
     * Checks if a Date instance is part of a leap year.
     * @return True if it is a leap year, otherwise false.
     */
    private int isLeapYear() {
        if (this.year % QUADRENNIAL == 0) {
            if (this.year % CENTENNIAL == 0) {
                if(this.year % QUATERCENTENNIAL==0)
                    return 1;
                else{
                    return 0;
                }
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    /**
     * Testbed main
     * @param args Command-line input arguments.
     */
    public static void main(String[] args) {
        //Test case 1 - invalid month -> Fail
        System.out.println("Running Test Case 1");
        Date testCase1 = new Date("13/1/2023");
        if(testCase1.isValid()==0) System.out.println("Test Case 1 - invalid month -> Pass");
        else System.out.println("Test Case 1 - invalid month -> Fail");

        // Test Case #2, testing day=31 on a 30-day month -> Fail
        System.out.println("Running Test Case 2");
        Date testCase2 = new Date("11/31/2023");
        if(testCase2.isValid()==0) System.out.println("Test Case#2, testing day=31 on a 30-day month -> Pass");
        else System.out.println("Test Case 2,testing day=31 on a 30-day month -> Fail");

        // Test Case #2a, testing day=31 on a 31-day month -> Pass
        System.out.println("Running Test Case #2a");
        Date testCase2a = new Date("12/31/2023");
        if(testCase2a.isValid()==0) System.out.println("Test Case#2a, testing day=31 on a 31-day month -> Pass");
        else System.out.println("Test Case#2a, testing day=31 on a 31-day month -> Fail");

        // Test Case#3, checking a date in the future. -> Fail
        System.out.println("Running Test Case#3");
        Date testCase3 = new Date("4/30/2023");
        if(testCase3.isValid()==0) System.out.println("Test Case #3, checking future date -> Pass");
        else System.out.println("Test Case#3. checking future date -> Fail");

        // Test Case#3a, checking a date in the future. -> Pass
        System.out.println("Running Test Case#3a");
        Date testCase3a = new Date("2/1/2024");
        if(testCase3a.isValid()==0) System.out.println("Test Case #3a, checking future date -> Pass");
        else System.out.println("Test Case#3a. checking future date -> Fail");

        // Test Case #4, checking 02/29 on a leap year -> Pass
        System.out.println("Running Test Case#4");
        Date testCase4 = new Date("2/29/2024");
        if(testCase4.isValid()==0) System.out.println("Test Case#4, checking a date with 02/29 on a leap year -> Pass");
        else System.out.println("Test Case4, checking a date 02/29 on a leap year -> Fail");

        //Test Case #5, checking a date that is past 6 months from current date -> Fail
        System.out.println("Running Test Case#5");
        Date testCase5 = new Date("6/30/2024");
        if(testCase5.isValid()==0) System.out.println("Test Case#5, checking a date that is past 6 months from current date -> Pass");
        else System.out.println("Test Case5, checking a date that is past 6 months from current date -> Fail");

        //Test Case #6, Normal date entry -> Pass
        System.out.println("Running Test Case#6");
        Date testCase6 = new Date("03/28/2024");
        if(testCase6.isValid()==0) System.out.println("Test Case#6, Normal Date entry -> Pass");
        else System.out.println("Test Case6, Normal Date entry -> Fail");

        //Test Case #7, past date -> Fail
        System.out.println("Running Test Case#7");
        Date testCase7 = new Date("01/01/1999");
        if(testCase7.isValid()==0) System.out.println("Test Case#7, checking future date -> Pass");
        else System.out.println("Test Case6, checking future date -> Fail");

    }
}