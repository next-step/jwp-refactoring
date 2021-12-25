package kitchenpos.order.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private MenuRepository menuRepository;

    private OrderValidator orderValidator;

    @BeforeEach
    void setUp() {
        orderValidator = new OrderValidator(orderTableRepository, menuRepository);
    }

    @DisplayName("주문 테이블과 메뉴가 존재해야 주문을 등록할 수 있다.")
    @Test
    void create() {
        // given
        long menuId = 1L;
        Order order = Order.of(1L, Collections.singletonList(
            OrderLineItem.of(menuId, 2)
        ));

        given(orderTableRepository.findById(order.getOrderTableId()))
            .willReturn(Optional.of(OrderTable.of(0, true)));
        given(menuRepository.findById(menuId)).willReturn(Optional.of(Menu.of(
            "양념치킨", new BigDecimal(10000), 1L)));

        // when && then
        assertDoesNotThrow(() -> orderValidator.validateCreate(order));
    }

    @DisplayName("주문 테이블이 존재하지 않거나 메뉴가 존재하지 않으면 주문을 등록할 수 없다.")
    @Test
    void createException() {
        // given
        long menuId = 1L;
        Order notExistOrderTable = Order.of(1L, Collections.singletonList(
            OrderLineItem.of(menuId, 2)
        ));
        Order notExistMenu = Order.of(2L, Collections.singletonList(
            OrderLineItem.of(menuId, 2)
        ));

        given(orderTableRepository.findById(notExistOrderTable.getOrderTableId()))
            .willReturn(Optional.empty());
        given(orderTableRepository.findById(notExistMenu.getOrderTableId()))
            .willReturn(Optional.of(OrderTable.of(0, true)));
        given(menuRepository.findById(menuId)).willReturn(Optional.empty());

        assertAll(
            () -> assertThatThrownBy(() -> orderValidator.validateCreate(notExistOrderTable))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NOT_FOUND_DATA.getMessage()),
            () -> assertThatThrownBy(() -> orderValidator.validateCreate(notExistMenu))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NOT_FOUND_DATA.getMessage())
        );
    }
}
