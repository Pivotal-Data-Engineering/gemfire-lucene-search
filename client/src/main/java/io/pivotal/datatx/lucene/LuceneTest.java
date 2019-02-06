package io.pivotal.datatx.lucene;

import io.pivotal.datatx.lucene.model.Contact;
import io.pivotal.datatx.lucene.model.EmployeeData;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.lucene.LuceneQuery;
import org.apache.geode.cache.lucene.LuceneQueryException;
import org.apache.geode.cache.lucene.LuceneService;
import org.apache.geode.cache.lucene.LuceneServiceProvider;

import java.util.ArrayList;
import java.util.Map;

public class LuceneTest {


    // These index names are predefined in gfsh scripts
    final static String SIMPLE_INDEX = "simpleIndex";
    final static String ANALYZER_INDEX = "analyzerIndex";
    final static String NESTEDOBJECT_INDEX = "nestedObjectIndex";

    // These region names are prefined in gfsh scripts
    final static String EXAMPLE_REGION = "employee";

    public static void main(String[] args) throws LuceneQueryException {
        // connect to the locator using default port 10334
        ClientCache cache = new ClientCacheFactory().addPoolLocator("35.232.109.65", 10334)
                .set("log-level", "WARN").create();

        // create a local region that matches the server region
        Region<Integer, EmployeeData> region =
                cache.<Integer, EmployeeData>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
                        .create("employee");

     //   insertValues(region);
        insertValues(region,10);
        query(cache);
        queryNestedObject(cache);
        cache.close();
    }

    private static void query(ClientCache cache) throws LuceneQueryException {
        LuceneService lucene = LuceneServiceProvider.get(cache);
        LuceneQuery<Integer, EmployeeData> query = lucene.createLuceneQueryFactory()
                .create(SIMPLE_INDEX, EXAMPLE_REGION, "firstName:Alex*", "firstname");
        System.out.println("Employees with first names like Alex: " + query.findValues());
    }

    private static void queryNestedObject(ClientCache cache) throws LuceneQueryException {
        LuceneService lucene = LuceneServiceProvider.get(cache);
        LuceneQuery<Integer, EmployeeData> query = lucene.createLuceneQueryFactory().create(
                NESTEDOBJECT_INDEX, EXAMPLE_REGION, "5035330001 AND 5036430001", "contacts.phoneNumbers");
        System.out.println("Employees with phone number 5035330001 and 5036430001 in their contacts: "
                + query.findValues());
    }

    public static void insertValues(Map<Integer, EmployeeData> region) {
        // insert values into the region
        String[] firstNames = "Alex,Bertie,Kris,Dale,Frankie,Jamie,Morgan,Pat,Ricky,Taylor".split(",");
        String[] lastNames = "Able,Bell,Call,Driver,Forth,Jive,Minnow,Puts,Reliable,Tack".split(",");
        String[] contactNames = "Jack,John,Tom,William,Nick,Jason,Daniel,Sue,Mary,Mark".split(",");
        int salaries[] = new int[] {60000, 80000, 75000, 90000, 100000};
        int hours[] = new int[] {40, 40, 40, 30, 20};
        int emplNumber = 10000;
        for (int index = 0; index < firstNames.length; index++) {
            emplNumber = emplNumber + index;
            Integer key = emplNumber;
            String email = firstNames[index] + "." + lastNames[index] + "@example.com";
            // Generating random number between 0 and 100000 for salary
            int salary = salaries[index % 5];
            int hoursPerWeek = hours[index % 5];

            ArrayList<Contact> contacts = new ArrayList<>();
            Contact contact1 = new Contact(contactNames[index] + " Jr",
                    new String[] {"50353" + (30000 + index), "50363" + (30000 + index)});
            Contact contact2 = new Contact(contactNames[index],
                    new String[] {"50354" + (30000 + index), "50364" + (30000 + index)});
            contacts.add(contact1);
            contacts.add(contact2);
            EmployeeData val = new EmployeeData(firstNames[index], lastNames[index], emplNumber, email,
                    salary, hoursPerWeek, contacts);
            region.put(key, val);
        }
    }

    public static void insertValues(Map<Integer, EmployeeData> region, int count){

        for (int index = 0; index < count; index++) {
            EmployeeData employeeData=DataUtil.createRandomEmployee();
            region.put(employeeData.getEmplNumber(),employeeData);
        }
    }

}
