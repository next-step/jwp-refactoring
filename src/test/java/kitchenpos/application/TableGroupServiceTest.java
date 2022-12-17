package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void 단체_지정할_테이블이_없을_경우_에러발생() {
        //when & then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroup()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정할_테이블이_2미만일_경우_에러발생() {
        //given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.singletonList(new OrderTable()));

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroup()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정할_테이블_중_없는_테이블이_있는경우_에러발생() {
        //given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(new OrderTable(), new OrderTable()));
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(new OrderTable()));

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정한_테이블중_주문_테이블이_있는경우_에러발생() {
        //given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(new OrderTable(), new OrderTable()));
        when(orderTableDao.findAllByIdIn(any()))
            .thenReturn(Arrays.asList(new OrderTable(), new OrderTable()));

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_해제_하려는_주문_테이블_상태가_조리_식사_라면_에러발생() {
        //given
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        //when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

}