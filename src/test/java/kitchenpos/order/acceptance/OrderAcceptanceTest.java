package kitchenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.acceptance.RestAssuredApi;
import kitchenpos.menu.dto.*;
import kitchenpos.order.dto.*;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_등록_요청;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록_요청;
import static kitchenpos.menu.acceptance.ProductAcceptanceTest.상품_등록_요청;
import static kitchenpos.order.acceptance.OrderTableAcceptanceTest.주문_테이블_등록_요청;
import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.order.dto.OrderStatusRequest.completion;
import static kitchenpos.order.dto.OrderStatusRequest.meal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@DisplayName("주문 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 테이블;
    private OrderTableResponse 빈테이블;
    private OrderLineItemRequest 주문항목1;
    private OrderLineItemRequest 주문항목2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        ProductResponse 매콤치킨 = 상품_등록_요청(ProductRequest.of("매콤치킨", BigDecimal.valueOf(13000))).as(ProductResponse.class);
        ProductResponse 허니치킨 = 상품_등록_요청(ProductRequest.of("허니치킨", BigDecimal.valueOf(15000))).as(ProductResponse.class);

        MenuProductRequest 매콤치킨구성 = new MenuProductRequest(매콤치킨.getId(), 1L);
        MenuProductRequest 허니치킨구성 = new MenuProductRequest(허니치킨.getId(), 1L);

        MenuGroupResponse 인기메뉴그룹 = 메뉴_그룹_등록_요청(new MenuGroupRequest("인기메뉴")).as(MenuGroupResponse.class);

        MenuRequest menuRequest1 = MenuRequest.of("매콤치킨단품", BigDecimal.valueOf(13000), 인기메뉴그룹.getId(), Collections.singletonList(매콤치킨구성));
        MenuRequest menuRequest2 = MenuRequest.of("허니치킨단품", BigDecimal.valueOf(15000), 인기메뉴그룹.getId(), Collections.singletonList(허니치킨구성));
        MenuResponse 매콤치킨단품 = 메뉴_등록_요청(menuRequest1).as(MenuResponse.class);
        MenuResponse 허니치킨단품 = 메뉴_등록_요청(menuRequest2).as(MenuResponse.class);

        테이블 = 주문_테이블_등록_요청(OrderTableRequest.of(2)).as(OrderTableResponse.class);
        빈테이블 = 주문_테이블_등록_요청(OrderTableRequest.empty()).as(OrderTableResponse.class);
        주문항목1 = new OrderLineItemRequest(매콤치킨단품.getId(), 1L);
        주문항목2 = new OrderLineItemRequest(허니치킨단품.getId(), 1L);
    }

    @Test
    @DisplayName("주문 정상 시나리오")
    void normalScenario() {
        OrderRequest 매콤치킨주문 = new OrderRequest(테이블.getId(), Collections.singletonList(주문항목1));
        OrderRequest 허니치킨주문 = new OrderRequest(테이블.getId(), Collections.singletonList(주문항목2));

        String createdUri1 = 주문_등록됨(주문_등록_요청(매콤치킨주문));
        String createdUri2 = 주문_등록됨(주문_등록_요청(허니치킨주문));

        주문_상태_변경됨(주문_상태_변경_요청(createdUri1, meal()));
        주문_상태_변경됨(주문_상태_변경_요청(createdUri2, completion()));

        ExtractableResponse<Response> response = 주문_목록_조회_요청();
        주문_목록_조회됨(response);
        주문_상태_일치됨(response, Arrays.asList(MEAL.name(), COMPLETION.name()));
    }

    @Test
    @DisplayName("주문 예외 시나리오")
    void exceptionScenario() {
        OrderRequest 주문테이블없음 = new OrderRequest(null, Collections.singletonList(주문항목1));
        OrderRequest 빈주문테이블 = new OrderRequest(빈테이블.getId(), Collections.singletonList(주문항목2));
        OrderRequest 매콤치킨주문 = new OrderRequest(테이블.getId(), Collections.singletonList(주문항목1));

        주문_등록_실패됨(주문_등록_요청(주문테이블없음));
        주문_등록_실패됨(주문_등록_요청(빈주문테이블));

        String createdUri = 주문_등록됨(주문_등록_요청(매콤치킨주문));
        주문_상태_변경됨(주문_상태_변경_요청(createdUri, completion()));
        주문_상태_변경_실패됨(주문_상태_변경_요청(createdUri, meal()));
    }

    private ExtractableResponse<Response> 주문_등록_요청(OrderRequest request) {
        return RestAssuredApi.post("/api/orders", request);
    }

    private ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssuredApi.get("/api/orders");
    }

    private ExtractableResponse<Response> 주문_상태_변경_요청(String uri, OrderStatusRequest request) {
        return RestAssuredApi.put(uri + "/order-status", request);
    }

    private String 주문_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.header("Location");
    }

    private void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 주문_상태_일치됨(ExtractableResponse<Response> response, List<String> excepted) {
        assertThat(response.jsonPath().getList("orderStatus"))
                .isEqualTo(excepted);
    }

    public static void 주문_등록_실패됨(ExtractableResponse<Response> response) {
        AssertionsForClassTypes.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 주문_상태_변경됨(ExtractableResponse<Response> response) {
        AssertionsForClassTypes.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_상태_변경_실패됨(ExtractableResponse<Response> response) {
        AssertionsForClassTypes.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
