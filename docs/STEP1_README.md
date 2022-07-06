## STEP1 - 테스트를 통한 코드 보호

### STEP1 요구사항
- 요구사항1
  - `kitchenpos` 패키지의 코드를 보고 키친포스의 요구 사항을 README.md에 작성한다.
  - 참고 사이트
    - [마크다운(Markdown)](https://dooray.com/htmls/guides/markdown_ko_KR.html)
- 요구사항2
  - 정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다. 모든 Business Object에 대한 테스트 코드를 작성한다.
      - `@SpringBootTest`를 이용한 통합 테스트 또는 `@ExtendWith(MockitoExtension.class)`을 이용한 단위 테스트 코드를 작성한다.
  - 참고 사이트
    - [Testing in Spring Boot - Baeldung](https://www.baeldung.com/spring-boot-testing)
    - [Exploring the Spring Boot TestRestTemplate](https://www.baeldung.com/spring-boot-testresttemplate)
- 프로그래밍 요구사항
    - Lombok은 그 강력한 기능만큼 사용상 주의를 요한다.
        - 무분별한 setter 메서드 사용
        - 객체 간에 상호 참조하는 경우 무한 루프에 빠질 가능성
    - 참고 사이트
      - [Lombok 사용상 주의점(Pitfall)](https://kwonnam.pe.kr/wiki/java/lombok/pitfall)
