package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderRepository, orderTableRepository);
    }

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void createOrderTable() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 5, false);
        final OrderTableResponse expectedOrderTableResponse = orderTable.toOrderTableResponse();
        when(orderTableRepository.save((any()))).thenReturn(orderTable);
        // when
        final OrderTableResponse actual = tableService.create(new OrderTableRequest(5, false));
        // then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(expectedOrderTableResponse.getId()),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(expectedOrderTableResponse.getNumberOfGuests()),
                () -> assertThat(actual.isEmpty()).isEqualTo(expectedOrderTableResponse.isEmpty())
        );
    }

    @Test
    @DisplayName("주문 테이블들을 조회한다.")
    void searchOrderTable() {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 5, false);
        final OrderTableResponse expectedOrderTableResponse = orderTable.toOrderTableResponse();
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(orderTable));
        // when
        final List<OrderTableResponse> actual = tableService.list();
        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getId()).isEqualTo(expectedOrderTableResponse.getId()),
                () -> assertThat(actual.get(0).getNumberOfGuests()).isEqualTo(expectedOrderTableResponse.getNumberOfGuests()),
                () -> assertThat(actual.get(0).isEmpty()).isEqualTo(expectedOrderTableResponse.isEmpty())
        );
    }

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 변경한다.")
    void changeEmptyOrderTable() {
        // given
        final OrderTable fullOrderTable = new OrderTable(1L, null, 5, false);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(fullOrderTable));
        when(orderRepository.existsOrdersV2ByOrderTableIdAndOrderStatusNot(1L, OrderStatus.COMPLETION))
                .thenReturn(false);
        // when
        final OrderTableResponse actual = tableService.changeEmpty(1L);
        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블 주문번호가 존재하지 않으면 예외 발생")
    void notExistOrderTableId() {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L));
    }

    @Test
    @DisplayName("빈 테이블로 변경할 테이블이 단체 그룹이면 예외 발생")
    void notTableGroup() {
        // given
        final TableGroup tableGroup = new TableGroup(1L, null, null);
        final OrderTable fullOrderTableGroup = new OrderTable(1L, tableGroup, 5, false);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(fullOrderTableGroup));
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L));
    }

    @Test
    @DisplayName("주문 테이블이 요리중이거나 식사중이면 예외 발생")
    void cookingAndMealOrderTable() {
        // given
        final OrderTable fullOrderTable = new OrderTable(1L, null, 5, false);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(fullOrderTable));
        when(orderRepository.existsOrdersV2ByOrderTableIdAndOrderStatusNot(1L, OrderStatus.COMPLETION))
                .thenReturn(true);
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L));
    }

    @Test
    @DisplayName("주문 테이블의 방문 고객수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable5Guests = new OrderTable(1L, null, 5, false);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable5Guests));
        // when
        final OrderTableResponse actual = tableService.changeNumberOfGuests(1L, new OrderTableRequest(3, null));
        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    @DisplayName("변경할 고객이 0명 미만이면 예외 발생")
    void negativeNumberOfGuests() {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableRequest(-1, null)));
    }

    @Test
    @DisplayName("방문 고객 변경할 테이블 주문번호가 존재하지 않으면 예외 발생")
    void notExistOrderTableIdByNumberOfGuests() {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableRequest(3, null)));
    }

    @Test
    @DisplayName("빈 테이블의 고객 수는 변경시 예외 발생")
    void notChangeEmptyOrderTable() {
        // given
        final OrderTable emptyOrderTable = new OrderTable(1L, null, 0, true);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(emptyOrderTable));
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableRequest(1, null)));
    }
}
