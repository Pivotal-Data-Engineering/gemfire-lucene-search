package io.pivotal.datatx.lucene.service;

import io.pivotal.datatx.lucene.LongRangeQueryProvider;
import io.pivotal.datatx.lucene.model.EmployeeData;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.lucene.LuceneQuery;
import org.apache.geode.cache.lucene.LuceneService;
import org.apache.geode.cache.lucene.LuceneServiceProvider;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LuceneServiceImpl implements ILuceneService {

    private static ClientCache cache=null;
    private Region<String, EmployeeData> employeeRegion =null;
    private LuceneService luceneService = null;

    // These index names are predefined in gfsh scripts
    final static String SIMPLE_INDEX = "employee-lucene-indx";
    final static String ANALYZER_INDEX = "employee-lucene-indx";
    final static String NESTEDOBJECT_INDEX = "employee-lucene-indx";
    private static String dateFormat = "MM-dd-yyyy";



    public LuceneServiceImpl() {

        // Create a client cache
        ReflectionBasedAutoSerializer pdxSerializer=new  ReflectionBasedAutoSerializer();

        Properties props=new Properties();
        props.setProperty("classes", "io.pivotal.datatx.lucene.model.*");
        pdxSerializer.init(props);

        cache = new ClientCacheFactory()
                .set("cache-xml-file", "client-cache.xml")
                .setPdxSerializer(pdxSerializer)
                .setPdxPersistent(true)
                .create();
        employeeRegion =cache.getRegion("/employee");
        luceneService=LuceneServiceProvider.get(cache);
    }


    public  void putEmployee(EmployeeData employee){
        employeeRegion.put(employee.getId(), employee);
    }

    public  EmployeeData getEmployee(String key){
        return employeeRegion.get(key);
    }

    public List<EmployeeData> queryEmpByFirstName(String firstName){

        List<EmployeeData> empList= Collections.EMPTY_LIST;
        try {
            LuceneQuery<Integer, EmployeeData> query = luceneService.createLuceneQueryFactory()
                    .create(SIMPLE_INDEX, employeeRegion.getName(), "firstName:" + firstName, "firstname");
            empList= new ArrayList<>(query.findValues());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return empList;
    }

    public List<EmployeeData> findEmpLikeFirstName(String firstName){

        List<EmployeeData> empList= Collections.EMPTY_LIST;
        try {
            LuceneQuery<Integer, EmployeeData> query = luceneService.createLuceneQueryFactory()
                    .create(SIMPLE_INDEX, employeeRegion.getName(), "firstName:" + firstName+"*", "firstname");
            empList= new ArrayList<>(query.findValues());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return empList;
    }



    public List<EmployeeData> queryEmpByFirstAndLastName(String firstName,String lastName){
        List<EmployeeData> empList= Collections.EMPTY_LIST;
        try {
            LuceneQuery<Integer, EmployeeData> query = luceneService.createLuceneQueryFactory()
                    .create(SIMPLE_INDEX, employeeRegion.getName(), "firstName:" + firstName+" AND lastName:"+lastName, "firstname");
            empList= new ArrayList<>(query.findValues());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return empList;
    }

    public List<EmployeeData> queryEmpByFirstAndORLastName(String firstName,String lastName, String ... orValues){

        List<EmployeeData> empList= Collections.EMPTY_LIST;
        try {
            StringBuilder sb=new StringBuilder();
            boolean firstTerm=true;
            for (String val:orValues){
                if (firstTerm!=true){
                    sb.append(" OR ").append(val);
                } else {
                    sb.append(val);
                    firstTerm=false;
                }
            }
            LuceneQuery<Integer, EmployeeData> query = luceneService.createLuceneQueryFactory()
                    .create(SIMPLE_INDEX, employeeRegion.getName(), "firstName:("+sb.toString()+" )" + firstName+" AND lastName:"+lastName, "firstname");
            empList= new ArrayList<>(query.findValues());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return empList;
    }

    public List<EmployeeData> findEmpDOBInBetween(String startDate, String endDate){
        List<EmployeeData> empList= Collections.EMPTY_LIST;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Date sDate=new Date();
        Date eDate=new Date();
        try {
            sDate = simpleDateFormat.parse(startDate);
            eDate= simpleDateFormat.parse(endDate);
        }catch (Exception e) {
            e.printStackTrace();
        }

        LongRangeQueryProvider dobRangeQueryProvider=new LongRangeQueryProvider("dob",sDate.getTime(),eDate.getTime());
         try {
            LuceneQuery<Integer, EmployeeData> query = luceneService.createLuceneQueryFactory()
                    .create(SIMPLE_INDEX, employeeRegion.getName(), dobRangeQueryProvider);
            empList= new ArrayList<>(query.findValues());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return empList;
    }

}
