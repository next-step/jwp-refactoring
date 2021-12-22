package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 서비스 테스트")
@ExtendWith({MockitoExtension.class})
public class TableServiceTest {

    private final OrderTable emptyTable = OrderTable.of(0, true);
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("빈 테이블을 등록한다.")
    void create() {
        OrderTableRequest request = new OrderTableRequest(0, true);

        when(orderTableRepository.save(any(OrderTable.class)))
            .thenReturn(emptyTable);

        OrderTableResponse saved = tableService.create(request);

        assertAll(
            () -> assertThat(saved.getNumberOfGuests()).isEqualTo(0),
            () -> assertTrue(saved.getEmpty())
        );
    }

    @Test
    @DisplayName("테이블 목록을 조회한다.")
    void list() {
        when(orderTableRepository.findAll())
            .thenReturn(Arrays.asList(emptyTable));

        List<OrderTableResponse> orderTables = tableService.list();

        assertThat(orderTables.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("테이블의 상태를 변경할 수 있다.")
    void changeEmpty() {

        assertTrue(emptyTable.isEmpty());
        when(orderTableRepository.findById(anyLong()))
            .thenReturn(Optional.of(emptyTable));

        OrderTableResponse orderTable = tableService.changeEmpty(1L, Boolean.FALSE);

        assertFalse(orderTable.getEmpty());
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경한다.")
    void changeNumberOfGuests() {

        assertThat(emptyTable.getNumberOfGuests()).isEqualTo(0);

        emptyTable.updateEmpty(Boolean.FALSE);
        when(orderTableRepository.findById(anyLong()))
            .thenReturn(Optional.of(emptyTable));

        OrderTableResponse orderTable = tableService.changeNumberOfGuests(1L, 2);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
    }

}
