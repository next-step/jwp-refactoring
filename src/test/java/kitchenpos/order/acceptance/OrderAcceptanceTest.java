package kitchenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.menu.fixture.MenuGroupTestFixture.메뉴그룹_생성_요청;
import static kitchenpos.menu.fixture.MenuGroupTestFixture.메뉴그룹_생성됨;
import static kitchenpos.menu.fixture.MenuTestFixture.*;
import static kitchenpos.menu.fixture.ProductTestFixture.상품_생성_요청;
import static kitchenpos.menu.fixture.ProductTestFixture.상품_생성됨;
import static kitchenpos.order.fixture.OrderTableTestFixture.*;
import static kitchenpos.order.fixture.OrderTestFixture.*;

@DisplayName("주문 관련 기능 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {
    /**
     * Given : 주문테이블과 메뉴가 생성되어 있다.
     * When : 주문 생성을 요청한다.
     * Then : 주문이 생성된다.
     */
    @DisplayName("주문 생성 인수 테스트")
    @Test
    void creatOrder() {
        // given : 주문테이블 및 메뉴 생성
        ProductResponse 상품 = 상품_생성_요청("후라이드", 18_000).as(ProductResponse.class);
        MenuGroupResponse 메뉴그룹 = 메뉴그룹_생성_요청("한마리치킨").as(MenuGroupResponse.class);
        MenuResponse 후라이드치킨 = 메뉴_생성_요청("후라이드치킨",
                BigDecimal.valueOf(18_000),
                메뉴그룹.getId(),
                Lists.newArrayList(new MenuProductRequest(상품.getId(), 1L))).as(MenuResponse.class);
        OrderTable 주문테이블 = 주문테이블_생성_요청(2, false).as(OrderTable.class);

        // when
//        ExtractableResponse<Response> response = 주문_생성_요청(주문테이블, 후라이드치킨);

        // then
//        주문_생성됨(response);
    }

    /**
     * Given : 주문테이블과 메뉴가 생성되어 있다.
     * When : 빈 테이블에서 주문 생성을 요청한다.
     * Then : 주문이 실패한다.
     */
    @DisplayName("주문 생성시 빈테이블 예외 인수 테스트")
    @Test
    void creatOrderExceptionByEmptyTable() {
        // given : 주문테이블 및 메뉴 생성
        ProductResponse 상품 = 상품_생성_요청("후라이드", 18_000).as(ProductResponse.class);
        MenuGroupResponse 메뉴그룹 = 메뉴그룹_생성_요청("한마리치킨").as(MenuGroupResponse.class);
        MenuResponse 후라이드치킨 = 메뉴_생성_요청("후라이드치킨",
                BigDecimal.valueOf(18_000),
                메뉴그룹.getId(),
                Lists.newArrayList(new MenuProductRequest(상품.getId(), 1L))).as(MenuResponse.class);
        OrderTable 주문테이블 = 주문테이블_생성_요청(2, true).as(OrderTable.class);

        // when
//        ExtractableResponse<Response> response = 주문_생성_요청(주문테이블, 후라이드치킨);

        // then
//        주문_생성_실패됨(response);
    }

    /**
     * Given : 주문이 생성되어 있고,
     * When : 주문 목록 조회를 요청한다.
     * Then : 주문 목록을 응답한다.
     */
    @DisplayName("주문 조회 인수 테스트")
    @Test
    void findOrders() {
        // given
        ProductResponse 상품 = 상품_생성_요청("후라이드", 18_000).as(ProductResponse.class);
        MenuGroupResponse 메뉴그룹 = 메뉴그룹_생성_요청("한마리치킨").as(MenuGroupResponse.class);
        MenuResponse 후라이드치킨 = 메뉴_생성_요청("후라이드치킨",
                BigDecimal.valueOf(18_000),
                메뉴그룹.getId(),
                Lists.newArrayList(new MenuProductRequest(상품.getId(), 1L))).as(MenuResponse.class);
        OrderTable 주문테이블 = 주문테이블_생성_요청(2, false).as(OrderTable.class);
//        주문_생성_요청(주문테이블, 후라이드치킨);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_조회됨(response);
    }

    /**
     * Given : 주문이 생성되어 있고,
     * When : 주문의 상태 변경을 요청한다.
     * Then : 주문의 상태가 변경된다.
     */
    @DisplayName("주문 상태 변경 요청 인수 테스트")
    @Test
    void changeOrderStatus() {
        // given
        ProductResponse 상품 = 상품_생성_요청("후라이드", 18_000).as(ProductResponse.class);
        MenuGroupResponse 메뉴그룹 = 메뉴그룹_생성_요청("한마리치킨").as(MenuGroupResponse.class);
        MenuResponse 후라이드치킨 = 메뉴_생성_요청("후라이드치킨",
                BigDecimal.valueOf(18_000),
                메뉴그룹.getId(),
                Lists.newArrayList(new MenuProductRequest(상품.getId(), 1L))).as(MenuResponse.class);
        OrderTable 주문테이블 = 주문테이블_생성_요청(2, false).as(OrderTable.class);
//        Order 주문 = 주문_생성_요청(주문테이블, 후라이드치킨).as(Order.class);

        // when
//        ExtractableResponse<Response> response = 주문_상태_수정_요청(주문.getId(), "MEAL");

        // then
//        주문_상태_수정됨(response);
    }

    /**
     * Given : 주문이 생성되어 있고, 주문의 상태가 완료되어 있다
     * When : 주문의 상태를 수정 요청하면
     * Then : 주문의 상태 변경이 실패한다.
     */
    @DisplayName("주문 상태 변경 요청 인수 테스트")
    @Test
    void changeOrderStatusException_이미완료된_주문() {
        // given
        Product 상품 = 상품_생성_요청("후라이드", 18_000).as(Product.class);
        MenuGroup 메뉴그룹 = 메뉴그룹_생성_요청("한마리치킨").as(MenuGroup.class);
        Menu 후라이드치킨 = 메뉴_생성_요청("후라이드치킨",
                BigDecimal.valueOf(18_000),
                메뉴그룹.getId(),
                Lists.newArrayList(new MenuProductRequest(상품.getId(), 1L))).as(Menu.class);
        OrderTable 주문테이블 = 주문테이블_생성_요청(2, false).as(OrderTable.class);
        Order 주문 = 주문_생성_요청(주문테이블, 후라이드치킨).as(Order.class);
        주문_상태_수정_요청(주문.getId(), "COMPLETION");

        // when
        ExtractableResponse<Response> response = 주문_상태_수정_요청(주문.getId(), "COOKING");

        // then
        주문_상태_수정_실패됨(response);
    }

    /**
     * Scenario : 주문을 성공적으로 생성
     * <p>
     * Given : 상품이 등록되어 있음
     * And : 메뉴그룹이 등록되어 있음
     * When : 메뉴를 생성한다.
     * Then : 메뉴가 생성된다.
     * And : 메뉴 목록을 조회한다.
     * And : 메뉴 목록이 조회된다.
     * <p>
     * Given : 빈 테이블을 생성한다.
     * And : 빈 테이블이 생성됨
     * And : 빈 테이블을 주문 가능한 테이블 상태로 변경(empty --> false)
     * And : 빈 테이블의 손님의 수를 2명으로 변경
     * When : 테이블에서 주문을 요청한다.
     * Then : 주문이 요청이 성공함.
     * And : 주문 목록을 조회 요청
     * And : 주문 목록을 조회함
     */
    @DisplayName("주문 관련 통합 인수 테스트")
    @Test
    void orderIntegrationTest() {
        // given
        ExtractableResponse<Response> createProductResponse = 상품_생성_요청("순살치킨", 9_000);
        Long productId = createProductResponse.jsonPath().getLong("id");
        상품_생성됨(createProductResponse);

        // and
        ExtractableResponse<Response> createMenuGroupResponse = 메뉴그룹_생성_요청("세마리치킨");
        Long menuGroupId = createMenuGroupResponse.jsonPath().getLong("id");
        메뉴그룹_생성됨(createMenuGroupResponse);

        // when
        ExtractableResponse<Response> createMenuResponse = 메뉴_생성_요청("순살세마리", BigDecimal.valueOf(27_000), menuGroupId, Lists.newArrayList(new MenuProductRequest(productId, 3L)));
        Menu 메뉴 = createMenuResponse.as(Menu.class);
        // And
        메뉴_생성됨(createMenuResponse);
        // And
        ExtractableResponse<Response> menuListResponse = 메뉴_조회_요청();
        // And
        메뉴_조회됨(menuListResponse);


        // given
        ExtractableResponse<Response> createOrderTableResponse = 주문테이블_생성_요청(0, true);
        OrderTable 주문테이블 = createOrderTableResponse.as(OrderTable.class);
        // and
        주문테이블_생성됨(createOrderTableResponse);
        // and
        ExtractableResponse<Response> changeEmptyResponse = 주문테이블_빈테이블_여부_수정_요청(주문테이블.getId(), false);
        주문테이블 = changeEmptyResponse.as(OrderTable.class);
        // and
        주문테이블_수정됨(changeEmptyResponse);
        // and
        ExtractableResponse<Response> orderTableChangeNumberOfGuestsResponse = 주문테이블_손님수_수정_요청(주문테이블.getId(), 2);
        주문테이블 = orderTableChangeNumberOfGuestsResponse.as(OrderTable.class);
        // and
        주문테이블_수정됨(orderTableChangeNumberOfGuestsResponse);

        // when
        ExtractableResponse<Response> createOrderResponse = 주문_생성_요청(주문테이블, 메뉴);
        // then
        주문_생성됨(createOrderResponse);
        // and
        ExtractableResponse<Response> ordersResponse = 주문_목록_조회_요청();
        // and
        주문_목록_조회됨(ordersResponse);
    }
}
