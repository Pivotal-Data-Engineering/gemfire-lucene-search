package io.pivotal.datatx.lucene.service;

import io.pivotal.datatx.lucene.model.EmployeeData;
import io.pivotal.datatx.lucene.model.LuceneRequest;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.lucene.LuceneQuery;
import org.apache.geode.cache.lucene.LuceneService;
import org.apache.geode.cache.lucene.LuceneServiceProvider;
import org.apache.geode.pdx.PdxInstance;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@Service
public class LuceneServiceImpl implements ILuceneService {

    private static ClientCache cache=null;
    private Region<String, Object> employeeRegion =null;
    private LuceneService luceneService = null;

    public LuceneServiceImpl() {

        // Create a client cache
        ReflectionBasedAutoSerializer pdxSerializer=new  ReflectionBasedAutoSerializer(".*");

        cache = new ClientCacheFactory()
                .set("cache-xml-file", "client-cache.xml")
                .setPdxSerializer(pdxSerializer)
                .setPdxReadSerialized(false)
             //   .setPdxPersistent(true)
                .create();
        luceneService=LuceneServiceProvider.get(cache);
    }


    public  void putEmployee(EmployeeData employee){
        employeeRegion.put(employee.getId(), employee);
    }

    public  PdxInstance getEmployee(String key){
        return (PdxInstance) employeeRegion.get(key);
    }


    @PostMapping("/findByLuceneQuery")
    public Collection<Object> findByLuceneQuery(@RequestBody LuceneRequest luceneRequest) throws Exception {

        try {
            LuceneQuery<Integer, Object> query = luceneService.createLuceneQueryFactory()
                    .create(luceneRequest.getIndex(), luceneRequest.getRegion(), luceneRequest.getQuery(), luceneRequest.getDefaultField());
            return  query.findValues();
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

}
