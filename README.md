# 키친포스

## 요구 사항

**상품(product)**

- 상품은 이름과 가격을 가진다.
- 상품은 등록할 수 있다.
- 상품의 가격이 올바르지 않으면 등록할 수 없다.
  - 가격이 0원 이상이어야 한다.
- 상품의 전체 목록을 조회할 수 있다.

**메뉴(menu)**

- 메뉴는 이름, 가격, 메뉴 그룹 그리고 1개 이상의 메뉴 상품으로 구성된다.

- 메뉴를 등록할 수 잇다. 
- 메뉴의 가격이 올바르지 않으면 등록될 수 없다.
  - 가격이 0원 이상이어야 한다.
- 메뉴는 메뉴 그룹이 올바르지 않으면 등록될 수 없다.
  - 메뉴를 등록하는 시점에 메뉴 그룹이 미리 등록되어 있어야 한다.
- 메뉴의 가격이 기존의 메뉴그룹의 가격 합보다 높을 경우 등록될 수 없다.

--

**주문 테이블(order table)**

- 주문 테이블 지정시, 단체 지정(table group)은 빈 값으로 초기화되어진다.
- 주문 테이블을 빈 테이블로 만들 수 있다.
  - 주문의 상태가 조리이거나, 식사의 경우에는 빈 테이블로 만들 수 없다.
- 주문 테이블에는 방문한 손님 수를 변경할 수 있다.
  - 만약 처음 방문한 손님의 수가 없을 경우, 변경할 수 없다.
  - 빈 주문 테이블의 경우에는 손님의 수를 변경할 수 없다.
- 주문 테이블의 전체 목록을 조회할 수 있다.

**단체 지정(table group)**

- 단체 지정을 원할 경우, 

  - 적어도 2개 이상의 주문된 테이블을 가져야 한다. 

  - 단체 지정 시점에, 주문 테이블이 단체 지정시 주문받은 주문 테이블과 숫자가 맞지 않으면 생성될 수 없다.
  - 단체 지정 시점에, 시점에 주문 테이블이 빈 테이블이 아니라면 단체 지정을 할 수 없다.

- 단체 지정을 취소할 수 있다.
  - 그러나 만약 주문이 조리 또는 식사 중일 때는 단체 지정을 취소할 수 없다.

**주문(order)**

- 주문은 등록 할 수 있다
- 주문의 주문 항목이 올바르지 않으면 주문을 등록할 수 없다.
  - 주문 항목이 비어있다면 주문을 등록할 수 없다.
  - 주문 항목의 갯수가 주문 항목의 메뉴의 갯수와 일치 하지 않으면 등록할 수 없다.
- 주문의 주문 테이블이 빈 테이블일 경우 주문을 등록할 수 없다.
- 주문이 등록되면, 처음 주문 상태(order status)는 조리(COOKING) 상태가 된다.
- 주문의 전체 현황을 조회할 수 있다.
- 주문의 주문 상태(order status)를 변경할 수 있다.
  - 주문의 주문 상태(order status)를 식사 상태로 변경 할 수 있다.
  
- 이미 계산이 완료된 주문은 주문 상태(order status)를 바꿀 수 없다.


## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |

TODO
 - [x] kitchenpos 패키지의 코드를 보고 키친포스의 요구 사항을 README.md 에 작성

-- 2단계
- [x] Menu JPA 적용
- [x] Order JPA 적용

- [x] Menu Service 리팩토링
- [x] Order Service 리팩토링

---
피드백 리뷰 결과
- [x] Custom Exception 만들기
- [x] setter 제거하기
- [x] Price 컬럼 원시값 포장 객체 생성
- [x] 일급 컬렉션 생성 (MenuProducts)

---

### **2단계**

서비스 생성 후, 해당 서비스의 역할을 호출하는 이벤트 생성 예정


- [x] 이벤트 기반 상태 변경
  - [x] OrderCreatedEvent 이벤트 생성  
  
  - [x] MenuCreatedEvent 이벤트 생성
    
  - [x] TableGroupCreatedEvent 이벤트 생성

