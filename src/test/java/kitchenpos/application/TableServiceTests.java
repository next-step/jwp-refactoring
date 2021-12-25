package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;

@DisplayName("테이블 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTests {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 주문테이블;

    @BeforeEach
    private void setup() {
        주문테이블 = new OrderTable.builder()
                .id(1L)
                .numberOfGuests(3)
                .empty(false)
                .build();

        lenient().when(orderTableDao.save(주문테이블))
                .thenReturn(주문테이블);
    }

    @Test
    public void 테이블_생성() {
        assertThat(tableService.create(주문테이블))
                .isNotNull()
                .isInstanceOf(OrderTable.class)
                .isEqualTo(주문테이블);
    }

    @Test
    public void 테이블_조회() {
        lenient().when(orderTableDao.findAll())
                .thenReturn(Arrays.asList(주문테이블));
        assertThat(tableService.list())
                .isNotNull()
                .isInstanceOf(List.class)
                .hasSize(1)
                .isEqualTo(Arrays.asList(주문테이블));
    }

    @Test
    public void 빈테이블로_변경() {
        lenient().when(orderTableDao.findById(주문테이블.getId()))
                .thenReturn(Optional.of(주문테이블));
        lenient().when(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(false);
        주문테이블.setEmpty(true);
        lenient().when(orderTableDao.save(주문테이블))
                .thenReturn(주문테이블);

        assertThat(tableService.changeEmpty(주문테이블.getId(), 주문테이블))
                .isNotNull()
                .isInstanceOf(OrderTable.class)
                .isEqualTo(주문테이블);
    }

    @Test
    public void 주문테이블_없어_빈테이블로_변경실패() {
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), 주문테이블))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("찾을 수 없는 주문테이블입니다.");
    }

    @Test
    public void 완료된_주문이_포함된_테이블일_경우_빈테이블로_변경실패() {
        lenient().when(orderTableDao.findById(주문테이블.getId()))
                .thenReturn(Optional.of(주문테이블));
        lenient().when(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), 주문테이블))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 완료된 주문입니다.");
    }

    @Test
    public void 손님수_변경() {
        주문테이블.setNumberOfGuests(5);
        lenient().when(orderTableDao.findById(주문테이블.getId()))
                .thenReturn(Optional.of(주문테이블));
        lenient().when(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(false);
        lenient().when(orderTableDao.save(주문테이블))
                .thenReturn(주문테이블);

        assertThat(tableService.changeEmpty(주문테이블.getId(), 주문테이블))
                .isNotNull()
                .isInstanceOf(OrderTable.class)
                .isEqualTo(주문테이블);
    }

    @Test
    public void 손님수가_음수일_경우_손님수_변경실패() {
        주문테이블.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 손님수는 음수가 될 수 없습니다.");
    }

    @Test
    public void 찾을_수_없는_테이블일_경우_손님수_변경실패() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("찾을 수 없는 테이블입니다.");
    }

}
