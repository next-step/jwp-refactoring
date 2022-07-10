package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_등록_요청;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_등록실패;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_변경실패;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_상태_변경_요청;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_조회_요청;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_조회성공;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_등록_요청;

@DisplayName("주문 관련")
public class OrderAcceptanceTest extends AcceptanceTest {
    private OrderTableResponse 주문테이블1;

    private MenuResponse 후라이드메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();
        //테이블
        주문테이블1 = 주문테이블_등록_요청(false, 5).as(OrderTableResponse.class);

    }

    @Test
    void 주문항목이_비어있으면_주문을_등록할_수_없다() {
        ExtractableResponse<Response> 주문_등록_결과 = 주문_등록_요청(주문테이블1.getId(), Arrays.asList());

        주문_등록실패(주문_등록_결과);
    }

    @Test
    void 메뉴가_등록되지_않았으면_주문을_등록할_수_없다() {
        long 등록되지않은_메뉴아이디 = 999L;
        OrderLineItemRequest 주문항목 = new OrderLineItemRequest(등록되지않은_메뉴아이디, 1);

        ExtractableResponse<Response> 주문_등록_결과 = 주문_등록_요청(주문테이블1.getId(), Arrays.asList(주문항목));

        주문_등록실패(주문_등록_결과);
    }





    @Test
    void 주문을_조회할_수_있다() {
        ExtractableResponse<Response> 주문_조회_결과 = 주문_조회_요청();

        주문_조회성공(주문_조회_결과);
    }



    @Test
    void 주문이_존재하지않으면_주문을_변경할_수_없다() {
        Order 존재하지않는_주문 = new Order(999L);
        OrderStatus 주문상태_요리중 = OrderStatus.COOKING;

        ExtractableResponse<Response> 주문_변경_결과 = 주문_상태_변경_요청(존재하지않는_주문.getId(), 주문상태_요리중);

        주문_변경실패(주문_변경_결과);
    }

}
