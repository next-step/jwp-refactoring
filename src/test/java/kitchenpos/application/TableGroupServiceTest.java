package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.table.domain.GuestNumber;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    private TableGroupService tableGroupService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);
    }

    @Test
    @DisplayName("주문 테이블을 단체로 엮는 테이블 그룹을 생성한다.")
    void createTableGroup() {
        // given
        final OrderTable orderTableOf5Guests = new OrderTable(1L, null, GuestNumber.of(5), true);
        final OrderTable orderTableOf3Guests = new OrderTable(2L, null, GuestNumber.of(3), true);
        final TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(),
                Arrays.asList(orderTableOf5Guests, orderTableOf3Guests));
        when(orderTableRepository.findAllById(any())).thenReturn(
                Arrays.asList(orderTableOf5Guests, orderTableOf3Guests));
        when(tableGroupRepository.save(any())).thenReturn(tableGroup);
        // when
        final TableGroupResponse actual = tableGroupService.create(
                new TableGroupRequest(Arrays.asList(1L, 2L)));
        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getOrderTables()).hasSize(2),
                () -> verifyEqualsOrderTables(actual.getOrderTables(),
                        Arrays.asList(orderTableOf5Guests.toOrderTableResponse(),
                                orderTableOf3Guests.toOrderTableResponse()))
        );
    }

    private void verifyEqualsOrderTables(List<OrderTableResponse> orderTableResponse1,
                                         List<OrderTableResponse> orderTableResponse2) {
        for (int idx = 0; idx < orderTableResponse1.size(); idx++) {
            verifyEqualsOrderTable(orderTableResponse1.get(idx), orderTableResponse2.get(idx));
        }
    }

    private void verifyEqualsOrderTable(OrderTableResponse orderTableResponse1,
                                        OrderTableResponse orderTableResponse2) {
        assertAll(
                () -> assertThat(orderTableResponse1.getId()).isEqualTo(
                        orderTableResponse2.getId()),
                () -> assertThat(orderTableResponse1.getTableGroupId()).isEqualTo(
                        orderTableResponse2.getTableGroupId()),
                () -> assertThat(orderTableResponse1.getNumberOfGuests()).isEqualTo(
                        orderTableResponse2.getNumberOfGuests())
        );
    }

    @Test
    @DisplayName("엮을 테이블은 한 개 이하면 예외 발생")
    void invalidLessThen2OrderTable() {
        // given
        final OrderTable orderTableOf5Guests = new OrderTable(1L, null, GuestNumber.of(5), true);
        when(orderTableRepository.findAllById(any())).thenReturn(Arrays.asList(orderTableOf5Guests));
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L));
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @Test
    @DisplayName("엮을 주문 테이블이 존재하지 않으면 예외 발생")
    void notExistOrderTableId() {
        // given
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(1L, 2L));
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @ParameterizedTest(name = "빈 테이블이거나 다른 테이블에 그룹화 되어 있으면 예외 발생")
    @MethodSource("invalidEmptyOrGroupingOrderTableParameter")
    void emptyOrGroupingOrderTable(List<OrderTable> orderTables) {
        // given
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L, 2L));
        when(orderTableRepository.findAllById(any())).thenReturn(orderTables);
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    public static Stream<Arguments> invalidEmptyOrGroupingOrderTableParameter() {
        // given
        final OrderTable orderTable = new OrderTable(99L, null, GuestNumber.of(5), true);
        final TableGroup existTableGroup = new TableGroup(2L, null, Arrays.asList(orderTable, orderTable));
        final OrderTable groupingOrderTable = new OrderTable(1L, existTableGroup, GuestNumber.of(5), false);
        final OrderTable notEmptyOrderTable = new OrderTable(2L, null, GuestNumber.of(3), false);

        return Stream.of(
                Arguments.of(
                        Arrays.asList(groupingOrderTable, groupingOrderTable)
                ),
                Arguments.of(
                        Arrays.asList(notEmptyOrderTable, notEmptyOrderTable)
                )
        );
    }

    @Test
    @DisplayName("그룹화된 테이블을 해제한다.")
    void ungroupGroupTable() {
        // given
        final OrderTable groupingOneOrderTable = new OrderTable(1L, null, GuestNumber.of(5), true);
        final OrderTable groupingTwoOrderTable = new OrderTable(2L, null, GuestNumber.of(3), true);
        final TableGroup tableGroup = new TableGroup(1L, null,
                Arrays.asList(groupingOneOrderTable, groupingTwoOrderTable));
        when(tableGroupRepository.findById(any())).thenReturn(Optional.of(tableGroup));
        when(orderRepository.existsOrdersByOrderTableInAndOrderStatusNot(
                Arrays.asList(groupingOneOrderTable, groupingTwoOrderTable), OrderStatus.COMPLETION)).thenReturn(false);
        // when && then
        tableGroupService.ungroup(1L);
    }

    @Test
    @DisplayName("식사가 완료되지 않은 상태에서 그룹화된 테이블을 해제시 예외 발생")
    void ungroupCookingAndMealGroupTable() {
        // given
        final OrderTable groupingOneOrderTable = new OrderTable(1L, null, GuestNumber.of(5), true);
        final OrderTable groupingTwoOrderTable = new OrderTable(2L, null, GuestNumber.of(3), true);
        final TableGroup tableGroup = new TableGroup(1L, null,
                Arrays.asList(groupingOneOrderTable, groupingTwoOrderTable));
        when(tableGroupRepository.findById(any())).thenReturn(Optional.of(tableGroup));
        when(orderRepository.existsOrdersByOrderTableInAndOrderStatusNot(
                Arrays.asList(groupingOneOrderTable, groupingTwoOrderTable), OrderStatus.COMPLETION)).thenReturn(true);

        // when && then
        assertThatIllegalStateException()
                .isThrownBy(() -> tableGroupService.ungroup(1L));
    }
}
