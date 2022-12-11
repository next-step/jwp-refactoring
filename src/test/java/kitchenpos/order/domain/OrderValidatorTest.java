package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    @DisplayName("메뉴는 등록되어 있어야 한다.")
    @Test
    void validateMenu() {
        Order order = new Order(1L);
        order.addLineItem(new OrderLineItem(1000L, 10));
        given(menuRepository.countByIdIn(Arrays.asList(1000L))).willReturn(0);

        assertThatThrownBy(() -> orderValidator.validateMenu(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 메뉴가 포함되어 있습니다.");
    }

    @DisplayName("주문 테이블은 등록되어 있어야 한다.")
    @Test
    void validateTable() {
        Order order = new Order(1000L);
        given(orderTableRepository.findById(1000L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderValidator.validateTable(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테이블입니다.");
    }

    @DisplayName("주문 테이블은 이용중이어야 한다.")
    @Test
    void validateTableEmpty() {
        Order order = new Order(1L);
        OrderTable orderTable = new OrderTable(0, true);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderValidator.validateTable(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이용중이지 않은 테이블에서는 주문 할 수 없습니다.");
    }
}
