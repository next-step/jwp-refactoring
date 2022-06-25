package kitchenpos.application;

import static kitchenpos.fixture.DomainFactory.createOrderTable;
import static kitchenpos.fixture.DomainFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
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
    private TableGroup 단체지정;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @BeforeEach
    void setUp() {
        주문테이블1 = createOrderTable(1L, null, 5, true);
        주문테이블2 = createOrderTable(2L, null, 5, true);
        단체지정 = createTableGroup(1L, null, Arrays.asList(주문테이블1, 주문테이블2));
    }

    @Test
    void 단체_지정_테이블_개수_2개_미만_예외() {
        createTableGroup(1L, null, Arrays.asList(주문테이블1));

        assertThatThrownBy(
                () -> tableGroupService.create(
                        createTableGroup(1L, null, Arrays.asList(주문테이블1))
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_존재하지_않는_테이블_예외() {
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()))).thenReturn(
                Arrays.asList(주문테이블1));

        assertThatThrownBy(
                () -> tableGroupService.create(단체지정)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_빈_테이블_아님_예외() {
        주문테이블1.setEmpty(false);
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()))).thenReturn(
                Arrays.asList(주문테이블1, 주문테이블2));

        assertThatThrownBy(
                () -> tableGroupService.create(단체지정)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_이미_단체_지정_테이블_예외() {
        주문테이블1.setTableGroupId(2L);
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()))).thenReturn(
                Arrays.asList(주문테이블1, 주문테이블2));

        assertThatThrownBy(
                () -> tableGroupService.create(단체지정)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정() {
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()))).thenReturn(
                Arrays.asList(주문테이블1, 주문테이블2));
        when(tableGroupDao.save(단체지정)).thenReturn(단체지정);

        assertThat(tableGroupService.create(단체지정).getId()).isEqualTo(단체지정.getId());
    }

    @Test
    void 단체_지정_삭제_주문_상태_예외() {
        when(orderTableDao.findAllByTableGroupId(단체지정.getId())).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(true);

        assertThatThrownBy(
                () -> tableGroupService.ungroup(단체지정.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_삭제() {
        when(orderTableDao.findAllByTableGroupId(단체지정.getId())).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(false);

        tableGroupService.ungroup(단체지정.getId());

        verify(orderTableDao).save(주문테이블1);
        verify(orderTableDao).save(주문테이블2);
    }
}