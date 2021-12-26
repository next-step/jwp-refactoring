package kitchenpos.table;

import kitchenpos.order.domain.Order;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderTableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문 테이블 생성하기")
    @Test
    void createTable() {

        //given
        final int numberOfGuests = 7;
        final boolean empty = true;
        final OrderTable orderTable = OrderTable.create(numberOfGuests, empty);
        final OrderTableRequest orderTableRequest = new OrderTableRequest();
        ReflectionTestUtils.setField(orderTableRequest, "numberOfGuests", numberOfGuests);
        ReflectionTestUtils.setField(orderTableRequest, "empty", empty);
        when(orderTableRepository.save(any())).thenReturn(orderTable);

        //when
        OrderTableResponse savedOrderTable = tableService.create(orderTableRequest);

        //then
        assertThat(savedOrderTable).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
    
    @DisplayName("주문 테이블 조회하기")
    @Test
    void getTables() {

        //given
        final int numberOfGuestsA = 10;
        final int numberOfGuestsB = 7;
        OrderTable orderTableA = OrderTable.create(numberOfGuestsA, true);
        OrderTable orderTableB = OrderTable.create(numberOfGuestsB, true);
        List<OrderTable> orderTables = Arrays.asList(orderTableA, orderTableB);
        when(orderTableRepository.findAll()).thenReturn(orderTables);

        //when
        List<OrderTableResponse> findOrderTables = tableService.list();

        //then
        assertThat(findOrderTables).isNotEmpty();
        assertThat(findOrderTables).extracting(OrderTableResponse::getNumberOfGuests).contains(numberOfGuestsA, numberOfGuestsB);
    }

    @DisplayName("주문 테이블 비우기")
    @Test
    void emptyTable() {

        //given
        final int numberOfGuests = 10;
        final boolean empty = false;
        OrderTable originOrderTable = OrderTable.create(numberOfGuests, empty);
        originOrderTable.full();
        ReflectionTestUtils.setField(originOrderTable, "id", 1L);

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.ofNullable(originOrderTable));

        //when
        OrderTableResponse emptyOrderTable = tableService.changeEmpty(originOrderTable.getId());

        //then
        assertThat(emptyOrderTable).isNotNull();
        assertThat(emptyOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블 비울 시 table group id가 존재할 경우")
    @Test
    void emptyTableByExistedTableGroupId() {

        //given
        final int numberOfGuests = 10;
        final boolean empty = true;
        final OrderTable originOrderTable = OrderTable.create(numberOfGuests, empty);
        final TableGroup tableGroup = TableGroup.create();
        originOrderTable.full();
        originOrderTable.grouping(tableGroup);
        ReflectionTestUtils.setField(originOrderTable, "id", 1L);

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.ofNullable(originOrderTable));

        //when
        assertThatThrownBy(() -> tableService.changeEmpty(originOrderTable.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 비울 시 주문상태가 요리단계거나 식사단계가 있을경우")
    @Test
    void emptyTableByOrderStatusCookingAndMeal() {

        //given
        final int numberOfGuests = 10;
        final boolean empty = false;
        final OrderTable originOrderTable = OrderTable.create(numberOfGuests, empty);
        Order order = originOrderTable.order();
        ReflectionTestUtils.setField(originOrderTable, "id", 1L);

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.ofNullable(originOrderTable));

        //when
        assertThatThrownBy(() -> tableService.changeEmpty(originOrderTable.getId()))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("주문 테이블 손님 수 조정하기")
    @Test
    void changeGuestNumberOfTable() {

        //given
        final int numberOfGuests = 10;
        final long orderTableId = 1L;
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        ReflectionTestUtils.setField(orderTableRequest, "numberOfGuests", numberOfGuests);
        ReflectionTestUtils.setField(orderTableRequest, "empty", false);

        OrderTable orderTable = orderTableRequest.toEntity();
        ReflectionTestUtils.setField(orderTable, "id", orderTableId);

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.ofNullable(orderTable));

        //when
        OrderTableResponse changeOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest);

        //then
        assertThat(changeOrderTable).isNotNull();
        assertThat(changeOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @DisplayName("주문 테이블 손님 수 조정할 때 조정하려는 손님 수가 음수일경우")
    @Test
    void changeGuestNumberOfTableByNumberNegative() {

        //given
        final int numberOfGuests = -1;
        final long orderTableId = 1L;
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        ReflectionTestUtils.setField(orderTableRequest, "numberOfGuests", numberOfGuests);
        ReflectionTestUtils.setField(orderTableRequest, "empty", false);

        OrderTable orderTable = OrderTable.create(10, false);
        ReflectionTestUtils.setField(orderTable, "id", orderTableId);

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.ofNullable(orderTable));

        //when
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 손님 수 조정할 때 테이블이 비어있을경우")
    @Test
    void changeGuestNumberOfTableByEmptyTable() {

        //given
        final int numberOfGuests = 10;
        final long orderTableId = 1L;
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        ReflectionTestUtils.setField(orderTableRequest, "numberOfGuests", numberOfGuests);
        ReflectionTestUtils.setField(orderTableRequest, "empty", true);

        OrderTable orderTable = orderTableRequest.toEntity();
        ReflectionTestUtils.setField(orderTable, "id", orderTableId);

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.ofNullable(orderTable));

        //when
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
