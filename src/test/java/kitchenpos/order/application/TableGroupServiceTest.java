package kitchenpos.order.application;

import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static kitchenpos.utils.DomainFixtureFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    private OrderTable 치킨주문테이블;
    private OrderTable 피자주문테이블;
    private OrderTable 단체지정_치킨주문테이블;
    private OrderTable 단체지정_피자주문테이블;
    private TableGroup 단체지정;

    @BeforeEach
    void setUp() {
        치킨주문테이블 = createOrderTable(1L, null, 2, true);
        피자주문테이블 = createOrderTable(2L, null, 3, true);
        단체지정 = createTableGroup(1L, null, Lists.newArrayList(치킨주문테이블, 피자주문테이블));
        단체지정_치킨주문테이블 = createOrderTable(1L, 단체지정.getId(), 2, true);
        단체지정_피자주문테이블 = createOrderTable(2L, 단체지정.getId(), 3, true);
    }

    @DisplayName("단체지정 생성 테스트")
    @Test
    void create() {
        given(orderTableDao.findAllByIdIn(Lists.newArrayList(치킨주문테이블.getId(), 피자주문테이블.getId()))).willReturn(
                Lists.newArrayList(치킨주문테이블, 피자주문테이블));
        given(tableGroupDao.save(단체지정)).willReturn(단체지정);
        given(orderTableDao.save(단체지정_치킨주문테이블)).willReturn(단체지정_치킨주문테이블);
        given(orderTableDao.save(단체지정_피자주문테이블)).willReturn(단체지정_피자주문테이블);
        TableGroup tableGroup = tableGroupService.create(단체지정);
        assertAll(
                () -> assertThat(tableGroup.getOrderTables()).containsExactlyElementsOf(
                        Lists.newArrayList(치킨주문테이블, 피자주문테이블)),
                () -> assertThat(tableGroup.getId()).isEqualTo(단체지정.getId())
        );
    }

    @DisplayName("단체지정 생성시 주문테이블이 2 미만인 경우 테스트")
    @Test
    void createWithOrderTableSizeUnderTwo() {
        단체지정.setOrderTables(Lists.newArrayList(치킨주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(단체지정));
    }

    @DisplayName("단체지정 생성시 주문테이블이 null 인 경우 테스트")
    @Test
    void createWithOrderTableNull() {
        단체지정.setOrderTables(null);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(단체지정));
    }

    @DisplayName("단체지정 생성시 단체지정의 주문테이블 수와 등록된 주문테이블에서 조회된 단체지정의 주문테이블 수가 일치하지 않는 경우 테스트")
    @Test
    void createWithNotEqualOrderTableSize() {
        given(orderTableDao.findAllByIdIn(Lists.newArrayList(치킨주문테이블.getId(), 피자주문테이블.getId()))).willReturn(
                Lists.newArrayList(치킨주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(단체지정));
    }

    @DisplayName("단체지정 생성시 주문테이블이 비어있지 않는 경우 테스트")
    @Test
    void createWithOrderTableNotEmpty() {
        피자주문테이블 = createOrderTable(2L, null, 3, false);
        given(orderTableDao.findAllByIdIn(Lists.newArrayList(치킨주문테이블.getId(), 피자주문테이블.getId()))).willReturn(
                Lists.newArrayList(치킨주문테이블, 피자주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(단체지정));
    }

    @DisplayName("단체지정 생성시 주문테이블이 테이블 그룹이 이미 있는 경우 테스트")
    @Test
    void createWithOrderTableAlreadyContainTableGroup() {
        given(orderTableDao.findAllByIdIn(Lists.newArrayList(치킨주문테이블.getId(), 단체지정_피자주문테이블.getId()))).willReturn(
                Lists.newArrayList(치킨주문테이블, 단체지정_피자주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(단체지정));
    }

    @DisplayName("단체지정 해제 테스트")
    @Test
    void ungroup() {
        given(orderTableDao.findAllByTableGroupId(단체지정.getId())).willReturn(
                Lists.newArrayList(단체지정_치킨주문테이블, 단체지정_피자주문테이블));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Lists.newArrayList(치킨주문테이블.getId(), 피자주문테이블.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        tableGroupService.ungroup(단체지정.getId());
        verify(orderTableDao).save(치킨주문테이블);
        verify(orderTableDao).save(피자주문테이블);
    }

    @DisplayName("단체지정 해제시 단체지정의 주문테이블들 중 주문상태가 조리, 식사인 것이 있는 경우 테스트")
    @Test
    void ungroupWithCookingOrMealOrderStatus() {
        given(orderTableDao.findAllByTableGroupId(단체지정.getId())).willReturn(
                Lists.newArrayList(단체지정_치킨주문테이블, 단체지정_피자주문테이블));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Lists.newArrayList(치킨주문테이블.getId(), 피자주문테이블.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(단체지정.getId()));
    }
}
