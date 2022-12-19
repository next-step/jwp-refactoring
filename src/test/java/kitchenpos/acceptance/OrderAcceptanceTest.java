package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static kitchenpos.acceptance.MenuGroupRestAssured.메뉴_그룹_생성_요청;
import static kitchenpos.acceptance.MenuRestAssured.메뉴_생성_요청;
import static kitchenpos.acceptance.OrderRestAssured.*;
import static kitchenpos.acceptance.ProductRestAssured.상품_생성_요청;
import static kitchenpos.acceptance.TableRestAssured.주문_테이블_생성_요청;
import static kitchenpos.domain.MenuGroupTestFixture.createMenuGroup;
import static kitchenpos.domain.MenuTestFixture.createMenu;
import static kitchenpos.domain.MenuTestFixture.createMenuProduct;
import static kitchenpos.domain.OrderTableTestFixture.createOrderTable;
import static kitchenpos.domain.OrderTestFixture.createOrder;
import static kitchenpos.domain.OrderTestFixture.createOrderLineItem;
import static kitchenpos.domain.ProductTestFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 관련 인수 테스트")
public class OrderAcceptanceTest extends AbstractAcceptanceTest {
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
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private OrderLineItem 불고기버거세트주문;
    private OrderLineItem 치킨버거세트주문;
    private Order 주문1;
    private Order 주문2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        햄버거세트 = 메뉴_그룹_생성_요청(createMenuGroup("햄버거세트")).as(MenuGroup.class);
        감자튀김 = 상품_생성_요청(createProduct(null, "감자튀김", BigDecimal.valueOf(3000L))).as(Product.class);
        콜라 = 상품_생성_요청(createProduct(null, "콜라", BigDecimal.valueOf(1500L))).as(Product.class);
        불고기버거 = 상품_생성_요청(createProduct(null, "불고기버거", BigDecimal.valueOf(4000L))).as(Product.class);
        치킨버거 = 상품_생성_요청(createProduct(null, "치킨버거", BigDecimal.valueOf(4500L))).as(Product.class);
        감자튀김상품 = createMenuProduct(1L, null, 감자튀김.getId(), 1L);
        콜라상품 = createMenuProduct(2L, null, 콜라.getId(), 1L);
        불고기버거상품 = createMenuProduct(3L, null, 불고기버거.getId(), 1L);
        치킨버거상품 = createMenuProduct(3L, null, 치킨버거.getId(), 1L);
        불고기버거세트 = 메뉴_생성_요청(createMenu(null, "불고기버거세트", BigDecimal.valueOf(8500L), 햄버거세트.getId(), Arrays.asList(감자튀김상품, 콜라상품, 불고기버거상품))).as(Menu.class);
        치킨버거세트 = 메뉴_생성_요청(createMenu(null, "치킨버거세트", BigDecimal.valueOf(9000L), 햄버거세트.getId(), Arrays.asList(감자튀김상품, 콜라상품, 치킨버거상품))).as(Menu.class);
        주문테이블1 = 주문_테이블_생성_요청(createOrderTable(null, null, 5, false)).as(OrderTable.class);
        주문테이블2 = 주문_테이블_생성_요청(createOrderTable(null, null, 4, false)).as(OrderTable.class);
        불고기버거세트주문 = createOrderLineItem(1L, null, 불고기버거세트.getId(), 2);
        치킨버거세트주문 = createOrderLineItem(2L, null, 치킨버거세트.getId(), 1);
        주문1 = createOrder(주문테이블1.getId(), null, null, Arrays.asList(불고기버거세트주문, 치킨버거세트주문));
        주문2 = createOrder(주문테이블2.getId(), null, null, singletonList(불고기버거세트주문));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() {
        // when
        ExtractableResponse<Response> response = 주문_생성_요청(주문1);
        // then
        주문_생성됨(response);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void 주문_목록_전체_조회() {
        // given
        ExtractableResponse<Response> response1 = 주문_생성_요청(주문1);
        ExtractableResponse<Response> response2 = 주문_생성_요청(주문2);
        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();
        // then
        주문_목록_응답됨(response);
        주문_목록_포함됨(response, Arrays.asList(response1, response2));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void 주문_상태_변경() {
        // given
        String expectOrderStatus = OrderStatus.MEAL.name();
        Order order = 주문_생성_요청(주문1).as(Order.class);
        Order changeOrder = createOrder(order.getOrderTableId(), expectOrderStatus, order.getOrderedTime(), order.getOrderLineItems());
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
