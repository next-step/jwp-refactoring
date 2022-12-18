package kitchenpos.table;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.exception.NotExistIdException;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;

    @DisplayName("[빈테이블 상태변경] 등록되지않은 주문테이블 id 는 변경할 수 없다")
    @Test
    void test1() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(1L, false))
            .isInstanceOf(NotExistIdException.class);
    }

    @DisplayName("[방문손님수 변경] 등록되지않은 주문테이블 id 는 변경할 수 없다")
    @Test
    void test2() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 10))
            .isInstanceOf(NotExistIdException.class);
    }

}
