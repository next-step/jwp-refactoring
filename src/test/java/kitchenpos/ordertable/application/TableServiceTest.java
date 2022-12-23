package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.dto.UpdateEmptyRequest;
import kitchenpos.ordertable.dto.UpdateNumberOfGuestsRequest;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
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
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        firstTable = new OrderTable(new NumberOfGuests(0), true);
        secondTable = new OrderTable(new NumberOfGuests(0), true);
        tableGroup = TableGroup.of(1L);
    }

    @Test
    void 주문_테이블을_등록할_수_있다() {
        given(orderTableRepository.save(firstTable)).willReturn(firstTable);

        OrderTableResponse savedOrderTable = tableService.create(
                OrderTableRequest.of(firstTable.getNumberOfGuests(), firstTable.isEmpty())
        );

        assertAll(
                () -> assertThat(savedOrderTable.getId()).isEqualTo(firstTable.getId()),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(firstTable.getNumberOfGuests())
        );
    }

    @Test
    void 주문_테이블_목록을_조회할_수_있다() {
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(firstTable, secondTable));

        List<OrderTableResponse> orderTables = tableService.findAll();

        assertThat(orderTables).hasSize(2);
        assertThat(orderTables.stream().map(OrderTableResponse::getId))
                .contains(firstTable.getId(), secondTable.getId());
    }

    @Test
    void 주문_테이블_이용_여부를_변경할_수_있다() {
        OrderTable expected = new OrderTable(1L, new NumberOfGuests(1), true);
        UpdateEmptyRequest request = UpdateEmptyRequest.of(false);
        given(orderTableRepository.findById(expected.getId())).willReturn(Optional.of(firstTable));
        given(orderTableRepository.save(firstTable)).willReturn(firstTable);

        OrderTableResponse changeOrderTable = tableService.changeEmpty(expected.getId(), request);

        assertThat(changeOrderTable.isEmpty()).isFalse();
    }

    @Test
    void 단체_테이블에_지정되어_있으면_주문_빈자리_여부를_변경할_수_없다() {
        OrderTable orderTable1 = new OrderTable(1L, new NumberOfGuests(0), true);
        OrderTable orderTable2 = new OrderTable(2L, new NumberOfGuests(0), true);
        OrderTables orderTables = OrderTables.of(Arrays.asList(orderTable1, orderTable2));
        orderTables.group(tableGroup.getId());

        when(orderTableRepository.findById(orderTable1.getId())).thenReturn(Optional.of(orderTable1));
        when(orderRepository.findAllByOrderTableId(any())).thenReturn(Collections.emptyList());

        UpdateEmptyRequest request = UpdateEmptyRequest.of(orderTable1.isEmpty());
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.ALREADY_GROUP.message());
    }

    @Test
    void 주문_상태가_조리_또는_식사중이면_테이블_이용_여부를_변경할_수_없다() {
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        Order order = Order.of(orderTable.getId(), null);
        order.setOrderStatus(OrderStatus.MEAL);
        UpdateEmptyRequest request = UpdateEmptyRequest.of(firstTable.isEmpty());
        given(orderTableRepository.findById(firstTable.getId())).willReturn(Optional.of(firstTable));
        given(orderRepository.findAllByOrderTableId(orderTable.getId())).willReturn(Arrays.asList(order));

        assertThatThrownBy(() -> tableService.changeEmpty(firstTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.NOT_PAYMENT_ORDER.message());
    }

    @Test
    void 주문_테이블의_손님_수를_변경할_수_있다() {
        OrderTable expected = new OrderTable(1L, new NumberOfGuests(5), false);
        firstTable.setEmpty(false);
        UpdateNumberOfGuestsRequest request = UpdateNumberOfGuestsRequest.of(expected.getNumberOfGuests());
        given(orderTableRepository.findById(expected.getId())).willReturn(Optional.of(firstTable));
        given(orderTableRepository.save(firstTable)).willReturn(firstTable);

        OrderTableResponse changeOrderTable = tableService.changeNumberOfGuests(expected.getId(), request);

        assertThat(changeOrderTable.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
    }

    @Test
    void 주문_테이블의_손님_수를_음수로_변경할_수_없다() {
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(0), false);
        UpdateNumberOfGuestsRequest request = UpdateNumberOfGuestsRequest.of(-1);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(firstTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.GUESTS_UNDER_ZERO.message());
    }

    @Test
    void 주문_테이블이_빈_테이블이면_손님_수를_변경할_수_없다() {
        UpdateNumberOfGuestsRequest request = UpdateNumberOfGuestsRequest.of(0);
        given(orderTableRepository.findById(firstTable.getId())).willReturn(Optional.of(firstTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(firstTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.ORDER_TABLE_IS_EMPTY.message());
    }
}
