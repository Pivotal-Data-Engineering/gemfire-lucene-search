package io.pivotal.datatx.lucene;

import io.pivotal.datatx.lucene.model.EmployeeData;
import io.pivotal.datatx.lucene.model.LuceneRequest;
import io.pivotal.datatx.lucene.service.ILuceneService;
import org.apache.geode.pdx.PdxInstance;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collection;

@SpringBootApplication
public class LuceneClient {

    public static void main(String[] args) {
        SpringApplication.run(LuceneClient.class, args);
    }


    /*
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
                LuceneRequest luceneRequestFirstName=new LuceneRequest("employee","firstName:"+emp1.getFirstName(),"employee-lucene-indx","firstname");

                Collection<PdxInstance> retEmps=luceneService.findByLuceneQuery(luceneRequestFirstName);
                System.out.println("Lucene query ");
                for (PdxInstance emp:retEmps){
                    System.out.println(emp.getObject());
                }

                // Lucene AND
                LuceneRequest luceneANDRequest=new LuceneRequest("employee","firstName:"+emp1.getFirstName()+" AND lastName:"+emp1.getLastName(),"employee-lucene-indx","firstname");

                retEmps=luceneService.findByLuceneQuery(luceneANDRequest);
                System.out.println("Lucene AND query ");
                for (PdxInstance emp:retEmps){
                    System.out.println(emp.getObject());
                }

                // Lucene grouping i.e query string = firstName:('fname OR lname') AND lastName:'last'
                LuceneRequest luceneGroupRequest=new LuceneRequest("employee","firstName:("+emp1.getFirstName()+" OR "+emp1.getLastName()+") AND lastName:"+emp1.getLastName(),"employee-lucene-indx","firstname");

                retEmps=luceneService.findByLuceneQuery(luceneGroupRequest);
                System.out.println("Lucene Grouping query ");
                for (PdxInstance emp:retEmps) {
                    System.out.println(emp.getObject());
                }


            }


        };
    }
    */
}
