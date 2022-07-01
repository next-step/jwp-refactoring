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
import kitchenpos.tablegroup.domain.TableGroup;
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
import static kitchenpos.acceptance.ProductAcceptanceFactory.상품_등록_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_등록_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_등록성공;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_등록실패;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_삭제_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_삭제성공;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_삭제실패;

@DisplayName("테이블그룹 관련")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private OrderTable 테이블3;
    private Menu 후라이드메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();
        테이블1 = 주문테이블_등록_요청(true, 5).as(OrderTable.class);
        테이블2 = 주문테이블_등록_요청(true, 5).as(OrderTable.class);
        테이블3 = 주문테이블_등록_요청(false, 5).as(OrderTable.class);

//        메뉴
        Product 후라이드 = 상품_등록_요청("후라이드", 16000).as(Product.class);
        MenuGroup 두마리메뉴 = 메뉴그룹_등록_요청("두마리메뉴").as(MenuGroup.class);
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(후라이드.getId());
        menuProduct.setQuantity(1);
        후라이드메뉴 = 메뉴_등록_요청(후라이드.getName(), 후라이드.getPriceBigDecimal(), 두마리메뉴.getId(), Arrays.asList(menuProduct)).as(Menu.class);
    }

    @Test
    void 테이블그룹을_등록할_수_있다() {
        List<OrderTable> 주문테이블_리스트 = Arrays.asList(테이블1, 테이블2);

        ExtractableResponse<Response> 테이블그룹_등록_결과 = 테이블그룹_등록_요청(주문테이블_리스트);

        테이블그룹_등록성공(테이블그룹_등록_결과);
    }

    @Test
    void 주문테이블이_1개면_테이블그룹을_등록할_수_없다() {
        List<OrderTable> 주문테이블_리스트 = Arrays.asList(테이블1);

        ExtractableResponse<Response> 테이블그룹_등록_결과 = 테이블그룹_등록_요청(주문테이블_리스트);

        테이블그룹_등록실패(테이블그룹_등록_결과);
    }

    @Test
    void 주문테이블이_존재하지않으면_테이블그룹을_등록할_수_없다() {
        OrderTable 존재하지않는테이블 = new OrderTable();
        List<OrderTable> 주문테이블_리스트 = Arrays.asList(테이블1, 존재하지않는테이블);

        ExtractableResponse<Response> 테이블그룹_등록_결과 = 테이블그룹_등록_요청(주문테이블_리스트);

        테이블그룹_등록실패(테이블그룹_등록_결과);
    }

    @Test
    void 비어있지_않은_주문테이블이_존재하면_테이블그룹을_등록할_수_없다() {
        List<OrderTable> 주문테이블_리스트 = Arrays.asList(테이블1, 테이블3);

        ExtractableResponse<Response> 테이블그룹_등록_결과 = 테이블그룹_등록_요청(주문테이블_리스트);

        테이블그룹_등록실패(테이블그룹_등록_결과);
    }

    @Test
    void 테이블그룹을_삭제할_수_있다() {
        List<OrderTable> 주문테이블_리스트 = Arrays.asList(테이블1, 테이블2);
        TableGroup 등록된_테이블그룹 = 테이블그룹_등록_요청(주문테이블_리스트).as(TableGroup.class);

        ExtractableResponse<Response> 테이블그룹_삭제_결과 = 테이블그룹_삭제_요청(등록된_테이블그룹.getId());

        테이블그룹_삭제성공(테이블그룹_삭제_결과);
    }

    @ParameterizedTest
    @MethodSource("cookingAndMeal")
    void 주문상태가_요리중이거나_식사중인_테이블그룹은_삭제할_수_없다(OrderStatus status) {
        TableGroup 등록된_테이블그룹 = 테이블그룹_등록_요청(Arrays.asList(테이블1, 테이블2)).as(TableGroup.class);
        OrderLineItem 주문항목 = new OrderLineItem();
        주문항목.setMenuId(후라이드메뉴.getId());
        주문항목.setQuantity(1);
        Order 주문 = 주문_등록_요청(테이블1.getId(), Arrays.asList(주문항목)).as(Order.class);
        주문_상태_변경_요청(주문, status);

        ExtractableResponse<Response> 테이블그룹_삭제_결과 = 테이블그룹_삭제_요청(등록된_테이블그룹.getId());

        테이블그룹_삭제실패(테이블그룹_삭제_결과);
    }

}
