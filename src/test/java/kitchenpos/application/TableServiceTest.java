package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setup() {
        this.tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void createOrderTableTest() {
        // given
        OrderTable orderTableRequest = new OrderTable();
        OrderTable saved = new OrderTable();
        saved.setId(1L);
        given(orderTableDao.save(orderTableRequest)).willReturn(saved);

        // when
        OrderTable orderTable = tableService.create(orderTableRequest);

        // then
        assertThat(orderTable.getId()).isNotNull();
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void orderTableListTest() {
        // given
        int expectedSize = 2;
        given(orderTableDao.findAll()).willReturn(Arrays.asList(new OrderTable(), new OrderTable()));

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).hasSize(expectedSize);
    }

    @DisplayName("존재하지 않는 주문 테이블의 비움 상태를 바꿀 수 없다.")
    @Test
    void changeEmptyFAilWithNotExistOrderTableTest() {
        // given
        Long targetId = 1L;
        OrderTable emptyRequest = new OrderTable();
        emptyRequest.setEmpty(false);
        given(orderTableDao.findById(targetId)).willThrow(new IllegalArgumentException());

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(targetId, emptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
