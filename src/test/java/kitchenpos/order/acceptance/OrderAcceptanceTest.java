package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_등록되어_있음;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록_되어_있음;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록_되어_있음;
import static kitchenpos.table.acceptance.TableAcceptanceTest.주문_테이블_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/orders";

    private OrderTableResponse 주문_테이블;
    private MenuGroupResponse 메뉴_그룹;
    private MenuProductRequest 메뉴_치킨;
    private MenuProductRequest 메뉴_피자;
    private ProductResponse 피자;
    private ProductResponse 치킨;
    private MenuResponse 메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블 = 주문_테이블_등록되어_있음(4, false);
        메뉴_그룹 = 메뉴_그룹_등록_되어_있음("추천 메뉴");
        피자 = 상품_등록_되어_있음("피자", BigDecimal.valueOf(10_000));
        치킨 = 상품_등록_되어_있음("치킨", BigDecimal.valueOf(10_000));

        메뉴_피자 = MenuProductRequest.of(피자.getId(), 1L);
        메뉴_치킨 = MenuProductRequest.of(치킨.getId(), 1L);

        메뉴 = 메뉴_등록되어_있음("피자_치킨_세트", 18_000, 메뉴_그룹.getId(), Arrays.asList(메뉴_치킨, 메뉴_피자));
    }

    @Test
    @DisplayName("주문을 관리한다 (주문 생성, 조회, 상태변경)")
    void Order() {
        ExtractableResponse<Response> 주문_생성_요청_결과 = 주문_생성_요청(
                주문_테이블.getId(),
                Arrays.asList(OrderLineItemRequest.of(메뉴.getId(), 1L))
        );

        주문_생성됨(주문_생성_요청_결과);

        Long 주문_ID = 주문_ID_조회(주문_생성_요청_결과);

        ExtractableResponse<Response> 주문_목록_조회_요청_결과 = 주문_목록_조회_요청();

        주문_목록_조회됨(주문_목록_조회_요청_결과);

        OrderStatus 주문_상태 = OrderStatus.MEAL;

        ExtractableResponse<Response> 주문_상태_수정_요청_결과 = 주문_상태_수정_요청(주문_ID, 주문_상태);

        주문_상태_수정됨(주문_상태_수정_요청_결과, 주문_상태);
    }

    private ExtractableResponse<Response> 주문_생성_요청(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        OrderRequest orderRequest = OrderRequest.of(orderTableId, orderLineItems);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when().post(API_URL)
                .then().log().all()
                .extract();
    }

    private void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private Long 주문_ID_조회(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(API_URL)
                .then().log().all()
                .extract();
    }

    private void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 주문_상태_수정_요청(Long orderId, OrderStatus orderStatus) {
        Map<String, String> params = new HashMap<>();
        params.put("orderStatus", orderStatus.name());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(String.format("%s/{orderId}/order-status", API_URL), orderId)
                .then().log().all()
                .extract();
    }

    private void 주문_상태_수정됨(ExtractableResponse<Response> response, OrderStatus orderStatus) {
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("orderStatus")).isEqualTo(orderStatus.name())
        );
    }
}
