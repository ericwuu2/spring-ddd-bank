Project Spring DDD Bank
=======================
A sample project following Domain Driven Design with Spring Data JPA

                                            (C) Christoph Knabe, 2017-03-17 ... 2022-01-14

In this project I am trying to apply principles of Domain Driven Design.
In contrary to more complex DDD examples on the web I am applying here some simplifications.
This sample application has been developed for a course on Software Engineering at [Berlin University of Applied Sciences and Technology (Berliner Hochschule für Technik: BHT)](https://www.bht-berlin.de/en/).

This project uses

- JDK 11 as the platform
- [Maven](https://maven.apache.org/) 3 as build tool
- [Spring Boot](https://spring.io/projects/spring-boot) 2 as enterprise framework and web server
- [Spring Data](https://spring.io/projects/spring-data) + [JPA](https://en.wikipedia.org/wiki/Jakarta_Persistence) + [Hibernate](https://hibernate.org/) as persistence API and object-relational mapper
- [Apache Derby](http://db.apache.org/derby/) as relational database
- [springfox-swagger](https://springfox.github.io/springfox/) for generating documentation and user interface for the REST service
- [JUnit 4](https://junit.org/junit4/)
- The Exception Handling and Reporting Framework [MulTEx](http://public.bht-berlin.de/~knabe/java/multex/)

Detailed version indications you can find in the file [pom.xml](pom.xml).

## Usage

If the correct JDK and Maven versions are installed you can simply use 
`mvn clean package site`
This will fetch all necessary libraries, compile the project, collect the exception message texts, 
execute the test suite, package all into an executable .jar file, and generate the reports and project site.
The most interesting report is about Code Coverage.

If you experience problems due to versioning of JDK and Maven, see the chapter about it in document [Maven and IDE Integration](src/doc/Maven-IDE-Integration.md).

After this is working you can import the Maven project into your Java IDE 
([Spring Tools](https://spring.io/tools) may be preferred, see the chapter about IDE Integration in  [Maven and IDE Integration](src/doc/Maven-IDE-Integration.md))

You can run the application (a REST server) in your IDE by running class [de.beuth.knabe.spring_ddd_bank.Application](src/main/java/de/beuth/knabe/spring_ddd_bank/Application.java) as Java Application or on the command line after 
`mvn package` by 
`java -jar target/spring-ddd-bank-0.1-SNAPSHOT.jar`

This will start the web server Tomcat with the web application *Spring DDD Bank*. The latter runs the database Derby in embedded mode for the web application, but also offers network access to the database.  In the last lines of the log you will see messages like the following:\
`Tomcat started on port(s): 8080 (http)`\
`Apache Derby Network Server 10.12.1.1 - (1704137) has been started and is ready to accept connections on port 1527.`\
In these messages you can see the port of the web server (8080), and of the Derby database (1527).

You can shutdown it by typing &lt;Ctrl/C&gt;. Killing it in IntelliJ IDEA or Spring Tools by the red icon also stopped it without a visible negative effect.

## Which DDD principles are implemented?

- Modeling the domain layer as one package, which does not depend on any other package besides standard Java SE packages as `java.time` and `javax.persistence`. The latter only for the JPA annotations.

- Prefer a [Rich Domain Model](https://www.amido.com/blog/anaemic-domain-model-vs-rich-domain-model) over an [Anemic Domain Model](https://martinfowler.com/bliki/AnemicDomainModel.html) by having relevant business logic methods in entity class [Client](src/main/java/de/beuth/knabe/spring_ddd_bank/domain/Client.java).
  This requires **Domain Object Dependency Injection** (DODI), which is now done manually by method `Client.provideWith`. (The former automatic DODI on Java 8 by AspectJ compile-time weaving was abandoned, as there were serious configuration issues with AspectJ on Java 11. AspectJ seems to become less popular.)

- The Domain Layer references required services only by self-defined, minimal interfaces (in package [domain.imports](src/main/java/de/beuth/knabe/spring_ddd_bank/domain/imports)).

- Implementing required services in the infrastructure layer (in package [infrastructure](src/main/java/de/beuth/knabe/spring_ddd_bank/infrastructure)).

- Linking together required services and their implementations by Spring Dependency Injection. 

- Implementing an interface layer for external access to the application. 
  This is implemented as a REST service in package [rest_interface](src/main/java/de/beuth/knabe/spring_ddd_bank/rest_interface).


## Other Characteristics

- It is a little bank application, where the bank can create clients and clients can create and manage accounts, e.g. deposit and transfer money.
- The analysis class diagram is in file [src/doc/BankModel.pdf](src/doc/BankModel.pdf). Its editable source by UMLet has extension `.uxf`.
- An overview of the REST endpoints is given at [src/doc/REST-API.md](src/doc/REST-API.md). Additionally when running the REST service, the REST endpoint documentation generated by Swagger, is accessible at http://localhost:8080/ and clicking on [REST API documentation](http://localhost:8080/swagger-ui.html).   
- As the application layer in this didactical example would only manage transactions, it is omitted, but the same effect is achieved by making the interface layer transactional by the annotation `@Transactional` to class [ApplicationController](https://github.com/christophknabe/spring-ddd-bank/blob/issue-16-ide-importable/src/main/java/de/beuth/knabe/spring_ddd_bank/rest_interface/ApplicationController.java).
- Internationalizable, parameterizable exception message texts
- Capture of each exception message text in the reference language (english) directly as main JavaDoc comment of the exception
- The application runs against a file-based Derby database. This is configured in file [src/main/resources/application.properties](src/main/resources/application.properties)
- Tests are run against an empty in-memory Derby database. This is configured in file [src/test/resources/application.properties](src/test/resources/application.properties)
- Generation of a test coverage report by the [JaCoCo Maven plugin](http://www.eclemma.org/jacoco/trunk/doc/maven.html) into [target/site/jacoco-ut/index.html](file:target/site/jacoco-ut/index.html).
- Demo-only Spring Security with a fixed number of predefined users (1 bank, and 4 clients).

### Where are the exception message texts?
In the file `MessageText.properties`. The editable original with some fixed message texts is in [src/main/resources/](src/main/resources/).
By Maven phase `compile` this file is copied to `target/classes/`.
During the following phase `process-classes` the exception message texts are extracted from the JavaDoc comments of all exceptions under [src/main/java/](src/main/java/)
by the  `ExceptionMessagesDoclet`  as configured for the `maven-javadoc-plugin`. They are appended to the message text file in `target/classes/`.
This process is excluded from m2e lifecycle mapping in the `pom.xml`.

## Plans

- Make [Amount](src/main/java/de/beuth/knabe/spring_ddd_bank/domain/Amount.java) a better [Value Object](https://martinfowler.com/bliki/ValueObject.html) by freezing its attributes. Seems, that for this goal Hibernate has to be used instead of JPA.
- Nice to have: Avoid own ID of [AccountAccess](src/main/java/de/beuth/knabe/spring_ddd_bank/domain/AccountAccess.java), because this class models an m:n association between [Client](src/main/java/de/beuth/knabe/spring_ddd_bank/domain/Client.java) and [Account](src/main/java/de/beuth/knabe/spring_ddd_bank/domain/Account.java). 
  There should not be a possibility for several links between a client and an account.
  This would require the usage of `client.id` and `account.id` as a composite ID for `AccountAccess`.
  Not so easy, see [JPA: How to associate entities with relationship attributes?](http://stackoverflow.com/questions/18739334/jpa-how-to-associate-entities-with-relationship-attributes)
- Change how the Repository interfaces are implemented in package [infrastructure](https://github.com/christophknabe/spring-ddd-bank/tree/issue-16-ide-importable/src/main/java/de/beuth/knabe/spring_ddd_bank/infrastructure), as it is confusing to beginners, that you implement something by writing an interface (the Spring Data way).
  We could for example use the JPA Criteria API, or JPQL. This would more look like a real implementation.
- Implementation of real unit tests with mock implementations of the repository interfaces.

## References and Sources
- [Detailed example text about DDD](https://www.mirkosertic.de/blog/2013/04/domain-driven-design-example/)
- [The DDD Sample project](https://github.com/citerus/dddsample-core), from which are taken some inspirations
- [The Ports and Adapters Pattern](http://alistair.cockburn.us/Hexagonal+architecture)
- [Can DDD be Adequately Implemented Without DI and AOP?](https://www.infoq.com/news/2008/02/ddd-di-aop)
- [Spring Boot](https://spring.io/guides/gs/spring-boot/)
- [Spring Dependency Injection](http://projects.spring.io/spring-framework/)
- [Spring Data JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [Spring Data JPA Query Methods](http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods)
- This application condenses my knowledge about layering and data access at a given point of time. Previous versions were in a 3-layer form in german under [Bank3Tier - Bankanwendung in 3 Schichten](http://public.beuth-hochschule.de/~knabe/java/bank3tier/).
