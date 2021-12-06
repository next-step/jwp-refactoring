package kitchenpos.application;

import kitchenpos.dao.InMemoryOrderDao;
import kitchenpos.dao.InMemoryOrderTableDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static kitchenpos.fixture.OrderFixture.주문;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * - 주문 테이블을 등록할 수 있다
 * - 주문 테이블 목록을 조회할 수 있다
 * - 주문 테이블을 빈 테이블로 변경할 수 있다
 * - 주문 테이블이 존재하지 않으면 빈 테이블로 변경할 수 없다
 * - 주문 테이블의 테이블 그룹 아이디가 올바르지 않으면 빈 테이블로 변경할 수 없다
 *     - 테이블 그룹 아이디가 존재해야 한다
 * - 주문 테이블 아이디와 주문 혹은 식사 주문 상태인 주문이 존재하면 빈 테이블 여부를 변경할 수 없다
 * - 주문 테이블의 방문한 손님 수를 변경할 수 있다
 * - 주문 테이블의 방문한 손님 수가 올바르지 않으면 방문한 손님 수를 변경할 수 없다
 *     - 방문한 손님 수는 0명 이상이어야 한다
 * - 주문 테이블이 빈 테이블 여부가 올바르지 않으면 방문한 손님 수를 변경할 수 없다
 *     - 빈 테이블이 아니여야 한다
 */
class TableServiceTest {

    private static final int 손님_수 = 0;
    private static final boolean 빈_테이블 = true;
    private static final OrderTable 주문_테이블 = 주문_테이블(손님_수, 빈_테이블);

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableService tableService;

    @BeforeEach
    void setUp() {
        orderDao = new InMemoryOrderDao();
        orderTableDao = new InMemoryOrderTableDao();
        tableService = new TableService(orderDao, orderTableDao);
    }

    @Test
    void create_주문_테이블을_등록할_수_있다() {
        OrderTable 저장된_주문_테이블 = tableService.create(주문_테이블);
        assertAll(
                () -> assertThat(저장된_주문_테이블.getTableGroupId()).isNull(),
                () -> assertThat(저장된_주문_테이블.getNumberOfGuests()).isEqualTo(손님_수),
                () -> assertThat(저장된_주문_테이블.isEmpty()).isTrue()
        );
    }

    @Test
    void list_주문_테이블_목록을_조회할_수_있다() {
        tableService.create(주문_테이블);
        List<OrderTable> orderTables = tableService.list();
        assertAll(
                () -> assertThat(orderTables.size()).isEqualTo(1),
                () -> assertThat(orderTables.get(0).getTableGroupId()).isNull(),
                () -> assertThat(orderTables.get(0).getNumberOfGuests()).isEqualTo(손님_수),
                () -> assertThat(orderTables.get(0).isEmpty()).isTrue()
        );
    }

    @Test
    void changeEmpty_주문_테이블을_빈_테이블로_변경할_수_있다() {
        OrderTable 저장된_주문_테이블 = tableService.create(주문_테이블(손님_수, false));
        orderDao.save(주문(저장된_주문_테이블, null, OrderStatus.COMPLETION));

        OrderTable 변경된_주문_테이블 = tableService.changeEmpty(저장된_주문_테이블.getId(), 주문_테이블(손님_수, true));

        assertThat(변경된_주문_테이블.isEmpty()).isTrue();
    }

    private static OrderTable 주문_테이블(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}