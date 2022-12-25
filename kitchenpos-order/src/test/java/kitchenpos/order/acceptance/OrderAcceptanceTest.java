package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderTableResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_생성_요청;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static kitchenpos.table.acceptance.TableAcceptanceTest.테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 테이블;
    private OrderTableResponse 빈_테이블;
    private MenuResponse 메뉴_파닭치킨;
    private OrderResponse 주문;

    @DisplayName("주문 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> order() {
        return Stream.of(
            dynamicTest("데이터를 세팅한다.", () -> {
                테이블 = 테이블_생성_요청(false, 5).as(OrderTableResponse.class);
                빈_테이블 = 테이블_생성_요청(true, 0).as(OrderTableResponse.class);
                메뉴_파닭치킨 = 메뉴_등록_요청().as(MenuResponse.class);
            }),
            dynamicTest("주문을 등록한다.", () -> {
                ExtractableResponse<Response> response = 주문_생성_요청(테이블, 메뉴_파닭치킨);

                주문_생성됨(response);
                주문_생성시_조리상태_확인(response);
                주문 = response.as(OrderResponse.class);
            }),
            dynamicTest("주문 항목 없이 주문을 등록한다.", () -> {
                ExtractableResponse<Response> response = 주문_생성_요청(테이블);

                주문_생성_실패됨(response);
            }),
            dynamicTest("존재하지 않는 메뉴가 포함된 주문 항목으로 주문을 등록한다.", () -> {
                ExtractableResponse<Response> response = 주문_생성_요청(테이블, null);

                주문_생성_실패됨(response);
            }),
            dynamicTest("빈 테이블에 주문을 등록한다.", () -> {
                ExtractableResponse<Response> response = 주문_생성_요청(빈_테이블, 메뉴_파닭치킨);

                주문_생성_실패됨(response);
            }),
            dynamicTest("주문 목록을 조회한다.", () -> {
                ExtractableResponse<Response> response = 주문_목록_조회_요청();

                주문_목록_응답됨(response);
                주문_목록_주문에_주문_항목이_포함됨(response, 메뉴_파닭치킨);
            }),
            dynamicTest("주문의 상태를 변경한다. (조리 -> 식사)", () -> {
                ExtractableResponse<Response> response = 주문_상태_변경_요청(주문.getId(), OrderStatus.MEAL);

                주문_상태_변경됨(response);
            }),
            dynamicTest("주문의 상태를 변경한다. (식사 -> 계산 완료)", () -> {
                ExtractableResponse<Response> response = 주문_상태_변경_요청(주문.getId(), OrderStatus.COMPLETION);

                주문_상태_변경됨(response);
            }),
            dynamicTest("주문의 상태를 변경한다. (계산 완료 -> 계산 완료)", () -> {
                ExtractableResponse<Response> response = 주문_상태_변경_요청(주문.getId(), OrderStatus.COMPLETION);

                주문_상태_변경_실패됨(response);
            }),
            dynamicTest("존재하지 않는 주문의 상태를 변경한다.", () -> {
                ExtractableResponse<Response> response = 주문_상태_변경_요청(Long.MAX_VALUE, OrderStatus.MEAL);

                주문_상태_변경_실패됨(response);
            })
        );
    }

    public static ExtractableResponse<Response> 주문_생성_요청(OrderTableResponse orderTableResponse, MenuResponse... menuResponses) {
        Map<String, Object> request = new HashMap<>();
        request.put("orderTableId", orderTableResponse.getId());
        request.put("orderLineItems", toOrderLoneItemRequests(menuResponses));
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/api/orders")
            .then().log().all()
            .extract();
    }

    private static List<OrderLineItemRequest> toOrderLoneItemRequests(MenuResponse[] menuResponses) {
        if (ObjectUtils.isEmpty(menuResponses)) {
            return Collections.emptyList();
        }
        return Arrays.stream(menuResponses)
            .map(m -> {
                OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(m.getId(), 1L);
                return orderLineItemRequest;
            })
            .collect(Collectors.toList());
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/orders")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Long orderId, OrderStatus status) {
        Map<String, Object> request = new HashMap<>();
        request.put("orderStatus", status.name());
        return RestAssured
            .given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/orders/{orderId}/order-status", orderId)
            .then().log().all()
            .extract();
    }

    public static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_생성시_조리상태_확인(ExtractableResponse<Response> response) {
        OrderResponse order = response.as(OrderResponse.class);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    public static void 주문_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 주문_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_상태_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_상태_변경_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 주문_목록_주문에_주문_항목이_포함됨(ExtractableResponse<Response> response, MenuResponse... menuResponses) {
        List<Long> menuIds = response.jsonPath().getList(".", OrderResponse.class)
            .stream()
            .flatMap(o -> o.getOrderLineItems().stream())
            .map(OrderLineItemResponse::getMenuId)
            .distinct()
            .collect(Collectors.toList());

        List<Long> expectedIds = Arrays.stream(menuResponses)
            .map(MenuResponse::getId)
            .collect(Collectors.toList());
        assertThat(menuIds).containsExactlyElementsOf(expectedIds);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청() {
        MenuGroup 신메뉴 = 메뉴_그룹_생성_요청("신메뉴").as(MenuGroup.class);
        ProductResponse 파닭치킨 = 상품_생성_요청("파닭치킨", BigDecimal.valueOf(15_000L)).as(ProductResponse.class);

        return 메뉴_생성_요청("파닭치킨",
            BigDecimal.valueOf(15_000L),
            신메뉴.getId(), 파닭치킨);
    }
}
