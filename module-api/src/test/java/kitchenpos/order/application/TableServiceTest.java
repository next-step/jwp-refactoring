package kitchenpos.order.application;

import kitchenpos.order.application.exception.TableNotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableState;
import kitchenpos.order.dto.TableRequest;
import kitchenpos.order.dto.TableResponse;
import kitchenpos.order.repository.TableRepository;
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

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("테이블 서비스")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private TableRepository tableRepository;
    @Mock
    private TableValidator tableValidator;
    @InjectMocks
    private TableService tableService;

    private OrderTable 테이블;
    private OrderTable 빈테이블;

    @BeforeEach
    void setUp() {
        테이블 = OrderTable.of(3, new TableState(false));
        빈테이블 = OrderTable.of(0, new TableState(true));
    }

    @Test
    @DisplayName("테이블을 등록한다.")
    void create() {
        TableRequest request = TableRequest.empty();

        when(tableRepository.save(any())).thenReturn(빈테이블);
        TableResponse response = tableService.create(request);

        verify(tableRepository, times(1)).save(any(OrderTable.class));
        assertThat(response).extracting("id", "numberOfGuests", "tableState")
                .containsExactly(빈테이블.getId(), 빈테이블.getGuests(), true);
    }

    @Test
    @DisplayName("테이블 목록을 조회한다.")
    void list() {
        when(tableRepository.findAll()).thenReturn(Arrays.asList(테이블, 빈테이블));

        List<TableResponse> responses = tableService.list();

        verify(tableRepository, times(1)).findAll();
        assertThat(responses).hasSize(2);
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        TableRequest request = TableRequest.from(5);

        when(tableRepository.findById(anyLong())).thenReturn(Optional.of(테이블));
        TableResponse response = tableService.changeGuests(1L, request);

        verify(tableRepository, times(1)).findById(anyLong());
        assertThat(response.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @Test
    @DisplayName("테이블을 빈 테이블로 변경한다.")
    void changeEmpty() {
        테이블.changeStatus(COMPLETION);
        when(tableRepository.findById(anyLong())).thenReturn(Optional.of(테이블));

        TableResponse response = tableService.changeEmpty(1L);

        verify(tableRepository, times(1)).findById(anyLong());
        assertThat(response.getTableState()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블이 없는 경우 예외가 발생한다.")
    void validateOrderTableEmpty() {
        TableRequest request = TableRequest.from(2);
        when(tableRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeGuests(1L, request))
                .isInstanceOf(TableNotFoundException.class);
        verify(tableRepository, times(1)).findById(anyLong());
    }
}