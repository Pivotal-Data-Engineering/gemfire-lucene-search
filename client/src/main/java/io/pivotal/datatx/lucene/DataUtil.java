package io.pivotal.datatx.lucene;

import io.pivotal.datatx.lucene.model.EmployeeData;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class DataUtil {

    private static Random random = new Random();
    private static int min=10000;
    private static int max=200000;
    private static String dateFormat = "MM-dd-yyyy";

    public static EmployeeData createRandomEmployee(){

        EmployeeData customer=new EmployeeData();
      //  customer.setId(UUID.randomUUID().toString());
        customer.setFirstName("next-fName-"+randomChars(5 + new Random().nextInt(5)));
        customer.setLastName("next-lName-"+randomChars(5 + new Random().nextInt(5)));
        customer.setEmplNumber(ThreadLocalRandom.current().nextInt(min, max + 1));
        customer.setEmail(customer.getFirstName()+"."+customer.getLastName()+"@abc.com");
        customer.setSalary(ThreadLocalRandom.current().nextInt(50000, 150000 + 1));
        customer.setHoursPerWeek(ThreadLocalRandom.current().nextInt(20, 39 + 1));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        try {
            Date date = simpleDateFormat.parse("12-01-2018");
            customer.setDob(date.getTime());
        }catch (Exception e) {
            e.printStackTrace();
        }


        return customer;

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
}
