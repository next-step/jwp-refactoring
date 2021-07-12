package kitchenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.menu.dto.*;
import kitchenpos.order.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_등록_되어있음;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴그룹_등록_되어있음;
import static kitchenpos.menu.acceptance.ProductAcceptanceTest.상품_등록_되어있음;
import static kitchenpos.order.acceptance.TableAcceptanceTest.테이블_등록되어있음;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@DisplayName("주문 관리 기능")
public class OrderAcceptanceTest extends AcceptanceTest {
    private ProductResponse 후라이드;
    private ProductResponse 양념치킨;
    private MenuGroupResponse 치킨메뉴;
    private MenuResponse 후라이드양념세트;
    private TableResponse 주문테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();
        후라이드 = 상품_등록_되어있음(new ProductRequest("후라이드", BigDecimal.valueOf(15_000)));
        양념치킨 = 상품_등록_되어있음(new ProductRequest("양념치킨", BigDecimal.valueOf(16_000)));
        치킨메뉴 = 메뉴그룹_등록_되어있음(new MenuGroupRequest("치킨메뉴"));
        MenuProductRequest 후라이드한개 = new MenuProductRequest(후라이드.getId(), 1L);
        MenuProductRequest 양념치킨한개 = new MenuProductRequest(양념치킨.getId(), 1L);
        후라이드양념세트 = 메뉴_등록_되어있음(new MenuRequest("후라이드양념세트", BigDecimal.valueOf(30_000), 치킨메뉴.getId(),
                Arrays.asList(후라이드한개, 양념치킨한개)));
        주문테이블 = 테이블_등록되어있음(new TableRequest(3, false));
    }


    @DisplayName("주문을 관리한다.")
    @Test
    public void orderManager() throws Exception {
        OrderLineItemRequest 후라이드양념세트두개 = new OrderLineItemRequest(후라이드양념세트.getId(), 2L);
        OrderRequest orderRequest = new OrderRequest(주문테이블.getId(), Arrays.asList(후라이드양념세트두개));
        ExtractableResponse<Response> postResponse = 주문_등록_요청(orderRequest);
        주문_등록됨(postResponse);

        ExtractableResponse<Response> getResponse = 주문_목록조회_요청();
        주문_목록조회됨(getResponse);

        OrderStatusRequest 식사 = new OrderStatusRequest(MEAL);
        ExtractableResponse<Response> orderStatusResponse = 주문_상태변경_요청(postResponse, 식사);
        주문_상태변경됨(orderStatusResponse);
    }

    private void 주문_상태변경됨(ExtractableResponse<Response> orderStatusResponse) {
        assertHttpStatus(orderStatusResponse, OK);
    }

    private ExtractableResponse<Response> 주문_상태변경_요청(ExtractableResponse<Response> postResponse, OrderStatusRequest orderStatusRequest) {
        return put(postResponse.header("Location") + "/order-status", orderStatusRequest);
    }

    private void 주문_목록조회됨(ExtractableResponse<Response> getResponse) {
        assertHttpStatus(getResponse, OK);
    }

    private ExtractableResponse<Response> 주문_목록조회_요청() {
        return get("/api/orders");
    }

    private void 주문_등록됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, CREATED);
    }

    private ExtractableResponse<Response> 주문_등록_요청(OrderRequest orderRequest) {
        return post("/api/orders", orderRequest);
    }
}
