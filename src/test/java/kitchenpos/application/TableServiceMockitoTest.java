package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TableServiceMockitoTest {
    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderDao orderDao;


    @DisplayName("주문 테이블 비어있는지 여부 변경 예외 - 요리중이거나 식사중인 경우")
    @Test
    void validOrderStatusCookingOrMeal() {
        OrderTable expected = mock(OrderTable.class);
        when(expected.getId()).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(expected));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
                .thenReturn(true);

        assertThatThrownBy(()->{
            tableService.changeEmpty(expected.getId(), expected);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}