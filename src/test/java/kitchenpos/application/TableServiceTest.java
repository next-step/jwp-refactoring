package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
public class TableServiceTest {
    @Autowired
    private TableService tableService;
    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 테이블 생성")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable(null, 3, true);

        OrderTable response = tableService.create(orderTable);

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("주문 테이블 목록 조회")
    @Test
    void list() {
        OrderTable orderTable = 주문_테이블_생성(true);

        List<OrderTable> response = tableService.list();
        List<Long> ids = response.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        assertThat(ids).contains(orderTable.getId());
    }

    @DisplayName("주문 테이블 비우기")
    @Test
    void changeEmpty() {
        OrderTable orderTable1 = 주문_테이블_생성(true);
        OrderTable orderTable2 = 주문_테이블_생성(true);
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()
                , Arrays.asList(orderTable1, orderTable2)));

        OrderTable orderTable = tableService.changeEmpty(tableGroup.getId(), orderTable1);

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = 주문_테이블_생성(false);
        OrderTable requestOrderTable = new OrderTable(null, 5, true);

        OrderTable response = tableService.changeNumberOfGuests(orderTable.getId(), requestOrderTable);

        assertThat(response.getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("손님 수가 0일 수 없고 저장된 테이블에 대해서도 0일 수 없다.")
    @Test
    void changNumberOfGuestsException() {
        OrderTable orderTable1 = 주문_테이블_생성(false);
        OrderTable orderTable2 = 주문_테이블_생성(true);
        OrderTable requestOrderTable1 = new OrderTable(null, -1, true);
        OrderTable requestOrderTable2 = new OrderTable(null, -1, true);

        assertAll(
                () -> assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), requestOrderTable1))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable2.getId(), requestOrderTable2))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    private OrderTable 주문_테이블_생성(boolean empty) {
        OrderTable orderTable = new OrderTable(null, 3, empty);
        return tableService.create(orderTable);
    }
}
