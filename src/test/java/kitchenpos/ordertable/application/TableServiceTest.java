package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private OrderTable firstTable;
    private OrderTable secondTable;

    @BeforeEach
    void setUp() {
        firstTable = new OrderTable(1L, null, 0, true);
        secondTable = new OrderTable(2L, null, 0, true);
    }

    @Test
    void 주문_테이블을_등록할_수_있다() {
        given(tableService.create(firstTable)).willReturn(firstTable);

        OrderTable savedOrderTable = tableService.create(firstTable);

        assertAll(
                () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(savedOrderTable.isEmpty()).isTrue(),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(0)
        );
    }

    @Test
    void 주문_테이블_목록을_조회할_수_있다() {
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(firstTable, secondTable));

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(2);
        assertThat(orderTables).contains(firstTable, secondTable);
    }

    @Test
    void 주문_테이블_이용_여부를_변경할_수_있다() {
        OrderTable expected = new OrderTable(1L, null, 0, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(firstTable));
        given(orderTableRepository.save(firstTable)).willReturn(firstTable);

        OrderTable changeOrderTable = tableService.changeEmpty(1L, expected);

        assertThat(changeOrderTable.isEmpty()).isFalse();
    }

    @Test
    void 단체_테이블에_지정되어_있으면_주문_테이블을_변경할_수_없다() {
        firstTable.setTableGroupId(1L);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(firstTable));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태가_조리_또는_식사중이면_테이블_이용_여부를_변경할_수_없다() {
        List<String> orderStatus = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(firstTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, orderStatus)).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_손님_수를_변경할_수_있다() {
        OrderTable expected = new OrderTable(1L, null, 5, false);
        firstTable.setEmpty(false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(firstTable));
        given(orderTableRepository.save(firstTable)).willReturn(firstTable);

        OrderTable changeOrderTable = tableService.changeNumberOfGuests(1L, expected);

        assertThat(changeOrderTable.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
    }

    @Test
    void 주문_테이블의_손님_수를_음수로_변경할_수_없다() {
        OrderTable expected = new OrderTable(1L, null, -1, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈_테이블이면_손님_수를_변경할_수_없다() {
        firstTable.setEmpty(true);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(firstTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