해볼 수 있는 기능들
- JPA 적용
- 이벤트 주도 개발
- 쿼리 커멘드 분리

도메인별 라이프사이클을 고려해 의존성을 분리한다.

- Product 는 메뉴가 주문되기 전 미리 등록되어있어야 한다. 그러므로, MenuProduct 에서 분리되어있어야 한다.

---

### **3단계**

3단계의 미션은 의존성 분리하기 입니다. 다시 말하면 도메인 어디에 어떤 의존성을 가져야하는가? 에 대한 질문으로�시작했어야 했었습니다.
그러나, 2단계 서비스 리팩토링 하면서 도메인 레이어의 의존관계를 임의대로 만들어 버리면서 3단계 미션에 대한 이해가 부족했습니다. 그런 결과로 리뷰어님 입장에서는 더욱 곤란했을 거라 판단됩니다.

이번에는 조금더 명확하게 의존성을 표현하기 위해서 다이어그램을 함께 첨부드립니다.

![images](https://tva1.sinaimg.cn/large/008i3skNgy1gseef9dy6gj316f0u078v.jpg)

 크게 5개의 큰 영역을 가집니다. **Order, Menu, MenuGroup, TableGroup, Product** 이렇게 가지며 이는 각 엔티티의 라이프사이클에 따라 구분되어졌습니다.

변경된 코드가 많아, 다이어그램과 함께 전체 코드를 보면 조금더 이해가 쉽지 않을까 싶습니다.

양방향 의존은 모두 제거되었고, 다이어그램에서 점섬은 id 를 통한 접근을 말합니다.

---

### **4단계** 

멀티 모듈 만들기

먼저 총 4개의 모듈로 존재합니다.

- common
- domain
- web

먼저 **common** 은 최대한 작게 가져가기 위한 노력을 했습니다. 단순히 Type, Util 을 가진 순수한 자바 객체만 존재하도록 했습니다. 

이 **Common** 모듈은 이름이 의도하는 것과 같이, domain, web 모듈 의존성에 추가되었습니다.

그래서 의존성에서도 이런 부분이 반영되어있습니다.

```groovy
plugins {
    id 'java-library'
}

group = 'camp.nextstep.edu'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '1.8'

dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.3.1'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.3.1'
}

repositories {
    mavenCentral()
}

test {
    useJUnitPlatform()
}

jar {
    enabled = true
}
```

다음  **Domain** 모듈은 핵심적인 역할을 합니다. 도메인 서비스, 도메인 엔티티, 레포지터리를 가지고 있습니다. 

```groovy
plugins {
    id 'org.springframework.boot' version '2.4.1'
    id 'io.spring.dependency-management' version '1.0.10.RELEASE'
    id 'java'
}

group = 'camp.nextstep.edu'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '1.8'

repositories {
    mavenCentral()
}

dependencies {

    compile project(':common')

    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.flywaydb:flyway-core'
    runtimeOnly 'com.h2database:h2'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
    useJUnitPlatform()
}

bootJar { enabled = false }
jar { enabled = true }
```

이전에 **Application** 패키지에 있던 Service는 도메인 서비스라 판단되어, **Application** 클래스를 도메인 서비스로 변경했습니다.

마지막으로 **Web 패키지** 는 컨트롤러 관련된 코드로 `spring-boot-starter-web` 에 대한 의존성을 가지고 있습니다.

```groovy
plugins {
    id 'org.springframework.boot' version '2.4.1'
    id 'io.spring.dependency-management' version '1.0.10.RELEASE'
    id 'java'
}

repositories {
    mavenCentral()
}

group = 'camp.nextstep.edu'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '1.8'

dependencies {

    compile project(':domain')
    compile project(':common')

    implementation 'org.springframework.boot:spring-boot-starter-web'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
    useJUnitPlatform()
}
```

**마지막으로, 멀티모듈로 나눈 최종본에 root 모듈은 없습니다. root 모듈이 책임져야할 부분이 없을 것같아 제거했습니다.**

