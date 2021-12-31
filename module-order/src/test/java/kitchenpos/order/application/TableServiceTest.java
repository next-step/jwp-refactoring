package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDataException;
import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DisplayName("Table 서비스 테스트")
@SpringBootTest(classes = OrderTableValidator.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderTableValidator orderTableValidator;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("테이블을 생성한다.")
    void createTableTest() {
        OrderTableRequest orderTableRequest = Mockito.mock(OrderTableRequest.class);
        OrderTable orderTable = Mockito.mock(OrderTable.class);

        Mockito.when(orderTableRequest.toEntity()).thenReturn(orderTable);
        Mockito.when(orderTableRepository.save(ArgumentMatchers.any(OrderTable.class)))
                .thenReturn(orderTable);

        tableService.create(orderTableRequest);

        Mockito.verify(orderTableRepository).save(orderTable);
    }

    @Test
    @DisplayName("Table을 조회한다.")
    void findTableTest() {
        OrderTable orderTable = Mockito.mock(OrderTable.class);
        Mockito.when(orderTable.getId()).thenReturn(1L);
        Mockito.when(orderTable.getNumberOfGuests()).thenReturn(5);
        Mockito.when(orderTableRepository.findAll()).thenReturn(Arrays.asList(orderTable));

        List<OrderTableResponse> orderTables = tableService.findAll();
        Assertions.assertThat(orderTables.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("등록된 테이블이 없을 경우 에러처리 테스트.")
    void changeWrongMemberCountTest() {

        Mockito.when(orderTableRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(1L, -20);
        }).isInstanceOf(InputTableDataException.class)
                .hasMessageContaining(InputTableDataErrorCode.THE_TABLE_CAN_NOT_FIND.errorMessage());
    }

    @Test
    @DisplayName("그룹지정이 되어있어 상태를 변경 할 수 없습니다.")
    void changeNotTableStatusBecauseOfGroup() {
        //given
        OrderTable orderTable = Mockito.mock(OrderTable.class);
        Mockito.when(orderTable.hasTableGroup()).thenReturn(true);

        OrderLineItem orderLineItem = new OrderLineItem(1L, 3);
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(orderLineItem));

        Order order = new Order(1L, orderLineItems);

        //when
        Assertions.assertThatThrownBy(() -> {
            orderTableValidator.checkUpdateTableGroup(Arrays.asList(order), orderTable);
            orderTable.updateEmpty(true);
        }).isInstanceOf(InputTableDataException.class)
                .hasMessageContaining(InputTableDataErrorCode.THE_TABLE_HAS_GROUP.errorMessage());
    }

    @Test
    @DisplayName("주문 상태가 조리중 또는 식사중인 테이블의 상태는 변경할 수 없습니다.")
    void changeNotTableStatusBecauseOfCookingOrEating() {
        //given
        OrderTable orderTable = new OrderTable(5, false);
        OrderLineItem orderLineItem = new OrderLineItem(1L, 4);
        List<Order> orders = Arrays.asList(new Order(orderTable.getId(), new OrderLineItems(Arrays.asList(orderLineItem))));

        //when
        Assertions.assertThatThrownBy(() -> {
            orderTableValidator.checkUpdateTableGroup(orders, orderTable);
            orderTable.updateEmpty(true);
        }).isInstanceOf(InputOrderDataException.class)
                .hasMessageContaining(InputOrderDataErrorCode.THE_ORDER_IS_COOKING_OR_IS_EATING.errorMessage());
    }

    @Test
    @DisplayName("빈 주문 테이븡의 손님 수는 변경 할 수 없습니다.")
    void changeMemnberCountEmptyTableTest() {
        //given
        OrderTable orderTable = new OrderTable(5, true);

        //when
        Assertions.assertThatThrownBy(() -> {
            orderTableValidator.checkTableEmpty(orderTable);
            orderTable.seatNumberOfGuests(10);

        }).isInstanceOf(InputTableDataException.class)
                .hasMessageContaining(InputTableDataErrorCode.THE_STATUS_IS_ALEADY_EMPTY.errorMessage());
    }
}
