# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.bolsadeideas.springboot.webflux.spring-boot-webflux' is invalid and this project uses 'com.bolsadeideas.springboot.webflux.spring_boot_webflux' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.4/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.4/maven-plugin/build-image.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.3.4/reference/htmlsingle/index.html#using.devtools)
* [Spring Data Reactive MongoDB](https://docs.spring.io/spring-boot/docs/3.3.4/reference/htmlsingle/index.html#data.nosql.mongodb)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/3.3.4/reference/htmlsingle/index.html#web.servlet.spring-mvc.template-engines)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.3.4/reference/htmlsingle/index.html#web.reactive)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)
* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

