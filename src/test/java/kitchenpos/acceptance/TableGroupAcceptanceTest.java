package kitchenpos.acceptance;

import static kitchenpos.OrderBuilder.anOrder;
import static kitchenpos.OrderBuilder.completionStatusOrder;
import static kitchenpos.OrderTableBuilder.emptyOrderTableWithGuestNo;
import static kitchenpos.OrderTableBuilder.nonEmptyOrderTableWithGuestNo;
import static kitchenpos.acceptance.AcceptanceTestHelper.assertCreatedStatus;
import static kitchenpos.acceptance.AcceptanceTestHelper.assertInternalServerErrorStatus;
import static kitchenpos.acceptance.AcceptanceTestHelper.assertNoContentStatus;
import static kitchenpos.acceptance.MenuAcceptanceTestHelper.createMenu;
import static kitchenpos.acceptance.MenuAcceptanceTestHelper.mapToMenuProduct;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestHelper.createMenuGroup;
import static kitchenpos.acceptance.OrderAcceptanceTestHelper.changeOrderStatus;
import static kitchenpos.acceptance.OrderAcceptanceTestHelper.createOrder;
import static kitchenpos.acceptance.OrderAcceptanceTestHelper.mapToOrderLineItem;
import static kitchenpos.acceptance.ProductAcceptanceTestHelper.createProduct;
import static kitchenpos.acceptance.TableAcceptanceTestHelper.createTable;
import static kitchenpos.acceptance.TableGroupAcceptanceTestHelper.createTableGroup;
import static kitchenpos.acceptance.TableGroupAcceptanceTestHelper.deleteTableGroup;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.OrderBuilder;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTable savedEmptyOrderTable1;
    private OrderTable savedEmptyOrderTable2;
    private OrderBuilder orderBuilderWithSingleLineItem;

    @Override
    @BeforeEach
    protected void setup() {
        super.setup();

        savedEmptyOrderTable1 = createTable(emptyOrderTableWithGuestNo(2)).as(OrderTable.class);
        savedEmptyOrderTable2 = createTable(emptyOrderTableWithGuestNo(4)).as(OrderTable.class);

        final Menu menu = new Menu(
            "메뉴",
            BigDecimal.ONE,
            createMenuGroup("메뉴그룹").as(MenuGroup.class).getId(),
            Collections.singletonList(mapToMenuProduct(createProduct("상품", BigDecimal.ONE).as(Product.class), 1)));

        orderBuilderWithSingleLineItem = anOrder()
            .withOrderLineItems(Collections.singletonList(mapToOrderLineItem(createMenu(menu).as(Menu.class), 1)));
    }

    @Test
    void 생성시_주문테이블목록이_비어있는경우_생성실패() {
        final List<OrderTable> orderTables = Collections.emptyList();
        assertInternalServerErrorStatus(createTableGroup(orderTables));
    }

    @Test
    void 생성시_주문테이블수가_2개미만인경우_생성실패() {
        final List<OrderTable> orderTables = Collections.singletonList(savedEmptyOrderTable1);
        assertInternalServerErrorStatus(createTableGroup(orderTables));
    }

    @Test
    void 생성시_주문테이블목록중_미등록주문테이블이포함된경우_생성실패() {
        final List<OrderTable> orderTables = Arrays.asList(
            savedEmptyOrderTable1,
            emptyOrderTableWithGuestNo(4)
        );
        assertInternalServerErrorStatus(createTableGroup(orderTables));
    }

    @Test
    void 생성시_주문테이블목록중_채워진주문테이블이포함된경우_생성실패() {
        final List<OrderTable> orderTables = Arrays.asList(
            savedEmptyOrderTable1,
            createTable(nonEmptyOrderTableWithGuestNo(2)).as(OrderTable.class)
        );
        assertInternalServerErrorStatus(createTableGroup(orderTables));
    }

    @Test
    void 생성시_모든주문테이블이_비어있고_단체지정되어있지않다면_생성성공() {
        final List<OrderTable> orderTables = Arrays.asList(
            savedEmptyOrderTable1,
            savedEmptyOrderTable2
        );
        assertCreatedStatus(createTableGroup(orderTables));
    }

    @Test
    void 생성시_주문테이블목록중_이미단체지정된주문테이블이포함된경우_생성실패() {
        final List<OrderTable> orderTables = Arrays.asList(
            savedEmptyOrderTable1,
            savedEmptyOrderTable2
        );
        assertAll(
            () -> assertCreatedStatus(createTableGroup(orderTables)),
            () -> assertInternalServerErrorStatus(createTableGroup(orderTables))
        );
    }

    @Test
    void 삭제시_아직완료하지않은주문의_테이블이포함되어있는경우_삭제실패() {
        final TableGroup tableGroup = createTableGroup(Arrays.asList(
            savedEmptyOrderTable1,
            savedEmptyOrderTable2
        )).as(TableGroup.class);

        createOrder(orderBuilderWithSingleLineItem
            .withOrderTable(savedEmptyOrderTable1)
            .build());

        assertInternalServerErrorStatus(deleteTableGroup(tableGroup.getId()));
    }

    @Test
    void 삭제시_모든주문테이블의_주문이완료된경우_삭제성공() {
        // given : 그룹설정
        final TableGroup tableGroup = createTableGroup(Arrays.asList(
            savedEmptyOrderTable1,
            savedEmptyOrderTable2
        )).as(TableGroup.class);
        // given : 주문 생성
        final Order order1 = createOrder(orderBuilderWithSingleLineItem
            .withOrderTable(savedEmptyOrderTable1)
            .build()).as(Order.class);
        final Order order2 = createOrder(orderBuilderWithSingleLineItem
            .withOrderTable(savedEmptyOrderTable2)
            .build()).as(Order.class);
        // when
        changeOrderStatus(order1.getId(), completionStatusOrder(savedEmptyOrderTable1.getId()));
        changeOrderStatus(order2.getId(), completionStatusOrder(savedEmptyOrderTable2.getId()));
        // then
        assertNoContentStatus(deleteTableGroup(tableGroup.getId()));
    }

}
