package kitchenpos.order;


import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
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
        List<OrderTableResponse> tables = tableService.list();
        // then
        assertThat(tables).isNotNull();
    }

    @Test
    @DisplayName("테이블을 비운다.")
    void changeEmpty() {
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        given(orderTableRepository.save(any())).willReturn(orderTable);

        OrderTableResponse emptyTable = tableService.changeEmpty(orderTable.getId(), orderTable);

        assertThat(emptyTable).isNotNull();
    }

    @Test
    @DisplayName("그룹이 존재하는 테이블은 비울 수 없다.")
    void changeEmptyOfNotNullTableGroupId() {
        orderTable.group(1L);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableService.changeEmpty(orderTable.getId(), orderTable);
        }).withMessageContaining("단체로 지정된 테이블입니다");
    }

    @Test
    @DisplayName("식사중이거나 조리중인 테이블은 비울 수 없다.")
    void changeEmptyOfExistsByOrderTableIdAndOrderStatusIn() {
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(true);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableService.changeEmpty(orderTable.getId(), orderTable);
        }).withMessageContaining("테이블이 식사중이거나 조리중인경우 테이블을 비울 수 없습니다.");
    }

    @Test
    @DisplayName("방문한 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        orderTable.changeEmpty(false);
        orderTable.changeNumberOfGuests(10);
        given(orderTableRepository.save(any())).willReturn(orderTable);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        OrderTableResponse changeTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
        assertThat(changeTable).isNotNull();
    }

    @Test
    @DisplayName("방문한 손님 수를 0이하로 변경하면 실패한다.")
    void changeNumberOfGuestsToZero() {
        orderTable.changeEmpty(false);
        orderTable.changeNumberOfGuests(-5);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
        }).withMessageContaining("손님수는 0이상의 값");
    }

    @Test
    @DisplayName("빈 테이블의 방문한 손님 수를 변경하면 실패한다.")
    void changeNumberOfGuestsEmptyTable() {
        orderTable.changeEmpty(true);
        orderTable.changeNumberOfGuests(10);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
        }).withMessageContaining("비어있는 테이블은 손님수를 변경할 수 없습니다.");
    }

    public static OrderTableRequest 테이블_등록_요청(Long id, int numberOfGuests, boolean empty) {
        return new OrderTableRequest(id, numberOfGuests, empty);
    }

    public static OrderTable 테이블_등록(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

}
