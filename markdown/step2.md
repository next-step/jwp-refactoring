## 리팩토링 과정
- 패키지 나누기
    - menu
    - order
    - product
  

- dao -> repository
    - jpa dependency 추가
    - `repository` 인터페이스 추가
    - `serviceTest` Mock repository를 이용한 테스트 케이스 추가
    - `service` dao대신 repository를 이용한 메서드 추가(메서드명_re)
    - `controllerTest` 리팩토링용 api 호출 테스트 추가
    - `controller` 리팩토링용 api 추가      
    - `domain` 클래스 @Entity 어노테이션 추가

  - dao 삭제 및 dao사용 리팩토링 전 메소드 삭제
    - `service`
      - dao 필드 삭제
      - 기존메서드 삭제 및 리팩토링 메서드로 교체
    - `serviceTest`
      - dao mock 삭제
      - 기존메서드 테스트 삭제
    - `controllerTest` 기존메서드 테스트 삭제
    - `controller` 기존메서드 삭제
    - `dao` dao 삭제

- `dto` 각 패키지 추가
    - `controller`, `service` dto 추가


- 리팩토링
- 자바 코드 번켄션 지키기
  - 메서드는 하나의 역할만 수행하도록 분리
  - 비즈니스 로직 도메인으로 위임