package kitchenpos.order.acceptanceTest;

import static kitchenpos.menugroup.acceptance.MenuGroupRestAssured.메뉴_그룹_생성_요청;
import static kitchenpos.menu.acceptance.MenuRestAssured.메뉴_생성_요청;
import static kitchenpos.order.acceptanceTest.OrderRestAssured.주문_목록_조회_요청;
import static kitchenpos.order.acceptanceTest.OrderRestAssured.주문_상태_수정_요청;
import static kitchenpos.order.acceptanceTest.OrderRestAssured.주문_생성_요청;
import static kitchenpos.product.acceptance.ProductRestAssured.상품_생성_요청;
import static kitchenpos.ordertable.acceptance.TableRestAssured.주문_테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.acceptance.AcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("주문 관련 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private ProductResponse 하와이안피자;
    private ProductResponse 콜라;
    private ProductResponse 피클;
    private MenuGroupResponse 피자;
    private MenuResponse 하와이안피자세트;
    private MenuProductRequest 하와이안피자상품;
    private MenuProductRequest 콜라상품;
    private MenuProductRequest 피클상품;
    private OrderTableResponse 주문테이블;
    private OrderRequest 주문;
    private OrderLineItemRequest 하와이안피자세트주문;

    @BeforeEach
    public void setUp() {
        super.setUp();

        하와이안피자 = 상품_생성_요청(ProductRequest.of("하와이안피자", BigDecimal.valueOf(15_000))).as(ProductResponse.class);
        콜라 = 상품_생성_요청(ProductRequest.of("콜라", BigDecimal.valueOf(2_000))).as(ProductResponse.class);
        피클 = 상품_생성_요청(ProductRequest.of("피클", BigDecimal.valueOf(1_000))).as(ProductResponse.class);

        피자 = 메뉴_그룹_생성_요청(MenuGroupRequest.from("피자")).as(MenuGroupResponse.class);

        하와이안피자상품 = MenuProductRequest.of(하와이안피자.getId(), 1L);
        콜라상품 = MenuProductRequest.of(콜라.getId(), 1L);
        피클상품 = MenuProductRequest.of(피클.getId(), 1L);

        하와이안피자세트 = 메뉴_생성_요청(MenuRequest.of("하와이안피자세트", BigDecimal.valueOf(18_000L), 피자.getId(),
            Arrays.asList(하와이안피자상품, 콜라상품, 피클상품))).as(MenuResponse.class);

        주문테이블 = 주문_테이블_생성_요청(OrderTableRequest.of(0, false)).as(OrderTableResponse.class);
        하와이안피자세트주문 = OrderLineItemRequest.from(하와이안피자세트.getId(), 1);
        주문 = OrderRequest.of(주문테이블.getId(), OrderStatus.COOKING, Collections.singletonList(하와이안피자세트주문));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // whenN
        ExtractableResponse<Response> response = 주문_생성_요청(주문);

        // then
        주문_생성됨(response);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void findAllOrder() {
        // given
        OrderResponse orderResponse = 주문_생성_요청(주문).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_응답됨(response);
        주문_목록_확인됨(response, Arrays.asList(orderResponse.getId()));
    }

    @DisplayName("주문 상태를 수정한다.")
    @Test
    void updaeOrderStatus() {
        // given
        OrderStatus expectOrderStatus = OrderStatus.MEAL;
        OrderResponse orderResponse = 주문_생성_요청(주문).as(OrderResponse.class);
        List<OrderLineItemRequest> orderLineItemRequests = orderResponse.getOrderLineItems()
            .stream()
            .map(orderLineItemResponse -> OrderLineItemRequest.from(orderLineItemResponse.getMenuId(), orderLineItemResponse.getQuantity()))
            .collect(Collectors.toList());
        OrderRequest orderRequest = OrderRequest.of(orderResponse.getOrderTableId(), expectOrderStatus, orderLineItemRequests);

        // when
        ExtractableResponse<Response> response = 주문_상태_수정_요청(orderResponse.getId(), orderRequest);

        // then
        주문_상태_수정됨(response, expectOrderStatus.name());
    }

    private void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static void 주문_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 주문_목록_확인됨(ExtractableResponse<Response> response, List<Long> orderIds) {
        List<Long> resultIds = response.jsonPath().getList(".", OrderResponse.class)
            .stream()
            .map(OrderResponse::getId)
            .collect(Collectors.toList());

        assertThat(resultIds).containsAll(orderIds);
    }

    private void 주문_상태_수정됨(ExtractableResponse<Response> response, String expectedOrderStatus) {
        String result = response.jsonPath().getString("orderStatus");

        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(result).isEqualTo(expectedOrderStatus)
        );
    }
}
