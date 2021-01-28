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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("애플리케이션 테스트 보호 - 주문테이블 서비스")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    private OrderTable orderTable;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @BeforeEach
    public void setup() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(null);
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);
    }

    @DisplayName("주문테이블 생성")
    @Test
    void create() {
        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(savedOrderTable).isEqualTo(orderTable);
    }

    @DisplayName("주문테이블 목록 조회")
    @Test
    void list() {
        given(orderTableDao.findAll()).willReturn(Collections.singletonList(orderTable));

        List<OrderTable> tables = tableService.list();

        assertThat(tables).containsExactly(orderTable);
    }

    @DisplayName("주문테이블의 빈 테이블 여부를 변경한다.")
    @Test
    void changeEmpty() {
        orderTable.setEmpty(false);
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        OrderTable updatedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

        assertThat(updatedOrderTable).isEqualTo(orderTable);
    }
}
