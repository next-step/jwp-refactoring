package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderTableRepository orderTableRepository;


    @DisplayName("테이블을 생성한다.")
    @Test
    void createTest() {

        // given
        OrderTable orderTable = new OrderTable(4, false);

        OrderTable expectedOrderTable = mock(OrderTable.class);
        when(expectedOrderTable.getId()).thenReturn(1L);
        when(orderTableRepository.save(orderTable)).thenReturn(expectedOrderTable);

        TableService tableService = new TableService(orderRepository, orderTableRepository);

        // when
        OrderTable createdOrderTable = tableService.create(OrderTableRequest.from(orderTable));

        // then
        assertThat(createdOrderTable.getId()).isNotNull();
    }


    @DisplayName("테이블을 조회한다.")
    @Test
    void getListTest() {

        // given
        OrderTable orderTable = new OrderTable();
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(orderTable));
        TableService tableService = new TableService(orderRepository, orderTableRepository);

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).contains(orderTable);
    }

    @DisplayName("테이블을 비운다.")
    @Test
    void changeEmptyTest() {

        //given
        Long orderTableId = 1L;

        OrderTable orderTable  = mock(OrderTable.class);
        when(orderTable .getTableGroup()).thenReturn(null);

        when(orderTableRepository.findById(orderTableId)).thenReturn(Optional.of(orderTable ));
        when(orderRepository.existsByOrderTableAndOrderStatusIn(
                orderTable, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).thenReturn(false);

        OrderTable savedOrderTable = mock(OrderTable.class);
        when(savedOrderTable.isEmpty()).thenReturn(true);
        when(orderTableRepository.save(orderTable )).thenReturn(savedOrderTable);

        TableService tableService = new TableService(orderRepository, orderTableRepository);

        // when
        OrderTable changedOrderTable = tableService.changeEmpty(orderTableId, OrderTableRequest.from(orderTable));

        // then
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests(){
        // given
        Long orderTableId = 1L;
        OrderTable orderTable = mock(OrderTable.class);
        when(orderTable.getNumberOfGuests()).thenReturn(3);
        when(orderTableRepository.findById(orderTableId)).thenReturn(Optional.of(orderTable));

        OrderTable savedOrderTable = mock(OrderTable.class);
        when(savedOrderTable.getNumberOfGuests()).thenReturn(3);
        when(orderTableRepository.save(orderTable)).thenReturn(savedOrderTable);
        TableService tableService = new TableService(orderRepository, orderTableRepository);

        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTableId, OrderTableRequest.from(orderTable));

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }
}
