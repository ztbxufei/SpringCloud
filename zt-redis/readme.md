springboot整合redis


springboot cache的整合步骤：

1）引入pom.xml依赖： 
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
           
2）开启缓存注解： @EnableCaching

3）在方法上面加入SpEL   