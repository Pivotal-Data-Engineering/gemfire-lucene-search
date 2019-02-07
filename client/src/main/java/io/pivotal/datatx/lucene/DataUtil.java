package io.pivotal.datatx.lucene;

import io.pivotal.datatx.lucene.model.EmployeeData;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class DataUtil {

    private static Random random = new Random();
    private static int min=10000;
    private static int max=200000;


    public static EmployeeData createRandomEmployee(){

        EmployeeData employee=new EmployeeData();
        employee.setId(UUID.randomUUID().toString());
        employee.setFirstName("next-fName-"+randomChars(5 + new Random().nextInt(5)));
        employee.setLastName("next-lName-"+randomChars(5 + new Random().nextInt(5)));
        employee.setEmplNumber(ThreadLocalRandom.current().nextInt(min, max + 1));
        employee.setEmail(employee.getFirstName()+"."+employee.getLastName()+"@abc.com");
        employee.setSalary(ThreadLocalRandom.current().nextInt(50000, 150000 + 1));
        employee.setHoursPerWeek(ThreadLocalRandom.current().nextInt(20, 39 + 1));
        try {
            employee.setDob(getRandomDate(1920,2018).getTime());
        }catch (Exception e) {
            e.printStackTrace();
        }


        return employee;

    }

    private static String randomChars(int length) {
        StringBuilder rtn = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int c = random.nextInt(26) + 97;
            rtn.append((char) c);
        }
        String result = rtn.toString();
        return Character.toUpperCase(result.charAt(0)) + result.substring(1);
    }

    private static Date getRandomDate(int start, int end){
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(start, end);
        gc.set(gc.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);
        return gc.getTime();
    }

    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
}
