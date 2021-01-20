package kitchenpos.order;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.menu.MenuAcceptanceTestSupport;
import kitchenpos.menugroup.MenuGroupAcceptanceTestSupport;
import kitchenpos.ordertable.OrderTableAcceptanceTestSupport;
import kitchenpos.product.ProductAcceptanceTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

@DisplayName("주문 관련 기능")
class OrdersAcceptanceTest extends OrderAcceptanceTestSupport {
    private OrderTableResponse orderTable;
    private Menu menu;

    @BeforeEach
    public void beforeEach() {
        orderTable = OrderTableAcceptanceTestSupport.주문_테이블_등록_되어있음(3, false).as(OrderTableResponse.class);
        MenuGroup 중화메뉴 = MenuGroupAcceptanceTestSupport.메뉴_그룹_등록_되어있음("중화메뉴").as(MenuGroup.class);
        Product 짬뽕 = ProductAcceptanceTestSupport.상품_등록되어_있음("짬뽕", 7_000).as(Product.class);
        Product 짜장면 = ProductAcceptanceTestSupport.상품_등록되어_있음("짜장면", 5_000).as(Product.class);
        menu = MenuAcceptanceTestSupport.메뉴_등록_되어있음(중화메뉴, Arrays.asList(짬뽕, 짜장면)).as(Menu.class);
    }

    @DisplayName("주문의 생성 / 목록 조회 / 주문 상태 변경")
    @Test
    void manageOrder() {
        // Given
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Collections.singletonList(new OrderLineItemRequest(menu.getId(), 1L)));

        // When
        ExtractableResponse<Response> createResponse = 주문_생성_요청(orderRequest);

        // Then
        주문_생성_완료(createResponse);

        // When
        ExtractableResponse<Response> findResponse = 주문_목록_조회_요청();

        // Then
        주문_응답(findResponse);

        // When
        OrderRequest updateRequest = new OrderRequest("COMPLETION");
        ExtractableResponse<Response> updateResponse = 주문_상태_변경_요청(createResponse, updateRequest);

        // Then
        주문_응답(updateResponse);

        // When
        OrderRequest invalidUpdateRequest = new OrderRequest("COMPLETION");
        ExtractableResponse<Response> wrongResponse = 주문_상태_변경_요청(createResponse, invalidUpdateRequest);

        // Then
        주문_응답_실패(wrongResponse);
    }
}
