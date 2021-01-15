package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 테이블 비즈니스 로직을 처리하는 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(2);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void 주문_테이블_생성() {
        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        final OrderTable createdTable = tableService.create(orderTable);

        assertThat(createdTable.getId()).isEqualTo(orderTable.getId());
        assertThat(createdTable.getTableGroupId()).isEqualTo(orderTable.getTableGroupId());
        assertThat(createdTable.isEmpty()).isEqualTo(orderTable.isEmpty());
        assertThat(createdTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @DisplayName("주문 테이블을 조회한다.")
    @Test
    void 주문_테이블_조회() {
        given(orderTableDao.findAll()).willReturn(Collections.singletonList(orderTable));

        final List<OrderTable> orderTables = tableService.list();

        final OrderTable firstOrderTable = orderTables.get(0);
        assertThat(firstOrderTable.getId()).isEqualTo(this.orderTable.getId());
        assertThat(firstOrderTable.getTableGroupId()).isEqualTo(this.orderTable.getTableGroupId());
        assertThat(firstOrderTable.isEmpty()).isEqualTo(this.orderTable.isEmpty());
        assertThat(firstOrderTable.getNumberOfGuests()).isEqualTo(this.orderTable.getNumberOfGuests());
    }

    @DisplayName("주문 테이블의 빈 설정을 한다.")
    @Test
    void 빈_테이블_설정() {
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
            .willReturn(false);

//        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
//            .willReturn(false);


        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        final OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

        assertThat(changedOrderTable.getId()).isEqualTo(orderTable.getId());
        assertThat(changedOrderTable.getTableGroupId()).isEqualTo(orderTable.getTableGroupId());
        assertThat(changedOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty());
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @DisplayName("등록되지 않은 주문 테이블인 경우 빈 테이블 설정을 할 수 없다.")
    @Test
    void 등록되지_않은_주문_테이블을_빈_테이블_설정() {
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            final OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("다른 테이블 그룹에 속한 주문 테이블인 경우 빈 테이블 설정을 할 수 없다.")
    @Test
    void 다른_테이블_그룹에_속한_주문_테이블을_빈_테이블_설정() {
        orderTable.setTableGroupId(1L);

        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> {
            final OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태가 조리 또는 식사인 경우 빈 테이블 설정을 할 수 없다.")
    @Test
    void 주문_테이블_상태가_조리_또는_식사인_경우_빈_테이블_설정() {
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
            .willReturn(true);

//        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
//            .willReturn(true);

        assertThatThrownBy(() -> {
            final OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("게스트 수를 수정한다.")
    @Test
    void 게스트_수_수정() {
        orderTable.setEmpty(false);

        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        final OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @DisplayName("게스트 수가 음수인 경우 게스트 수를 변경할 수 없다.")
    @Test
    void 게스트_수_음수인_경우_게스트_수_수정() {
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> {
            final OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 주문 테이블일 경우 게스트 수를 변경할 수 없다.")
    @Test
    void 빈_주문_테이블인_경우_게스트_수_수정() {
        orderTable.setEmpty(false);

        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            final OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
