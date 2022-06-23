package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableResponse;
import org.assertj.core.util.Lists;
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

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = 테이블_등록2(1L, 4, true);
    }


    @Test
    @DisplayName("테이블을 등록한다.")
    void createTable() {
        // given
        given(orderTableRepository.save(any())).willReturn(orderTable);

        // when-then
        assertThat(tableService.create(orderTable)).isNotNull();
    }

    @Test
    @DisplayName("테이블 목록을 조회한다.")
    void getTable() {
        // given
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
        orderTable.ungroup();
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
        // when-then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("식사중이거나 조리중인 테이블은 비울 수 없다.")
    void changeEmptyOfExistsByOrderTableIdAndOrderStatusIn() {
        // given
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
        orderTable.changeEmpty(false);
        orderTable.changeNumberOfGuests(10);
        given(orderTableRepository.save(any())).willReturn(orderTable);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        // when
        OrderTableResponse changeTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

        // then
        assertThat(changeTable).isNotNull();
    }

    @Test
    @DisplayName("방문한 손님 수가 0이하이면 실패한다. ")
    void changeNumberOfGuestsOfZero() {
        // given
        orderTable.changeEmpty(false);
        orderTable.changeNumberOfGuests(0);

        // when-then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블에 손님 수를 변경할수 없다.")
    void changeNumberOfGuestsOfEmptyTable() {
        // given
        orderTable.changeEmpty(true);
        orderTable.changeNumberOfGuests(0);

        // when-then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static kitchenpos.domain.OrderTable 테이블_등록(Long id, boolean empty) {
        kitchenpos.domain.OrderTable orderTable = new kitchenpos.domain.OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable 테이블_등록2(long tableGroupId, int numberOfGuests, boolean empty) {
        return OrderTable.of(tableGroupId, numberOfGuests, empty);
    }
}
