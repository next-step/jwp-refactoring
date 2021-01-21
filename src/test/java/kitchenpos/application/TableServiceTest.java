package kitchenpos.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void create() {
        //given
        ArgumentCaptor<OrderTable> argumentCaptor = ArgumentCaptor.forClass(OrderTable.class);
        OrderTableRequest orderTableRequest = new OrderTableRequest(null, 0, true);

        given(orderTableRepository.save(any()))
                .willReturn(new OrderTable(1L, null, 0, true));

        //when
        OrderTableResponse createOrderTable = tableService.create(orderTableRequest);
        verify(orderTableRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getId()).isNull();
        assertThat(argumentCaptor.getValue().getNumberOfGuests()).isEqualTo(0);
        assertThat(argumentCaptor.getValue().isEmpty()).isEqualTo(true);

        //then
        assertThat(createOrderTable.getId()).isEqualTo(1L);
        assertThat(createOrderTable.getNumberOfGuests()).isEqualTo(0);
        assertThat(createOrderTable.isEmpty()).isEqualTo(true);
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        given(orderTableRepository.findAll())
                .willReturn(
                        Arrays.asList(
                                new OrderTable(1L, null, 0, true),
                                new OrderTable(2L, null, 0, true)
                        )
                );

        //when
        List<OrderTableResponse> orderTables = tableService.list();

        //then
        assertThat(orderTables.size()).isEqualTo(2);
        assertThat(orderTables.get(0).getId()).isEqualTo(1);
        assertThat(orderTables.get(0).getNumberOfGuests()).isEqualTo(0);
        assertThat(orderTables.get(1).getId()).isEqualTo(2);
        assertThat(orderTables.get(1).getNumberOfGuests()).isEqualTo(0);
    }

    @DisplayName("주문 테이블을 비울수 있다.")
    @Test
    void changeEmpty1() {
        //given
        OrderTable savedOrderTable = new OrderTable(2L, null, 0, true);

        given(orderTableRepository.findById(2L))
                .willReturn(
                        Optional.of(savedOrderTable)
                );

        //when
        OrderTableRequest orderTableRequest = new OrderTableRequest(2L, 0, false);
        OrderTableResponse changedOrderTable = tableService.changeEmpty(2L, orderTableRequest);

        //then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블을 비울수 있다. - 그룹 지정이 되어 있는것은 비울수 없음")
    @Test
    void changeEmpty2() {
        //given
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        given(orderTableRepository.findById(2L))
                .willReturn(
                        Optional.of(new OrderTable(2L, tableGroup, 0, true))
                );

        OrderTableRequest orderTable = new OrderTableRequest(1L, 0, false);

        //when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(2L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블을 비울수 있다. - 주문 테이블이 없거나, 이미 조리중, 식사중인 상태이면 주문 테이블을 비울 수 없다.")
    @Test
    void changeEmpty3() {
        //given
        OrderTable orderTable = new OrderTable(2L, null, 0, true);
        orderTable.addOrder(new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now()));
        orderTable.addOrder(new Order(2L, OrderStatus.COMPLETION, LocalDateTime.now()));
        orderTable.addOrder(new Order(3L, OrderStatus.MEAL, LocalDateTime.now()));

        given(orderTableRepository.findById(2L))
                .willReturn(Optional.of(orderTable));

        OrderTableRequest orderTableRequest = new OrderTableRequest(null, 0, false);

        //when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(2L, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("주문 테이블의 방문한 손님수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests1() {
        //given
        given(orderTableRepository.findById(3L))
                .willReturn(Optional.of(new OrderTable(3L, null, 1, false)));

        //when
        OrderTableRequest orderTableRequest = new OrderTableRequest(null, 3, true);
        OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(3L, orderTableRequest);

        //then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("주문 테이블의 방문한 손님수를 변경할 수 있다. - 손님수를 0 이하로 변경 할 수 없다.")
    @Test
    void changeNumberOfGuests2() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest(null, -1, true);

        //when
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(3L, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님수를 변경할 수 있다. - 현재 빈 테이블은 변경할 수 없다.")
    @Test
    void changeNumberOfGuests3() {
        //given
        given(orderTableRepository.findById(3L))
                .willReturn(Optional.of(new OrderTable(3L, null, 0, true)));

        OrderTableRequest orderTableRequest = new OrderTableRequest(null, 3, true);

        //when
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(3L, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
