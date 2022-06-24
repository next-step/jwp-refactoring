package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
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
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup 단체_테이블;
    private OrderTable 테이블_1;
    private OrderTable 테이블_2;
    private OrderTable 테이블_3;

    @BeforeEach
    void setUp() {
        테이블_1 = new OrderTable(1L, null, 0, true);
        테이블_2 = new OrderTable(2L, null, 0, true);
        테이블_3 = new OrderTable(3L, null, 0, true);
        단체_테이블 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(테이블_1, 테이블_2, 테이블_3));
    }

    @DisplayName("테이블그룹을 등록할 수 있다")
    @Test
    void 테이블그룹_등록(){
        //given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블_1, 테이블_2, 테이블_3));
        given(tableGroupDao.save(단체_테이블)).willReturn(단체_테이블);

        //when
        TableGroup savedTableGroup = tableGroupService.create(단체_테이블);

        //then
        assertThat(savedTableGroup).isEqualTo(단체_테이블);
    }

    @DisplayName("테이블그룹 내 주문테이블은 2개 이상이어야 한다")
    @Test
    void 테이블그룹_등록_주문테이블_두개_이상_검증(){
        //given
        TableGroup invalidTable = new TableGroup(2L, LocalDateTime.now(), Arrays.asList(테이블_1));

        //then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(invalidTable));
    }

    @DisplayName("등록하려는 테이블그룹의 주문테이블이 모두 존재해야 한다")
    @Test
    void 테이블그룹_등록_주문테이블_검증(){
        //given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블_1, 테이블_2));

        //then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(단체_테이블));
    }

    @DisplayName("비어있는 주문테이블만 등록할 수 있다")
    @Test
    void 테이블그룹_등록_주문테이블_Empty_검증(){
        //given
        OrderTable 테이블_Full = new OrderTable(4L, null, 4, false);
        단체_테이블.setOrderTables(Arrays.asList(테이블_1, 테이블_2, 테이블_Full));
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블_1, 테이블_2, 테이블_Full));

        //then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(단체_테이블));
    }

    @DisplayName("이미 테이블그룹에 속해있는 주문테이블은 등록할 수 없다")
    @Test
    void 테이블그룹_등록_주문테이블_이미_테이블그룹에_속해있는지_검증(){
        //given
        OrderTable 테이블_AlreadyGroup = new OrderTable(4L, 2L, 4, false);
        단체_테이블.setOrderTables(Arrays.asList(테이블_1, 테이블_2, 테이블_AlreadyGroup));
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블_1, 테이블_2, 테이블_AlreadyGroup));

        //then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(단체_테이블));
    }

    @DisplayName("테이블 그룹을 삭제할 수 있다")
    @Test
    void 테이블그룹_삭제(){
        //given
        테이블_1.setTableGroupId(단체_테이블.getId());
        테이블_2.setTableGroupId(단체_테이블.getId());
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(테이블_1, 테이블_2));

        //when
        tableGroupService.ungroup(단체_테이블.getId());

        //then
        Assertions.assertAll(
                () -> assertThat(테이블_1.getTableGroupId()).isNull(),
                () -> assertThat(테이블_2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("주문테이블에 COOKING이나 MEAL 상태의 주문이 있으면 삭제할 수 없다")
    @Test
    void 테이블그룹_삭제_주문상태_검증(){
        //given
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(테이블_1, 테이블_2, 테이블_3));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        //then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.ungroup(단체_테이블.getId()));
    }
}