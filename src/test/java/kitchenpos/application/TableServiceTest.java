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

import static org.assertj.core.api.Assertions.assertThat;
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
}
