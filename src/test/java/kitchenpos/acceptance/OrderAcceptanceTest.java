package kitchenpos.acceptance;


import static kitchenpos.acceptance.step.OrderAcceptanceStep.*;
import static kitchenpos.acceptance.step.MenuAcceptanceStep.메뉴_등록됨;
import static kitchenpos.acceptance.step.MenuGroupAcceptanceStep.메뉴그룹_등록됨;
import static kitchenpos.acceptance.step.ProductAcceptanceStep.상품_등록됨;
import static kitchenpos.acceptance.step.ProductAcceptanceStep.양념치킨;
import static kitchenpos.acceptance.step.TableAcceptanceStep.주문테이블_생성됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.menu.MenuGroupRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 관리 기능")
class OrderAcceptanceTest extends AcceptanceTest {

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
        ExtractableResponse<Response> 주문_상태변경_결과 = 주문_상태변경_요청(등록된_주문_번호,
            요리중_요청_파라미터(OrderStatus.MEAL));
        // then
        주문_상태변경_검증(주문_상태변경_결과, OrderStatus.MEAL.name());
    }

    private OrderRequest 치킨_주문_파라미터() {
        Long 치킨상품_번호 = 상품_등록됨(양념치킨());
        Long 치킨메뉴_번호 = 치킨메뉴_등록됨(치킨상품_번호);
        Long 주문테이블_번호 = 주문테이블_생성됨(1, false);
        OrderLineItemRequest 주문항목 = new OrderLineItemRequest(치킨메뉴_번호, 1);

        return new OrderRequest(주문테이블_번호, Collections.singletonList(주문항목));
    }

    private Long 치킨메뉴_등록됨(Long 상품_번호) {
        MenuGroupResponse 치킨류 = 메뉴그룹_등록됨(new MenuGroupRequest("치킨류"));
        MenuRequest 메뉴등록_요청_파라미터 = new MenuRequest("치킨메뉴", 16000, 치킨류.getId(),
            Collections.singletonList(new MenuProductRequest(상품_번호, 1L)));
        return 메뉴_등록됨(메뉴등록_요청_파라미터);
    }

    private OrderStatusRequest 요리중_요청_파라미터(OrderStatus orderStatus) {
        return new OrderStatusRequest(orderStatus);
    }
}
