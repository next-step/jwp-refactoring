package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("테이블 그룹 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    private TableService tableService;
    private OrderTable orderTable;
    private Order order;
    private Menu menu;
    private OrderLineItem orderLineItem;
    private OrderLineItem orderLineItem2;
    private OrderTableRequest orderTableRequest;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderRepository, orderTableRepository);
        orderTable = new OrderTable(1L, 3);
        orderLineItem = new OrderLineItem(order, menu, 10);
        orderLineItem2 = new OrderLineItem(order, menu, 3);
        order = new Order(orderTable, Lists.newArrayList(orderLineItem, orderLineItem2));
        orderTable.addOrder(order);
        menu = new Menu();
        orderTableRequest = new OrderTableRequest(3);
    }

    @DisplayName("새로운 테이블을 등록한다.")
    @Test
    void createTableTest() {
        when(orderTableRepository.save(any())).thenReturn(new OrderTable(1L, null, 3, false));

        // when
        final OrderTableResponse createdOrderTable = tableService.create(new OrderTableRequest(3));

        // then
        assertAll(
                () -> assertThat(createdOrderTable.getId()).isNotNull(),
                () -> assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(3),
                () -> assertThat(createdOrderTable.isEmpty()).isEqualTo(false)
        );
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void getListTableTest() {
        when(orderTableRepository.findAll())
                .thenReturn(Lists.newArrayList(new OrderTable(1L, new TableGroup(), 3, false), new OrderTable(2L, new TableGroup(), 7, false)));

        // when
        final List<OrderTableResponse> createdOrderTables = tableService.list();

        // then
        assertAll(
                () -> assertThat(createdOrderTables.get(0).getId()).isEqualTo(1L),
                () -> assertThat(createdOrderTables.get(1).getId()).isEqualTo(2L)
        );
    }

    @DisplayName("빈 테이블로 변경한다.")
    @Test
    void changeEmptyTableTest() {
        when(orderTableRepository.findById(anyLong()))
                .thenReturn(Optional.of(new OrderTable(1L, null, 3, false)));
        when(orderTableRepository.save(any())).thenReturn(new OrderTable(1L, null, 3, false));
        // given
        final OrderTableResponse createdOrderTable = tableService.create(orderTableRequest);


        // when
        final OrderTableResponse changeEmptyTable = tableService.changeEmpty(createdOrderTable.getId());

        // then
        assertAll(
                () -> assertThat(changeEmptyTable.isEmpty()).isEqualTo(true)
        );
    }

    @DisplayName("주문 테이블이 반드시 존재한다.")
    @Test
    void changeEmptyTableExistTableExceptionTest() {
        // when
        assertThatThrownBy(() -> {
            final OrderTableResponse changeEmptyTable = tableService.changeEmpty(null);
            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태는 cooking이나 meal이 아니어야 한다. ")
    @Test
    void changeEmptyTableOrderStatusExceptionTest() {
        assertThatThrownBy(() -> {
            when(orderTableRepository.save(any())).thenReturn(orderTable);
            when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(orderTable));

            // given
            final OrderTableResponse createdOrderTable = tableService.create(orderTableRequest);

            // when
            final OrderTableResponse changeEmptyTable = tableService.changeEmpty(createdOrderTable.getId());

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 게스트 숫자를 변경한다. ")
    @Test
    void changeNumberOfGuestsTest() {
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(orderTable));

        // given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(7);

        // when
        OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(1L, orderTableRequest);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(7);
    }

    @DisplayName("테이블 게스트 숫자는 0 미만일 수 없다.")
    @Test
    void changeNumberOfGuestsNegativeNumberExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            final OrderTable orderTable = new OrderTable(-1);

            // when
            tableService.changeNumberOfGuests(1L, orderTableRequest);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
