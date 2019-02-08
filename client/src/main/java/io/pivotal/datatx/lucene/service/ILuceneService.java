package io.pivotal.datatx.lucene.service;

import io.pivotal.datatx.lucene.model.EmployeeData;
import io.pivotal.datatx.lucene.model.LuceneRequest;
import org.apache.geode.pdx.PdxInstance;

import java.util.Collection;
import java.util.List;

public interface ILuceneService {

    /*

    public  void putEmployee(EmployeeData employee);

    public  PdxInstance getEmployee(String key);

    public List<PdxInstance> queryEmpByFirstName(String firstName);

    public List<PdxInstance> findEmpLikeFirstName(String firstName);

    public List<PdxInstance> queryEmpByFirstAndLastName(String firstName,String lastName);

    public List<PdxInstance> queryEmpByFirstAndORLastName(String firstName,String lastName, String ... orValues);

    public List<PdxInstance> findEmpDOBInBetween(String startDate,String endDate);

*/

    public  void putEmployee(EmployeeData employee);

    public Collection<Object> findByLuceneQuery(LuceneRequest luceneRequest) throws Exception;



}
