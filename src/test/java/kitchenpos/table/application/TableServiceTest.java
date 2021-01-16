package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블 비즈니스 로직을 처리하는 서비스 테스트")
@SpringBootTest
@Sql("/db/test_data.sql")
class TableServiceTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableService tableService;

    private OrderTableRequest orderTableRequest;

    @BeforeEach
    void setUp() {
        orderTableRequest = new OrderTableRequest();
        orderTableRequest.setTableGroupId(null);
        orderTableRequest.setEmpty(true);
        orderTableRequest.setNumberOfGuests(2);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void 주문_테이블_생성() {
        final OrderTableResponse savedOrderTable = tableService.create(orderTableRequest);

        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getTableGroupId()).isEqualTo(orderTableRequest.getTableGroupId());
        assertThat(savedOrderTable.isEmpty()).isEqualTo(orderTableRequest.isEmpty());
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
    }

    @DisplayName("주문 테이블을 조회한다.")
    @Test
    void 주문_테이블_조회() {
        final List<OrderTableResponse> orderTables = tableService.list();

        assertThat(orderTables.size()).isGreaterThan(0);
    }

    @DisplayName("주문 테이블의 빈 설정을 한다.")
    @Test
    void 빈_테이블_설정() {
        final OrderTableResponse savedOrderTable = tableService.create(orderTableRequest);
        final OrderTableResponse changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), orderTableRequest);

        assertThat(changedOrderTable.getId()).isEqualTo(savedOrderTable.getId());
        assertThat(changedOrderTable.getTableGroupId()).isEqualTo(savedOrderTable.getTableGroupId());
        assertThat(changedOrderTable.isEmpty()).isEqualTo(savedOrderTable.isEmpty());
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(savedOrderTable.getNumberOfGuests());
    }

    @DisplayName("등록되지 않은 주문 테이블인 경우 빈 테이블 설정을 할 수 없다.")
    @Test
    void 등록되지_않은_주문_테이블을_빈_테이블_설정() {
        assertThatThrownBy(() -> tableService.changeEmpty(Long.MAX_VALUE, orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("다른 테이블 그룹에 속한 주문 테이블인 경우 빈 테이블 설정을 할 수 없다.")
    @Test
    void 다른_테이블_그룹에_속한_주문_테이블을_빈_테이블_설정() {
        final OrderTable newOrderTable = OrderTable.of(1L, 2, false);
        final OrderTable savedOrderTable = orderTableRepository.save(newOrderTable);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태가 조리 또는 식사인 경우 빈 테이블 설정을 할 수 없다.")
    @Test
    void 주문_테이블_상태가_조리_또는_식사인_경우_빈_테이블_설정() {
        final OrderTableResponse firstSavedOrderTable = tableService.create(orderTableRequest);
        final Order firstOrder = Order.of(firstSavedOrderTable.getId());
        firstOrder.changeOrderStatus(OrderStatus.COOKING);
        orderRepository.save(firstOrder);

        final OrderTableResponse secondSavedOrderTable = tableService.create(orderTableRequest);
        final Order secondOrder = Order.of(secondSavedOrderTable.getId());
        secondOrder.changeOrderStatus(OrderStatus.MEAL);
        orderRepository.save(secondOrder);

        assertThatThrownBy(() -> tableService.changeEmpty(firstSavedOrderTable.getId(), orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> tableService.changeEmpty(secondSavedOrderTable.getId(), orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("게스트 수를 수정한다.")
    @Test
    void 게스트_수_수정() {
        orderTableRequest.setEmpty(false);
        final OrderTableResponse savedOrderTable = tableService.create(orderTableRequest);
        orderTableRequest.setNumberOfGuests(5);

        final OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableRequest);
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
    }

    @DisplayName("게스트 수가 음수인 경우 게스트 수를 변경할 수 없다.")
    @Test
    void 게스트_수_음수인_경우_게스트_수_수정() {
        orderTableRequest.setEmpty(false);
        final OrderTableResponse savedOrderTable = tableService.create(orderTableRequest);
        orderTableRequest.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 주문 테이블일 경우 게스트 수를 변경할 수 없다.")
    @Test
    void 빈_주문_테이블인_경우_게스트_수_수정() {
        final OrderTableResponse savedOrderTable = tableService.create(orderTableRequest);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
