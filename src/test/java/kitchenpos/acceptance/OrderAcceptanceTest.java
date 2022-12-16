package kitchenpos.acceptance;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.*;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.rest.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderAcceptanceTest extends BaseAcceptanceTest {

    private Menu 후라이드치킨;
    private List<OrderLineItem> 주문_상품_목록;
    private OrderTable 주문테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();

        Product 후라이드 = ProductRestAssured.상품_등록됨(ProductFixture.후라이드).as(Product.class);
        MenuGroup 한마리메뉴 = MenuGroupRestAssured.메뉴_그룹_등록됨(MenuGroupFixture.한마리메뉴).as(MenuGroup.class);
        this.후라이드치킨 = MenuRestAssured.메뉴_등록됨(후라이드, 한마리메뉴, MenuFixture.후라이드치킨, 2L).as(Menu.class);
        this.주문_상품_목록 = Arrays.asList(new OrderLineItem(후라이드치킨.getId(), 1));
        this.주문테이블 = TableRestAssured.주문_테이블_등록됨(1).as(OrderTable.class);
    }

    @Test
    void 신규_주문_정보가_주어진_경우_주문_등록_요청시_요청에_성공한다() {
        // when
        ExtractableResponse<Response> response = OrderRestAssured.주문_등록_요청(주문테이블.getId(), 주문_상품_목록);

        // then
        신규_주문_등록됨(response);
    }

    @Test
    void 주문_조회_목록_요청시_요청에_성공한다() {
        // given
        Order 기존_주문 = OrderRestAssured.주문_등록됨(주문테이블.getId(), 주문_상품_목록).as(Order.class);

        // when
        ExtractableResponse<Response> response = OrderRestAssured.주문_목록_조회_요청();

        // then
        주문_목록_조회됨(response, Arrays.asList(기존_주문));
    }

    @Test
    void 주문_상태_변경_요청시_요청에_성공한다() {
        // given
        Order 기존_주문 = OrderRestAssured.주문_등록됨(주문테이블.getId(), 주문_상품_목록).as(Order.class);

        // when
        ExtractableResponse<Response> response = OrderRestAssured.주문_상태_변경_요청(기존_주문.getId(), OrderStatus.COMPLETION);

        // then
        주문_목록_조회됨(response, Arrays.asList(기존_주문));
    }

    private void 신규_주문_등록됨(ExtractableResponse<Response> response) {
        Order 신규_주문 = response.as(Order.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(신규_주문.getId()).isNotNull(),
                () -> assertThat(신규_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    private void 주문_목록_조회됨(ExtractableResponse<Response> response, List<Order> expectedOrders) {
        List<Order> orders = response.as(new TypeRef<List<Order>>() {});
        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());
        List<Long> expectedOrderIds = expectedOrders.stream().map(Order::getId).collect(Collectors.toList());
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderIds).containsAll(expectedOrderIds)
        );
    }
}
