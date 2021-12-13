# 🚀 1단계 - 테스트를 통한 코드 보호

## 실습 환경 구축

[리팩터링](https://github.com/next-step/jwp-refactoring) 저장소를 기반으로 미션을
진행한다. [온라인 코드 리뷰 요청 1단계](https://github.com/next-step/nextstep-docs/blob/master/codereview/review-step1.md) 문서를 참고해 실습
환경을 구축한다.

1. 미션 시작 버튼을 눌러 미션을 시작한다.
2. 저장소에 자신의 GitHub 아이디로 된 브랜치가 생성되었는지 확인한다.
3. 저장소를 자신의 계정으로 Fork 한다.

## 요구 사항 1

**`kitchenpos`** 패키지의 코드를 보고 키친포스의 요구 사항을 **`README.md`**에 작성한다. 미션을 진행함에 있어 아래 문서를 적극 활용한다.

- [마크다운(Markdown) - Dooray!](https://dooray.com/htmls/guides/markdown_ko_KR.html)

## 요구 사항 2

정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다. 모든 Business Object에 대한 테스트 코드를 작성한다. **`@SpringBootTest`**를 이용한 통합 테스트 코드
또는 **`@ExtendWith(MockitoExtension.class)`**를 이용한 단위 테스트 코드를 작성한다.

인수 테스트 코드 작성은 권장하지만 필수는 아니다. 미션을 진행함에 있어 아래 문서를 적극 활용한다.

- [Testing in Spring Boot - Baeldung](https://www.baeldung.com/spring-boot-testing)
- [Exploring the Spring Boot TestRestTemplate](https://www.baeldung.com/spring-boot-testresttemplate)

## 프로그래밍 요구 사항

Lombok은 그 강력한 기능만큼 사용상 주의를 요한다.

- 무분별한 setter 메서드 사용
- 객체 간에 상호 참조하는 경우 무한 루프에 빠질 가능성
- [Lombok 사용상 주의점(Pitfall)](https://kwonnam.pe.kr/wiki/java/lombok/pitfall)

이번 과정에서는 Lombok 없이 미션을 진행해 본다.

## TO-DO 리스트

- [ ] 테스트 작성
    - [ ] 상품
        - [ ] 상품 생성
            - [ ] 상품의 가격이 존재하지 않거나, 0이하 일때 생성 실패
        - [ ] 상품 목록 조회
    - [ ] 메뉴
        - [ ] 메뉴 생성
            - [ ] 메뉴의 가격이 존재하지 않거나, 0이하 일때 생성 실패
            - [ ] 메뉴가 이미 존재할 때 생성실패
            - [ ] 메뉴에 등록할 상품이 존재하지 않으면 생성실패
            - [ ] 메뉴 가격은 등록할 상품의 갯수와 상품의 가격을 곱한 값을 반환
        - [ ] 메뉴 목록 조회
    - [ ] 메뉴 그룹
        - [ ] 메뉴 그룹 생성
        - [ ] 메뉴 그룹 목록 조회
    - [ ] 주문
        - [ ] 주문 생성
            - [ ] 주문할 상품이 존재하지 않으면 생성 실패
            - [ ] 메뉴의 상품 갯수와 주문 상품 갯수가 다를 때 생성 실패
            - [ ] 주문한 테이블이 존재하지 않을 때 생성 실패
        - [ ] 주문 목록 조회
        - [ ] 주문 상태 변경
            - [ ] 주문이 존재하지 않으면 변경 실패
            - [ ] 주문의 상태가 COMPLETION 이면 변경 실패
    - [ ] 테이블
        - [ ] 테이블 생성
        - [ ] 테이블 목록 조회
        - [ ] 테이블 상태 치우기(비우기) 변경
            - [ ] 테이블 그룹이 존재하지 않으면 변경 실패
            - [ ] 테이블의 상태가 COOKING, MEAL 일 경우 변경 실패
        - [ ] 테이블 인원 변경
            - [ ] 변경 인원이 0보다 작으면 변경 실패
            - [ ] 테이블이 존재 하지 않으면 변경 실패
            - [ ] 테이블이 비어있는 상태면 변경 실패
    - [ ] 테이블 그룹
        - [ ] 테이블 그룹 생성
            - [ ] 주문 테이블이 존재하지 않거나, 하나일 때 생성 실패
            - [ ] 등록한 주문 테이블의 갯수가 실제 저장된 주문 테이블의 갯수와 다를 때 생성 실패
            - [ ] 등록하려는 주문 테이블이 이미 테이블 다른 그룹에 속해 있을 때 생성 실패
        - [ ] 테이블 그룹 삭제
            - [ ] 주문의 상태가 COOKING, MEAL인 주문 테이블이 존재하면 삭제 실패
