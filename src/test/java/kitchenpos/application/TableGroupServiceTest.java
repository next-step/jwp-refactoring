package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.factory.OrderTableFixtureFactory;
import kitchenpos.factory.TableGroupFixtureFactory;
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

import static kitchenpos.factory.OrderTableFixtureFactory.*;
import static kitchenpos.factory.TableGroupFixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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
    private OrderTable 테이블_Full;
    private OrderTable 테이블_Grouped;

    @BeforeEach
    void setUp() {
        테이블_1 = createOrderTable(1L, null, 0, true);
        테이블_2 = createOrderTable(2L, null, 0, true);
        테이블_3 = createOrderTable(3L, null, 0, true);
        단체_테이블 = createTableGroup(1L, LocalDateTime.now(), Arrays.asList(테이블_1, 테이블_2, 테이블_3));

        테이블_Full = createOrderTable(4L, null, 4, false);
        테이블_Grouped = createOrderTable(4L, 2L, 4, false);
    }

    @DisplayName("테이블그룹을 등록할 수 있다")
    @Test
    void 테이블그룹_등록(){
        //given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블_1, 테이블_2, 테이블_3));
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(단체_테이블);
        TableGroupRequest 단체_테이블_request = TableGroupRequest.from(
                Arrays.asList(테이블_1.getId(), 테이블_2.getId(), 테이블_3.getId())
        );

        //when
        TableGroupResponse savedTableGroup = tableGroupService.create(단체_테이블_request);

        //then
        assertThat(savedTableGroup).isEqualTo(TableGroupResponse.from(단체_테이블));
    }

    @DisplayName("테이블그룹 내 주문테이블은 2개 이상이어야 한다")
    @Test
    void 테이블그룹_등록_주문테이블_두개_이상_검증(){
        //given
        TableGroupRequest invalidRequest = TableGroupRequest.from(Arrays.asList(테이블_1.getId()));

        //then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(invalidRequest));
    }

    @DisplayName("등록하려는 테이블그룹의 주문테이블이 모두 존재해야 한다")
    @Test
    void 테이블그룹_등록_주문테이블_검증(){
        //given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블_1, 테이블_2));
        TableGroupRequest invalidRequest = TableGroupRequest.from(
                Arrays.asList(테이블_1.getId(), 테이블_2.getId(), 테이블_3.getId())
        );

        //then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(invalidRequest));
    }

    @DisplayName("비어있는 주문테이블만 등록할 수 있다")
    @Test
    void 테이블그룹_등록_주문테이블_Empty_검증(){
        //given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블_1, 테이블_2, 테이블_Full));
        TableGroupRequest invalidRequest = TableGroupRequest.from(
                Arrays.asList(테이블_1.getId(), 테이블_2.getId(), 테이블_Full.getId())
        );

        //then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(invalidRequest));
    }

    @DisplayName("이미 테이블그룹에 속해있는 주문테이블은 등록할 수 없다")
    @Test
    void 테이블그룹_등록_주문테이블_이미_테이블그룹에_속해있는지_검증(){
        //given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블_1, 테이블_2, 테이블_Grouped));
        TableGroupRequest invalidRequest = TableGroupRequest.from(
                Arrays.asList(테이블_1.getId(), 테이블_2.getId(), 테이블_Grouped.getId())
        );

        //then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(invalidRequest));
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