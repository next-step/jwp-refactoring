package kitchenpos.application;

import static kitchenpos.application.TableServiceTest.주문_테이블_생성;
import static kitchenpos.application.TableServiceTest.테이블_그룹_있는_주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupDao tableGroupDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 일번_주문_테이블;
    private OrderTable 이번_주문_테이블;

    private TableGroup 테이블_그룹;

    @BeforeEach
    void init() {
        일번_주문_테이블 = 주문_테이블_생성(1L, 4, true);
        이번_주문_테이블 = 주문_테이블_생성(2L, 6, true);

        테이블_그룹 = 테이블_그룹_생성(1L);
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void create() {
        // given
        테이블_그룹.setOrderTables(Arrays.asList(일번_주문_테이블, 이번_주문_테이블));
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(일번_주문_테이블, 이번_주문_테이블));
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(테이블_그룹);

        // when
        TableGroup savedTableGroup = tableGroupService.create(테이블_그룹);

        assertAll(
            () -> assertThat(savedTableGroup).isNotNull(),
            () -> assertThat(savedTableGroup.getOrderTables()).containsExactly(일번_주문_테이블, 이번_주문_테이블)
        );
    }

    @Test
    @DisplayName("테이블 그룹이 2개 이상이 아닐 경우 - 오류")
    void createIfNoMoreThanTwo() {
        // given
        테이블_그룹.setOrderTables(Arrays.asList(일번_주문_테이블));

        // when then
        assertThatThrownBy(() -> tableGroupService.create(테이블_그룹))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    @DisplayName("존재하지 않는 주문 테이블이 있을 경우 - 오류")
    void createIfNonExistentOrderTable() {
        // given
        List<OrderTable> 존재하지_않는_주문_테이블 = Arrays.asList(일번_주문_테이블);
        테이블_그룹.setOrderTables(Arrays.asList(일번_주문_테이블, 이번_주문_테이블));
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(존재하지_않는_주문_테이블);

        // when then
        assertThatThrownBy(() -> tableGroupService.create(테이블_그룹))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 비어있지 않거나 이미 테이블 그룹이 지정되어 있는 경우 - 오류")
    void createIfExistentTableGroup() {
        // given
        OrderTable 테이블_그룹_있는_주문_테이블 = 테이블_그룹_있는_주문_테이블_생성(1L, 1L, 6, true);
        List<OrderTable> 유효하지_않는_주문_테이블 = Arrays.asList(일번_주문_테이블, 테이블_그룹_있는_주문_테이블);
        테이블_그룹.setOrderTables(유효하지_않는_주문_테이블);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(유효하지_않는_주문_테이블);

        // when then
        assertThatThrownBy(() -> tableGroupService.create(테이블_그룹))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 단체 지정을 해제한다.")
    void ungroup() {
        // given
        OrderTable 테이블_그룹_있는_일번_주문_테이블 = 테이블_그룹_있는_주문_테이블_생성(일번_주문_테이블.getId(), 일번_주문_테이블.getTableGroupId(), 6, true);
        OrderTable 테이블_그룹_있는_이번_주문_테이블 = 테이블_그룹_있는_주문_테이블_생성(이번_주문_테이블.getId(), 이번_주문_테이블.getTableGroupId(), 6, true);

        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(테이블_그룹_있는_일번_주문_테이블, 테이블_그룹_있는_이번_주문_테이블));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(테이블_그룹_있는_일번_주문_테이블, 테이블_그룹_있는_이번_주문_테이블);

        // when
        tableGroupService.ungroup(테이블_그룹.getId());

        // then
        assertAll(
            () -> assertThat(테이블_그룹_있는_일번_주문_테이블.getTableGroupId()).isNull(),
            () -> assertThat(테이블_그룹_있는_이번_주문_테이블.getTableGroupId()).isNull()
        );
    }

    @Test
    @DisplayName("주문의 상태가 `요리중`이거나 `식사`일 경우 - 오류")
    void ungroupIfOrderStatusCookingOrMeal() {
        // given
        테이블_그룹.setOrderTables(Arrays.asList(일번_주문_테이블, 이번_주문_테이블));
        given(orderTableDao.findAllByTableGroupId(테이블_그룹.getId())).willReturn(Arrays.asList(일번_주문_테이블, 이번_주문_테이블));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // when then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블_그룹.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    public static TableGroup 테이블_그룹_생성(Long id) {
        return new TableGroup(id);
    }
}
