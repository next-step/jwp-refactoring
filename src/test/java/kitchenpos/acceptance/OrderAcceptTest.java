package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.acceptance.step.MenuAcceptStep;
import kitchenpos.acceptance.step.TableAcceptStep;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.menu.acceptance.step.MenuGroupAcceptStep;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.product.acceptance.step.ProductAcceptStep;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.acceptance.step.OrderAcceptStep.주문_등록_요청;
import static kitchenpos.acceptance.step.OrderAcceptStep.주문_등록_확인;
import static kitchenpos.acceptance.step.OrderAcceptStep.주문_목록_조회_요청;
import static kitchenpos.acceptance.step.OrderAcceptStep.주문_목록_조회_확인;
import static kitchenpos.acceptance.step.OrderAcceptStep.주문_상태_변경_요청;
import static kitchenpos.acceptance.step.OrderAcceptStep.주문_상태_변경_확인;

@DisplayName("주문 인수테스트")
class OrderAcceptTest extends AcceptanceTest {

    private Menu 더블강정;
    private OrderTable 테이블;

    @BeforeEach
    void setup() {
        ProductResponse 강정치킨 = ProductAcceptStep.상품이_등록되어_있음("강정치킨", BigDecimal.valueOf(17_000));
        MenuGroupResponse 추천메뉴 = MenuGroupAcceptStep.메뉴_그룹이_등록되어_있음("추천메뉴");
        MenuProduct 메뉴_상품 = new MenuProduct();
        메뉴_상품.setProductId(강정치킨.getId());
        메뉴_상품.setQuantity(2);

        더블강정 = MenuAcceptStep.메뉴가_등록되어_있음("더블강정", 32_000, 추천메뉴, 메뉴_상품);
        테이블 = TableAcceptStep.테이블이_등록되어_있음(2, false);
    }

    @DisplayName("주문을 관리한다")
    @Test
    void 주문을_관리한다() {
        // given
        OrderLineItem 주문_항목 = new OrderLineItem();
        주문_항목.setMenuId(더블강정.getId());
        주문_항목.setQuantity(1);

        Order 등록_요청_데이터 = new Order();
        등록_요청_데이터.setOrderTableId(테이블.getId());
        등록_요청_데이터.setOrderLineItems(Collections.singletonList(주문_항목));

        // when
        ExtractableResponse<Response> 주문_등록_응답 = 주문_등록_요청(등록_요청_데이터);

        // then
        Order 등록된_주문 = 주문_등록_확인(주문_등록_응답, 등록_요청_데이터);

        // when
        ExtractableResponse<Response> 주문_목록_조회_응답 = 주문_목록_조회_요청();

        // then
        주문_목록_조회_확인(주문_목록_조회_응답, 등록된_주문);

        // given
        Order 상태_변경_요청_데이터 = new Order();
        상태_변경_요청_데이터.setOrderStatus("MEAL");

        // when
        ExtractableResponse<Response> 주문_상태_변경_응답 = 주문_상태_변경_요청(주문_등록_응답, 상태_변경_요청_데이터);

        // then
        주문_상태_변경_확인(주문_상태_변경_응답, 상태_변경_요청_데이터);
    }
}
