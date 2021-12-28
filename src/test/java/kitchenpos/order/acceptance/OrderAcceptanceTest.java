package kitchenpos.order.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTestHelper.*;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTestHelper.*;
import static kitchenpos.order.acceptance.OrderAcceptanceTestHelper.*;
import static kitchenpos.product.acceptance.ProductAcceptanceTestHelper.*;
import static kitchenpos.table.acceptance.TableAcceptanceTestHelper.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    OrderTable 좌석1;
    OrderTable 좌석2;
    MenuProduct 제육볶음_메뉴;
    MenuGroup 분식;
    Menu 제육볶음;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        Product 제육볶음_상품 = 상품_등록되어_있음("제육볶음", 8900).as(Product.class);
        제육볶음_메뉴 = 메뉴_상품(제육볶음_상품.getId(), 1);
        분식 = 메뉴그룹_등록되어_있음("분식").as(MenuGroup.class);
        제육볶음 = 메뉴_등록되어_있음("제육볶음", 7000, 분식.getId(), 제육볶음_메뉴).as(Menu.class);

        좌석1 = 좌석_등록_되어_있음(false, 0, null).as(OrderTable.class);
        좌석2 = 좌석_등록_되어_있음(true, 0, null).as(OrderTable.class);
    }

    @DisplayName("주문 생성")
    @Test
    void createOrder() {
        // when
        ExtractableResponse<Response> 주문_생성_요청_응답 = 주문_생성_요청(좌석1.getId(), OrderStatus.COOKING, 주문_항목(제육볶음, 3));

        // then
        주문_생성됨(주문_생성_요청_응답);
    }

    @DisplayName("주문 목록 조회")
    @Test
    void listOrder() {
        // given
        ExtractableResponse<Response> 주문_생성_요청_응답 = 주문_생성_요청(좌석1.getId(), OrderStatus.COOKING, 주문_항목(제육볶음, 3));

        // when
        ExtractableResponse<Response> 주문_목록_조회_요청_응답 = 주문_목록_조회_요청();

        // then
        주문_목록_조회됨(주문_목록_조회_요청_응답);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        // given
        OrderStatus 주문_상태 = OrderStatus.COOKING;
        ExtractableResponse<Response> 주문_생성_요청_응답 = 주문_생성_요청(좌석1.getId(), 주문_상태, 주문_항목(제육볶음, 3));
        Order 주문_생성_정보 = 주문_생성_요청_응답.as(Order.class);

        // when
        ExtractableResponse<Response> 주문_상태_변경_요청_응답 = 주문_상태_변경_요청(주문_생성_정보, OrderStatus.MEAL);

        // then
        주문_상태_변경됨(주문_상태_변경_요청_응답, 주문_상태);
    }
}
