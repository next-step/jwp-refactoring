package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@DisplayName("테이블 그룹 서비스 테스트")
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

    @DisplayName("2개 이상의 개별 주문 테이블을 하나의 단체 지정 테이블로 생성한다.")
    @Test
    void create() {
        // given
        TableGroup tableGroup = new TableGroup.Builder().orderTables(
                Arrays.asList(new OrderTable.Builder().id(1L).numberOfGuests(0).empty(true).build(),
                        new OrderTable.Builder().id(2L).numberOfGuests(0).empty(true).build(),
                        new OrderTable.Builder().id(3L).numberOfGuests(0).empty(true).build())).build();

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(tableGroup.getOrderTables());
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(new TableGroup.Builder().id(1L).build());

        // when
        TableGroup created = tableGroupService.create(tableGroup);

        // then
        assertThat(created.getId()).isNotNull();

        // verity
        then(orderTableDao).should(times(1)).findAllByIdIn(anyList());
        then(tableGroupDao).should(times(1)).save(any(TableGroup.class));
        then(orderTableDao).should(times(3)).save(any(OrderTable.class));
    }

    @DisplayName("1개의 개별 주문 테이블로는 단체 지정 테이블을 생성할 수 없다.")
    @Test
    void create_throwException_ifIncorrectSize() {
        // given
        TableGroup tableGroup = new TableGroup.Builder().orderTables(Arrays.asList(createOrderTable(1L, 0, true))).build();

        // when
        // then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));

        // verify
        then(orderTableDao).should(never()).findAllByIdIn(anyList());
    }

    @DisplayName("단체 지정 테이블을 개별 주문 테이블로 변경한다.")
    @Test
    void ungroup() {
        // given
        given(orderTableDao.findAllByTableGroupId(1L))
                .willReturn(Arrays.asList(createOrderTable(1L, 4, false), createOrderTable(2L, 0, false)));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(1L, 2L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        // when
        tableGroupService.ungroup(1L);

        // then

        // verify
        then(orderTableDao).should(times(2)).save(any(OrderTable.class));
    }

    @DisplayName("주문 상태가 조리 또는 식사인 경우 개별 주문 테이블로 변경할 수 없다.")
    @Test
    void ungroup_throwException_ifOrderStatusInCookingAndMeal() {
        // given
        given(orderTableDao.findAllByTableGroupId(1L))
                .willReturn(Arrays.asList(createOrderTable(1L, 4, false), createOrderTable(2L, 0, false)));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(1L, 2L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when
        // then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(1L));

        // then

        // verify
        then(orderTableDao).should(never()).save(any(OrderTable.class));
    }

    public static OrderTable createOrderTable(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable.Builder().id(id).numberOfGuests(numberOfGuests).empty(empty).build();
    }
}
