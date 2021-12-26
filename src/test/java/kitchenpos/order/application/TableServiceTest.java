package kitchenpos.order.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Table 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("테이블을 생성한다.")
    void createTableTest() {
        OrderTableRequest orderTableRequest = mock(OrderTableRequest.class);
        OrderTable orderTable = mock(OrderTable.class);

        when(orderTableRequest.toEntity()).thenReturn(orderTable);
        when(orderTableRepository.save(any(OrderTable.class)))
                .thenReturn(orderTable);

        tableService.create(orderTableRequest);

        verify(orderTableRepository).save(orderTable);
    }

    @Test
    @DisplayName("Table을 조회한다.")
    void findTableTest() {
        OrderTable orderTable = mock(OrderTable.class);
        when(orderTable.getId()).thenReturn(1L);
        when(orderTable.getNumberOfGuests()).thenReturn(5);
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(orderTable));

        List<OrderTableResponse> orderTables = tableService.findAll();
        assertThat(orderTables.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("등록된 테이블이 없을 경우 에러처리 테스트.")
    void changeWrongMemberCountTest(){

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(1L, -20);
        }).isInstanceOf(InputTableDataException.class)
                .hasMessageContaining(InputTableDataErrorCode.THE_TABLE_CAN_NOT_FIND.errorMessage());
    }
}
