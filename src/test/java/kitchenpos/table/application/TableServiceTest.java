package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 관련")
@SpringBootTest
class TableServiceTest {
    @Autowired
    TableService tableService;
    @MockBean
    OrderTableRepository orderTableRepository;
    @MockBean
    TableValidator orderTableValidator;

    Long orderTableId;
    OrderTable orderTable;

    @BeforeEach
    void setUp() {
        setOrderTable();
    }

    void setOrderTable() {
        orderTableId = 1L;
        orderTable = new OrderTable(0, false);
        when(orderTableValidator.findExistsOrderTableById(orderTableId)).thenReturn(orderTable);
    }

    @DisplayName("개별 주문 테이블을 생성할 수 있다")
    @Test
    void create() {
        // given
        OrderTable orderTable = new OrderTable(0, false);

        // when
        tableService.create(orderTable);

        // then
        verify(orderTableRepository).save(orderTable);
    }

    @DisplayName("주문 테이블의 목록을 조회할 수 있다")
    @Test
    void list() {
        // when
        tableService.list();

        // then
        verify(orderTableRepository).findAll();
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경할 수 있다")
    @Test
    void changeEmpty() {
        // when
        OrderTable changedOrderTable = tableService.changeEmpty(orderTableId, true);

        // then
        verify(orderTableValidator).checkOrderStatus(orderTableId);
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("없는 주문 테이블은 변경할 수 없다")
    @Test
    void orderTable_is_exists() {
        // given
        Long notExistsOrderTableId = 1000L;
        when(orderTableValidator.findExistsOrderTableById(notExistsOrderTableId)).thenThrow(IllegalArgumentException.class);

        // when then
        assertAll(() -> {
            assertThatThrownBy(() -> tableService.changeEmpty(notExistsOrderTableId, true))
                    .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistsOrderTableId, 2))
                    .isInstanceOf(IllegalArgumentException.class);
        });
    }

    @DisplayName("주문 테이블에 방문한 손님 수를 변경할 수 있다")
    @Test
    void changeNumberOfGuests() {
        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTableId, 2);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(2);
    }
}
