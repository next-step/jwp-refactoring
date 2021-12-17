package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    TableGroupService tableGroupService;

    private OrderTable 테이블1번;
    private OrderTable 테이블2번;
    private TableGroup 단체_지정;

    @BeforeEach
    void setUp() {
        테이블1번 = new OrderTable();
        테이블1번.setId(1L);
        테이블1번.setEmpty(true);

        테이블2번 = new OrderTable();
        테이블2번.setId(2L);
        테이블2번.setEmpty(true);

        단체_지정 = new TableGroup();
        단체_지정.setId(1L);
        단체_지정.setOrderTables(Arrays.asList(테이블1번, 테이블2번));
    }

    @DisplayName("단체 지정을 주문 테이블 목록 으로 등록 할 수 있다.")
    @Test
    void create() {
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(테이블1번, 테이블2번));
        given(tableGroupDao.save(단체_지정)).willReturn(단체_지정);
        given(orderTableDao.save(테이블1번)).willReturn(테이블1번);
        given(orderTableDao.save(테이블2번)).willReturn(테이블2번);

        TableGroup createTableGroup = tableGroupService.create(단체_지정);

        assertAll(
                () -> assertThat(createTableGroup).isNotNull(),
                () -> assertThat(테이블1번.isEmpty()).isFalse(),
                () -> assertThat(테이블2번.isEmpty()).isFalse()
        );
    }

    @DisplayName("단체 지정 할 때 주문 테이블이 2개 이상이여야 한다.")
    @Test
    void createOrderTableSizeError() {
        OrderTable 테이블 = new OrderTable();
        테이블.setId(3L);
        테이블.setEmpty(true);

        TableGroup 단체_지정_주문_테이블이_1개 = new TableGroup();
        단체_지정_주문_테이블이_1개.setId(2L);
        단체_지정_주문_테이블이_1개.setOrderTables(Arrays.asList(테이블));

        assertThatThrownBy(
                () -> tableGroupService.create(단체_지정_주문_테이블이_1개)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 할때 주문 테이블은 빈 테이블이어야한다.")
    @Test
    void shouldBeEmptyTable() {
        OrderTable 손님이_채워진_테이블3번 = new OrderTable();
        손님이_채워진_테이블3번.setId(3L);
        손님이_채워진_테이블3번.setEmpty(false);
        OrderTable 손님이_채워진_테이블4번 = new OrderTable();
        손님이_채워진_테이블4번.setId(4L);
        손님이_채워진_테이블4번.setEmpty(false);
        TableGroup 단체_지정 = new TableGroup();
        단체_지정.setId(2L);
        단체_지정.setOrderTables(Arrays.asList(손님이_채워진_테이블3번, 손님이_채워진_테이블4번));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(손님이_채워진_테이블3번, 손님이_채워진_테이블4번));

        assertThatThrownBy(
                () -> tableGroupService.create(단체_지정)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해체 할 수 있다.")
    @Test
    void ungroup() {
        OrderTable 테이블3번 = new OrderTable();
        테이블3번.setId(3L);
        테이블3번.setEmpty(true);
        OrderTable 테이블4번 = new OrderTable();
        테이블4번.setId(4L);
        테이블4번.setEmpty(true);
        TableGroup 단체_지정 = new TableGroup();
        단체_지정.setId(2L);
        단체_지정.setOrderTables(Arrays.asList(테이블3번, 테이블4번));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(테이블3번, 테이블4번));
        given(tableGroupDao.save(단체_지정)).willReturn(단체_지정);
        given(orderTableDao.save(테이블3번)).willReturn(테이블3번);
        given(orderTableDao.save(테이블4번)).willReturn(테이블4번);
        tableGroupService.create(단체_지정);
        given(orderTableDao.findAllByTableGroupId(단체_지정.getId())).willReturn(Arrays.asList(테이블3번, 테이블4번));
        given(orderTableDao.save(테이블3번)).willReturn(테이블3번);
        given(orderTableDao.save(테이블4번)).willReturn(테이블4번);

        tableGroupService.ungroup(단체_지정.getId());

        assertAll(
                () -> assertThat(테이블3번.getTableGroupId()).isEqualTo(null),
                () -> assertThat(테이블4번.getTableGroupId()).isEqualTo(null)
        );
    }

    @DisplayName("단체지정 해제는 주문 테이블 계산 완료일때만 가능하다.")
    @Test
    void shouldBeCompletionStatus() {
        OrderTable 테이블3번 = new OrderTable();
        테이블3번.setId(3L);
        테이블3번.setEmpty(true);
        OrderTable 테이블4번 = new OrderTable();
        테이블4번.setId(4L);
        테이블4번.setEmpty(true);
        TableGroup 단체_지정 = new TableGroup();
        단체_지정.setId(2L);
        단체_지정.setOrderTables(Arrays.asList(테이블3번, 테이블4번));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(테이블3번, 테이블4번));
        given(tableGroupDao.save(단체_지정)).willReturn(단체_지정);
        given(orderTableDao.save(테이블3번)).willReturn(테이블3번);
        given(orderTableDao.save(테이블4번)).willReturn(테이블4번);
        tableGroupService.create(단체_지정);
        given(orderTableDao.findAllByTableGroupId(단체_지정.getId())).willReturn(Arrays.asList(테이블3번, 테이블4번));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        assertThatThrownBy(
                () -> tableGroupService.ungroup(단체_지정.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }

}
