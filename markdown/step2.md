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