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
import static kitchenpos.acceptance.OrderAcceptanceTestHelper.mapToOrderLineItem;
import static kitchenpos.acceptance.ProductAcceptanceTestHelper.createProduct;
import static kitchenpos.acceptance.TableAcceptanceTestHelper.changeTableEmptyStatus;
import static kitchenpos.acceptance.TableAcceptanceTestHelper.changeTableNumberOfGuests;
import static kitchenpos.acceptance.TableAcceptanceTestHelper.createTable;
import static kitchenpos.acceptance.TableAcceptanceTestHelper.getTables;
import static kitchenpos.acceptance.TableGroupAcceptanceTestHelper.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
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

class TableAcceptanceTest extends AcceptanceTest {

    private OrderBuilder orderBuilderWithSingleLineItem;

    @Override
    @BeforeEach
    protected void setup() {
        super.setup();

        final Menu menu = new Menu(
            "메뉴",
            BigDecimal.ONE,
            createMenuGroup("메뉴그룹").as(MenuGroup.class).getId(),
            Collections.singletonList(mapToMenuProduct(createProduct("상품", BigDecimal.ONE).as( Product.class), 1)));

        orderBuilderWithSingleLineItem = anOrder()
            .withOrderTable(createTable(nonEmptyOrderTableWithGuestNo(2)).as(OrderTable.class))
            .withOrderLineItems(Collections.singletonList(mapToOrderLineItem(createMenu(menu).as(Menu.class), 1)));
    }

    @Test
    void 생성시_빈테이블일경우_생성성공() {
        assertCreatedStatus(createTable(emptyOrderTableWithGuestNo(2)));
    }

    @Test
    void 생성시_채워진테이블일경우_생성성공() {
        assertCreatedStatus(createTable(nonEmptyOrderTableWithGuestNo(2)));
    }

    @Test
    void 비움상태변경시_빈테이블에서_채워진테이블로_변경성공() {
        OrderTable created = createTable(emptyOrderTableWithGuestNo(2)).as(OrderTable.class);
        ExtractableResponse<Response> response = changeTableEmptyStatus(created.getId(),
            nonEmptyOrderTableWithGuestNo(2));
        assertAll(
            () -> assertOkStatus(response),
            () -> assertThat(response.as(OrderTable.class).isEmpty()).isFalse()
        );
    }

    @Test
    void 비움상태변경시_채워진테이블에서_빈테이블로_변경성공() {
        OrderTable created = createTable(nonEmptyOrderTableWithGuestNo(2)).as(OrderTable.class);
        ExtractableResponse<Response> response = changeTableEmptyStatus(created.getId(),
            emptyOrderTableWithGuestNo(2));
        assertAll(
            () -> assertOkStatus(response),
            () -> assertThat(response.as(OrderTable.class).isEmpty()).isTrue()
        );
    }

    @Test
    void 비움상태변경시_단체지정되어있는경우_변경실패() {
        OrderTable created1 = createTable(emptyOrderTableWithGuestNo(2)).as(OrderTable.class);
        OrderTable created2 = createTable(emptyOrderTableWithGuestNo(4)).as(OrderTable.class);

        createTableGroup(Arrays.asList(created1, created2));

        assertAll(
            () -> assertInternalServerErrorStatus(changeTableEmptyStatus(created1.getId(), created1)),
            () -> assertInternalServerErrorStatus(changeTableEmptyStatus(created2.getId(), created2))
        );
    }

    @Test
    void 비움상태변경시_해당테이블의_주문이완료되지않은경우_변경실패() {
        OrderTable created = createTable(nonEmptyOrderTableWithGuestNo(2)).as(OrderTable.class);

        Order createdOrder = createOrder(orderBuilderWithSingleLineItem
            .withOrderTable(created)
            .build()).as(Order.class);

        assertAll(
            () -> assertThat(createdOrder.getOrderStatus()).isNotEqualTo(OrderStatus.COMPLETION.name()),
            () -> assertInternalServerErrorStatus(changeTableEmptyStatus(created.getId(),
                emptyOrderTableWithGuestNo(2)))
        );
    }

    @Test
    void 비움상태변경시_해당테이블의_주문이완료된경우_변경성공() {
        OrderTable created = createTable(nonEmptyOrderTableWithGuestNo(2)).as(OrderTable.class);

        Order createdOrder = createOrder(orderBuilderWithSingleLineItem
            .withOrderTable(created)
            .build()).as(Order.class);
        createdOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        changeOrderStatus(createdOrder.getId(), createdOrder);

        assertAll(
            () -> assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name()),
            () -> assertOkStatus(changeTableEmptyStatus(created.getId(),
                emptyOrderTableWithGuestNo(2)))
        );
    }

    @Test
    void 방문손님수변경시_변경하려는테이블이등록되지않은경우_변경실패() {
        OrderTable created = createTable(nonEmptyOrderTableWithGuestNo(2)).as(OrderTable.class);
        assertInternalServerErrorStatus(changeTableNumberOfGuests(-1L, created));
    }

    @Test
    void 방문손님수변경시_변경하려는수가0미만일경우_변경실패() {
        OrderTable created = createTable(nonEmptyOrderTableWithGuestNo(-1)).as(OrderTable.class);
        assertInternalServerErrorStatus(changeTableNumberOfGuests(created.getId(), created));
    }

    @Test
    void 방문손님수변경시_빈테이블일경우_변경실패() {
        OrderTable created = createTable(emptyOrderTableWithGuestNo(2)).as(OrderTable.class);
        assertInternalServerErrorStatus(changeTableNumberOfGuests(created.getId(), created));
    }

    @Test
    void 방문손님수변경시_채워진테이블일경우_변경성공() {
        OrderTable created = createTable(nonEmptyOrderTableWithGuestNo(4)).as(OrderTable.class);
        ExtractableResponse<Response> response = changeTableNumberOfGuests(created.getId(),
            nonEmptyOrderTableWithGuestNo(2));
        assertAll(
            () -> assertOkStatus(response),
            () -> assertThat(response.as(OrderTable.class).getNumberOfGuests()).isEqualTo(2)
        );
    }

    @Test
    void 조회시_존재하는목록반환() {
        OrderTable created = createTable(emptyOrderTableWithGuestNo(2)).as(OrderTable.class);
        ExtractableResponse<Response> response = getTables();
        assertAll(
            () -> assertOkStatus(response),
            () -> assertThat(mapToOrderTableIds(response)).contains(created.getId())
        );
    }

    private List<Long> mapToOrderTableIds(ExtractableResponse<Response> response) {
        return response.jsonPath()
            .getList(".", OrderTable.class)
            .stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }
}
