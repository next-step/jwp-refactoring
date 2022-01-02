package common;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import menu.dto.*;
import order.domain.OrderStatus;
import order.dto.OrderLineItemRequest;
import order.dto.OrderRequest;
import order.dto.OrderResponse;
import order.dto.OrderStatusUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import table.dto.OrderTableCreateRequest;
import table.dto.OrderTableResponse;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 테이블;
    private MenuResponse 메뉴;
    private ProductResponse 소고기한우;
    private MenuGroupResponse 추천메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();

        추천메뉴 = MenuAcceptanceTest.메뉴그룹_등록되어있음(MenuGroupRequest.of("추천메뉴"));
        소고기한우 = MenuAcceptanceTest.상품_등록되어있음(ProductRequest.of("소고기한우", BigDecimal.valueOf(30000)));
        메뉴 = MenuAcceptanceTest.메뉴_등록되어있음(
                "소고기+소고기",
                50000,
                추천메뉴.toEntity(),
                Arrays.asList(
                        MenuProductRequest.of(소고기한우.toEntity(), 2L)
                )
        );
        테이블 = TableAcceptanceTest.테이블_등록되어_있음(OrderTableCreateRequest.of(4, false));
    }

    @DisplayName("주문 관리")
    @Test
    void handleOrder() {
        // 주문 생성
        OrderRequest orderRequest = OrderRequest.of(
                테이블.getId(),
                Arrays.asList(
                        OrderLineItemRequest.of(메뉴.getId(), 2)
                )
        );
        ExtractableResponse<Response> createResponse = 주문_생성_요청(orderRequest);
        OrderResponse savedOrder = 주문_생성_확인(createResponse);

        // 주문 조회
        ExtractableResponse<Response> findResponse = 모든_주문_조회_요청();
        모든_주문_조회_확인(findResponse, savedOrder);

        // 주문 상태 변경
        OrderStatusUpdateRequest changeOrderStatus = OrderStatusUpdateRequest.of(OrderStatus.COMPLETION.name());
        ExtractableResponse<Response> updateResponse = 주문_상태_변경_요청(savedOrder.getId(), changeOrderStatus);
        주문_상태_변경_확인(updateResponse, changeOrderStatus);
    }

    public static void 주문_상태_변경_확인(ExtractableResponse<Response> updateResponse, OrderStatusUpdateRequest request) {
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        OrderResponse order = updateResponse.as(OrderResponse.class);
        assertThat(order.getOrderStatus()).isEqualTo(request.getOrderStatus());
    }

    private void 모든_주문_조회_확인(ExtractableResponse<Response> findResponse, OrderResponse expected) {
        assertAll(
                () -> assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> {
                    List<OrderResponse> orderResponses = findResponse.jsonPath().getList(".", OrderResponse.class);
                    List<Long> orderIds = orderResponses.stream()
                            .map(orderResponse -> orderResponse.getId())
                            .collect(Collectors.toList());
                    List<Long> orderLineItems = orderResponses.stream()
                            .map(orderResponse -> orderResponse.getOrderLineItemResponseList())
                            .flatMap(Collection::stream)
                            .map(orderLineItemResponse -> orderLineItemResponse.getSeq())
                            .collect(Collectors.toList());

                    assertThat(orderIds).contains(expected.getId());
                    assertThat(orderLineItems).containsAll(expected.createOrderLineItemResponseSeqs());
                }
        );
    }

    private OrderResponse 주문_생성_확인(ExtractableResponse<Response> createResponse) {
        OrderResponse savedOrder = createResponse.as(OrderResponse.class);
        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> {
                    assertThat(savedOrder.getOrderTableId()).isEqualTo(테이블.getId());
                    assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                    assertThat(savedOrder.getOrderLineItemResponseList()).isNotEmpty();
                }
        );
        return savedOrder;
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Long id, OrderStatusUpdateRequest request) {
        return TestApiClient.update(request, "/api/orders/" + id + "/order-status");
    }

    private ExtractableResponse<Response> 모든_주문_조회_요청() {
        return TestApiClient.get("/api/orders");
    }

    private static ExtractableResponse<Response> 주문_생성_요청(OrderRequest order) {
        return TestApiClient.create(order, "/api/orders");
    }

    public static OrderResponse 주문_생성됨(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        OrderRequest order = OrderRequest.of(orderTableId, orderLineItems);
        return 주문_생성_요청(order).as(OrderResponse.class);
    }

}
