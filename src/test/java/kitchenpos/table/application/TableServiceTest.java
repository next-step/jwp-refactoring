package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    public static final OrderTable 일번_테이블 = OrderTable.of(0, true);
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    TableService tableService;

    @AfterEach
    void afterEach() {
    }

    @Test
    @DisplayName("테이블 추가")
    void create() {
        // given
        given(orderTableRepository.save((any())))
                .willReturn(일번_테이블);
        // when
        OrderTableResponse orderTableResponse = tableService.create(일번_테이블);
        // then
        assertThat(orderTableResponse).isInstanceOf(OrderTableResponse.class);
    }

    @Test
    @DisplayName("테이블 전체 조회")
    void list() {
        // given
        orderTableFindAll();
        // when
        final List<OrderTableResponse> list = tableService.list();
        // then
        assertThat(list).hasSize(1);
    }

    @Test
    @DisplayName("테이블 빈 값 상태 변경")
    void changeEmpty() {
        // given
        given(orderTableRepository.findById(any()))
                .willReturn(Optional.of(일번_테이블));
        given(orderRepository.findByOrderTableId(any()))
                .willReturn(Optional.of(Order.of(일번_테이블.getId(), Arrays.asList())));
        // when
        final OrderTableResponse orderTableResponse = tableService.changeEmpty(일번_테이블.getId(), OrderTable.of(0, false));
        // then
        assertThat(orderTableResponse.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블 사람수 변경")
    void changeNumberOfGuests() {
        // given
        given(orderTableRepository.findById(any()))
                .willReturn(Optional.of(OrderTable.of(0, false)));
        // when
        final OrderTableResponse orderTable = tableService.changeNumberOfGuests(일번_테이블.getId(), OrderTable.of(5, false));
        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }

    private void orderTableFindAll() {
        given(orderTableRepository.findAll())
                .willReturn(Collections.singletonList(일번_테이블));
    }
}
