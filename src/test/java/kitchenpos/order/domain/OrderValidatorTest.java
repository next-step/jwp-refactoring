package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("주문 유효성검사 관련")
@SpringBootTest
class OrderValidatorTest {
    @Autowired
    OrderValidator orderValidator;
    @MockBean
    MenuRepository menuRepository;
    @MockBean
    OrderTableRepository orderTableRepository;

    Long orderTableId;
    OrderTable orderTable;
    Long menuId;
    Menu menu = new Menu("메뉴", 10000, 1L);

    @BeforeEach
    void setUp() {
        setOrderTable();
        setMenu();
    }

    void setOrderTable() {
        orderTableId = 1L;
        orderTable = new OrderTable(0, false);
        when(orderTableRepository.findById(orderTableId)).thenReturn(Optional.of(orderTable));
    }

    void setMenu() {
        menuId = 1L;
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
    }

    @DisplayName("주문 항목이 없으면 안된다")
    @Test
    void orderLineItem_is_not_empty() {
        // given
        Order order = new Order(orderTableId);

        // when then
        assertThatThrownBy(() -> orderValidator.checkOrderLineItems(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("없는 메뉴를 주문할 수 없다")
    @Test
    void menu_is_exists() {
        // given
        Order order = new Order(orderTableId);
        Long notExistsMenuId = 1000L;
        order.addOrderLineItem(new OrderLineItem(notExistsMenuId, 1));

        // when then
        assertThatThrownBy(() -> orderValidator.checkOrderLineItems(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 지정되어야 한다")
    @Test
    void orderTable_is_not_null() {
        // given
        Order order = new Order(null);

        // when then
        assertThatThrownBy(() -> orderValidator.checkOrderTable(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("없는 주문 테이블에 주문할 수 없다")
    @Test
    void orderTable_is_exists() {
        // given
        Long notExistsOrderTableId = 1000L;
        Order order = new Order(notExistsOrderTableId);

        // when then
        assertThatThrownBy(() -> orderValidator.checkOrderTable(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블에 주문할 수 없다")
    @Test
    void orderTable_is_not_empty() {
        // given
        Long emptyOrderTableId = 1001L;
        OrderTable emptyOrderTable = new OrderTable(0, true);
        when(orderTableRepository.findById(emptyOrderTableId)).thenReturn(Optional.of(emptyOrderTable));
        Order order = new Order(emptyOrderTableId);

        // when then
        assertThatThrownBy(() -> orderValidator.checkOrderTable(order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
