package io.pivotal.datatx.lucene;

import io.pivotal.datatx.lucene.model.EmployeeData;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.lucene.LuceneQuery;
import org.apache.geode.cache.lucene.LuceneQueryException;
import org.apache.geode.cache.lucene.LuceneService;
import org.apache.geode.cache.lucene.LuceneServiceProvider;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class LuceneEmployeeTest {

    // These index names are predefined in gfsh scripts
    final static String SIMPLE_INDEX = "employee-lucene-indx";
    final static String ANALYZER_INDEX = "employee-lucene-indx";
    final static String NESTEDOBJECT_INDEX = "employee-lucene-indx";
    private static String dateFormat = "MM-dd-yyyy";

    // These region names are prefined in gfsh scripts
    final static String EXAMPLE_REGION = "employee";

    public static void main(String[] args) throws LuceneQueryException {

        // Create a client cache
        ReflectionBasedAutoSerializer pdxSerializer=new  ReflectionBasedAutoSerializer();

        Properties props=new Properties();
        props.setProperty("classes", "io.pivotal.datatx.lucene.model.*");
        pdxSerializer.init(props);

        // connect to the locator using default port 10334
        ClientCache cache = new ClientCacheFactory().addPoolLocator("35.232.109.65", 10334)
                .set("log-level", "WARN").setPdxSerializer(pdxSerializer)
                .setPdxPersistent(true).
                        create();

        // create a local region that matches the server region
        Region<Integer, EmployeeData> region =
                cache.<Integer, EmployeeData>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
                        .create(EXAMPLE_REGION);


        insertValues(region,1000);
        query(cache);
        queryDOB(cache);
        querySalary(cache);
        queryHoursPerWeek(cache);
        queryfirstANDlast(cache);
        cache.close();
    }

    private static void query(ClientCache cache) throws LuceneQueryException {
        LuceneService lucene = LuceneServiceProvider.get(cache);
        LuceneQuery<Integer, EmployeeData> query = lucene.createLuceneQueryFactory()
                .create(SIMPLE_INDEX, EXAMPLE_REGION, "firstName:next-fName-Mxepm", "firstname");
        System.out.println("Employees with first names firstName: " + query.findValues());
    }

    // Query dob
    private static void queryDOB(ClientCache cache) throws LuceneQueryException {
        LuceneService lucene = LuceneServiceProvider.get(cache);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Date date=new Date();
        try {
            date = simpleDateFormat.parse("12-01-2018");
            System.out.println("DOB in long :1543644000000 - "+date.getTime());
        }catch (Exception e) {
            e.printStackTrace();
        }

        String queryS="dob:"+date.getTime();

        LongRangeQueryProvider dobRangeQueryProvider=new LongRangeQueryProvider("dob",date.getTime()+1,date.getTime()+2);

        LuceneQuery<Integer, EmployeeData> query = lucene.createLuceneQueryFactory()
                .create(SIMPLE_INDEX, EXAMPLE_REGION, dobRangeQueryProvider);
        Collection<EmployeeData> empDataList=query.findValues();
        System.out.println("Size of query is :"+empDataList.size());

        System.out.println("Employees DOB on 12-01-2018: " + query.findValues());
    }

    // Query Salary
    private static void querySalary(ClientCache cache) throws LuceneQueryException {
        LuceneService lucene = LuceneServiceProvider.get(cache);

        IntRangeQueryProvider salaryRangeQuery=new IntRangeQueryProvider("salary",143775,1437707);

        LuceneQuery<Integer, EmployeeData> query = lucene.createLuceneQueryFactory()
                .create(SIMPLE_INDEX, EXAMPLE_REGION, salaryRangeQuery);
        Collection<EmployeeData> empDataList=query.findValues();
        System.out.println("Size of query is :"+empDataList.size());

        System.out.println("Employees salary  with salary is " + query.findValues());
    }

    // Query hoursperweek
    private static void queryHoursPerWeek(ClientCache cache) throws LuceneQueryException {
        LuceneService lucene = LuceneServiceProvider.get(cache);

        IntRangeQueryProvider salaryRangeQuery=new IntRangeQueryProvider("hoursPerWeek",35,40);

        LuceneQuery<Integer, EmployeeData> query = lucene.createLuceneQueryFactory()
                .create(SIMPLE_INDEX, EXAMPLE_REGION, salaryRangeQuery);
        Collection<EmployeeData> empDataList=query.findValues();
        System.out.println("Size of query is :"+empDataList.size());

        System.out.println("Employees with hours per week " + query.findValues());
    }

    // Query AndQuery
    private static void queryfirstANDlast(ClientCache cache) throws LuceneQueryException {
        LuceneService lucene = LuceneServiceProvider.get(cache);

        LuceneQuery<Integer, EmployeeData> query = lucene.createLuceneQueryFactory()
                .create(SIMPLE_INDEX, EXAMPLE_REGION, "firstName:(next-fName OR next-fName-Wtnpaoe) AND lastName:next-lName-Ihtqueh  ", "firstname" );
        Collection<EmployeeData> empDataList=query.findValues();
        System.out.println("Size of query is :"+empDataList.size());

        System.out.println("Employees with first and last name :" + query.findValues());
    }




    public static void insertValues(Map<Integer, EmployeeData> region, int count){

        for (int index = 0; index < count; index++) {
            EmployeeData employeeData=DataUtil.createRandomEmployee();
            region.put(employeeData.getEmplNumber(),employeeData);
        }
    }

}
