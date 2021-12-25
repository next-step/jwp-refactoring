package kitchenpos.application;


import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
public class TableTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = 테이블_등록(1L, true);
    }

    public static OrderTable 테이블_등록(Long id, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    @Test
    @DisplayName("테이블을 등록한다.")
    void createTable() {
        // when
        given(orderTableDao.save(any())).willReturn(orderTable);

        // then
        assertThat(tableService.create(orderTable)).isNotNull();
    }

    @Test
    @DisplayName("테이블 목록을 조회한다.")
    void getTable() {
        // given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(orderTable));
        // when
        List<OrderTable> tables = tableService.list();
        // then
        assertThat(tables).isNotNull();
    }

    @Test
    @DisplayName("테이블을 비운다.")
    void changeEmpty() {
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        given(orderTableDao.save(any())).willReturn(orderTable);

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
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableService.changeEmpty(orderTable.getId(), orderTable);
        });
    }

    @Test
    @DisplayName("방문한 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(10);
        given(orderTableDao.save(any())).willReturn(orderTable);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

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
}
