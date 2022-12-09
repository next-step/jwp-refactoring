package kitchenpos.acceptance;

import static java.util.Collections.singletonList;
import static kitchenpos.acceptance.MenuGroupRestAssured.메뉴_그룹_등록되어_있음;
import static kitchenpos.acceptance.MenuRestAssured.메뉴_등록되어_있음;
import static kitchenpos.acceptance.OrderRestAssured.주문_등록되어_있음;
import static kitchenpos.acceptance.OrderRestAssured.주문_목록_조회_요청;
import static kitchenpos.acceptance.OrderRestAssured.주문_상태_변경_요청;
import static kitchenpos.acceptance.OrderRestAssured.주문_생성_요청;
import static kitchenpos.acceptance.ProductRestAssured.상품_등록되어_있음;
import static kitchenpos.acceptance.TableRestAssured.주문_테이블_등록되어_있음;
import static kitchenpos.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.domain.MenuTestFixture.generateMenu;
import static kitchenpos.domain.OrderLineItemTestFixture.generateOrderLineItem;
import static kitchenpos.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.domain.OrderTestFixture.generateOrder;
import static kitchenpos.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
import org.springframework.http.HttpStatus;

@DisplayName("주문 관련 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private Product 감자튀김;
    private Product 불고기버거;
    private Product 치킨버거;
    private Product 콜라;
    private MenuGroup 햄버거세트;
    private MenuProduct 감자튀김상품;
    private MenuProduct 불고기버거상품;
    private MenuProduct 치킨버거상품;
    private MenuProduct 콜라상품;
    private Menu 불고기버거세트;
    private Menu 치킨버거세트;
    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;
    private OrderLineItem 불고기버거세트주문;
    private OrderLineItem 치킨버거세트주문;
    private Order 주문A;
    private Order 주문B;

    @BeforeEach
    public void setUp() {
        super.setUp();
        햄버거세트 = 메뉴_그룹_등록되어_있음(generateMenuGroup("햄버거세트")).as(MenuGroup.class);
        감자튀김 = 상품_등록되어_있음(generateProduct("감자튀김", BigDecimal.valueOf(3000L))).as(Product.class);
        콜라 = 상품_등록되어_있음(generateProduct("콜라", BigDecimal.valueOf(1500L))).as(Product.class);
        불고기버거 = 상품_등록되어_있음(generateProduct("불고기버거", BigDecimal.valueOf(4000L))).as(Product.class);
        치킨버거 = 상품_등록되어_있음(generateProduct("치킨버거", BigDecimal.valueOf(4500L))).as(Product.class);
        감자튀김상품 = generateMenuProduct(1L, null, 감자튀김.getId(), 1L);
        콜라상품 = generateMenuProduct(2L, null, 콜라.getId(), 1L);
        불고기버거상품 = generateMenuProduct(3L, null, 불고기버거.getId(), 1L);
        치킨버거상품 = generateMenuProduct(3L, null, 치킨버거.getId(), 1L);
        불고기버거세트 = 메뉴_등록되어_있음(generateMenu("불고기버거세트", BigDecimal.valueOf(8500L), 햄버거세트.getId(), Arrays.asList(감자튀김상품, 콜라상품, 불고기버거상품))).as(Menu.class);
        치킨버거세트 = 메뉴_등록되어_있음(generateMenu("치킨버거세트", BigDecimal.valueOf(9000L), 햄버거세트.getId(), Arrays.asList(감자튀김상품, 콜라상품, 치킨버거상품))).as(Menu.class);
        주문테이블A = 주문_테이블_등록되어_있음(generateOrderTable(null, 5, false)).as(OrderTable.class);
        주문테이블B = 주문_테이블_등록되어_있음(generateOrderTable(null, 4, false)).as(OrderTable.class);
        불고기버거세트주문 = generateOrderLineItem(1L, null, 불고기버거세트.getId(), 2);
        치킨버거세트주문 = generateOrderLineItem(2L, null, 치킨버거세트.getId(), 1);
        주문A = generateOrder(주문테이블A.getId(), null, null, Arrays.asList(불고기버거세트주문, 치킨버거세트주문));
        주문B = generateOrder(주문테이블B.getId(), null, null, singletonList(불고기버거세트주문));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // when
        ExtractableResponse<Response> response = 주문_생성_요청(주문A);

        // then
        주문_생성됨(response);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void findAllOrders() {
        // given
        ExtractableResponse<Response> 주문A_응답 = 주문_등록되어_있음(주문A);
        ExtractableResponse<Response> 주문B_응답 = 주문_등록되어_있음(주문B);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_응답됨(response);
        주문_목록_포함됨(response, Arrays.asList(주문A_응답, 주문B_응답));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        String expectOrderStatus = OrderStatus.MEAL.name();
        Order order = 주문_등록되어_있음(주문A).as(Order.class);
        Order changeOrder = generateOrder(order.getOrderTableId(), expectOrderStatus, order.getOrderedTime(), order.getOrderLineItems());

        // when
        ExtractableResponse<Response> response = 주문_상태_변경_요청(order.getId(), changeOrder);

        // then
        주문_상태_변경됨(response, expectOrderStatus);
    }

    private static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    private static void 주문_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 주문_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedOrderIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultOrderIds = response.jsonPath().getList(".", Order.class).stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        assertThat(resultOrderIds).containsAll(expectedOrderIds);
    }

    private static void 주문_상태_변경됨(ExtractableResponse<Response> response, String expectOrderStatus) {
        String actualOrderStatus = response.jsonPath().getString("orderStatus");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actualOrderStatus).isEqualTo(expectOrderStatus)
        );
    }
}
