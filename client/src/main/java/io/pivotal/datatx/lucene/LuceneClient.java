package io.pivotal.datatx.lucene;

import io.pivotal.datatx.lucene.model.EmployeeData;
import io.pivotal.datatx.lucene.service.ILuceneService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class LuceneClient {

    public static void main(String[] args) {
        SpringApplication.run(LuceneClient.class, args);
    }


    @Bean
    CommandLineRunner runClientApp(final ILuceneService luceneService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... arg0) throws Exception {

                EmployeeData emp1 = DataUtil.createRandomEmployee();
                EmployeeData emp2 = DataUtil.createRandomEmployee();
                luceneService.putEmployee(emp1);
                luceneService.putEmployee(emp2);

                // Lucene search
                List<EmployeeData> retEmps=luceneService.queryEmpByFirstName(emp1.getFirstName());

                System.out.println("Lucene query ");
                for (EmployeeData emp:retEmps){
                    System.out.println(emp);
                }

                // Lucene AND
                retEmps=luceneService.queryEmpByFirstAndLastName(emp1.getFirstName(),emp1.getLastName());
                System.out.println("Lucene AND query ");
                for (EmployeeData emp:retEmps){
                    System.out.println(emp);
                }

                // Lucene grouping i.e query string = firstName:('fname OR lname') AND lastName:'last'
                retEmps=luceneService.queryEmpByFirstAndORLastName(emp1.getFirstName(),emp1.getLastName(), "val1","val2");
                System.out.println("Lucene Grouping query ");
                for (EmployeeData emp:retEmps){
                    System.out.println(emp);
                }

                // Lucene Range query
                retEmps=luceneService.findEmpDOBInBetween("01-01-1980","01-01-2000");
                System.out.println("Lucene Range query ");
                for (EmployeeData emp:retEmps){
                    System.out.println(emp);
                }

            }

        };
    }
}
