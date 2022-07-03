package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private TableService tableService;

    @BeforeEach
    public void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("단체ID 가 null 이 아닌 테이블을 빈 테이블로 변경할 경우 오류가 발생한다.")
    @Test
    void changeEmptyWithUnavailableTableGroupId() {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);

        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님의 수를 0보다 작은 숫자로 변경할 경우 오류가 발생한다")
    @Test
    void changeEmptyWithCookingStatusTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
