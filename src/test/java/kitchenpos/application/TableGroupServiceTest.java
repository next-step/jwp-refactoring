package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("단체 지정 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    TableGroupRepository tableGroupRepository;

    @InjectMocks
    TableGroupService tableGroupService;

    @DisplayName("복수의 주문 테이블을 단체 지정할 수 있다.")
    @Test
    void create1() {
        //given
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable(1L, null, 0, true));
        orderTables.add(new OrderTable(2L, null, 0, true));

        TableGroupRequest newTableGroup = new TableGroupRequest(null, LocalDateTime.now(), orderTables);
        newTableGroup.setOrderTables(orderTables);

        given(orderTableDao.findAllByIdIn(any()))
                .willReturn(
                        Arrays.asList(
                                new OrderTable(1L, null, 0, true),
                                new OrderTable(2L, null, 0, true)
                        )
                );
        // TODO: 임시로 any() 로 돌려놓음.
        given(tableGroupRepository.save(any()))
                .willReturn(
                        new TableGroup(1L, LocalDateTime.now(),
                                Arrays.asList(
                                        new OrderTable(1L, null, 0, true),
                                        new OrderTable(2L, null, 0, true)
                                )
                        )
                );

        //when
        TableGroupResponse savedTableGroup = tableGroupService.create(newTableGroup);

        //then
        assertThat(savedTableGroup.getId()).isEqualTo(1L);
        assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(2);
    }

    @DisplayName("이미 등록한 주문 테이블 목록이 없거나 1개라면 단체 지정할 수 없다.")
    @Test
    void create2() {
        //given
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable(1L, null, 0, true));

        TableGroupRequest newTableGroup = new TableGroupRequest(1L, LocalDateTime.now(), orderTables);

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(newTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정할 주문 테이블은 2개 이상이어야 합니다.");
    }

    @DisplayName("이미 단체 지정된 것을 풀 수 있다.")
    @Test
    void ungroup1() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(1L, null, 0, true),
                new OrderTable(2L, null, 0, true)
        );

        given(orderTableDao.findAllByTableGroupId(any()))
                .willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
                .willReturn(false);

        //when
        tableGroupService.ungroup(1L);

        //then
        verify(orderTableDao, times(2)).save(any());
        verify(orderTableDao).save(orderTables.get(0));
        verify(orderTableDao).save(orderTables.get(1));
    }

    @DisplayName("지정한 주문 테이블들이 모두 완료상태여야 그룹 해제가 가능합니다.")
    @Test
    void ungroup2() {
        //given
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
                .willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지정한 주문 테이블들이 모두 완료상태여야 그룹 해제가 가능합니다.");
    }
}
