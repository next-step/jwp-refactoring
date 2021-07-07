package kitchenpos.application;

import static kitchenpos.domain.OrderTableTest.*;
import static kitchenpos.domain.TableGroupTest.*;
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

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.exception.OrderTableNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("테이블 서비스")
class TableServiceTest {

    @InjectMocks
    TableService tableService;

    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;

    @Mock
    OrderTableRepository orderTableRepository;

    OrderTable 손님이_앉은_테이블;

    @BeforeEach
    void setUp() {
        손님이_앉은_테이블 = new OrderTable(50L, 8, false);
    }

    @Test
    @DisplayName("테이블을 생성한다")
    void create() {
        // given
        OrderTableRequest 새로운_테이블_요청 = new OrderTableRequest(100L, 0, true);
        OrderTable 새로운_테이블 = new OrderTable(100L, 0, true);
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
        List<OrderTable> orderTables = Arrays.asList(테이블1, 테이블3, 테이블5);
        when(orderTableRepository.findAll()).thenReturn(orderTables);

        // when
        List<OrderTable> allOrderTables = tableService.list();

        // then
        assertThat(allOrderTables.size()).isEqualTo(orderTables.size());
        assertThat(allOrderTables).containsExactly(테이블1, 테이블3, 테이블5);
    }

    @Test
    @DisplayName("특정 테이블의 테이블 상태를 변경한다")
    void changeEmpty() {
        // given
        OrderTableRequest 비우는_상태 = new OrderTableRequest(10, true);
        when(orderTableRepository.findById(손님이_앉은_테이블.getId()))
            .thenReturn(Optional.ofNullable(손님이_앉은_테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
            .thenReturn(false);

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
        OrderTable 그룹에_포함된_테이블 = new OrderTable(20L, 그룹1, 10, false); // TODO TableGroup 변경 필요
        OrderTableRequest 비우는_상태 = new OrderTableRequest(10, true);
        when(orderTableRepository.findById(그룹에_포함된_테이블.getId())).thenReturn(Optional.ofNullable(그룹에_포함된_테이블));

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(그룹에_포함된_테이블.getId(), 비우는_상태))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 상태 변경 실패(테이블이 조리/식사 상태)")
    void changeEmpty_failed3() {
        // given
        OrderTable 식사중인_테이블 = new OrderTable(20L, 10, false);
        OrderTableRequest 비우는_상태 = new OrderTableRequest(10, true);
        when(orderTableRepository.findById(식사중인_테이블.getId()))
            .thenReturn(Optional.ofNullable(식사중인_테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
            .thenReturn(true);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(식사중인_테이블.getId(), 비우는_상태))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 테이블의 손님 수를 변경한다")
    void changeNumberOfGuests() {
        // given
        OrderTable 바꿀_테이블 = new OrderTable(200L, 4, false);
        OrderTableRequest 손님10명 = new OrderTableRequest(10, false);
        when(orderTableRepository.findById(바꿀_테이블.getId())).thenReturn(Optional.ofNullable(바꿀_테이블));

        // when
        OrderTable changedTable = tableService.changeNumberOfGuests(바꿀_테이블.getId(), 손님10명);

        // then
        assertThat(changedTable.getId()).isEqualTo(200L);
        assertThat(changedTable.getNumberOfGuests()).isEqualTo(손님10명.getNumberOfGuests());
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
        when(orderTableRepository.findById(테이블1.getId())).thenReturn(Optional.ofNullable(테이블1));

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블1.getId(), 손님10명))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
