package kitchenpos.order.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_등록_되어있음;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록_되어있음;
import static kitchenpos.table.acceptance.TableAcceptanceTest.테이블_등록_되어있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderTableResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DisplayName("주문 관련 기능 인수테스트")
public class OrderAcceptanceTest extends AcceptanceTest {
    private MenuGroupResponse 추천메뉴;
    private ProductResponse 허니콤보;
    private MenuResponse 허니레드콤보;
    private OrderTableResponse 손님_4명_테이블;
    private OrderResponse 주문;

    @TestFactory
    @DisplayName("주문 관련 기능 정상 시나리오")
    Stream<DynamicTest> successTest() {
        return Stream.of(
                dynamicTest("주문 등록요청하면 주문이 등록된다.", () -> {
                    추천메뉴 = 메뉴_그룹_등록되어_있음("추천메뉴");
                    허니콤보 = 상품_등록_되어있음("허니콤보", 20_000L);
                    허니레드콤보 = 메뉴_등록_되어있음(추천메뉴, "허니레드콤보", 19_000L, 허니콤보, 1L);
                    손님_4명_테이블 = 테이블_등록_되어있음(4, false);

                    ResponseEntity<OrderResponse> 주문_등록_요청_결과 = 주문_등록_요청(손님_4명_테이블, 1L, 허니레드콤보);

                    주문_등록됨(주문_등록_요청_결과);
                }),
                dynamicTest("주문 목록 조회요청하면 주문 목록이 조회된다.", () -> {
                    ResponseEntity<List<OrderResponse>> 주문_목록_조회_요청_결과 = 주문_목록_조회_요청();

                    주문_목록_조회됨(주문_목록_조회_요청_결과, 허니레드콤보);
                }),
                dynamicTest("주문 상태 변경요청하면 주문 상태가 변경된다.", () -> {
                    주문 = 주문_등록_되어있음(손님_4명_테이블, 1L, 허니레드콤보);

                    ResponseEntity<OrderResponse> 주문_상태_변경_요청_결과 = 주문_상태_변경_요청(주문, OrderStatus.COMPLETION);

                    주문_상태_변경됨(주문_상태_변경_요청_결과);
                })
        );
    }

    @TestFactory
    @DisplayName("주문 관련 기능 예외 시나리오")
    Stream<DynamicTest> failTest() {
        return Stream.of(
                dynamicTest("계산완료인 주문을 변경요청하면 변경 실패된다.", () -> {
                    추천메뉴 = 메뉴_그룹_등록되어_있음("추천메뉴");
                    허니콤보 = 상품_등록_되어있음("허니콤보", 20_000L);
                    허니레드콤보 = 메뉴_등록_되어있음(추천메뉴, "허니레드콤보", 19_000L, 허니콤보, 1L);
                    손님_4명_테이블 = 테이블_등록_되어있음(4, false);
                    주문 = 주문_등록_되어있음(손님_4명_테이블, 1L, 허니레드콤보);
                    주문_상태_변경_요청(주문, OrderStatus.COMPLETION);

                    ResponseEntity<OrderResponse> 계산_완료_주문_상태_변경_요청_결과 = 주문_상태_변경_요청(주문, OrderStatus.MEAL);

                    주문_상태_변경_실패됨(계산_완료_주문_상태_변경_요청_결과);
                })
        );
    }

    public static ResponseEntity<OrderResponse> 주문_상태_변경_요청(OrderResponse order, OrderStatus orderStatus) {
        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest(orderStatus);
        HttpEntity<OrderUpdateRequest> httpEntity = new HttpEntity<>(orderUpdateRequest);

        Map<String, Object> params = new HashMap<>();
        params.put("orderId", order.getId());

        return testRestTemplate.exchange("/api/orders/{orderId}/order-status",
                HttpMethod.PUT,
                httpEntity,
                OrderResponse.class,
                params);
    }

    public static OrderResponse 주문_등록_되어있음(OrderTableResponse orderTable, long quantity, MenuResponse menu) {
        return 주문_등록_요청(orderTable, quantity, menu).getBody();
    }

    public static ResponseEntity<OrderResponse> 주문_등록_요청(OrderTableResponse orderTable,long quantity, MenuResponse menu) {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), menu.getName(), menu.getPrice(), quantity);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Lists.list(orderLineItemRequest));
        return testRestTemplate.postForEntity("/api/orders", orderRequest, OrderResponse.class);
    }

    private void 주문_상태_변경_실패됨(ResponseEntity<OrderResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void 주문_상태_변경됨(ResponseEntity<OrderResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void 주문_목록_조회됨(ResponseEntity<List<OrderResponse>> response, MenuResponse... menus) {
        List<Long> actualMenuIds = 주문_항목_메뉴_아이디_추출(response);
        List<Long> expectedMenuIds = Arrays.stream(menus)
                .map(MenuResponse::getId)
                .collect(Collectors.toList());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualMenuIds).containsExactlyElementsOf(expectedMenuIds);
    }

    private List<Long> 주문_항목_메뉴_아이디_추출(ResponseEntity<List<OrderResponse>> response) {
        List<OrderLineItemResponse> orderLineItems = 주문_항목_조회(response);
        return orderLineItems.stream()
                .map(OrderLineItemResponse::getMenuId)
                .collect(Collectors.toList());
    }

    private List<OrderLineItemResponse> 주문_항목_조회(ResponseEntity<List<OrderResponse>> response) {
        return response.getBody().stream()
                .map(OrderResponse::getOrderLineItems)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private ResponseEntity<List<OrderResponse>> 주문_목록_조회_요청() {
        return testRestTemplate.exchange("/api/orders", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderResponse>>() {});
    }

    private void 주문_등록됨(ResponseEntity<OrderResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().get("Location")).isNotNull();
    }
}
