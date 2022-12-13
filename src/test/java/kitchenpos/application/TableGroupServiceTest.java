package kitchenpos.application;

import static java.util.Arrays.*;
import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체지정하려는 주문테이블 목록이 비어있거나 2개 미만인 경우 단체지정할 수 없다")
    @Test
    void group1() {
        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(Collections.emptyList())))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(Collections.singletonList(new OrderTable()))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정하려는 주문테이블은 등록되어있어야 한다")
    @Test
    void group2() {
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Collections.emptyList());

        assertThatThrownBy(
            () -> tableGroupService.create(new TableGroup(asList(new OrderTable(), new OrderTable()))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정하려는 주문테이블은 비어있어야 한다")
    @Test
    void group3() {
        List<OrderTable> orderTables = asList(new OrderTable(null, 10, false), new OrderTable());
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(orderTables)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정하려는 주문테이블은 이미 단체지정 되어있지 않아야한다")
    @Test
    void group4() {
        List<OrderTable> orderTables = asList(new OrderTable(10L, 10, true), new OrderTable());
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(orderTables)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("요리중, 식사중 주문상태에선 단체지정 해제할 수 없다")
    @Test
    void group5() {
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(),
            eq(asList(COOKING.name(), MEAL.name())))).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
