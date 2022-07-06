package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableChangeEmptyValidator;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("주문 테이블 관련")
@SpringBootTest
class TableServiceTest {
    @Autowired
    TableService tableService;
    @MockBean
    OrderTableRepository orderTableRepository;
    @MockBean
    TableChangeEmptyValidator tableChangeEmptyValidator;

    Long orderTableId;
    OrderTable orderTable;

    @BeforeEach
    void setUp() {
        setOrderTable();
    }

    void setOrderTable() {
        orderTableId = 1L;
        orderTable = new OrderTable(0, false);
        when(orderTableRepository.findById(orderTableId)).thenReturn(Optional.of(orderTable));
    }

    @DisplayName("개별 주문 테이블을 생성할 수 있다")
    @Test
    void create() {
        // given
        TableRequest given = new TableRequest(0, false);
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(given.toEntity());

        // when
        TableResponse actual = tableService.create(given);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(given.getNumberOfGuests());
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
        TableResponse changedOrderTable = tableService.changeEmpty(orderTableId, true);

        // then
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블을 변경할 수 없다")
    @Test
    void invalidChangeEmpty() {
        // given
        doThrow(IllegalArgumentException.class).when(tableChangeEmptyValidator).validate(orderTableId);

        // when then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("없는 주문 테이블은 변경할 수 없다")
    @Test
    void orderTable_is_exists() {
        // given
        Long notExistsOrderTableId = 1000L;

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
        TableResponse changedOrderTable = tableService.changeNumberOfGuests(orderTableId, 2);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(2);
    }
}
