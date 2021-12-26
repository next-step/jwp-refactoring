package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable;

    private OrderTable orderTable2;

    private OrderTableRequest orderTableRequest;

    @BeforeEach
    void setUp() {
        orderTable = OrderTable.of(1L, null, 2, false);
        orderTable2 = OrderTable.of(2L, null, 3, false);
        orderTableRequest = new OrderTableRequest(4, true);
    }

    @DisplayName("테이블을 등록한다.")
    @Test
    void create() {
        //given
        when(orderTableRepository.save(any())).thenReturn(orderTable);

        //when
        OrderTable expected = tableService.create(orderTableRequest);

        //then
        assertThat(orderTable.getId()).isEqualTo(expected.getId());
    }

    @DisplayName("테이블을 조회한다.")
    @Test
    void list() {
        //given
        List<OrderTable> actual = Arrays.asList(orderTable, orderTable2);
        when(orderTableRepository.findAll()).thenReturn(actual);

        //when
        List<OrderTable> expected = tableService.list();

        //then
        assertThat(actual.size()).isEqualTo(expected.size());
    }

    @DisplayName("테이블을 빈테이블로 변경한다.")
    @Test
    void changeEmpty() {
        //given
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));

        //when
        OrderTable expected = tableService.changeEmpty(orderTable.getId(), orderTableRequest);

        //then
        assertThat(orderTableRequest.getEmpty()).isEqualTo(expected.isEmpty());
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));

        //when
        OrderTable expected = tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest);

        //then
        assertThat(orderTableRequest.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
    }
}
