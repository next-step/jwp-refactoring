package kitchenpos.table.application;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dto.ChangeNumberOfGuestsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static kitchenpos.table.application.TableService.CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("테이블 서비스")
@SpringBootTest
class TableServiceTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableService tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("손님수를 변경한다. / 0명보다 작을 수 없다.")
    @Test
    void changeNumberOfGuests_fail_minimum() {

        Long orderTableId = 1L;

        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changeNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE);
    }
}
