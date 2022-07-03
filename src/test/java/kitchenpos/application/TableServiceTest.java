package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블 등록")
    @Test
    public void createOrderTable() {
        //given
        OrderTable given = new OrderTable(1l,  1, true);
        when(orderTableRepository.save(any())).thenReturn(given);
        //when
        OrderTable result = tableService.create(given);
        //then
        assertThat(result).isNotNull();
    }

    @DisplayName("주문 테이블 전체 조회")
    @Test
    public void getOrderTables() {
        //given
        OrderTable given = new OrderTable(1l,  1, true);
        OrderTable given2 = new OrderTable(2l,  1, true);

        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(given, given2));
        //when
        List<OrderTable> result = tableService.list();
        //then
        assertThat(result).hasSize(2);
    }

    @DisplayName("주문 테이블의 빈 테이블 여부를 수정")
    @Test
    public void changeEmpty() {
        //given
        OrderTable given = new OrderTable(1l, 1, false);
        OrderTable notEmptyOrderTable = new OrderTable(2l,  2, true);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(given));
        when(orderTableRepository.save(any())).thenReturn(notEmptyOrderTable);

        //when
        OrderTable result = tableService.changeEmpty(1l, given);
        //then
        assertThat(result.isEmpty()).isEqualTo(notEmptyOrderTable.isEmpty());
    }

    @DisplayName("단체 지정이 되어있으면 수정 실패")
    @Test
    public void changeEmptyWithExistTableGroupId() {
        //given
        OrderTable given = new OrderTable(1l,  1, false);
        given.groupBy(new TableGroup(1l, LocalDateTime.now()));
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(given));
        //when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(1l, given)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조리, 식사 중인 경우 수정 실패")
    @Test
    public void changeEmptyWitStatus() {
        //given
        OrderTable given = new OrderTable(1l,  1, false);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(given));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);
        //when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(1l, given)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    public void changeNumberOfGuests() {
        //given
        OrderTable given = new OrderTable(1l,  1, false);
        OrderTable anotherOrderTable = new OrderTable(2l, 2, true);

        when(orderTableRepository.findById(any())).thenReturn(Optional.of(given));
        when(orderTableRepository.save(any())).thenReturn(anotherOrderTable);

        //when
        OrderTable result = tableService.changeNumberOfGuests(1l, anotherOrderTable);
        //then
        assertThat(result.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("손님의 수는 0보다 작다면 에러")
    @Test
    public void changeNumberOfGuestsWithMinus() {
        //given
        OrderTable given = new OrderTable(1l, 1, false);
        OrderTable anotherOrderTable = new OrderTable(2l, -1, true);
        //when
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1l, anotherOrderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 이미 등록되어 있지 않으면 에러")
    @Test
    public void changeNumberOfGuestsWithNoExistOrderTable() {
        //given
        OrderTable given = new OrderTable(1l, 1, false);
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1l, given)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 빈 테이블이면 에러")
    @Test
    public void changeNumberOfGuestsWithEmptyOrderTable() {
        //given
        OrderTable given = new OrderTable(1l,  1, true);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(given));
        //when
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1l, given)).isInstanceOf(IllegalArgumentException.class);
    }
}
