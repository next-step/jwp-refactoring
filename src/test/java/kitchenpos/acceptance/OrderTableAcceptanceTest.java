package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.acceptance.MenuAcceptanceFactory.메뉴_등록_요청;
import static kitchenpos.acceptance.MenuGroupAcceptanceFactory.메뉴그룹_등록_요청;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_등록_요청;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_상태_변경_요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_등록_요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_등록성공;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_빈테이블_변경성공;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_빈테이블_변경실패;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_빈테이블로_변경요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_인원수_변경성공;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_인원수_변경실패;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_조회_요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_조회성공;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.테이블_손님수_변경_요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.테이블그룹에속한_주문테이블_등록_요청;
import static kitchenpos.acceptance.ProductAcceptanceFactory.상품_등록_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_등록_요청;

@DisplayName("주문테이블 관련")
public class OrderTableAcceptanceTest extends AcceptanceTest {

    private Menu 후라이드메뉴;
    private OrderLineItem 주문항목;

    @BeforeEach
    public void setUp() {
        super.setUp();

        //메뉴
        Product 후라이드 = 상품_등록_요청("후라이드", 16000).as(Product.class);
        MenuGroup 두마리메뉴 = 메뉴그룹_등록_요청("두마리메뉴").as(MenuGroup.class);
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(후라이드.getId());
        menuProduct.setQuantity(1);
        후라이드메뉴 = 메뉴_등록_요청(후라이드.getName(), 후라이드.getPrice(), 두마리메뉴.getId(), Arrays.asList(menuProduct)).as(Menu.class);

        //주문
        주문항목 = new OrderLineItem();
        주문항목.setMenuId(후라이드메뉴.getId());
        주문항목.setQuantity(1);

    }

    @Test
    void 주문테이블_등록_성공() {
        ExtractableResponse<Response> 주문테이블_등록_결과 = 주문테이블_등록_요청(true, 5);

        주문테이블_등록성공(주문테이블_등록_결과);
    }

    @Test
    void 주문테이블_조회_성공() {
        ExtractableResponse<Response> 주문테이블_조회_결과 = 주문테이블_조회_요청();

        주문테이블_조회성공(주문테이블_조회_결과);
    }

    @Test
    void 주문테이블_빈테이블로_변경_성공() {
        OrderTable 주문테이블 = 주문테이블_등록_요청(true, 5).as(OrderTable.class);
        ExtractableResponse<Response> 주문테이블_빈테이블로_변경_결과 = 주문테이블_빈테이블로_변경요청(주문테이블);

        주문테이블_빈테이블_변경성공(주문테이블_빈테이블로_변경_결과);
    }

    @Test
    void 주문테이블_빈테이블로_변경_실패_존재하지않는_주문테이블() {
        long 존재하지않는_주문테이블번호 = 999L;
        OrderTable 존재하지않는_주문테이블 = new OrderTable();
        존재하지않는_주문테이블.setId(존재하지않는_주문테이블번호);

        ExtractableResponse<Response> 주문테이블_빈테이블로_변경_결과 = 주문테이블_빈테이블로_변경요청(존재하지않는_주문테이블);

        주문테이블_빈테이블_변경실패(주문테이블_빈테이블로_변경_결과);
    }

    @Test
    void 주문테이블_빈테이블로_변경_실패_테이블그룹에속한_주문테이블() {
        OrderTable 테이블그룹에속한_주문테이블 = 테이블그룹에속한_주문테이블_등록_요청(true, 5).as(OrderTable.class);
        OrderTable 테이블그룹에속한_주문테이블2 = 테이블그룹에속한_주문테이블_등록_요청(true, 5).as(OrderTable.class);
        List<OrderTable> 주문테이블_리스트 = Arrays.asList(테이블그룹에속한_주문테이블, 테이블그룹에속한_주문테이블2);
        ExtractableResponse<Response> 테이블그룹_등록_결과 = 테이블그룹_등록_요청(주문테이블_리스트);

        ExtractableResponse<Response> 주문테이블_빈테이블로_변경_결과 = 주문테이블_빈테이블로_변경요청(테이블그룹에속한_주문테이블);

        주문테이블_빈테이블_변경실패(주문테이블_빈테이블로_변경_결과);
    }

    @ParameterizedTest
    @MethodSource("cookingAndMeal")
    void 주문테이블_빈테이블로_변경_실패_주문상태가_요리중이나_식사중(OrderStatus status) {
        OrderTable 테이블그룹에속한_주문테이블 = 테이블그룹에속한_주문테이블_등록_요청(false, 5).as(OrderTable.class);
        Order 주문 = 주문_등록_요청(테이블그룹에속한_주문테이블.getId(), Arrays.asList(주문항목)).as(Order.class);
        ExtractableResponse<Response> 요리중으로_변경 = 주문_상태_변경_요청(주문, status);

        ExtractableResponse<Response> 주문테이블_빈테이블로_변경_결과 = 주문테이블_빈테이블로_변경요청(테이블그룹에속한_주문테이블);

        주문테이블_빈테이블_변경실패(주문테이블_빈테이블로_변경_결과);
    }

    @Test
    void 주문테이블_손님수변경_성공() {
        OrderTable 주문테이블 = 주문테이블_등록_요청(false, 5).as(OrderTable.class);

        ExtractableResponse<Response> 테이블_손님수_변경_결과 = 테이블_손님수_변경_요청(주문테이블.getId(), 3);

        주문테이블_인원수_변경성공(테이블_손님수_변경_결과, 3);
    }

    @Test
    void 주문테이블_손님수변경_실패_손님의수를_음수로변경() {
        OrderTable 주문테이블 = 주문테이블_등록_요청(false, 5).as(OrderTable.class);

        ExtractableResponse<Response> 테이블_손님수_변경_결과 = 테이블_손님수_변경_요청(주문테이블.getId(), -1);

        주문테이블_인원수_변경실패(테이블_손님수_변경_결과);
    }

    @Test
    void 주문테이블_손님수변경_실패_존재하지않는_테이블() {
        OrderTable 존재하지않는_주문테이블 = new OrderTable();
        존재하지않는_주문테이블.setId(999L);

        ExtractableResponse<Response> 테이블_손님수_변경_결과 = 테이블_손님수_변경_요청(존재하지않는_주문테이블.getId(), 3);

        주문테이블_인원수_변경실패(테이블_손님수_변경_결과);
    }

    @Test
    void 주문테이블_손님수변경_실패_주문테이블이_빈경우() {
        OrderTable 주문테이블 = 주문테이블_등록_요청(true, 5).as(OrderTable.class);

        ExtractableResponse<Response> 테이블_손님수_변경_결과 = 테이블_손님수_변경_요청(주문테이블.getId(), 3);

        주문테이블_인원수_변경실패(테이블_손님수_변경_결과);
    }

}
