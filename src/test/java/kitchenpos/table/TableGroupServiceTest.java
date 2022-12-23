package kitchenpos.table;

import static kitchenpos.order.OrderFixture.주문항목;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.table.TableFixture.일번테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.TableGroup;
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
        Order 조리중 = new Order(1L, 일번테이블, COOKING.name(), null, Collections.singletonList(주문항목));
        OrderTable 조리중테이블 = new OrderTable(1L, null, 0, false, Collections.singletonList(조리중));
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Collections.singletonList(조리중테이블));
        when(tableGroupDao.findById(any())).thenReturn(Optional.of(tableGroup));

        //when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중이거나 식사중에는 단체 지정해제할 수 없습니다.");
    }

}
