package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.TableServiceTest.generateOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체지정")
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private TableGroupDao tableGroupDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderDao orderDao;

    private OrderTable 메뉴테이블1;
    private OrderTable 메뉴테이블2;
    private OrderTable 메뉴테이블3;
    private OrderTable 메뉴테이블4;

    List<Long> 메뉴테이블_아이디들;

    private TableGroup 단체지정;


    @BeforeEach
    void setUp() {
        메뉴테이블1 = generateOrderTable(1L, 5, true);
        메뉴테이블2 = generateOrderTable(2L, 3, true);
        메뉴테이블3 = generateOrderTable(3L, 1L, 3, true);
        메뉴테이블4 = generateOrderTable(4L, 3, false);

        메뉴테이블_아이디들 = Arrays.asList(메뉴테이블1.getId(), 메뉴테이블2.getId());

        단체지정 = generateTable(1L, Arrays.asList(메뉴테이블1, 메뉴테이블2));
    }

    @Test
    @DisplayName("단체 지정을 할 수 있다.")
    void tableGroupTest1() {
        given(orderTableDao.findAllByIdIn(메뉴테이블_아이디들)).willReturn(Arrays.asList(메뉴테이블1, 메뉴테이블2));
        given(tableGroupDao.save(단체지정)).willReturn(단체지정);
        given(orderTableDao.save(메뉴테이블1)).willReturn(메뉴테이블1);
        given(orderTableDao.save(메뉴테이블2)).willReturn(메뉴테이블2);

        TableGroup 추가된_단체지정 = tableGroupService.create(단체지정);
        assertThat(추가된_단체지정.getId()).isEqualTo(단체지정.getId());
    }

    @Test
    @DisplayName("단체 지정 : 주문 테이블은 필수값이며, 2개 미만이어선 안된다.")
    void tableGroupTest2() {
        TableGroup 주문테이블이_NULL인_단체지정 = generateTable(1L, null);
        TableGroup 주문테이블이_2개미만인_단체지정 = generateTable(2L, Arrays.asList(메뉴테이블1));

        assertThatThrownBy(() -> tableGroupService.create(주문테이블이_NULL인_단체지정))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> tableGroupService.create(주문테이블이_2개미만인_단체지정))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 : 주문 테이블은 존재하는 주문 테이블들로만 구성되야 한다.")
    void tableGroupTest3() {
        given(orderTableDao.findAllByIdIn(메뉴테이블_아이디들)).willReturn(Arrays.asList(메뉴테이블1));

        assertThatThrownBy(() -> tableGroupService.create(단체지정))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 : 다른 테이블 그룹에 속한 주문 테이블로는 요청할 수 없다.")
    void tableGroupTest4() {
        given(orderTableDao.findAllByIdIn(메뉴테이블_아이디들)).willReturn(Arrays.asList(메뉴테이블1, 메뉴테이블3));

        assertThatThrownBy(() -> tableGroupService.create(단체지정))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 : 주문 테이블이 비어있지 않으면 요청할 수 없다.")
    void tableGroupTest5() {
        given(orderTableDao.findAllByIdIn(메뉴테이블_아이디들)).willReturn(Arrays.asList(메뉴테이블1, 메뉴테이블4));

        assertThatThrownBy(() -> tableGroupService.create(단체지정))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 제거할 수 있다.")
    void tableGroupTest6() {
        given(orderTableDao.findAllByTableGroupId(단체지정.getId())).willReturn(Arrays.asList(메뉴테이블1, 메뉴테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(메뉴테이블1.getId(), 메뉴테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(메뉴테이블1)).willReturn(메뉴테이블1);
        given(orderTableDao.save(메뉴테이블2)).willReturn(메뉴테이블2);

        tableGroupService.ungroup(단체지정.getId());
    }

    @Test
    @DisplayName("단체 지정 제거 : 주문 테이블의 상태가 조리중이거나 식사중이면 안된다.")
    void tableGroupTest7() {
        given(orderTableDao.findAllByTableGroupId(단체지정.getId())).willReturn(Arrays.asList(메뉴테이블1, 메뉴테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(메뉴테이블1.getId(), 메뉴테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(단체지정.getId()))
                .isInstanceOf(IllegalArgumentException.class);

    }

    public static TableGroup generateTable(long id, List<OrderTable> orderTables) {
        return TableGroup.of(id, null, orderTables);
    }

}
