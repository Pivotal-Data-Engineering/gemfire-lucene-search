package io.pivotal.datatx.lucene.service;

import io.pivotal.datatx.lucene.model.EmployeeData;

import java.util.List;

public interface ILuceneService {

    public  void putEmployee(EmployeeData employee);

    public  EmployeeData getEmployee(String key);

    public List<EmployeeData> queryEmpByFirstName(String firstName);

    public List<EmployeeData> findEmpLikeFirstName(String firstName);

    public List<EmployeeData> queryEmpByFirstAndLastName(String firstName,String lastName);

    public List<EmployeeData> queryEmpByFirstAndORLastName(String firstName,String lastName, String ... orValues);

    public List<EmployeeData> findEmpDOBInBetween(String startDate,String endDate);


}
