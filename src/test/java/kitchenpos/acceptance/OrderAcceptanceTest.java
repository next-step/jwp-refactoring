package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.acceptance.MenuAcceptanceFactory.메뉴_등록_요청;
import static kitchenpos.acceptance.MenuGroupAcceptanceFactory.메뉴그룹_등록_요청;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_등록_요청;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_등록성공;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_등록실패;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_변경성공;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_변경실패;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_상태_변경_요청;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_조회_요청;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_조회성공;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_등록_요청;
import static kitchenpos.acceptance.ProductAcceptanceFactory.상품_등록_요청;

@DisplayName("주문 관련")
public class OrderAcceptanceTest extends AcceptanceTest {
    private OrderTable 주문테이블1;

    private Menu 후라이드메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();
        //테이블
        주문테이블1 = 주문테이블_등록_요청(false, 5).as(OrderTable.class);

        //메뉴
        Product 후라이드 = 상품_등록_요청("후라이드", 16000).as(Product.class);
        MenuGroup 두마리메뉴 = 메뉴그룹_등록_요청("두마리메뉴").as(MenuGroup.class);
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(후라이드.getId());
        menuProduct.setQuantity(1);
        후라이드메뉴 = 메뉴_등록_요청(후라이드.getName(), 후라이드.getPrice().getPrice(), 두마리메뉴.getId(), Arrays.asList(menuProduct)).as(Menu.class);
    }

    @Test
    void 주문을_등록할_수_있다() {
        OrderLineItem 주문항목 = new OrderLineItem();
        주문항목.setMenuId(후라이드메뉴.getId());
        주문항목.setQuantity(1);

        ExtractableResponse<Response> 주문_등록_결과 = 주문_등록_요청(주문테이블1.getId(), Arrays.asList(주문항목));

        주문_등록성공(주문_등록_결과);
    }

    @Test
    void 주문항목이_비어있으면_주문을_등록할_수_없다() {
        ExtractableResponse<Response> 주문_등록_결과 = 주문_등록_요청(주문테이블1.getId(), Arrays.asList());

        주문_등록실패(주문_등록_결과);
    }

    @Test
    void 메뉴가_등록되지_않았으면_주문을_등록할_수_없다() {
        long 등록되지않은_메뉴아이디 = 999L;
        OrderLineItem 주문항목 = new OrderLineItem();
        주문항목.setMenuId(등록되지않은_메뉴아이디);
        주문항목.setQuantity(1);

        ExtractableResponse<Response> 주문_등록_결과 = 주문_등록_요청(주문테이블1.getId(), Arrays.asList(주문항목));

        주문_등록실패(주문_등록_결과);
    }

    @Test
    void 주문테이블이_존재하지_않으면_주문을_등록할_수_없다() {
        Long 존재하지않는_주문테이블 = 999L;
        OrderLineItem 주문항목 = new OrderLineItem();
        주문항목.setMenuId(후라이드메뉴.getId());
        주문항목.setQuantity(1);

        ExtractableResponse<Response> 주문_등록_결과 = 주문_등록_요청(존재하지않는_주문테이블, Arrays.asList(주문항목));

        주문_등록실패(주문_등록_결과);
    }

    @Test
    void 빈테이블은_주문을_등록할_수_없다() {
        OrderTable 빈테이블 = 주문테이블_등록_요청(true, 5).as(OrderTable.class);
        OrderLineItem 주문항목 = new OrderLineItem();
        주문항목.setMenuId(후라이드메뉴.getId());
        주문항목.setQuantity(1);

        ExtractableResponse<Response> 주문_등록_결과 = 주문_등록_요청(빈테이블.getId(), Arrays.asList(주문항목));

        주문_등록실패(주문_등록_결과);
    }


    @Test
    void 주문을_조회할_수_있다() {
        ExtractableResponse<Response> 주문_조회_결과 = 주문_조회_요청();

        주문_조회성공(주문_조회_결과);
    }

    @Test
    void 주문의_주문상태를_변경할_수_있다() {
        OrderStatus 주문상태_요리중 = OrderStatus.COOKING;
        OrderLineItem 주문항목 = new OrderLineItem();
        주문항목.setMenuId(후라이드메뉴.getId());
        주문항목.setQuantity(1);
        Order 주문 = 주문_등록_요청(주문테이블1.getId(), Arrays.asList(주문항목)).as(Order.class);

        ExtractableResponse<Response> 주문_변경_결과 = 주문_상태_변경_요청(주문, 주문상태_요리중);

        주문_변경성공(주문_변경_결과, 주문상태_요리중);
    }

    @Test
    void 주문이_존재하지않으면_주문을_변경할_수_없다() {
        Order 존재하지않는_주문 = new Order();
        존재하지않는_주문.setId(999L);
        OrderStatus 주문상태_요리중 = OrderStatus.COOKING;

        ExtractableResponse<Response> 주문_변경_결과 = 주문_상태_변경_요청(존재하지않는_주문, 주문상태_요리중);

        주문_변경실패(주문_변경_결과);
    }

    @Test
    void 이미완료된_주문의_주문상태를_변경할_수_없다() {
        OrderStatus 주문상태_완료 = OrderStatus.COMPLETION;
        OrderLineItem 주문항목 = new OrderLineItem();
        주문항목.setMenuId(후라이드메뉴.getId());
        주문항목.setQuantity(1);
        Order 주문 = 주문_등록_요청(주문테이블1.getId(), Arrays.asList(주문항목)).as(Order.class);
        ExtractableResponse<Response> 완료상태로_변경 = 주문_상태_변경_요청(주문, 주문상태_완료);

        ExtractableResponse<Response> 주문_변경_결과 = 주문_상태_변경_요청(주문, 주문상태_완료);

        주문_변경실패(주문_변경_결과);
    }
}
