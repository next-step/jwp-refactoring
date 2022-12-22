package kitchenpos.order.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.*;
import static kitchenpos.table.acceptance.TableAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.exception.CustomErrorResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {
    @DisplayName("주문 기능 통합 테스트")
    @TestFactory
    Stream<DynamicNode> order() {
        Long 짜장_탕수_세트_ID = 짜장_탕수_세트_생성됨();
        Long 채워진_테이블_ID = 테이블_생성됨(2, false);
        Long 빈_테이블_ID = 테이블_생성됨(2, true);
		
        return Stream.of(dynamicTest("주문을 생성한다.", () -> {
                // when
                ExtractableResponse<Response> response = 주문_생성_요청(채워진_테이블_ID, 짜장_탕수_세트_ID);
                // then
                주문_정상_생성됨(response);
            }), dynamicTest("등록되지 않은 메뉴로는 주문을 생성할 수 없다.", () -> {
                // given
                Long 미등록_메뉴_ID = Long.MAX_VALUE;
                // when
                ExtractableResponse<Response> response = 주문_생성_요청(채워진_테이블_ID, 미등록_메뉴_ID);
                // then
                요청_실패됨_엔티티_찾을_수_없음(response);
            }),
            dynamicTest("빈테이블은 주문을 생성 할 수 없다.", () -> {
                // when
                ExtractableResponse<Response> response = 주문_생성_요청(빈_테이블_ID, 짜장_탕수_세트_ID);
                // then
                요청_실패됨_잘못된_요청(response);
            }),
            dynamicTest("주문테이블이 생성되지 않았으면 주문을 생성 할 수 없다.", () -> {
                // given
                Long 생성되지_않은_주문테이블_ID = Long.MAX_VALUE;
                // when
                ExtractableResponse<Response> response = 주문_생성_요청(생성되지_않은_주문테이블_ID, 짜장_탕수_세트_ID);
                // then
                요청_실패됨_잘못된_요청(response);
            }),
            dynamicTest("주문 목록을 조회한다.", () -> {
                // when
                ExtractableResponse<Response> response = 주문_목록_조회_요청();
                // then
                주문_목록_정상_조회됨(response, 짜장_탕수_세트_ID);
            }), dynamicTest("주문 상태를 변경한다.", () -> {
                // given
                Long 생성된_주문_ID = 주문_생성됨(채워진_테이블_ID, 짜장_탕수_세트_ID);
                // when
                ExtractableResponse<Response> response = 주문상태_변경_요청(생성된_주문_ID, OrderStatus.MEAL);
                // then
                주문_정상_변경됨(response, OrderStatus.MEAL);
            }), dynamicTest("주문상태가 현재 종료되었으면 상태를 변경할 수 없다.", () -> {
                // given
                Long 생성된_주문_ID = 주문_생성됨(채워진_테이블_ID, 짜장_탕수_세트_ID);
                주문상태_변경_요청(생성된_주문_ID, OrderStatus.COMPLETION);
                // when
                ExtractableResponse<Response> response = 주문상태_변경_요청(생성된_주문_ID, OrderStatus.MEAL);
                // then
                요청_실패됨_잘못된_요청(response);
            }));
    }

    public static Long 주문_생성됨(Long orderTableId, Long... menuIds) {
        return 주문_생성_요청(orderTableId, menuIds).as(OrderResponse.class).getId();
    }

    public static ExtractableResponse<Response> 주문_생성_요청(Long orderTableId, Long... menuIds) {
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(menuIds)
            .stream()
            .map(it -> OrderLineItemRequest.of(it, 1))
            .collect(Collectors.toList());
        OrderRequest orderRequest = OrderRequest.of(orderTableId, orderLineItemRequests);
        return RestAssured.given()
            .log()
            .all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderRequest)
            .when()
            .post("/api/orders")
            .then()
            .log()
            .all()
            .extract();

    }

    private ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured.given().log().all().when().get("/api/orders").then().log().all().extract();
    }

    private ExtractableResponse<Response> 주문상태_변경_요청(Long id, OrderStatus orderStatus) {
        return RestAssured.given()
            .log()
            .all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderStatus.name())
            .when()
            .put("/api/orders/{orderId}/order-status", id)
            .then()
            .log()
            .all()
            .extract();
    }

    private void 주문_정상_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 요청_실패됨_잘못된_요청(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 요청_실패됨_엔티티_찾을_수_없음(ExtractableResponse<Response> response) {
        CustomErrorResponse customErrorResponse = response.as(CustomErrorResponse.class);
        assertAll(
            () -> assertThat(customErrorResponse.getStatusCode()).isEqualTo(404),
            () -> assertThat(customErrorResponse.getStatusName()).isEqualTo("NOT_FOUND")
        );
    }

    private void 주문_목록_정상_조회됨(ExtractableResponse<Response> response, Long... 메뉴아이디목록) {
        List<Long> 조회_결과_목록 = response.jsonPath()
            .getList(".", OrderResponse.class)
            .stream()
            .map(it -> it.getOrderLineItemResponses())
            .flatMap(Collection::stream)
            .map(it -> it.getMenuId())
            .collect(Collectors.toList());

        assertAll(() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(조회_결과_목록).containsAll(Arrays.asList(메뉴아이디목록)));
    }

    private void 주문_정상_변경됨(ExtractableResponse<Response> response, OrderStatus 주문상태) {

        assertAll(() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.as(OrderResponse.class).getOrderStatus()).isEqualTo(주문상태));
    }

}
