package kitchenpos.order.application;

import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        주문테이블 = createOrderTable(1L, null, 2, false);
    }

    @DisplayName("주문테이블 생성 테스트")
    @Test
    void create() {
        given(orderTableDao.save(주문테이블)).willReturn(주문테이블);
        OrderTable orderTable = tableService.create(주문테이블);
        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문테이블 목록 조회 테스트")
    @Test
    void list() {
        given(orderTableDao.findAll()).willReturn(Lists.newArrayList(주문테이블));
        List<OrderTable> orderTables = tableService.list();
        assertThat(orderTables).containsExactlyElementsOf(Lists.newArrayList(주문테이블));
    }

    @DisplayName("주문테이블 비어있는지 여부 변경 테스트")
    @Test
    void changeEmpty() {
        OrderTable orderTable = createOrderTable(1L, null, 2, true);
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));
        given(orderRepository.existsByOrderTableAndOrderStatusIn(주문테이블,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(false);
        given(orderTableDao.save(주문테이블)).willReturn(주문테이블);
        OrderTable changedOrderTable = tableService.changeEmpty(주문테이블.getId(), orderTable);
        assertAll(
                () -> assertThat(changedOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(changedOrderTable.isEmpty()).isTrue()
        );
    }

    @DisplayName("주문테이블이 등록이 안되어있을 때 주문테이블 비어있는지 여부 변경 테스트")
    @Test
    void changeEmptyWithNotFoundOrderTable() {
        OrderTable orderTable = createOrderTable(1L, null, 2, true);
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), orderTable));
    }

    @DisplayName("주문테이블의 단체지정이 있는 경우 주문테이블 비어있는지 여부 변경 테스트")
    @Test
    void changeEmptyWithTableGroup() {
        주문테이블.setTableGroupId(1L);
        OrderTable orderTable = createOrderTable(1L, null, 2, true);
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), orderTable));
    }

    @DisplayName("주문상태가 조리, 식사인 것이 있는 주문테이블의 비어있는지 여부 변경 테스트")
    @Test
    void changeEmptyWithCookingOrMeal() {
        OrderTable orderTable = createOrderTable(1L, null, 2, true);
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));
        given(orderRepository.existsByOrderTableAndOrderStatusIn(주문테이블,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(true);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), orderTable));
    }

    @DisplayName("주문테이블 손님 수 변경 테스트")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = createOrderTable(1L, null, 4, false);
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));
        given(orderTableDao.save(주문테이블)).willReturn(주문테이블);
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(주문테이블.getId(), orderTable);
        assertAll(
                () -> assertThat(changedOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(4),
                () -> assertThat(changedOrderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문테이블이 손님 수가 0보다 적은 경우 손님 수 변경 테스트")
    @Test
    void changeNumberOfGuestsWithGuestUnderZero() {
        주문테이블 = createOrderTable(1L, null, 2, true);
        OrderTable orderTable = createOrderTable(1L, null, -1, true);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), orderTable));
    }

    @DisplayName("주문테이블이 등록이 안되어있을 때 손님 수 변경 테스트")
    @Test
    void changeNumberOfGuestsWithNotFoundOrderTable() {
        OrderTable orderTable = createOrderTable(1L, null, 4, false);
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), orderTable));
    }

    @DisplayName("주문테이블이 비어있는 경우 손님 수 변경 테스트")
    @Test
    void changeNumberOfGuestsWithEmpty() {
        주문테이블 = createOrderTable(1L, null, 2, true);
        OrderTable orderTable = createOrderTable(1L, null, 4, true);
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), orderTable));
    }
}
