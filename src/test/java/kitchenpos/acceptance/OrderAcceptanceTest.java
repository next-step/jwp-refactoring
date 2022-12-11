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
import static kitchenpos.acceptance.TableAcceptanceTestFixture.주문_테이블_생성되어_있음;
import static kitchenpos.domain.MenuFixture.createMenu;
import static kitchenpos.domain.MenuGroupFixture.createMenuGroup;
import static kitchenpos.domain.MenuProductFixture.createMenuProduct;
import static kitchenpos.domain.OrderFixture.createOrder;
import static kitchenpos.domain.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.domain.OrderTableFixture.createTable;
import static kitchenpos.product.acceptance.ProductAcceptanceTestFixture.상품_등록_되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.product.domain.ProductFixture;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {
    private ProductResponse 후라이드치킨;
    private ProductResponse 양념치킨;
    private ProductResponse 콜라;
    private MenuGroup 추천메뉴;
    private MenuProduct 후라이드치킨상품;
    private MenuProduct 양념치킨상품;
    private MenuProduct 콜라상품;
    private Menu 두마리치킨;
    private Menu 양념세트;
    private Order 주문_A;
    private Order 주문_B;

    @BeforeEach
    void orderSetUp() {
        super.setUp();

        후라이드치킨 = 상품_등록_되어_있음(ProductFixture.후라이드치킨).as(ProductResponse.class);
        양념치킨 = 상품_등록_되어_있음(ProductFixture.양념치킨).as(ProductResponse.class);
        콜라 = 상품_등록_되어_있음(ProductFixture.콜라).as(ProductResponse.class);

        추천메뉴 = 메뉴_그룹_등록되어_있음(createMenuGroup("추천메뉴")).as(MenuGroup.class);

        후라이드치킨상품 = createMenuProduct(후라이드치킨.getId(), 1);
        양념치킨상품 = createMenuProduct(양념치킨.getId(), 1);
        콜라상품 = createMenuProduct(콜라.getId(), 1);

        두마리치킨 = 메뉴_등록되어_있음(createMenu("두마리치킨", new BigDecimal(3000), 추천메뉴.getId(), Arrays.asList(후라이드치킨상품, 양념치킨상품))).as(Menu.class);
        양념세트 = 메뉴_등록되어_있음(createMenu("양념세트", new BigDecimal(2500), 추천메뉴.getId(), Arrays.asList(양념치킨상품, 콜라상품))).as(Menu.class);

        OrderTable 주문테이블_A = 주문_테이블_생성되어_있음(createTable(5, false)).as(OrderTable.class);
        OrderTable 주문테이블_B = 주문_테이블_생성되어_있음(createTable(3, false)).as(OrderTable.class);

        OrderLineItem 주문항목_A = createOrderLineItem(두마리치킨.getId(), 1);
        OrderLineItem 주문항목_B = createOrderLineItem(양념세트.getId(), 1);

        주문_A = createOrder(주문테이블_A.getId(), Collections.singletonList(주문항목_A));
        주문_B = createOrder(주문테이블_B.getId(), Collections.singletonList(주문항목_B));
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create() {
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
        Order updateOrder = createOrder(existOrder.getId(), existOrder.getOrderTableId(), exceptedStatus,
                existOrder.getOrderedTime(), existOrder.getOrderLineItems());

        ExtractableResponse<Response> response = 주문_상태_변경_요청(existOrder.getId(), updateOrder);

        주문_상태_변경됨(response, exceptedStatus);
    }
}
