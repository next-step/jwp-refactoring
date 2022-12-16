package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_생성을_요청;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴그룹_생성을_요청;
import static kitchenpos.table.acceptance.TableAcceptanceTest.테이블_생성을_요청;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 치킨;
    private MenuResponse 양념치킨;
    private OrderTableResponse 빈_주문테이블;
    private OrderTableResponse 비어있지_않은_주문테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();
        치킨 = 메뉴그룹_생성을_요청("치킨").as(MenuGroupResponse.class);
        양념치킨 = 메뉴_생성을_요청("양식 세트", new BigDecimal(0), 치킨.getId(),
                Collections.emptyList()).as(MenuResponse.class);
        빈_주문테이블 = 테이블_생성을_요청(0, true).as(OrderTableResponse.class);
        비어있지_않은_주문테이블 = 테이블_생성을_요청(2, false).as(OrderTableResponse.class);
    }

    @DisplayName("등록되어 있지 않은 주문 테이블에서 주문을 하면 실패")
    @Test
    void createOrderWithNullOrderTable() {
        ExtractableResponse<Response> response = 주문_생성을_요청(-1L, Arrays.asList(new OrderLineItemRequest(양념치킨.getId(), 1L)));

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("등록되지 않은 메뉴를 포함해 주문하면 실패")
    @Test
    void createOrderWithNullMenu() {
        ExtractableResponse<Response> response = 주문_생성을_요청(-1L, Arrays.asList(new OrderLineItemRequest(-1L, 1L)));

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("빈 주문 테이블에서 주문한다")
    @Test
    void createOrderWithEmptyOrderTable() {
        ExtractableResponse<Response> response = 주문_생성을_요청(빈_주문테이블.getId(), Arrays.asList(new OrderLineItemRequest(양념치킨.getId(), 1L)));

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    public static ExtractableResponse<Response> 주문_생성을_요청(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        OrderRequest request = new OrderRequest(orderTableId, OrderStatus.COOKING, orderLineItems);
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }
}
