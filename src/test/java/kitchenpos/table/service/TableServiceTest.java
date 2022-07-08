package kitchenpos.table.service;

import kitchenpos.order.application.OrderTableService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.util.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @InjectMocks
    TableService tableService;

    @Mock
    private OrderTableService orderTableService;

    @Mock
    private OrderTableRepository orderTableRepository;

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @BeforeEach
    void setUp() {
        주문테이블1 = 빈_주문테이블_1_생성();
        주문테이블2 = 빈_주문테이블_2_생성();
    }

    @DisplayName("주문 테이블 등록")
    @Test
    void createTable() {
        // given
        when(orderTableRepository.save(any()))
                .thenReturn(주문테이블1);

        // when
        OrderTableResponse result = tableService.create(new OrderTableRequest(주문테이블1.getId(), 주문테이블1.getNumberOfGuests(), 주문테이블1.isEmpty()));

        // then
        assertThat(result.getId()).isEqualTo(주문테이블1.getId());
    }

    @DisplayName("주문 테이블 전체 조회")
    @Test
    void findAllTables() {
        // given
        when(orderTableRepository.findAll())
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        // when
        List<OrderTableResponse> result = tableService.list();

        // then
        assertThat(result).hasSize(2);
    }

    @DisplayName("주문 테이블 빈 테이블로 상태 변경")
    @Test
    void changeEmpty() {
        // given
        주문_테이블_생성(주문테이블1, 1L);
        when(orderTableRepository.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(주문테이블1));
        when(orderTableRepository.save(주문테이블1))
                .thenReturn(주문테이블1);

        // when
        OrderTableResponse result = tableService.changeEmpty(주문테이블1.getId());

        // then
        assertThat(result.isEmpty());
    }

    @DisplayName("주문 테이블 빈 테이블로 상태 변경 시 `조리`, `식사` 상태인 경우 변경 불가능")
    @Test
    void changeEmptyTestAndNotCompletionStatus() {
        // given
        when(orderTableRepository.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(주문테이블1));
        when(orderTableService.existsByOrderTableIdAndOrderStatusIn(주문테이블1.getId()))
                .thenReturn(true);

        // then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문테이블1.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable 주문테이블 = OrderTable.of(1L, 1, false);
        OrderTableRequest 주문테이블_요청 = new OrderTableRequest(주문테이블1.getId(), 100, false);
        when(orderTableRepository.findById(1L))
                .thenReturn(Optional.of(주문테이블));
        when(orderTableRepository.save(주문테이블))
                .thenReturn(주문테이블);

        // when
        OrderTableResponse result = tableService.changeNumberOfGuests(1L, 주문테이블_요청);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(100);
    }

    @DisplayName("방문한 손님 수 변경 시 `0` 명보다 작은 경우 변경 불가능")
    @Test
    void changeNumberOfGuestsAndGuestMin() {
        // given
        OrderTableRequest 주문테이블_요청 = new OrderTableRequest(주문테이블1.getId(), -100, false);

        // then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문테이블1.getId(), 주문테이블_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수 변경 시 미등록된 주문 테이블인 경우 변경 불가능")
    @Test
    void changeNumberOfGuestsAndNotResistTable() {
        OrderTableRequest 주문테이블_요청 = new OrderTableRequest(100L, 100, false);
        // given
        when(orderTableRepository.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(주문테이블1));

        // then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문테이블1.getId(), 주문테이블_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}