package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.factory.fixture.OrderTableFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        OrderTable orderTable = createOrderTable(1L, true, null);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);
        OrderTable result = tableService.create(createOrderTable(true, 1L));

        assertThat(result.getId()).isNotNull();
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void list() {
        List<OrderTable> orderTables = Arrays.asList(new OrderTable(), new OrderTable());
        given(orderTableDao.findAll()).willReturn(orderTables);

        assertThat(tableService.list()).hasSize(2);
    }

    @DisplayName("테이블을 비운다.")
    @Test
    void changeEmpty() {
        Long orderTableId = 1L;
        OrderTable request = createOrderTable(false);
        OrderTable orderTable = createOrderTable(1L ,true);

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(request));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .willReturn(Boolean.FALSE);
        given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(orderTable);

        OrderTable result = tableService.changeEmpty(orderTableId, request);

        assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블이 존재하지 않는다면, 테이블을 비운다.")
    @Test
    void changeEmpty_notExisted_orderTable() {
        Long orderTableId = 1L;
        OrderTable request = createOrderTable(false);

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹이 이미 존재한다면, 테이블을 비운다.")
    @Test
    void changeEmpty_nonNull_tableGroupId() {
        Long orderTableId = 1L;
        OrderTable request = createOrderTable(false, 1L);

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(request));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인원을 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        Long orderTableId = 1L;
        OrderTable orderTable = createOrderTable(0);

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        OrderTable result = tableService.changeNumberOfGuests(orderTableId, createOrderTable( 5));

        assertThat(result.getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("인원을 1명 미만으로 변경요청 하면  변경할 수 없다.")
    @Test
    void changeNumberOfGuests_invalid_numberOfGuests() {
        Long orderTableId = 1L;

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, createOrderTable( 0)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않는다면, 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_notExisted_orderTable() {
        Long orderTableId = 1L;
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, createOrderTable( 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있다면, 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_empty_orderTable() {
        Long orderTableId = 1L;
        OrderTable orderTable = createOrderTable(true, 0);

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, createOrderTable( 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
