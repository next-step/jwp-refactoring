package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.dao.OrderTableDao;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.testfixtures.TableTestFixtures;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("테이블을 등록할 수 있다")
    @Test
    void create() {
        //given
        OrderTable orderTable = new OrderTable(1L, 0, true);
        TableTestFixtures.주문테이블_저장_결과_모킹(orderTableDao, orderTable);

        //when
        OrderTableResponse savedOrderTable = tableService.create(
            TableTestFixtures.convertToOrderTableRequest(orderTable));

        //then
        Assertions.assertThat(savedOrderTable.getId()).isEqualTo(orderTable.getId());
    }

    @DisplayName("테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 0, true),
            new OrderTable(2L, 6, false));

        TableTestFixtures.주문테이블_전체_조회_모킹(orderTableDao, orderTables);

        //when
        List<OrderTableResponse> findOrderTables = tableService.list();

        //then
        assertThat(findOrderTables.size()).isEqualTo(orderTables.size());
        테이블목록_검증(findOrderTables, orderTables);
    }

    @DisplayName("테이블 상태를 변경할 수 있다")
    @Test
    void changeEmpty() {
        //given
        OrderTable orderTable = new OrderTable(1L, 0, true);
        TableTestFixtures.특정_주문테이블_조회_모킹(orderTableDao, orderTable);

        //when
        OrderTable changeOrderTable = new OrderTable(false);
        OrderTableResponse savedOrderTable = tableService.changeEmpty(orderTable.getId(),
            TableTestFixtures.convertToOrderTableRequest(changeOrderTable));

        //then
        Assertions.assertThat(savedOrderTable.isOrderClose())
            .isEqualTo(changeOrderTable.isOrderClose());
    }

    @DisplayName("테이블 방문 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable orderTable = new OrderTable(1L, 0, false);
        TableTestFixtures.특정_주문테이블_조회_모킹(orderTableDao, orderTable);

        //when
        OrderTable changeOrderTable = new OrderTable(6);
        OrderTableResponse savedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(),
            TableTestFixtures.convertToOrderTableRequest(changeOrderTable));

        //then
        Assertions.assertThat(savedOrderTable.getNumberOfGuests())
            .isEqualTo(changeOrderTable.getNumberOfGuests());
    }
    
    private void 테이블목록_검증(List<OrderTableResponse> findOrderTables, List<OrderTable> orderTables) {
        List<Long> findOrderTableIds = findOrderTables.stream()
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());
        List<Long> expectOrderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
        assertThat(findOrderTableIds).containsAll(expectOrderTableIds);
    }
}
