package kitchenpos.order;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.MenuAcceptanceTestSupport;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.MenuGroupAcceptanceTestSupport;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.OrderTableAcceptanceTestSupport;
import kitchenpos.product.ProductAcceptanceTestSupport;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@DisplayName("주문 관련 기능")
class OrderAcceptanceTest extends OrderAcceptanceTestSupport {
    private OrderTableResponse orderTable;
    private MenuResponse menu;

    @BeforeEach
    public void beforeEach() {
        orderTable = OrderTableAcceptanceTestSupport.주문_테이블_등록_되어있음(3, false).as(OrderTableResponse.class);
        MenuGroupResponse 중화메뉴 = MenuGroupAcceptanceTestSupport.메뉴_그룹_등록_되어있음("중화메뉴").as(MenuGroupResponse.class);
        ProductResponse 짬뽕 = ProductAcceptanceTestSupport.상품_등록되어_있음("짬뽕", 7_000).as(ProductResponse.class);
        ProductResponse 짜장면 = ProductAcceptanceTestSupport.상품_등록되어_있음("짜장면", 5_000).as(ProductResponse.class);
        menu = MenuAcceptanceTestSupport.메뉴_등록_되어있음(중화메뉴, Arrays.asList(짬뽕, 짜장면)).as(MenuResponse.class);
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
        OrderRequest updateRequest = new OrderRequest(OrderStatus.COMPLETION);
        ExtractableResponse<Response> updateResponse = 주문_상태_변경_요청(createResponse, updateRequest);

        // Then
        주문_응답(updateResponse);

        // When
        OrderRequest invalidUpdateRequest = new OrderRequest(OrderStatus.COMPLETION);
        ExtractableResponse<Response> wrongResponse = 주문_상태_변경_요청(createResponse, invalidUpdateRequest);

        // Then
        주문_응답_실패(wrongResponse);
    }

    @DisplayName("주문 생성시, 주문 항목이 없으면 예외를 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void exceptionToCreateOrderWithoutItems(List<OrderLineItemRequest> orderLineItemRequests) {
        // Given
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLineItemRequests);

        // When
        ExtractableResponse<Response> createResponse = 주문_생성_요청(orderRequest);

        // Then
        잘못된_주문_요청(createResponse);
    }
}
