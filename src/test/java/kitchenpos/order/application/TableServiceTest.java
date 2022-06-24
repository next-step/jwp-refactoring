package kitchenpos.order.application;

import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static kitchenpos.utils.DomainFixtureFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.NumberOfGuests;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroup;
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
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;

    private TableGroup 단체지정;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        주문테이블 = createOrderTable(1L, null, 2, false);
        단체지정 = createTableGroup(1L, Lists.newArrayList(주문테이블));
    }

    @DisplayName("주문테이블 생성 테스트")
    @Test
    void create() {
        given(orderTableRepository.save(주문테이블)).willReturn(주문테이블);
        OrderTable orderTable = tableService.create(주문테이블);
        assertAll(
                () -> assertThat(orderTable.tableGroup()).isNull(),
                () -> assertThat(orderTable.numberOfGuests()).isEqualTo(2),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문테이블 목록 조회 테스트")
    @Test
    void list() {
        given(orderTableRepository.findAll()).willReturn(Lists.newArrayList(주문테이블));
        List<OrderTable> orderTables = tableService.list();
        assertThat(orderTables).containsExactlyElementsOf(Lists.newArrayList(주문테이블));
    }

    @DisplayName("주문테이블 비어있는지 여부 변경 테스트")
    @Test
    void changeEmpty() {
        OrderTable orderTable = createOrderTable(1L, null, 2, true);
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.ofNullable(주문테이블));
        given(orderRepository.existsByOrderTableAndOrderStatusIn(주문테이블,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(false);
        given(orderTableRepository.save(주문테이블)).willReturn(주문테이블);
        OrderTable changedOrderTable = tableService.changeEmpty(주문테이블.id(), orderTable);
        assertAll(
                () -> assertThat(changedOrderTable.tableGroup()).isNull(),
                () -> assertThat(changedOrderTable.numberOfGuests()).isEqualTo(NumberOfGuests.of(2)),
                () -> assertThat(changedOrderTable.isEmpty()).isTrue()
        );
    }

    @DisplayName("주문테이블이 등록이 안되어있을 때 주문테이블 비어있는지 여부 변경 테스트")
    @Test
    void changeEmptyWithNotFoundOrderTable() {
        OrderTable orderTable = createOrderTable(1L, null, 2, true);
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(주문테이블.id(), orderTable));
    }

    @DisplayName("주문테이블의 단체지정이 있는 경우 주문테이블 비어있는지 여부 변경 테스트")
    @Test
    void changeEmptyWithTableGroup() {
        주문테이블.setTableGroup(단체지정);
        OrderTable orderTable = createOrderTable(1L, null, 2, true);
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.ofNullable(주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(주문테이블.id(), orderTable));
    }

    @DisplayName("주문상태가 조리, 식사인 것이 있는 주문테이블의 비어있는지 여부 변경 테스트")
    @Test
    void changeEmptyWithCookingOrMeal() {
        OrderTable orderTable = createOrderTable(1L, null, 2, true);
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.ofNullable(주문테이블));
        given(orderRepository.existsByOrderTableAndOrderStatusIn(주문테이블,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(true);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(주문테이블.id(), orderTable));
    }

    @DisplayName("주문테이블 손님 수 변경 테스트")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = createOrderTable(1L, null, 4, false);
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.ofNullable(주문테이블));
        given(orderTableRepository.save(주문테이블)).willReturn(주문테이블);
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(주문테이블.id(), orderTable);
        assertAll(
                () -> assertThat(changedOrderTable.tableGroup()).isNull(),
                () -> assertThat(changedOrderTable.numberOfGuests()).isEqualTo(4),
                () -> assertThat(changedOrderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문테이블이 등록이 안되어있을 때 손님 수 변경 테스트")
    @Test
    void changeNumberOfGuestsWithNotFoundOrderTable() {
        OrderTable orderTable = createOrderTable(1L, null, 4, false);
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.id(), orderTable));
    }

    @DisplayName("주문테이블이 비어있는 경우 손님 수 변경 테스트")
    @Test
    void changeNumberOfGuestsWithEmpty() {
        주문테이블 = createOrderTable(1L, null, 2, true);
        OrderTable orderTable = createOrderTable(1L, null, 4, true);
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.ofNullable(주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.id(), orderTable));
    }
}
