package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("테이블 관련 기능")
public class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("테이블을 등록한다.")
    void createTable() {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(true, 1);
        OrderTable orderTable = 테이블_등록(1L, 4, true);
        given(orderTableRepository.save(any())).willReturn(orderTable);

        // when-then
        assertThat(tableService.create(orderTableRequest)).isNotNull();
    }

    @Test
    @DisplayName("테이블 목록을 조회한다.")
    void getTable() {
        // given
        OrderTable orderTable = 테이블_등록(1L, 4, true);
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(orderTable));

        // when
        List<OrderTableResponse> tables = tableService.list();

        // then
        assertThat(tables).isNotNull();
    }

    @Test
    @DisplayName("테이블을 비운다.")
    void changeEmpty() {
        // given
        OrderTable orderTable = 테이블_등록(4, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(orderTableRepository.save(any())).willReturn(orderTable);

        // when
        OrderTableResponse emptyTable = tableService.changeEmpty(orderTable.getId(), orderTable);

        // then
        assertThat(emptyTable).isNotNull();
    }

    @Test
    @DisplayName("그룹이 존재하는 테이블은 비울 수 없다.")
    void changeEmptyOfNotNullTableGroupId() {
        // given
        OrderTable orderTable = 테이블_등록(1L, 4, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        // when-then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("식사중이거나 조리중인 테이블은 비울 수 없다.")
    void changeEmptyOfExistsByOrderTableIdAndOrderStatusIn() {
        // given
        OrderTable orderTable = 테이블_등록(1L, 4, true);
        orderTable.ungroup();
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(true);

        // when-then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("방문한 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = 테이블_등록(1L, 10, false);
        given(orderTableRepository.save(any())).willReturn(orderTable);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        // when
        OrderTableResponse changeTable = tableService.changeNumberOfGuests(orderTable.getId(), OrderTable.of(5));

        // then
        assertThat(changeTable).isNotNull();
    }

    @Test
    @DisplayName("방문한 손님 수가 0이하이면 실패한다.")
    void changeNumberOfGuestsOfZero() {
        // given
        OrderTable orderTable = 테이블_등록(1L, 10, false);

        // when-then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), OrderTable.of(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블에 손님 수를 변경할수 없다.")
    void changeNumberOfGuestsOfEmptyTable() {
        // given
        OrderTable orderTable = 테이블_등록(1L, 4, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        // when-then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), OrderTable.of(10)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static OrderTable 테이블_등록(long tableGroupId, int numberOfGuests, boolean empty) {
        return OrderTable.of(tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable 테이블_등록(int numberOfGuests, boolean empty) {
        return OrderTable.of(numberOfGuests, empty);
    }
}
