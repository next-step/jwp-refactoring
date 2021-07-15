package kitchenpos.tablegroup.domain;

import static kitchenpos.ordertable.domain.OrderTableTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.generic.exception.IllegalOperationException;
import kitchenpos.generic.exception.NotEnoughTablesException;
import kitchenpos.generic.exception.OrderNotCompletedException;
import kitchenpos.generic.exception.TableGroupNotFoundException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {

    @InjectMocks
    TableGroupValidator validator;

    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderTableRepository orderTableRepository;
    @Mock
    TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("그룹화 실패 - 목록이 비어있거나 목록이 2보다 작음")
    void create_failed1() {
        // given
        when(orderTableRepository.findById(any()))
            .thenReturn(Optional.of(orderTable(1L, null, 6, false)));

        List<Long> orderTableIds = Collections.singletonList(1L);

        // then
        assertThatThrownBy(() -> validator.validateGrouping(orderTableIds))
            .isInstanceOf(NotEnoughTablesException.class);
    }

    @Test
    @DisplayName("그룹화 실패 - 테이블이 비어있지 않거나 이미 그룹화 된 테이블")
    void create_failed2() {
        // given
        OrderTable 테이블3 = orderTable(3L, null, 0, true);
        OrderTable 테이블9_사용중 = orderTable(9L, 1L, 4, false);
        when(orderTableRepository.findById(3L)).thenReturn(Optional.of(테이블3));
        when(orderTableRepository.findById(9L)).thenReturn(Optional.of(테이블9_사용중));

        List<Long> orderTableIds = Arrays.asList(3L, 9L);
        List<Long> reversedOrderTableIds = Arrays.asList(9L, 3L);

        // then
        assertThatThrownBy(() -> validator.validateGrouping(orderTableIds))
            .isInstanceOf(IllegalOperationException.class);
        assertThatThrownBy(() -> validator.validateGrouping(reversedOrderTableIds))
            .isInstanceOf(IllegalOperationException.class);
    }

    @Test
    @DisplayName("그룹 해제 실패 - 테이블 그룹이 존재하지 않음")
    void ungroup_failed1() {
        // given
        TableGroup dummy = new TableGroup();
        when(tableGroupRepository.existsById(any())).thenReturn(false);

        // then
        assertThatThrownBy(() -> validator.validateUngrouping(dummy))
            .isInstanceOf(TableGroupNotFoundException.class);
    }

    @Test
    @DisplayName("그룹 해제 실패 - 조리/식사중인 테이블이 존재")
    void ungroup_failed2() {
        // given
        TableGroup dummy = new TableGroup();
        when(tableGroupRepository.existsById(any())).thenReturn(true);
        when(orderRepository.existsAllByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);

        // then
        assertThatThrownBy(() -> validator.validateUngrouping(dummy))
            .isInstanceOf(OrderNotCompletedException.class);
    }
}
