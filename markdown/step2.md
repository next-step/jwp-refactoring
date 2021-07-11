# 🚀 2단계 - 서비스 리팩터링

## 용어 사전

[바로가기](../README.md)

## 비즈니스 요구 사항

[바로가기](step1.md)

## DDD Start!

- [ ] 테스트하기 쉬운 부분과 어려운 부분을 분리하여 단위 테스트 구현
  - [X] Presentation Layer 에서 모델이 직접 받는 것 삭제 후 dto 정의
    - [X] Mock 테스트 말고 통합테스트로 변경
  - [ ] Service Layer Diet
    - [ ] 도메인 추출
    - [ ] 핵심 비즈니스 로직을 추출하여 TDD 방식으로 개발
  - [ ] Aggregate 도출
    - [ ] Repository 작성
- [X] JDBC -> JPA 변경
  - [X] 데이터베이스 스키마 변경 및 마이그레이션시 FlyWay 적용
- [ ] 시나리오 기반 인수 테스트 작성
