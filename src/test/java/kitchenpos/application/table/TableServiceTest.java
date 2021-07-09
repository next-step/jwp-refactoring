package kitchenpos.application.table;

import kitchenpos.domain.table.TableGroup;
import kitchenpos.exception.order.InvalidOrderStatusException;
import kitchenpos.exception.order.InvalidOrderTableException;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.order.OrderTableRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.OrderTableRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static kitchenpos.domain.table.TableGroup.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    private static final long ANY_ORDER_TABLE_ID = 1L;

    private OrderTable orderTableDummy;

    @BeforeEach
    void setUp() {
        orderTableDummy = OrderTable.of(10, false);
    }

    @Test
    @DisplayName("주문 테이블 등록시, 단체 지정(table group)은 빈 값으로 초기화되어진다.")
    void create() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(10, false);

        given(orderTableRepository.save(orderTableDummy)).willReturn(orderTableDummy);

        OrderTable savedOrderTable = tableService.create(orderTableRequest);
        assertThat(savedOrderTable.getTableGroup()).isEqualTo(EMPTY);
    }

    @Test
    @DisplayName("식사가 끝난 테이블을 빈 테이블로 만들 수 있다.")
    void changeEmptyTable() {
        orderTableDummy.changeTableGroup(TableGroup.of(
                Lists.list(OrderTable.of(10, false), OrderTable.of(10, false)
                )));
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTableDummy));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableRepository.save(orderTableDummy)).willReturn(orderTableDummy);

        OrderTable changedOrderTable = tableService.changeEmpty(ANY_ORDER_TABLE_ID);

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문의 상태가 조리이거나, 식사의 경우에는 빈 테이블로 만들 수 없다.")
    void exception_when_orderStatus_is_meal_or_cook() {
        orderTableDummy.changeTableGroup(TableGroup.of(
                Lists.list(OrderTable.of(10, false), OrderTable.of(10, false)
        )));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTableDummy));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(ANY_ORDER_TABLE_ID,
                Lists.list(OrderStatus.COOKING, OrderStatus.MEAL)))
                .willReturn(true);


        assertThatThrownBy(() -> tableService.changeEmpty(ANY_ORDER_TABLE_ID))
                .isInstanceOf(InvalidOrderStatusException.class);
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    void changeNumberOfGuestTest() {

        given(orderTableRepository.findById(ANY_ORDER_TABLE_ID)).willReturn(Optional.of(orderTableDummy));
        given(orderTableRepository.save(orderTableDummy)).willReturn(orderTableDummy);

        OrderTable savedOrderTable = tableService.changeNumberOfGuests(ANY_ORDER_TABLE_ID, 10);

        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @Test
    @DisplayName("빈 주문 테이블의 경우에는 손님의 수를 변경할 수 없다.")
    void exception2_changeNumberOfGuestTest() {

        orderTableDummy.changeEmptyTable();
        given(orderTableRepository.findById(ANY_ORDER_TABLE_ID)).willReturn(Optional.of(orderTableDummy));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(ANY_ORDER_TABLE_ID, 10))
                .isInstanceOf(InvalidOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블의 전체 목록을 조회할 수 있다.")
    void getAllOrderTable() {
        given(orderTableRepository.findAll()).willReturn(Lists.list(orderTableDummy));

        assertThat(tableService.list()).containsExactly(orderTableDummy);
    }
}