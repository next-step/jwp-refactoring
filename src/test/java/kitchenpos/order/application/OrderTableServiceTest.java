package kitchenpos.order.application;

import kitchenpos.order.applicaiton.OrderTableService;
import kitchenpos.order.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderTableServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderTableService orderTableService;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = OrderTable.of(1L, null, 4, false);
    }

    @Test
    @DisplayName("주문 테이블 생성")
    public void createOrderTable() {
        given(orderTableRepository.save(orderTable)).willReturn(orderTable);

        OrderTable createdOrderTable = orderTableService.create(orderTable);

        assertThat(createdOrderTable.getId()).isEqualTo(orderTable.getId());
    }

    @Test
    @DisplayName("주문 테이블 조회")
    public void queryOrderTable() {
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(orderTable));

        assertThat(orderTableService.list()).contains(orderTable);
    }

    @Test
    @DisplayName("주문 테이블 비울 때 주문 테이블 존재하지 않음 Exception")
    public void throwExceptionWhenOrderTableIsNotExist() {
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderTableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 비울 때 주문 테이블이 이미 단체 지정됐을 경우 Exception")
    public void throwExceptionWhenOrderTableToBeEmptyWasAlreadyGrouped() {
        TableGroup tableGroup = TableGroup.of();
        tableGroup.addOrderTable(orderTable);

        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 비울 때 주문 상태가 COOKING 또는 MEAL 이면 Exception")
    public void throwExceptionWhenChangeOrderTableToEmptyOrderStatusIsCookingOrMeal() {
        OrderTable orderTable = OrderTable.of(1L, null, 4, false);

        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableAndOrderStatusIn(orderTable,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 비움")
    public void changEmpty() {
        OrderTable emptyOrderTable = OrderTable.of(1L, null, 4, true);

        given(orderTableRepository.findById(emptyOrderTable.getId())).willReturn(Optional.of(emptyOrderTable));
        given(orderRepository.existsByOrderTableAndOrderStatusIn(emptyOrderTable,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        assertThat(orderTableService.changeEmpty(orderTable.getId(), emptyOrderTable).isEmpty()).isEqualTo(
                emptyOrderTable.isEmpty());
    }

    @Test
    @DisplayName("고객 수는 0 미만이면 Exception")
    public void throwExceptionNumberOfGuestsIsNotPositive() {
        assertThatThrownBy(() -> OrderTable.of(1L, null, -1, true)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("고객 수 변경 시 존재하지 않는 주문 테이블이면 Exception")
    public void throwExceptionWhenTryToChangeNumberOfGuestsOrderTableIsNotExist() {
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.empty());
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(1L, orderTable)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("고객 수 변경 시 비어있는 주문 테이블이면 Exception")
    public void throwExceptionWhenTryToChangeNumberOfGuestsOrderTableIsEmpty() {
        OrderTable emptyOrderTable = OrderTable.of(1L, null, 4, true);
        given(orderTableRepository.findById(emptyOrderTable.getId())).willReturn(Optional.of(emptyOrderTable));
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(1L, emptyOrderTable)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("고객 수 변경")
    public void changeNumberOfGuests() {
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        assertThat(orderTableService.changeNumberOfGuests(1L, orderTable).getNumberOfGuests())
                .isEqualTo(orderTable.getNumberOfGuests());
    }
}