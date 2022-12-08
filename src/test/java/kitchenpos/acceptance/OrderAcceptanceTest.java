package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTestFixture.메뉴_등록되어_있음;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestFixture.메뉴_그룹_등록되어_있음;
import static kitchenpos.acceptance.OrderAcceptanceTestFixture.주문_목록_조회_요청;
import static kitchenpos.acceptance.OrderAcceptanceTestFixture.주문_목록_조회됨;
import static kitchenpos.acceptance.OrderAcceptanceTestFixture.주문_목록_포함됨;
import static kitchenpos.acceptance.OrderAcceptanceTestFixture.주문_상태_변경_요청;
import static kitchenpos.acceptance.OrderAcceptanceTestFixture.주문_상태_변경됨;
import static kitchenpos.acceptance.OrderAcceptanceTestFixture.주문_생성_요청;
import static kitchenpos.acceptance.OrderAcceptanceTestFixture.주문_생성되어_있음;
import static kitchenpos.acceptance.OrderAcceptanceTestFixture.주문_생성됨;
import static kitchenpos.acceptance.ProductAcceptanceTestFixture.상품_등록_되어_있음;
import static kitchenpos.acceptance.TableAcceptanceTestFixture.주문_테이블_생성되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {
    private Product 메모리;
    private Product 디스플레이;
    private MenuGroup 핸드폰;
    private MenuProduct 메모리상품;
    private MenuProduct 디스플레이상품;
    private Menu 아이폰;
    private Menu 갤럭시;
    private Order 주문_A;
    private Order 주문_B;

    @BeforeEach
    void orderSetUp() {
        super.setUp();

        메모리 = 상품_등록_되어_있음(new Product(null, "메모리", new BigDecimal(3000))).as(Product.class);
        디스플레이 = 상품_등록_되어_있음(new Product(null, "디스플레이", new BigDecimal(5000))).as(Product.class);
        핸드폰 = 메뉴_그룹_등록되어_있음(new MenuGroup(null, "애플")).as(MenuGroup.class);
        메모리상품 = new MenuProduct(null, null, 메모리.getId(), 1);
        디스플레이상품 = new MenuProduct(null, null, 디스플레이.getId(), 1);

        아이폰 = new Menu(null, "아이폰", new BigDecimal(7000), 핸드폰.getId(), new ArrayList<>());
        아이폰.setMenuProducts(Arrays.asList(메모리상품, 디스플레이상품));
        아이폰 = 메뉴_등록되어_있음(아이폰).as(Menu.class);
        갤럭시 = new Menu(null, "갤럭시", new BigDecimal(5000), 핸드폰.getId(), new ArrayList<>());
        갤럭시.setMenuProducts(Arrays.asList(메모리상품, 디스플레이상품));
        갤럭시 = 메뉴_등록되어_있음(갤럭시).as(Menu.class);

        OrderTable 주문테이블_A = 주문_테이블_생성되어_있음(new OrderTable(null, null, 5, false)).as(OrderTable.class);
        OrderTable 주문테이블_B = 주문_테이블_생성되어_있음(new OrderTable(null, null, 3, false)).as(OrderTable.class);

        OrderLineItem 주문항목_A = new OrderLineItem(null, null, 아이폰.getId(), 1);
        OrderLineItem 주문항목_B = new OrderLineItem(null, null, 갤럭시.getId(), 1);

        주문_A = new Order(null, 주문테이블_A.getId(), null, null, Collections.singletonList(주문항목_A));
        주문_B = new Order(null, 주문테이블_B.getId(), null, null, Collections.singletonList(주문항목_B));
    }

    @DisplayName("주문을 등록한다")
    @Test
    void createOrder() {
        ExtractableResponse<Response> response = 주문_생성_요청(주문_A);

        주문_생성됨(response);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void getOrderList() {
        ExtractableResponse<Response> createResponse1 = 주문_생성되어_있음(주문_A);
        ExtractableResponse<Response> createResponse2 = 주문_생성되어_있음(주문_B);

        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        주문_목록_조회됨(response);
        주문_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        String exceptedStatus = OrderStatus.COOKING.name();
        Order existOrder = 주문_생성되어_있음(주문_A).as(Order.class);
        Order updateOrder = new Order(existOrder.getId(), existOrder.getOrderTableId(), exceptedStatus,
                existOrder.getOrderedTime(), existOrder.getOrderLineItems());

        ExtractableResponse<Response> response = 주문_상태_변경_요청(existOrder.getId(), updateOrder);

        주문_상태_변경됨(response, exceptedStatus);
    }
}
