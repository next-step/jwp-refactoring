package kitchenpos.application;

import static kitchenpos.domain.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableDao;
    @Mock
    private TableValidator tableValidator;
    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블 등록 API")
    @Test
    void create() {
        // given
        int numberOfGuests = 0;
        boolean empty = true;
        OrderTableRequest orderTableRequest = orderTableParam(numberOfGuests, empty);
        OrderTable savedOrderTable = savedOrderTable(1L, null, numberOfGuests, empty);
        given(orderTableDao.save(any())).willReturn(savedOrderTable);

        // when
        OrderTableResponse actual = tableService.create(orderTableRequest);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getTableGroupId()).isNull();
        assertThat(actual.getNumberOfGuests()).isEqualTo(savedOrderTable.getNumberOfGuests());
        assertThat(actual.isEmpty()).isEqualTo(savedOrderTable.isEmpty());
    }

    @DisplayName("주문 테이블 목록 조회 API")
    @Test
    void list() {
        // given
        OrderTable savedOrderTable1 = savedOrderTable(1L, 1L, 5, true);
        OrderTable savedOrderTable2 = savedOrderTable(2L, 1L, 8, true);
        given(orderTableDao.findAll()).willReturn(Arrays.asList(savedOrderTable1, savedOrderTable2));

        // when
        List<OrderTableResponse> actual = tableService.list();

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("주문 테이블 비우기 API - ID에 해당하는 주문 테이블 존재하지 않음")
    @Test
    void changeEmpty_saved_order_table_not_exists() {
        // given
        Long orderTableId = 1L;
        boolean empty = false;
        OrderTableRequest orderTableRequest = orderTableParam(empty);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 비우기 API")
    @Test
    void changeEmpty() {
        // given
        Long orderTableId = 1L;
        Long tableGroupId = null;
        boolean empty = false;
        OrderTableRequest orderTableRequest = orderTableParam(empty);
        OrderTable savedOrderTable = savedOrderTable(1L, tableGroupId, !empty);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(false);
        doNothing().when(tableValidator).validateChangeEmpty(savedOrderTable, false);

        // when
        tableService.changeEmpty(orderTableId, orderTableRequest);

        // then
        assertThat(savedOrderTable.isEmpty()).isEqualTo(empty);
    }

    @DisplayName("방문한 손님수 수정 API - 방문한 손님수 0 미만")
    @Test
    void changeNumberOfGuests_number_of_guests_less_than_0() {
        // given
        Long orderTableId = 1L;
        int numberOfGuests = -1;
        OrderTableRequest orderTableRequest = orderTableParam(numberOfGuests);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님수 수정 API - ID에 해당하는 주문 테이블 존재 하지 않음")
    @Test
    void changeNumberOfGuests_saved_order_table_not_exists() {
        // given
        Long orderTableId = 1L;
        int numberOfGuests = 4;
        OrderTableRequest orderTableRequest = orderTableParam(numberOfGuests);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님수 수정 API - ID에 해당하는 주문 테이블이 빈 테이블")
    @Test
    void changeNumberOfGuests_saved_order_table_is_empty() {
        // given
        Long orderTableId = 1L;
        int numberOfGuests = 4;
        OrderTableRequest orderTableRequest = orderTableParam(numberOfGuests);
        OrderTable savedOrderTable = savedOrderTable(orderTableId, true);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님수 수정 API")
    @Test
    void changeNumberOfGuests() {
        // given
        Long orderTableId = 1L;
        int numberOfGuests = 4;
        OrderTableRequest orderTableRequest = orderTableParam(numberOfGuests);
        OrderTable savedOrderTable = savedOrderTable(orderTableId, false);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));

        // when
        tableService.changeNumberOfGuests(orderTableId, orderTableRequest);

        // then
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
}
