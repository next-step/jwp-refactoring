package kitchenpos.acceptance;

import static kitchenpos.OrderBuilder.anOrder;
import static kitchenpos.OrderTableBuilder.emptyOrderTableWithGuestNo;
import static kitchenpos.OrderTableBuilder.nonEmptyOrderTableWithGuestNo;
import static kitchenpos.acceptance.AcceptanceTestHelper.assertCreatedStatus;
import static kitchenpos.acceptance.AcceptanceTestHelper.assertInternalServerErrorStatus;
import static kitchenpos.acceptance.AcceptanceTestHelper.assertOkStatus;
import static kitchenpos.acceptance.MenuAcceptanceTestHelper.createMenu;
import static kitchenpos.acceptance.MenuAcceptanceTestHelper.mapToMenuProduct;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestHelper.createMenuGroup;
import static kitchenpos.acceptance.OrderAcceptanceTestHelper.changeOrderStatus;
import static kitchenpos.acceptance.OrderAcceptanceTestHelper.createOrder;
import static kitchenpos.acceptance.OrderAcceptanceTestHelper.getOrders;
import static kitchenpos.acceptance.OrderAcceptanceTestHelper.mapToOrderLineItem;
import static kitchenpos.acceptance.ProductAcceptanceTestHelper.createProduct;
import static kitchenpos.acceptance.TableAcceptanceTestHelper.createTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.OrderBuilder;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderAcceptanceTest extends AcceptanceTest {

    private OrderBuilder orderBuilderWithSingleLineItem;
    private Menu menu;

    @Override
    @BeforeEach
    protected void setup() {
        super.setup();

        menu = new Menu(
            "메뉴",
            BigDecimal.ONE,
            createMenuGroup("메뉴그룹").as(MenuGroup.class).getId(),
            Collections.singletonList(mapToMenuProduct(createProduct("상품", BigDecimal.ONE).as(Product.class), 1)));

        orderBuilderWithSingleLineItem = anOrder()
            .withOrderTable(createTable(nonEmptyOrderTableWithGuestNo(2)).as(OrderTable.class))
            .withOrderLineItems(Collections.singletonList(mapToOrderLineItem(createMenu(menu).as(Menu.class), 1)));
    }

    @Test
    void 생성시_주문테이블이_비어있지않고_정상적인_주문항목이포함된경우_생성성공() {
        final Order order = orderBuilderWithSingleLineItem.build();
        assertCreatedStatus(createOrder(order));
    }

    @Test
    void 생성시_주문항목이_비어있는경우_생성실패() {
        final Order order = orderBuilderWithSingleLineItem
            .withOrderLineItems(Collections.emptyList())
            .build();
        assertInternalServerErrorStatus(createOrder(order));
    }

    @Test
    void 생성시_미등록메뉴를가진_주문항목이포함된경우_생성실패() {
        final Order order = orderBuilderWithSingleLineItem
            .withOrderLineItems(Collections.singletonList(mapToOrderLineItem(menu, 1)))
            .build();
        assertInternalServerErrorStatus(createOrder(order));
    }

    @Test
    void 생성시_주문테이블이_비어있는경우_생성실패() {
        final Order order = orderBuilderWithSingleLineItem
            .withOrderTable(createTable(emptyOrderTableWithGuestNo(2)).as(OrderTable.class))
            .build();
        assertInternalServerErrorStatus(createOrder(order));
    }

    @Test
    void 상태변경시_등록된주문이아닌경우_변경실패() {
        final Order order = orderBuilderWithSingleLineItem.build();
        assertInternalServerErrorStatus(changeOrderStatus(-1L, order));
    }

    @Test
    void 상태변경시_완료된주문인경우_변경실패() {
        final Order created = createOrder(orderBuilderWithSingleLineItem.build()).as(Order.class);
        created.setOrderStatus(OrderStatus.COMPLETION.name());

        final Order changed = changeOrderStatus(created.getId(), created).as(Order.class);

        assertAll(
            () -> assertThat(changed.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name()),
            () -> assertInternalServerErrorStatus(changeOrderStatus(created.getId(), changed))
        );
    }

    @Test
    void 상태변경시_아직완료되지않은주문인경우_변경성공() {
        final Order created = createOrder(orderBuilderWithSingleLineItem.build()).as(Order.class);
        created.setOrderStatus(OrderStatus.MEAL.name());

        ExtractableResponse<Response> response = changeOrderStatus(created.getId(), created);
        final Order changed = response.as(Order.class);
        assertAll(
            () -> assertOkStatus(response),
            () -> assertThat(changed.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
        );
    }

    @Test
    void 조회시_존재하는목록반환() {
        Order created = createOrder(orderBuilderWithSingleLineItem.build()).as(Order.class);
        ExtractableResponse<Response> response = getOrders();
        assertAll(
            () -> assertOkStatus(response),
            () -> assertThat(mapToOrderIds(response)).contains(created.getId())
        );
    }

    private List<Long> mapToOrderIds(ExtractableResponse<Response> response) {
        return response.jsonPath()
            .getList(".", Order.class)
            .stream()
            .map(Order::getId)
            .collect(Collectors.toList());
    }
}
