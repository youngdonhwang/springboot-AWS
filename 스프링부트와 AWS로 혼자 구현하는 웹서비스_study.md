# 스프링부트와 AWS로 혼자 구현하는 웹서비스_study



## 1. IntelliJ

인텔리제이에서는 하나의 프로젝트만 열린다. 이클립스의 Workspace 개념과는 사뭇 다르다. 특히나 multi-module 환경에서 효과가 좋다.

https://jojoldu.tistory.com/334



## 2. Gradle

https://docs.gradle.org/current/userguide/what_is_gradle.html

혹시 gradle 탭이 안나온다면

* build.gradle에 import gradle
* 플러그인 설치 and...



## 3. Test Code

TDD랑 단위테스트는 조금 다릅니다. TDD는

> 1. 항상 실패하는 테스트를 먼저 작성(RED)
> 2. 테스트가 통과하는 프로덕션 코드를 작성(GREEN)
> 3. 해당 프로덕션 코드를 리팩토링(Refactor)



### assertThat(assertj) Vs. assertThat(junit) 

> https://youtu.be/zLx_fI24UXM
>
> youtube - AssertJ가 JUnit의 assertThat 보다 편리한 이유 (백기선)
>
>
> https://joel-costigliola.github.io/assertj/assertj-core.html
>
> official

Matchers (junit...depend...hamcrest)

자동완성기능이 좀 약한편.. Matchers.is(T)

결론적으로 해당 패키지 asserThat 추천

```java
import static org.assertj.core.api.Assertions.assertThat;
```



### @WebMvcTest Vs. @SpringBootTest

API 테스트를 할 때 @SpringBootTest(o.s.b.test.context..) 와 TestRestTemplate(o.s.b.test.web..)을 사용한다. @WebMvcTest의 경우에는 JPA 테스트까지 관여하지 않는다. 외부 연동과 관련된 부분(@Controller, @ControllerAdvice)에 집중한다.





## 4. ORM(Object Relational Mapping) - JPA

### 공부할 참고자료

* <자바 ORM 표준 JPA 프로그래밍> (김영한, 에이콘)



RDB를 사용하면 CRUD는 피할 수 없다.

패러다임 불일치가 일어난다. 영원성, 일관성, 저장성이 강조된 DB 와 메시지 기반, 기능, 속성 강조의 객체지향 프로그래밍 언어가 추구하는 철학이 다르다.



* @Entity

  + 테이블과 링크될 클래스
  + 기본적으로 클래스의 CamelCase 를 under_score_case로 매칭해준다.
  + ***setter를 만들지 않는다!!***
  + Builder 패턴 추천(Lombok 사용 시, @Builder)
    - 어느 필드에 어떤 값이 들어가는지 명확

* @Id

  + 해당 테이블의 PK 필드

* @GenerateValue

  + PK의 생성규칙

* @Column

  + 테이블의 컬럼을 나타냄
  + 선언이 없더라도 필드는 모두 컬럼에 해당
  + 문자열의 경우 VARCHAR(255)가 기본값

  
  
  

### Repository (interface Type)

DBLayer 접근자, MyBatis에서 DAO라 불리는 객체

extends JpaRepository<Entity 클래스, PK 타입> 사용 시 기본적인 CRUD 메소드 생성

🚨 Entity 와 기본 EntityRepository 는 함께 위치하도록 할 것



* save(S entity) 
  + CrudRepository 인터페이스에 선언
  + 해당 Entity 테이블에 insert/update 실행
  + id 값의 존재 여부로 쿼리결정



### Permanence Context(영속성 컨텍스트 feat.Entity)

JPA 사용할 때 Entity 를 조회하고 update하는 경우 update 쿼리를 작성하지 않는다. 한 트랜잭션 안에서 select 한 내용을 JPA의 Entity Manager가 영속성 컨텍스트를 유지한다. 값의 변경이 일어나면 끝나는 시점에 해당 테이블에 변경분을 반영한다. 

* 더티 체킹(dirty checking) (참고: https://jojoldu.tistory.com/415)
  + DB에서 꺼낸 깨끗하고 무결한 데이터가 더러워졌는지(변했는지) 체크한다고 기억하자



## 4.1. JPA Auditing







## 5. application.properties / application.yml



### Query Logging 관련

* 실행 로그 설정

  +  ```properties
     #spring.jpa.show-sql=true
     spring.jpa.properties.hibernate.show_sql=true
     ```

  + 결과 예시

    ```
    Hibernate: drop table posts if exists
    Hibernate: drop sequence if exists hibernate_sequence
    Hibernate: create sequence hibernate_sequence start with 1 increment by 1
    Hibernate: create table posts (id bigint not null, author varchar(255), content TEXT not null, title varchar(500) not null, primary key (id))
    ...
    Hibernate: call next value for hibernate_sequence
    Hibernate: insert into posts (author, content, title, id) values (?, ?, ?, ?)
    ...
    Hibernate: select posts0_.id as id1_0_, posts0_.author as author2_0_, posts0_.content as content3_0_, posts0_.title as title4_0_ from posts posts0_
    Hibernate: select posts0_.id as id1_0_, posts0_.author as author2_0_, posts0_.content as content3_0_, posts0_.title as title4_0_ from posts posts0_
    Hibernate: delete from posts where id=?
    ...
    Hibernate: drop table posts if exists
    Hibernate: drop sequence if exists hibernate_sequence
    ```

* MySQL 쿼리로 변경

  + ```properties
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
    ```



### Web Console

```properties
#MemoryAccessBy Web Console
spring.h2.console.enabled=true
```








## 6. API

* DTO(Datat Transfer Object) for Datas

* Controller for API Requst
* Service for ensuring order about transaction and domain

Service 는 트랜잭션, 도메인 간 순서 보장하는 역할이다. 비즈니스 로직은 Domain에서 처리한다. (아래 자료 참고) 기존 Service Layer에서 처리하는 건 **트랜잭션 스크립트**라고 한다.

![spring_web_layers](C:\coding\Git\GitHub\SpringBootAndAWS\spring_web_layers.png)

* Web Layer
  + 외부와 요청/응답 관련 
* Service Layer
  + @Service
  + @Transactional
* Repository Layer
  + 데이터 접근 영역 (like DAO)
* DTOs
  + 계층 간(Layers) 데이터 전달을 위한 객체들
* Domain Model
  + 공통의 이해(단순화)
  + @Entity





## 7. JDK1.8 Features

### 1) LocalDate, LocalDateTime

* Date / Calander 
  + 불변객체가 아니다. → 멀티 스레드 환경에서 불안하다.
  + Calander의 월(Month) 값이 상식적이지 않다. ex) `Calander.OCTOBER` 값은 '9'이다.
  + http://d2.naver.com/helloworld/645609
* Hibernate 5.2.10 부터 데이터베이스에 정상 매핑 (SpringBoot 2.X 부터)





















