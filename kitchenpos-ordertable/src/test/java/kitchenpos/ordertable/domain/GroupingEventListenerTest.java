package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.ordertable.event.GroupEvent;
import kitchenpos.ordertable.event.GroupInfo;
import kitchenpos.ordertable.event.UngroupEvent;
import kitchenpos.ordertable.exception.DuplicateTablesException;
import kitchenpos.ordertable.exception.IllegalGroupingTableStateException;
import kitchenpos.ordertable.exception.NotEnoughTablesException;
import java.util.Arrays;
import java.util.List;
import kitchenpos.ordertable.TableTestFixtures;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kitchenpos.ordertable.vo.NumberOfGuests;

@ExtendWith(MockitoExtension.class)
class GroupingEventListenerTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private GroupingEventListener groupingEventHandler;

    @DisplayName("그룹 설정")
    @Test
    void groupTables() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, new NumberOfGuests(6), true),
            new OrderTable(2L, new NumberOfGuests(3), true));
        Long tableGroupId=1L;
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        TableTestFixtures.특정_테이블ID_리스트에_해당하는_테이블_조회_모킹(orderTableRepository, orderTables);

        //when
        GroupEvent groupEvent = new GroupEvent(this,
            new GroupInfo(tableGroupId, orderTableIds));
        groupingEventHandler.handleGroupTables(groupEvent);

        //then
        주문테이블_그룹핑_결과_확인(orderTables);
    }

    @DisplayName("그룹 해제")
    @Test
    void ungroup() {
        //given
        Long tableGroupId=1L;
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, tableGroupId, new NumberOfGuests(6), true),
            new OrderTable(2L, tableGroupId, new NumberOfGuests(3), true));
        TableTestFixtures.특정그룹에_해당하는_주문테이블_리스트_조회_모킹(orderTableRepository, orderTables);

        //when
        UngroupEvent ungroupEvent = new UngroupEvent(this, tableGroupId);
        groupingEventHandler.handleUnGroupTables(ungroupEvent);

        //then
        주문테이블_그룹해제_확인(orderTables);
    }

    @DisplayName("그룹 대상 테이블은 2개 이상이어야 인다.")
    @Test
    void groupTables_exception1() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, new NumberOfGuests(6), true));
        Long tableGroupId = 1L;
        List<Long> orderTableIds = Arrays.asList(1L);
        TableTestFixtures.특정_테이블ID_리스트에_해당하는_테이블_조회_모킹(orderTableRepository, orderTables);

        //when
        GroupEvent groupEvent = new GroupEvent(this,
            new GroupInfo(tableGroupId, orderTableIds));

        //then
        assertThatThrownBy(() -> groupingEventHandler.handleGroupTables(groupEvent))
            .isInstanceOf(NotEnoughTablesException.class);
    }

    @DisplayName("주문 가능한 테이블은 그룹이 될 수 없다.")
    @Test
    void groupTables_exception2() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, new NumberOfGuests(6), false),
            new OrderTable(2L, new NumberOfGuests(3), true));
        Long tableGroupId = 1L;
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        TableTestFixtures.특정_테이블ID_리스트에_해당하는_테이블_조회_모킹(orderTableRepository, orderTables);

        //when
        GroupEvent groupEvent = new GroupEvent(this,
            new GroupInfo(tableGroupId, orderTableIds));

        //then
        assertThatThrownBy(() -> groupingEventHandler.handleGroupTables(groupEvent))
            .isInstanceOf(IllegalGroupingTableStateException.class);
    }

    @DisplayName("그룹 대상 테이블 리스트에 중복이 존재해서는 안된다.")
    @Test
    void groupTables_exception3() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, new NumberOfGuests(6), true),
            new OrderTable(2L, new NumberOfGuests(3), true));
        Long tableGroupId = 1L;
        List<Long> orderTableIds = Arrays.asList(1L, 1L, 2L);
        TableTestFixtures.특정_테이블ID_리스트에_해당하는_테이블_조회_모킹(orderTableRepository, orderTables);

        //when
        GroupEvent groupEvent = new GroupEvent(this,
            new GroupInfo(tableGroupId, orderTableIds));

        //then
        assertThatThrownBy(() -> groupingEventHandler.handleGroupTables(groupEvent))
            .isInstanceOf(DuplicateTablesException.class);
    }

    @DisplayName("이미 그룹에 소속된 테이블은 그룹화 할 수 없다.")
    @Test
    void groupTables_exception4() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 1L, new NumberOfGuests(6), true),
            new OrderTable(2L, new NumberOfGuests(3), true));
        Long tableGroupId = 1L;
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        TableTestFixtures.특정_테이블ID_리스트에_해당하는_테이블_조회_모킹(orderTableRepository, orderTables);

        //when
        GroupEvent groupEvent = new GroupEvent(this,
            new GroupInfo(tableGroupId, orderTableIds));

        //then
        assertThatThrownBy(() -> groupingEventHandler.handleGroupTables(groupEvent))
            .isInstanceOf(IllegalGroupingTableStateException.class);
    }

    private void 주문테이블_그룹핑_결과_확인(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            assertThat(orderTable.hasGroup()).isTrue();
            assertThat(orderTable.isOrderClose()).isFalse();
        }
    }

    private void 주문테이블_그룹해제_확인(List<OrderTable> orderTables) {
        orderTables.stream()
            .forEach(orderTable -> assertThat(orderTable.hasGroup()).isFalse());
    }
}
