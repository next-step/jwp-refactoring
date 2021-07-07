package kitchenpos.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderTableRepository orderTableRepository;
    @Mock
    TableGroupRepository tableGroupRepository;

    TableGroupService tableGroupService;

    TableGroupRequest tableGroupRequest;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);
        tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableIdRequest(1L),
                new OrderTableIdRequest(2L)
        ));
    }

    @DisplayName("정상적으로 테이블 그룹 생성")
    @Test
    void 정상적으로_테이블_그룹_생성() {
        //given
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());

        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(
                new OrderTable(1L, null, 0, true),
                new OrderTable(2L, null, 0, true)
        ));

        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        //when
        TableGroupResponse resultTableGroup = tableGroupService.create(tableGroupRequest);

        //then
        assertThat(resultTableGroup.getId()).isNotNull();
    }

    @DisplayName("주문 테이블의 수가 하나 이하인 경우")
    @Test
    void 주문_테이블의_수가_하나_이하인_경우() {
        //given
        tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableIdRequest(1L)
        ));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("요청한 테이블 그룹에 속한 주문 테이블을 찾을 수 없는 경우")
    @Test
    void 요청한_테이블_그룹에_속한_주문_테이블을_찾을_수_없는_경우() {
        //given
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(
                new OrderTable(1L, null, 0, true)
        ));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("요청한 테이블 그룹에 속한 주문 테이블이 이미 다른 테이블 그룹에 속해있는 경우")
    @Test
    void 요청한_테이블_그룹에_속한_주문_테이블이_이미_다른_테이블_그룹에_속해있는_경우() {
        //given
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(
                new OrderTable(1L, null, 0, false),
                new OrderTable(2L, null, 0, true)
        ));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에서 테이블 그룹과의 관계를 해제한다.")
    @Test
    void 주문_테이블에서_테이블_그룹과의_관계를_해제() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 4, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 3, false);

        given(orderTableRepository.findAllByTableGroupId(anyLong())).willReturn(
                Arrays.asList(orderTable1, orderTable2)
        );

        given(orderRepository.existsByOrderTableInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        //when
        tableGroupService.ungroup(1L);

        //then
        assertThat(orderTable1.getTableGroup()).isNull();
        assertThat(orderTable2.getTableGroup()).isNull();
    }

    @DisplayName("테이블 그룹과의 관계 해제시 조리 또는 식사 상태일 경우")
    @Test
    void 테이블_그룹과의_관계_해제시_조리_또는_식사_상태일_경우() {
        //given
        given(orderRepository.existsByOrderTableInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        //when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L)).isInstanceOf(IllegalArgumentException.class);
    }
}
