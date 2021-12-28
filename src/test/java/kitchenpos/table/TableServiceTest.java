package kitchenpos.table;


import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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

    private OrderTableRequest orderTableRequest;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTableRequest = 테이블_등록_요청(1L, 6, true);
        orderTable = 테이블_등록(1L, 6, true);
    }

    @Test
    @DisplayName("테이블을 등록한다.")
    void createTable() {
        given(orderTableRepository.save(any())).willReturn(orderTable);
        // then
        assertThat(tableService.create(orderTableRequest)).isNotNull();
    }


    @Test
    @DisplayName("테이블 목록을 조회한다.")
    void getTable() {
        // given
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(orderTable));
        // when
        List<OrderTable> tables = tableService.list();
        // then
        assertThat(tables).isNotNull();
    }

    @Test
    @DisplayName("테이블을 비운다.")
    void changeEmpty() {
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        given(orderTableRepository.save(any())).willReturn(orderTable);

        OrderTable emptyTable = tableService.changeEmpty(orderTable.getId(), orderTable);

        assertThat(emptyTable).isNotNull();
    }

    @Test
    @DisplayName("그룹이 존재하는 테이블은 비울 수 없다.")
    void changeEmptyOfNotNullTableGroupId() {
        orderTable.setTableGroupId(1L);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableService.changeEmpty(orderTable.getId(), orderTable);
        });
    }

    @Test
    @DisplayName("식사중이거나 조리중인 테이블은 비울 수 없다.")
    void changeEmptyOfExistsByOrderTableIdAndOrderStatusIn() {
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(true);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableService.changeEmpty(orderTable.getId(), orderTable);
        });
    }

    @Test
    @DisplayName("방문한 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(10);
        given(orderTableRepository.save(any())).willReturn(orderTable);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        OrderTable changeTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
        assertThat(changeTable).isNotNull();
    }

    @Test
    @DisplayName("방문한 손님 수를 0이하로 변경하면 실패한다.")
    void changeNumberOfGuestsToZero() {
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(-5);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
        });
    }

    @Test
    @DisplayName("빈 테이블의 방문한 손님 수를 변경하면 실패한다.")
    void changeNumberOfGuestsEmptyTable() {
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(10);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
        });
    }

    public static OrderTableRequest 테이블_등록_요청(Long id, int numberOfGuests, boolean empty) {
        return new OrderTableRequest(id, numberOfGuests, empty);
    }

    public static OrderTable 테이블_등록(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

}
