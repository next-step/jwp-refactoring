package kitchenpos.Acceptance;

import static kitchenpos.Acceptance.MenuGroupTestFixture.메뉴_그룹_생성_요청함;
import static kitchenpos.Acceptance.MenuTestFixture.메뉴_생성_요청함;
import static kitchenpos.Acceptance.OrderTestFixture.주문_상태_변경됨;
import static kitchenpos.Acceptance.OrderTestFixture.주문_상태변경_요청함;
import static kitchenpos.Acceptance.OrderTestFixture.주문_생성_요청함;
import static kitchenpos.Acceptance.OrderTestFixture.주문_생성됨;
import static kitchenpos.Acceptance.OrderTestFixture.주문_요청_응답됨;
import static kitchenpos.Acceptance.OrderTestFixture.주문_조회_요청함;
import static kitchenpos.Acceptance.OrderTestFixture.주문_조회_포함됨;
import static kitchenpos.Acceptance.ProductTestFixture.상품_생성_요청함;
import static kitchenpos.Acceptance.TableTestFixture.주문_테이블_생성_요청함;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.AcceptanceTest;
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

public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTable 주문테이블;
    private OrderLineItem 주문항목;
    private Product 후라이드;
    private Product 콜라;
    private MenuProduct 후라이드메뉴상품;
    private MenuProduct 콜라메뉴상품;
    private MenuGroup 메뉴분류세트;
    private Menu 후라이드한마리;
    private Order 주문;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블 = 주문_테이블_생성_요청함(new OrderTable(null, null, 3, false)).as(OrderTable.class);
        메뉴분류세트 = 메뉴_그룹_생성_요청함(new MenuGroup("메뉴분류세트")).as(MenuGroup.class);
        후라이드 = 상품_생성_요청함(new Product("후라이드", BigDecimal.valueOf(15000))).as(Product.class);
        콜라 = 상품_생성_요청함(new Product("콜라", BigDecimal.valueOf(1000))).as(Product.class);
        후라이드메뉴상품 = new MenuProduct(1L, null, 후라이드.getId(), 1L);
        콜라메뉴상품 = new MenuProduct(2L, null, 콜라.getId(), 1L);
        후라이드한마리 = 메뉴_생성_요청함(new Menu(null, "후라이드한미리", BigDecimal.valueOf(15000), 메뉴분류세트.getId(),
                Arrays.asList(후라이드메뉴상품, 콜라메뉴상품))).as(Menu.class);
        주문항목 = new OrderLineItem(null, null, 후라이드한마리.getId(), 1L);
        주문 = new Order(null, 주문테이블.getId(), null, null, Collections.singletonList(주문항목));
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        //when
        ExtractableResponse<Response> response = 주문_생성_요청함(주문);
        //then
        주문_생성됨(response);
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void list() {
        //given
        ExtractableResponse<Response> 주문생성_response = 주문_생성_요청함(주문);
        //when
        ExtractableResponse<Response> response = 주문_조회_요청함();
        //then
        주문_요청_응답됨(response);
        주문_조회_포함됨(response, Collections.singletonList(주문생성_response));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeStatus() {
        //given
        Order 주문 = 주문_생성_요청함(this.주문).as(Order.class);
        Order 상태변경요청 = new Order(null, null, OrderStatus.MEAL.name(), null, null);
        //when
        ExtractableResponse<Response> response = 주문_상태변경_요청함(주문.getId(), 상태변경요청);
        //then
        주문_요청_응답됨(response);
        주문_상태_변경됨(response.as(Order.class), 상태변경요청);
    }
}
