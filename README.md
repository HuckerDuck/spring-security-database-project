# A Secuure Spring Boot Web Application 

This is a groupprojekt from school with the focus of authentication,
a general focus on Securirty and the use of REST API. 

The Application in it self uses JWT-token for authentation and 
is used with my MySQL database hosted in AWS.  

# Functionality 

- Register uses (only as a admin)
- Set diffrent roles (admin / user)
- Authentication with a JWT-token for added security
- Rolebased right to diffrent endpoints for managing
  (/admin) /// (/user)
- Use of Swagger to simplify the usage of adding, removing or
  editing towards the database
- Error manages with a global exceptions handler,
  that is connected to write a message to diffrent exceptions


## Tech-Stack 
                    
|-------------|---------------------------|
                                          | 
 Backend       Spring Boot (Java 17+)     |
 Security      Spring Security + JWT      |
 Database       MySQL (AWS RDS)           |
 Building Tool   Maven                    |
 API-doc     Swagger (springdoc-openapi)  | 
 Testing      JUnit, MockMvc              | 
                                          | 
 |-------------|--------------------------|

 
