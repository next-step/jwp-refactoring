package kitchenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.acceptance.step.MenuAcceptStep;
import kitchenpos.menu.acceptance.step.MenuGroupAcceptStep;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.acceptance.step.ProductAcceptStep;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.acceptance.step.TableAcceptStep;
import kitchenpos.table.dto.TableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.order.acceptance.step.OrderAcceptStep.주문_등록_요청;
import static kitchenpos.order.acceptance.step.OrderAcceptStep.주문_등록_확인;
import static kitchenpos.order.acceptance.step.OrderAcceptStep.주문_목록_조회_요청;
import static kitchenpos.order.acceptance.step.OrderAcceptStep.주문_목록_조회_확인;
import static kitchenpos.order.acceptance.step.OrderAcceptStep.주문_상태_변경_요청;
import static kitchenpos.order.acceptance.step.OrderAcceptStep.주문_상태_변경_확인;

@DisplayName("주문 인수테스트")
class OrderAcceptTest extends AcceptanceTest {

    private MenuResponse 더블강정;
    private TableResponse 테이블;

    @BeforeEach
    void setup() {
        ProductResponse 강정치킨 = ProductAcceptStep.상품이_등록되어_있음("강정치킨", BigDecimal.valueOf(17_000));
        MenuGroupResponse 추천메뉴 = MenuGroupAcceptStep.메뉴_그룹이_등록되어_있음("추천메뉴");
        MenuProductRequest 메뉴_상품_요청 = MenuProductRequest.of(강정치킨.getId(), 2L);

        더블강정 = MenuAcceptStep.메뉴가_등록되어_있음("더블강정", BigDecimal.valueOf(32_000L), 추천메뉴, 메뉴_상품_요청);
        테이블 = TableAcceptStep.테이블이_등록되어_있음(2, false);
    }

    @DisplayName("주문을 관리한다")
    @Test
    void 주문을_관리한다() {
        // given
        OrderLineItemRequest 주문_항목 = OrderLineItemRequest.of(더블강정.getId(), 1L);
        OrderRequest 등록_요청_데이터 = OrderRequest.of(테이블.getId(), Collections.singletonList(주문_항목));

        // when
        ExtractableResponse<Response> 주문_등록_응답 = 주문_등록_요청(등록_요청_데이터);

        // then
        OrderResponse 등록된_주문 = 주문_등록_확인(주문_등록_응답, 등록_요청_데이터);

        // when
        ExtractableResponse<Response> 주문_목록_조회_응답 = 주문_목록_조회_요청();

        // then
        주문_목록_조회_확인(주문_목록_조회_응답, 등록된_주문);

        // given
        ChangeOrderStatusRequest 상태_변경_요청_데이터 = ChangeOrderStatusRequest.of("MEAL");


        // when
        ExtractableResponse<Response> 주문_상태_변경_응답 = 주문_상태_변경_요청(주문_등록_응답, 상태_변경_요청_데이터);

        // then
        주문_상태_변경_확인(주문_상태_변경_응답, 상태_변경_요청_데이터);
    }
}
