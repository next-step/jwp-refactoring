package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static kitchenpos.fixtures.TableGroupFixtures.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * packageName : kitchenpos.application
 * fileName : TableGroupServiceTest
 * author : haedoang
 * date : 2021/12/18
 * description :
 */
@DisplayName("그룹 테이블 통합 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    private TableGroup tableGroup;
    private OrderTable orderTableFirst;
    private OrderTable orderTableSecond;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;


    @BeforeEach
    void setUp() {
//        orderTableFirst = createOrderTable(1L, null, 2, true);
//        orderTableSecond = createOrderTable(2L, null, 3, true);
        tableGroup = createTableGroup(1L, LocalDateTime.now(), Lists.newArrayList(orderTableFirst, orderTableSecond));
    }

    @Test
    @DisplayName("테이블을 그룹화할 수 있다.")
    public void group() {
        // given
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(tableGroup);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(tableGroup.getOrderTables());

        // when
        TableGroup actual = tableGroupService.create(tableGroup);

        // then
        assertAll(
                () -> assertThat(actual).isEqualTo(tableGroup),
                () -> assertThat(actual.getOrderTables()).extracting(OrderTable::isEmpty).containsOnly(false)
        );
    }

    @Test
    @DisplayName("테이블 개수가 2개보다 작은 경우 등록할 수 없다.")
    public void createFailByTables() {
        // given
//        tableGroup.setOrderTables(Lists.newArrayList(orderTableFirst));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되지 않은 테이블인 경우 그룹화 할 수 없다.")
    public void createFailByUnknownTable() {
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 비워있지 않으면 등록할 수 없다.")
    public void createFailByUsingTable() {
//        orderTableFirst.setEmpty(false);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(tableGroup.getOrderTables());
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 비워있지 않으면 등록할 수 없다.")
    public void createFailByAlreadyGrouped() {
        // given
//        orderTableFirst.setTableGroupId(tableGroup.getId());
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(tableGroup.getOrderTables());

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹화를 해제할 수 있다.")
    public void ungroup() {
        // given
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(tableGroup.getOrderTables());
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTableFirst, orderTableSecond);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        verify(orderTableDao).save(orderTableFirst);
        verify(orderTableDao).save(orderTableSecond);
    }

    @Test
    @DisplayName("테이블의 주문 상태가 조리, 식사중인 경우 그룹화를 해제할 수 없다.")
    public void ungroupFail() {
        // given
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(tableGroup.getOrderTables());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId())).isInstanceOf(IllegalArgumentException.class);
    }
}
