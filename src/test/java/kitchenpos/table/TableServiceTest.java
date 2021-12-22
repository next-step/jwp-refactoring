package kitchenpos.table;

import kitchenpos.AcceptanceTest;
import kitchenpos.application.*;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("테이블 관련 기능")
class TableServiceTest extends AcceptanceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("테이블을 생성할 수 있다.")
    void createTable() {
        // given
        final OrderTable orderTable = new OrderTable();

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertAll(
                () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isZero(),
                () -> assertThat(savedOrderTable.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("테이블 목록을 조회할 수 있다.")
    void findTable() {
        // given
        tableService.create(new OrderTable());

        // when
        List<OrderTable> findByTables = tableService.list();

        // then
        assertThat(findByTables.size()).isOne();
    }

    @Test
    @DisplayName("사용여부를 변경하고자 하는 테이블이 존재하지 않으면 예외가 발생한다.")
    void isNotExistTable() {
        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableService.changeEmpty(1L, new OrderTable());
        });
    }

    @Test
    @DisplayName("사용여부를 변경하고자 하는 테이블이 단체 지정 되어 있다면 예외가 발생한다.")
    void isTableGroup() {
        // given
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(true));
        OrderTable secondOrderTable = orderTableDao.save(new OrderTable(true));
        tableGroupService.create(new TableGroup(Arrays.asList(firstOrderTable, secondOrderTable)));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableService.changeEmpty(firstOrderTable.getId(), new OrderTable(false));
        });
    }

    @Test
    @DisplayName("주문상태가 조리 또는 식사면 예외가 발생한다.")
    void orderStatusCookingOrMeal() {
        // given
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(false));
        final MenuGroup savedMenuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        final Product savedProduct = productService.create(new Product("후라이드", BigDecimal.valueOf(9000)));
        final Menu savedMenu = menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuProduct(savedProduct.getId(), 2L))));
        orderService.create(new Order(firstOrderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(new OrderLineItem(savedMenu.getId(), 2L))));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableService.changeEmpty(firstOrderTable.getId(), new OrderTable());
        });
    }


    @Test
    @DisplayName("테이블의 사용여부를 변경할 수 있다.")
    void changeEmpty() {
        // given
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(false));
        final MenuGroup savedMenuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        final Product savedProduct = productService.create(new Product("후라이드", BigDecimal.valueOf(9000)));
        final Menu savedMenu = menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuProduct(savedProduct.getId(), 2L))));
        final Order savedOrder = orderService.create(new Order(firstOrderTable.getId(), Arrays.asList(new OrderLineItem(savedMenu.getId(), 2L))));
        orderService.changeOrderStatus(savedOrder.getId(), new Order(OrderStatus.COMPLETION.name()));

        // when
        OrderTable savedOrderTable = tableService.changeEmpty(savedOrder.getId(), new OrderTable(true));

        // then
        assertThat(savedOrderTable.isEmpty()).isTrue();
    }


    @Test
    @DisplayName("방문한 손님 수가 0명 미만일 시 예외가 발생한다.")
    void numberOfGuestLessThanZero() {
        // given
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(false));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableService.changeNumberOfGuests(firstOrderTable.getId(), new OrderTable(-1));
        });
    }


    @Test
    @DisplayName("방문한 손님 수를 변경하고자 하는 테이블이 존재하지 않으면 예외가 발생한다.")
    void changeNumberOfGuestsIsNotExistTable() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableService.changeNumberOfGuests(1L, new OrderTable(0));
        });
    }

    @Test
    @DisplayName("방문한 손님 수를 변경하고자 하는 테이블이 사용불가할 경우 예외가 발생한다.")
    void changeNumberOfGuestsIsEmptyTable() {
        // given
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(true));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableService.changeNumberOfGuests(firstOrderTable.getId(), new OrderTable(0));
        });
    }


    @Test
    @DisplayName("방문한 손님 수를 변경할 수 있다.")
    void changeNumberOfGuests() {
        // given
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(false));

        // when
        OrderTable savedOrderTable = tableService.changeNumberOfGuests(firstOrderTable.getId(), new OrderTable(5));

        // then
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(5);
    }
}
