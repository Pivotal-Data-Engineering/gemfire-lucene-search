<h1> VMware has ended active development of this project, this repository will no longer be updated.</h1><br># Geode Lucene Search Capabilities

This is a Spring boot Geode application demonstrating Lucene search capabilities


# To build

gradlew wrapper

gradle build

# To Run

Run LuceneClient

Use postman to post the lucene search query term to Geode

For postman URL : localhost:8080/findByLuceneQuery as POST request

Set Header as 
Content-Type:application/json 

Query param as body
{
  "region": "employee",
  "query": "firstName:next*",
  "index": "employee-lucene-indx",
  "defaultField": "firstName"
}

Curl 

curl -H "Content-Type:application/json" -d '{"region":"employee","query":"firstName:next*","index":"employee-lucene-indx","defaultField":"firstName"}' http://localhost:8080/findByLuceneQuery

# Lucene query string 

AND query

'firstName:someFirstName AND lastName:someLastName'


OR query

'firstName:someFirstName OR lastName:someLastName'

IN query


'firstName:(someSomeName1 OR someName2 OR someName3)'

AND and OR query

'firstName:(someSomeName1 OR someName2 OR someName3) AND lastName:someLastName'

