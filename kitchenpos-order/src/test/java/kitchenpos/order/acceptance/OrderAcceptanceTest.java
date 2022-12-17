package kitchenpos.order.acceptance;


import static java.util.Collections.singletonList;
import static kitchenpos.acceptance.MenuAcceptanceTestFixture.메뉴_등록되어_있음;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestFixture.메뉴_그룹_등록되어_있음;
import static kitchenpos.order.acceptance.OrderAcceptanceTestFixture.주문_목록_조회_요청;
import static kitchenpos.order.acceptance.OrderAcceptanceTestFixture.주문_목록_조회됨;
import static kitchenpos.order.acceptance.OrderAcceptanceTestFixture.주문_목록_포함됨;
import static kitchenpos.order.acceptance.OrderAcceptanceTestFixture.주문_상태_변경_요청;
import static kitchenpos.order.acceptance.OrderAcceptanceTestFixture.주문_상태_변경됨;
import static kitchenpos.order.acceptance.OrderAcceptanceTestFixture.주문_생성_요청;
import static kitchenpos.order.acceptance.OrderAcceptanceTestFixture.주문_생성되어_있음;
import static kitchenpos.order.acceptance.OrderAcceptanceTestFixture.주문_생성됨;
import static kitchenpos.acceptance.ProductAcceptanceTestFixture.상품_등록_되어_있음;
import static kitchenpos.acceptance.TableAcceptanceTestFixture.주문_테이블_생성되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderRequest.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {
    private OrderRequest 주문_A;
    private OrderRequest 주문_B;

    @BeforeEach
    void orderSetUp() {
        super.setUp();

        Long 후라이드치킨 = 상품_등록_되어_있음("후라이드치킨", BigDecimal.valueOf(15000));
        Long 추천메뉴 = 메뉴_그룹_등록되어_있음("추천메뉴");
        Long 두마리치킨 = 메뉴_등록되어_있음("두마리치킨", new BigDecimal(25000), 추천메뉴, 후라이드치킨, 2);
        Long 주문테이블_A = 주문_테이블_생성되어_있음(5, false);
        Long 주문테이블_B = 주문_테이블_생성되어_있음(3, false);
        OrderLineItemRequest 주문항목_A = new OrderLineItemRequest(두마리치킨, 1);
        OrderLineItemRequest 주문항목_B = new OrderLineItemRequest(두마리치킨, 2);
        주문_A = new OrderRequest(주문테이블_A, singletonList(주문항목_A));
        주문_B = new OrderRequest(주문테이블_B, singletonList(주문항목_B));
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create() {
        ExtractableResponse<Response> response = 주문_생성_요청(주문_A);

        주문_생성됨(response);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void getOrderList() {
        ExtractableResponse<Response> createResponse1 = 주문_생성되어_있음(주문_A);
        ExtractableResponse<Response> createResponse2 = 주문_생성되어_있음(주문_B);

        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        주문_목록_조회됨(response);
        주문_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        OrderStatus exceptedStatus = OrderStatus.COOKING;
        OrderResponse existOrder = 주문_생성되어_있음(주문_A).as(OrderResponse.class);

        ExtractableResponse<Response> response = 주문_상태_변경_요청(existOrder.getId(), exceptedStatus);

        주문_상태_변경됨(response, exceptedStatus);
    }
}
