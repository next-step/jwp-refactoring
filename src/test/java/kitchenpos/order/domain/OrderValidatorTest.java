package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static java.util.Collections.singletonList;
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
        when(menuRepository.findAllById(singletonList(menuId))).thenReturn(singletonList(menu));
    }

    @DisplayName("주문 항목이 없으면 안된다")
    @Test
    void orderLineItem_is_not_empty() {
        // given
        OrderRequest order = new OrderRequest(orderTableId);

        // when then
        assertThatThrownBy(() -> orderValidator.checkItems(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("없는 메뉴를 주문할 수 없다")
    @Test
    void menu_is_exists() {
        // given
        OrderRequest order = new OrderRequest(orderTableId);
        Long notExistsMenuId = 1000L;
        order.setOrderLineItems(singletonList(new OrderLineItemRequest(notExistsMenuId, 1)));

        // when then
        assertThatThrownBy(() -> orderValidator.checkItems(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 지정되어야 한다")
    @Test
    void orderTable_is_not_null() {
        // given
        OrderRequest order = new OrderRequest(null);

        // when then
        assertThatThrownBy(() -> orderValidator.checkOrderTable(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("없는 주문 테이블에 주문할 수 없다")
    @Test
    void orderTable_is_exists() {
        // given
        Long notExistsOrderTableId = 1000L;
        OrderRequest order = new OrderRequest(notExistsOrderTableId);

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
        OrderRequest order = new OrderRequest(emptyOrderTableId);

        // when then
        assertThatThrownBy(() -> orderValidator.checkOrderTable(order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
