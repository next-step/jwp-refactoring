package kitchenpos.order.acceptance;


import static kitchenpos.order.acceptance.step.OrderAcceptanceStep.*;
import static kitchenpos.ordertable.acceptance.step.TableAcceptanceStep.주문테이블_생성됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("주문 관리 기능")
class OrderAcceptanceTest extends AcceptanceTest {

    @MockBean
    private OrderValidator orderValidator;

    @Test
    @DisplayName("주문관리 한다.")
    void 주문관리_기능() {
        // given
        OrderRequest 요청_주문_파라미터 = 치킨_주문_파라미터();

        // when
        ExtractableResponse<Response> 주문_등록_결과 = 주문_등록_요청(요청_주문_파라미터);
        // then
        Long 등록된_주문_번호 = 주문_등록_검증(주문_등록_결과);

        // when
        ExtractableResponse<Response> 주문_목록조회_결과 = 주문_목록조회_요청();
        // then
        주문_목록조회_검증(주문_목록조회_결과, 등록된_주문_번호);

        // when
        ExtractableResponse<Response> 주문_상태변경_결과 = 주문_상태변경_요청(등록된_주문_번호, 요리중_요청_파라미터());
        // then
        주문_상태변경_검증(주문_상태변경_결과, OrderStatus.MEAL.name());
    }

    private OrderRequest 치킨_주문_파라미터() {
        Long 치킨메뉴_번호 = 1L;
        Long 주문테이블_번호 = 주문테이블_생성됨(1, false);
        OrderLineItemRequest 주문항목 = new OrderLineItemRequest(치킨메뉴_번호, 1);

        return new OrderRequest(주문테이블_번호, Collections.singletonList(주문항목));
    }

    private OrderStatusRequest 요리중_요청_파라미터() {
        return new OrderStatusRequest(OrderStatus.MEAL);
    }
}
