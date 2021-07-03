package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;
    @Mock
    TableGroupDao tableGroupDao;

    TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @DisplayName("정상적으로 테이블 그룹 생성")
    @Test
    void 정상적으로_테이블_그룹_생성() {
        //given
        TableGroup tableGroup = new TableGroup(Arrays.asList(
                new OrderTable(1L),
                new OrderTable(2L)
        ));

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(
                new OrderTable(1L, null, 0, true),
                new OrderTable(2L, null, 0, true)
        ));

        when(tableGroupDao.save(tableGroup)).thenAnswer(i -> i.getArgument(0));

        //when
        TableGroup resultTableGroup = tableGroupService.create(tableGroup);

        //then
        assertThat(resultTableGroup.getOrderTables()).hasSize(2);
    }

    @DisplayName("주문 테이블의 수가 하나 이하인 경우")
    @Test
    void 주문_테이블의_수가_하나_이하인_경우() {
        //given
        TableGroup tableGroup = new TableGroup(Arrays.asList(
                new OrderTable(1L)
        ));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("요청한 테이블 그룹에 속한 주문 테이블을 찾을 수 없는 경우")
    @Test
    void 요청한_테이블_그룹에_속한_주문_테이블을_찾을_수_없는_경우() {
        //given
        TableGroup tableGroup = new TableGroup(Arrays.asList(
                new OrderTable(1L),
                new OrderTable(2L)
        ));

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(
                new OrderTable(1L, null, 0, true)
        ));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("요청한 테이블 그룹에 속한 주문 테이블이 이미 다른 테이블 그룹에 속해있는 경우")
    @Test
    void 요청한_테이블_그룹에_속한_주문_테이블이_이미_다른_테이블_그룹에_속해있는_경우() {
        //given
        TableGroup tableGroup = new TableGroup(Arrays.asList(
                new OrderTable(1L),
                new OrderTable(2L)
        ));

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(
                new OrderTable(1L, 3L, 0, false),
                new OrderTable(2L, null, 0, true)
        ));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에서 테이블 그룹과의 관계를 해제한다.")
    @Test
    void 주문_테이블에서_테이블_그룹과의_관계를_해제() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, 1L, 4, false);
        OrderTable orderTable2 = new OrderTable(2L, 1L, 3, false);

        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(
                Arrays.asList(orderTable1, orderTable2)
        );

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        //when
        tableGroupService.ungroup(1L);

        //then
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
    }

    @DisplayName("테이블 그룹과의 관계 해제시 조리 또는 식사 상태일 경우")
    @Test
    void 테이블_그룹과의_관계_해제시_조리_또는_식사_상태일_경우() {
        //given
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        //when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L)).isInstanceOf(IllegalArgumentException.class);
    }
}
