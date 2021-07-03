package kitchenpos.tablegroup.application;

import static kitchenpos.util.TestDataSet.산악회;
import static kitchenpos.util.TestDataSet.테이블_1번;
import static kitchenpos.util.TestDataSet.테이블_2번;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.dao.TableGroupDao;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroupRequest request;

    @BeforeEach
    void setUp() {
        request = new TableGroupRequest(Arrays.asList(테이블_1번.getId(), 테이블_2번.getId()));
    }

    @Test
    @DisplayName("테이블 그룹 정상 생성 케이스")
    void create() {
        //given
        given(orderTableDao.findAllByIdIn(any())).willReturn(산악회.getOrderTables());
        given(tableGroupDao.save(any())).willReturn(산악회);

        //when
        TableGroup result = tableGroupService.create(request);

        // then
        assertThat(result.getId()).isEqualTo(산악회.getId());
        assertThat(result.getOrderTables()).containsExactly(테이블_1번, 테이블_2번);

        verify(orderTableDao, times(1)).findAllByIdIn(any());
        verify(tableGroupDao, times(1)).save(any());
    }

    @Test
    @DisplayName("주문_테이블이 2개 미만일 경우 실패한다.")
    void underTwo() {
        //when
        TableGroupRequest 테이블이_1개 = new TableGroupRequest(Arrays.asList(테이블_1번.getId()));

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableGroupService.create(테이블이_1개);
        });
    }

    @Test
    @DisplayName("주문_테이블이 존재하지 않는 경우 실패한다.")
    void noTable() {
        //when
        given(orderTableDao.findAllByIdIn(any())).willReturn(Collections.emptyList());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableGroupService.create(request);
        });

        verify(orderTableDao, times(1)).findAllByIdIn(any());
    }

    @Test
    @DisplayName("중복된 주문이 존재할 시 실패한다.")
    void overlapTable() {
        //when
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(테이블_1번));

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableGroupService.create(request);
        });

        verify(orderTableDao, times(1)).findAllByIdIn(any());
    }

    @Test
    @DisplayName("주문_테이블이 다른 그룹에 속해있을 경우 실패한다.")
    void aleadyTable() {
        //when
        OrderTable already = new OrderTable(1L, 10, true);
        already.setTableGroupId(1L);
        given(orderTableDao.findAllByIdIn(any()))
            .willReturn(Arrays.asList(already, new OrderTable(2L, 10, true)));
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableGroupService.create(request);
        });
    }

    @Test
    @DisplayName("테이블이 비지 않았을 경우 실패한다.")
    void noEmptyTable() {
        //when
        given(orderTableDao.findAllByIdIn(any()))
            .willReturn(Arrays.asList(new OrderTable(1L, 10, false), new OrderTable(2L, 10, false)));
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableGroupService.create(request);
        });

        verify(orderTableDao, times(1)).findAllByIdIn(any());
    }

    @Test
    @DisplayName("테이블 그룹에서 개별 테이블의 주문상태가 조리중이거나 식사중이면 테이블 그룹 제거 실패한다.")
    void notAbliableRemove() {
        //when
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(Arrays.asList(테이블_1번, 테이블_2번));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableGroupService.ungroup(산악회.getId());
        });

        verify(orderTableDao, times(1)).findAllByTableGroupId(any());
        verify(orderDao, times(1)).existsByOrderTableIdInAndOrderStatusIn(any(), any());
    }

}
