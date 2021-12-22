package kitchenpos.tablegroup;

import kitchenpos.AcceptanceTest;
import kitchenpos.application.*;
import kitchenpos.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("단체 지정 관련 기능")
class TableGroupServiceTest extends AcceptanceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("단체 지정 하고자 하는 테이블이 존재하지 않으면 예외가 발생한다.")
    void createTableGroupFailBecauseOfNotExistTable() {
        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableGroupService.create(new TableGroup(Arrays.asList(new OrderTable(1L), new OrderTable(2L))));
        });
    }


    @Test
    @DisplayName("단체 지정 하고자 하는 테이블의 상태가 사용중이라면 예외가 발생한다.")
    void createTableGroupFailBecauseOfTableNotEmpty() {
        // given
        OrderTable firstOrderTable = tableService.create(new OrderTable(true));
        OrderTable secondOrderTable = tableService.create(new OrderTable(false));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableGroupService.create(new TableGroup(Arrays.asList(new OrderTable(firstOrderTable.getId()), new OrderTable(secondOrderTable.getId()))));
        });
    }


    @Test
    @DisplayName("단체 지정 하고자 하는 테이블이 단체 지정이 되어 있다면 예외가 발생한다.")
    void createTableGroupFailBecauseOfAlreadyTableGroup() {
        // given
        OrderTable firstOrderTable = tableService.create(new OrderTable(true));
        OrderTable secondOrderTable = tableService.create(new OrderTable(true));
        tableGroupService.create(new TableGroup(Arrays.asList(firstOrderTable, secondOrderTable)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableGroupService.create(new TableGroup(Arrays.asList(firstOrderTable, secondOrderTable)));
        });
    }

    @Test
    @DisplayName("단체 지정을 할 수 있다.")
    void createTableGroup() {
        // given
        OrderTable firstOrderTable = tableService.create(new OrderTable(true));
        OrderTable secondOrderTable = tableService.create(new OrderTable(true));

        // when
        final TableGroup savedTableGroup = tableGroupService.create(new TableGroup(Arrays.asList(firstOrderTable, secondOrderTable)));

        // then
        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("단체 지정 해제시 테이블의 주문 상태가 조리 또는 식사 변경이 불가능하다.")
    void ungroupFailBecauseOfOrderStatusCookingOrMeal() {
        // given
        OrderTable firstOrderTable = tableService.create(new OrderTable(true));
        OrderTable secondOrderTable = tableService.create(new OrderTable(true));
        final TableGroup savedTableGroup = tableGroupService.create(new TableGroup(Arrays.asList(firstOrderTable, secondOrderTable)));
        final MenuGroup savedMenuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        final Product savedProduct = productService.create(new Product("후라이드", BigDecimal.valueOf(9000)));
        final Menu savedMenu = menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuProduct(savedProduct.getId(), 2L))));
        orderService.create(new Order(firstOrderTable.getId(), Arrays.asList(new OrderLineItem(savedMenu.getId(), 2L))));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableGroupService.ungroup(savedTableGroup.getId());
        });
    }


    @Test
    @DisplayName("단체 지정을 해제할 수 있다.")
    void ungroup() {
        // given
        OrderTable firstOrderTable = tableService.create(new OrderTable(true));
        OrderTable secondOrderTable = tableService.create(new OrderTable(true));
        final TableGroup savedTableGroup = tableGroupService.create(new TableGroup(Arrays.asList(firstOrderTable, secondOrderTable)));
        final MenuGroup savedMenuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        final Product savedProduct = productService.create(new Product("후라이드", BigDecimal.valueOf(9000)));
        final Menu savedMenu = menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuProduct(savedProduct.getId(), 2L))));
        final Order order = orderService.create(new Order(firstOrderTable.getId(), Arrays.asList(new OrderLineItem(savedMenu.getId(), 2L))));
        orderService.changeOrderStatus(order.getId(), new Order(OrderStatus.COMPLETION.name()));

        // when
        tableGroupService.ungroup(savedTableGroup.getId());
    }
}
