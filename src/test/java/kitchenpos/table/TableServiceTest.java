package kitchenpos.table;

import static kitchenpos.order.OrderFixture.주문항목;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.table.TableFixture.일번테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.order.OrderFixture;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.application.TableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    @Test
    void 변경할_테이블이_존재하지_않으면__변경_불가능() {
        //given
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> tableService.changeEmpty(1L, true))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블이_단체지정된_테이블이면_빈테이블로_변경_불가능() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);
        when(orderTableDao.findById(any()))
            .thenReturn(Optional.of(orderTable));

        //when
        assertThatThrownBy(() -> tableService.changeEmpty(1L, true))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블의_상태가_조리거나_식사면_빈테이블로_변경_불가능() {
        //given
        Order order = new Order(1L, 일번테이블, MEAL.name(), null,
            Collections.singletonList(주문항목));
        OrderTable orderTable = new OrderTable(1L, null, 0, false, Collections.singletonList(order));
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, true))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블의_방문한_손님_수_0미만으로_변경_불가능() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-1);

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable.getNumberOfGuests()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 방문한_손님수를_변경할_주문_테이블이_없으면_변경_불가능() {
        //given
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTable().getNumberOfGuests()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 방문한_손님수를_변경할_주문_테이블이_빈_테이블이면_변경_불가능() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

        //when
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTable().getNumberOfGuests()))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
