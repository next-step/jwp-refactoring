package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    TableGroupDao tableGroupDao;

    @Mock
    OrderDao orderDao;

    @InjectMocks
    TableGroupService tableGroupService;

    @DisplayName("주문 테이블 단체 지정을 해제한다.")
    @Test
    void 단체_지정_해제() {
        // given
        OrderTable 테이블1 = TableServiceTest.주문_테이블_생성(null, 1L, true, 0);
        OrderTable 테이블2 = TableServiceTest.주문_테이블_생성(null, 1L, true, 0);
        TableGroup 테이블_그룹 = 테이블_단체_지정(1L, 테이블1, 테이블2);
        given(orderTableDao.findAllByTableGroupId(any(Long.class))).willReturn(Arrays.asList(테이블1, 테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(테이블1, 테이블2);

        // when
        tableGroupService.ungroup(테이블_그룹.getId());

        // then
        assertThat(테이블1.getTableGroupId()).isNull();
    }

    @DisplayName("주문 테이블 단체 지정 해제에 실패한다.")
    @Test
    void 단체_지정_해제_예외() {
        // given
        OrderTable 테이블1 = TableServiceTest.주문_테이블_생성(null, 1L, true, 0);
        OrderTable 테이블2 = TableServiceTest.주문_테이블_생성(null, 1L, true, 0);
        TableGroup 테이블_그룹 = 테이블_단체_지정(1L, 테이블1, 테이블2);
        given(orderTableDao.findAllByTableGroupId(any(Long.class))).willReturn(Arrays.asList(테이블1, 테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블_그룹.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static TableGroup 테이블_단체_지정(Long tableGroupId, OrderTable... orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(tableGroupId);
        tableGroup.setOrderTables(Arrays.asList(orderTables));
        return tableGroup;
    }
}
