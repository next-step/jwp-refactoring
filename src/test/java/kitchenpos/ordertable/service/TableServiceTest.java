package kitchenpos.ordertable.service;

import static kitchenpos.ordertable.domain.OrderTableTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.generic.guests.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTableTest;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupTest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.generic.exception.IllegalOperationException;
import kitchenpos.generic.exception.OrderTableNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("테이블 서비스")
class TableServiceTest {

    @InjectMocks
    TableService tableService;

    @Mock
    OrderTableRepository orderTableRepository;

    OrderTable 손님이_앉은_테이블;

    @BeforeEach
    void setUp() {
        손님이_앉은_테이블 = OrderTableTest.orderTable(50L, NumberOfGuests.of(8), false);
    }

    @Test
    @DisplayName("테이블을 생성한다")
    void create() {
        // given
        OrderTableRequest 새로운_테이블_요청 = new OrderTableRequest(100L, 0, true);
        OrderTable 새로운_테이블 = OrderTableTest.orderTable(100L, NumberOfGuests.of(0), true);
        when(orderTableRepository.save(any())).thenReturn(새로운_테이블);

        // when
        OrderTable savedTable = tableService.create(새로운_테이블_요청);

        // then
        assertThat(savedTable.getId()).isEqualTo(새로운_테이블_요청.getId());
    }

    @Test
    @DisplayName("테이블 목록을 가져온다")
    void list() {
        // given
        List<OrderTable> orderTables = Arrays.asList(테이블1, 테이블2, 테이블3);
        when(orderTableRepository.findAll()).thenReturn(orderTables);

        // when
        List<OrderTable> allOrderTables = tableService.list();

        // then
        assertThat(allOrderTables.size()).isEqualTo(orderTables.size());
        assertThat(allOrderTables).containsExactly(테이블1, 테이블2, 테이블3);
    }

    @Test
    @DisplayName("특정 테이블의 테이블 상태를 변경한다")
    void changeEmpty() {
        // given
        OrderTableRequest 비우는_상태 = new OrderTableRequest(10, true);
        when(orderTableRepository.findById(손님이_앉은_테이블.getId()))
            .thenReturn(Optional.ofNullable(손님이_앉은_테이블));

        // when
        OrderTable changedTable = tableService.changeEmpty(손님이_앉은_테이블.getId(), 비우는_상태);

        // then
        assertThat(changedTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블 상태 변경 실패(주문 테이블 없음)")
    void changeEmpty_failed1() {
        // given
        OrderTableRequest 비우는_상태 = new OrderTableRequest(10, true);
        when(orderTableRepository.findById(손님이_앉은_테이블.getId())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(손님이_앉은_테이블.getId(), 비우는_상태))
            .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    @DisplayName("테이블 상태 변경 실패(테이블이 그룹에 포함되어 있음)")
    void changeEmpty_failed2() {
        // given
        OrderTable 테이블A = OrderTableTest.orderTable(1L, NumberOfGuests.of(0), true);
        OrderTable 테이블B = OrderTableTest.orderTable(2L, NumberOfGuests.of(0), true);
        TableGroup 그룹 = TableGroupTest.tableGroup(1L, OrderTables.of(테이블A, 테이블B));
        OrderTableRequest 비우는_상태 = new OrderTableRequest(10, true);
        when(orderTableRepository.findById(테이블A.getId())).thenReturn(Optional.ofNullable(테이블A));

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(테이블A.getId(), 비우는_상태))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 테이블의 손님 수를 변경한다")
    void changeNumberOfGuests() {
        // given
        OrderTable 바꿀_테이블 = OrderTableTest.orderTable(200L, NumberOfGuests.of(4), false);
        OrderTableRequest 손님10명 = new OrderTableRequest(10, false);
        when(orderTableRepository.findById(바꿀_테이블.getId())).thenReturn(Optional.ofNullable(바꿀_테이블));

        // when
        OrderTable changedTable = tableService.changeNumberOfGuests(바꿀_테이블.getId(), 손님10명);

        // then
        assertThat(changedTable.getId()).isEqualTo(200L);
        assertThat(changedTable.getNumberOfGuests().value()).isEqualTo(손님10명.getNumberOfGuests());
    }

    @Test
    @DisplayName("손님 수 변경 실패(손님 숫자가 0보다 작음)")
    void changeNumberOfGuests_failed1() {
        // given
        OrderTableRequest 손님_음수1명 = new OrderTableRequest(-1, false);
        when(orderTableRepository.findById(테이블9_사용중.getId())).thenReturn(Optional.ofNullable(테이블9_사용중));

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블9_사용중.getId(), 손님_음수1명))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("손님 수 변경 실패(주문 테이블 없음)")
    void changeNumberOfGuests_failed2() {
        // given
        OrderTableRequest 손님10명 = new OrderTableRequest(10, false);
        when(orderTableRepository.findById(테이블9_사용중.getId())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블9_사용중.getId(), 손님10명))
            .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    @DisplayName("손님 수 변경 실패(테이블이 비어있음)")
    void changeNumberOfGuests_failed3() {
        // given
        OrderTableRequest 손님10명 = new OrderTableRequest(10, false);
        when(orderTableRepository.findById(테이블3.getId())).thenReturn(Optional.ofNullable(테이블3));

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블3.getId(), 손님10명))
            .isInstanceOf(IllegalOperationException.class);
    }
}
