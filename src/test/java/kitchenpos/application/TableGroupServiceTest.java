package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;
    @Mock
    TableGroupDao tableGroupDao;
    @InjectMocks
    TableGroupService tableGroupService;

    OrderTable fiveGuestSavedOrderTable;
    OrderTable threeGuestSavedOrderTable;

    @BeforeEach
    void setup() {
        fiveGuestSavedOrderTable = new OrderTable(1L, null, 5, true);
        threeGuestSavedOrderTable = new OrderTable(2L, null, 3, true);
    }

    @DisplayName("단체 지정 생성한다.")
    @Test
    void create() {
        // Given
        TableGroup tableGroupRequest = new TableGroup(Arrays.asList(fiveGuestSavedOrderTable, threeGuestSavedOrderTable));

        given(orderTableDao.findAllByIdIn(Arrays.asList(fiveGuestSavedOrderTable.getId(), threeGuestSavedOrderTable.getId())))
                .willReturn(Arrays.asList(fiveGuestSavedOrderTable, threeGuestSavedOrderTable));
        given(tableGroupDao.save(tableGroupRequest)).willReturn(new TableGroup(1L, LocalDateTime.now(), Arrays.asList(threeGuestSavedOrderTable, fiveGuestSavedOrderTable)));

        // When
        TableGroup tableGroupResponse = tableGroupService.create(tableGroupRequest);

        // Then
        Assertions.assertAll(
                () -> assertThat(tableGroupResponse.getId()).isNotNull(),
                () -> assertThat(tableGroupResponse.getOrderTables()).containsExactly(fiveGuestSavedOrderTable, threeGuestSavedOrderTable)
        );
    }

    @DisplayName("단체 지정 생성 1테이블 에러 처리")
    @Test
    void oneTableCreateException() {
        // Given
        TableGroup oneTableGroupRequest = new TableGroup(Arrays.asList(fiveGuestSavedOrderTable));

        // When, Then
        assertThatThrownBy(() -> tableGroupService.create(oneTableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 생성 빈 테이블 아닐경우 에러 처리")
    @Test
    void notEmptyTableCreateException() {
        // Given
        OrderTable notEmptySavedOrderTable = new OrderTable(3L, null, 3, false);

        TableGroup notEmptyTableGroupRequest = new TableGroup(Arrays.asList(fiveGuestSavedOrderTable, notEmptySavedOrderTable));

        given(orderTableDao.findAllByIdIn(Arrays.asList(fiveGuestSavedOrderTable.getId(), notEmptySavedOrderTable.getId())))
                .willReturn(Arrays.asList(fiveGuestSavedOrderTable, notEmptySavedOrderTable));

        // When, Then
        assertThatThrownBy(() -> tableGroupService.create(notEmptyTableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 생성 단체 id 존재 에러 처리")
    @Test
    void existGroupIdTableCreateException() {
        // Given
        OrderTable existGroupIdSavedOrderTable = new OrderTable(4L, 1L, 3, true);

        TableGroup existGroupIdTableGroupRequest = new TableGroup(Arrays.asList(fiveGuestSavedOrderTable, existGroupIdSavedOrderTable));

        given(orderTableDao.findAllByIdIn(Arrays.asList(fiveGuestSavedOrderTable.getId(), existGroupIdSavedOrderTable.getId())))
                .willReturn(Arrays.asList(fiveGuestSavedOrderTable, existGroupIdSavedOrderTable));

        // When, Then
        assertThatThrownBy(() -> tableGroupService.create(existGroupIdTableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제")
    @Test
    void unGroup() {
        // Given
        TableGroup savedTableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(threeGuestSavedOrderTable, fiveGuestSavedOrderTable));
        given(orderTableDao.findAllByTableGroupId(savedTableGroup.getId())).willReturn(Arrays.asList(fiveGuestSavedOrderTable, threeGuestSavedOrderTable));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(fiveGuestSavedOrderTable.getId(), threeGuestSavedOrderTable.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);

        // When
        tableGroupService.ungroup(savedTableGroup.getId());
    }
}