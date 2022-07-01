package kitchenpos.Acceptance.order;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderTableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.menu.MenuGenerator.*;
import static kitchenpos.order.OrderGenerator.*;
import static kitchenpos.product.ProductGenerator.상품_생성_API_요청;
import static kitchenpos.table.TableGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderAcceptanceTest extends AcceptanceTest {

    private Long 메뉴_아이디;
    private Long 주문_테이블_아이디;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블_아이디 = 테이블_생성_API_호출(주문_테이블_생성_요청(10)).as(OrderTableResponse.class).getId();
        Long 메뉴_그룹_아이디 = 메뉴_그룹_생성_API_호출("메뉴 그룹").as(MenuGroupResponse.class).getId();
        Long 상품_아이디 = 상품_생성_API_요청("상품", 1_000).as(ProductResponse.class).getId();
        MenuProductRequest 메뉴_상품_생성_요청 = 메뉴_상품_생성_요청(상품_아이디, 1L);
        메뉴_아이디 = 메뉴_생성_API_호출("메뉴", 1_000, 메뉴_그룹_아이디, Collections.singletonList(메뉴_상품_생성_요청))
                .body().jsonPath().getLong("id");
    }

    @DisplayName("주문 생성시 주문 물품 리스트가 없으면 예외가 발생해야 한다")
    @Test
    void createOrderByNotIncludeOrderItems() {
        // when
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(주문_테이블_아이디, Collections.emptyList());
        ExtractableResponse<Response> 주문_생성_요청_결과 = 주문_생성_API_요청(주문_생성_요청);

        // then
        주문_생성_실패됨(주문_생성_요청_결과);
    }

    @DisplayName("없는 메뉴로 주문 생성 시 예외가 발생해야 한다")
    @Test
    void createOrderByNotSavedMenuTest() {
        // given
        Long 없는_메뉴_아이디 = -1L;
        OrderLineItemRequest 주문_물품_생성_요청 = 주문_물품_생성_요청(없는_메뉴_아이디, 1L);
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(주문_테이블_아이디, Collections.singletonList(주문_물품_생성_요청));

        // when
        ExtractableResponse<Response> 주문_생성_요청_결과 = 주문_생성_API_요청(주문_생성_요청);

        // then
        주문_생성_실패됨(주문_생성_요청_결과);
    }

    @DisplayName("없는 주문 테이블로 주문하면 예외가 발생해야 한다")
    @Test
    void createOrderByNotSavedOrderTableTest() {
        // given
        Long 없는_테이블_아이디 = -1L;
        OrderLineItemRequest 주문_물품_생성_요청 = 주문_물품_생성_요청(메뉴_아이디, 1L);
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(없는_테이블_아이디, Collections.singletonList(주문_물품_생성_요청));

        // when
        ExtractableResponse<Response> 주문_생성_요청_결과 = 주문_생성_API_요청(주문_생성_요청);

        // then
        주문_생성_실패됨(주문_생성_요청_결과);
    }

    @DisplayName("빈 주문 테이블에 주문하면 예외가 발생해야 한다")
    @Test
    void createOrderByNotEmptyOrderTableTest() {
        // given
        빈_테이블_변경_API_호출(주문_테이블_아이디, true);
        OrderLineItemRequest 주문_물품_생성_요청 = 주문_물품_생성_요청(메뉴_아이디, 1L);
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(주문_테이블_아이디, Collections.singletonList(주문_물품_생성_요청));

        // when
        ExtractableResponse<Response> 주문_생성_요청_결과 = 주문_생성_API_요청(주문_생성_요청);

        // then
        주문_생성_실패됨(주문_생성_요청_결과);
    }

    @DisplayName("정상 상태의 주문 요청 시 주문이 정상 생성 되어야 한다")
    @Test
    void createOrderTest() {
        // given
        빈_테이블_변경_API_호출(주문_테이블_아이디, false);
        OrderLineItemRequest 주문_물품_생성_요청 = 주문_물품_생성_요청(메뉴_아이디, 1L);
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(주문_테이블_아이디, Collections.singletonList(주문_물품_생성_요청));

        // when
        ExtractableResponse<Response> 주문_생성_요청_결과 = 주문_생성_API_요청(주문_생성_요청);

        // then
        주문_생성_성공됨(주문_생성_요청_결과);
    }

    @DisplayName("주문 목록 조회 시 정상 조회되어야 한다")
    @Test
    void findAllOrderTest() {
        // given
        빈_테이블_변경_API_호출(주문_테이블_아이디, false);
        OrderLineItemRequest 주문_물품_생성_요청 = 주문_물품_생성_요청(메뉴_아이디, 1L);
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(주문_테이블_아이디, Collections.singletonList(주문_물품_생성_요청));
        Long 생성된_주문_아이디 = 주문_생성_API_요청(주문_생성_요청).as(OrderResponse.class).getId();

        // when
        ExtractableResponse<Response> 주문_생성_요청_결과 = 주문_목록_조회_API_요청();

        // then
        주문_목록_조회_성공됨(주문_생성_요청_결과, 생성된_주문_아이디);
    }

    @DisplayName("저장되지 않은 주문의 상태를 변경하면 예외가 발생해야 한다")
    @Test
    void updateOrderStateByNotSavedOrderTest() {
        // when
        ExtractableResponse<Response> 주문_상태_변경_결과 = 주문_상태_변경_API_요청(-1L, OrderStatus.MEAL);

        // then
        주문_상태_변경_실패됨(주문_상태_변경_결과);
    }


    @DisplayName("정상 상태의 주문 변경 요청은 해당 상태로 변경되어야 한다")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = { "COOKING", "MEAL", "COMPLETION" })
    void updateOrderStateTest(OrderStatus orderStatus) {
        // given
        빈_테이블_변경_API_호출(주문_테이블_아이디, false);
        OrderLineItemRequest 주문_물품_생성_요청 = 주문_물품_생성_요청(메뉴_아이디, 1L);
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(주문_테이블_아이디, Collections.singletonList(주문_물품_생성_요청));
        Long 생성된_주문_아이디 = 주문_생성_API_요청(주문_생성_요청).as(OrderResponse.class).getId();

        // when
        ExtractableResponse<Response> 주문_상태_변경_결과 = 주문_상태_변경_API_요청(생성된_주문_아이디, orderStatus);

        // then
        주문_상태_변경됨(주문_상태_변경_결과, orderStatus);
    }

    void 주문_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    void 주문_생성_성공됨(ExtractableResponse<Response> response) {
        OrderResponse order = response.as(OrderResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    void 주문_목록_조회_성공됨(ExtractableResponse<Response> response, Long savedOrderId) {
        List<Long> orderIds = response.body().jsonPath().getList("id", Long.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(orderIds.size()).isGreaterThanOrEqualTo(1);
        assertThat(orderIds).contains(savedOrderId);
    }

    void 주문_상태_변경_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    void 주문_상태_변경됨(ExtractableResponse<Response> response, OrderStatus expectedOrderState) {
        OrderResponse order = response.as(OrderResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(order.getOrderStatus()).isEqualTo(expectedOrderState);
    }
}
