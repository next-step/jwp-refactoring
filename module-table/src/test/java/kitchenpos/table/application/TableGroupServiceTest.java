package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.common.exception.NotCompletionStatusException;
import kitchenpos.table.domain.GuestNumber;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
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
    private OrderTable orderTableOf5Guests, orderTableOf3Guests;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository);

        orderTableOf5Guests = new OrderTable.Builder()
                .setId(1L)
                .setGuestNumber(GuestNumber.of(5))
                .setEmpty(true)
                .build();
        orderTableOf3Guests = new OrderTable.Builder()
                .setId(2L)
                .setGuestNumber(GuestNumber.of(3))
                .setEmpty(true)
                .build();
    }

    @Test
    @DisplayName("주문 테이블을 단체로 엮는 테이블 그룹을 생성한다.")
    void createTableGroup() {
        // given
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
        final OrderTable orderTable = new OrderTable.Builder()
                .setId(99L)
                .setGuestNumber(GuestNumber.of(5))
                .setEmpty(true)
                .build();
        final OrderTable groupingOrderTable = new OrderTable.Builder()
                .setId(1L)
                .setTableGroupId(2L)
                .setGuestNumber(GuestNumber.of(5))
                .setEmpty(false)
                .build();
        final OrderTable notEmptyOrderTable = new OrderTable.Builder()
                .setId(2L)
                .setGuestNumber(GuestNumber.of(3))
                .setEmpty(false)
                .build();

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
        final TableGroup tableGroup = new TableGroup(1L, null,
                Arrays.asList(orderTableOf5Guests, orderTableOf3Guests));
        when(tableGroupRepository.findById(any())).thenReturn(Optional.of(tableGroup));
        // when && then
        tableGroupService.ungroup(1L);
    }

    @Test
    @DisplayName("식사가 완료되지 않은 상태에서 그룹화된 테이블을 해제시 예외 발생")
    void ungroupCookingAndMealGroupTable() {
        final TableGroup mockTableGroup = mock(TableGroup.class);
        when(tableGroupRepository.findById(any())).thenReturn(Optional.of(mockTableGroup));
        doThrow(new NotCompletionStatusException()).when(mockTableGroup).ungroup();

        // when && then
        assertThatIllegalStateException()
                .isThrownBy(() -> tableGroupService.ungroup(1L));
    }
}
