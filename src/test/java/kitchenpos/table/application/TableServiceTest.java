package kitchenpos.table.application;

import static kitchenpos.util.TestDataSet.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("주문_테이블의 손님 수 정상 업데이트 케이스")
    void changeNumberOfGuests() {
        //given
        OrderTableRequest chagneRequest = new OrderTableRequest(10, false);
        OrderTable change = new OrderTable(테이블_1번.getId(), 10, false);
        OrderTable target = new OrderTable(테이블_1번.getId(), 5, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(target));

        //when
        OrderTableResponse result = tableService.changeNumberOfGuests(테이블_1번.getId(), chagneRequest);

        // then
        assertThat(result.getId()).isEqualTo(change.getId());
        assertThat(result.getNumberOfGuests()).isEqualTo(change.getNumberOfGuests());

        verify(orderTableRepository, times(1)).findById(any());

    }

    @Test
    @DisplayName("주문_테이블의 빈 여부 업데이트 수정 성공 케이스")
    void changeEmpty() {
        //given
        OrderTableRequest chagneRequest = new OrderTableRequest(10, false);
        OrderTable change = new OrderTable(테이블_1번.getId(), 10, false);
        OrderTable target = new OrderTable(테이블_1번.getId(), 10, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(target));

        //when
        OrderTableResponse result = tableService.changeEmpty(테이블_1번.getId(), chagneRequest);

        // then
        assertThat(result.getId()).isEqualTo(change.getId());
        assertThat(result.isEmpty()).isEqualTo(false);

        verify(orderTableRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("존재 하지 않는 테이블을 업데이트 할 수 없다.")
    void noTable() {
        //given
        OrderTableRequest chagneRequest = new OrderTableRequest(10, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableService.changeEmpty(테이블_1번.getId(), chagneRequest);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            tableService.changeNumberOfGuests(테이블_1번.getId(), chagneRequest);
        });

        verify(orderTableRepository, times(2)).findById(any());
    }

}
