package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;


@DisplayName("테이블그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;


    private TableGroup 테이블그룹;
    private OrderTable 주문테이블_4명;
    private OrderTable 주문테이블_6명;

    @BeforeEach
    void set_up() {
        주문테이블_4명 = OrderTableFixture.create(1L, null, 4, true);
        주문테이블_6명 = OrderTableFixture.create(2L, null, 6, true);
        테이블그룹 = TableGroupFixture.create(
                1L, LocalDateTime.now(), Arrays.asList(주문테이블_4명, 주문테이블_6명));
    }

    @DisplayName("테이블 그룹을 등록할 수 있다.")
    @Test
    void create() {
        // given
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(주문테이블_4명, 주문테이블_6명));
        when(tableGroupDao.save(any())).thenReturn(테이블그룹);

        // when
        TableGroup 테이블_그룹_등록 = tableGroupService.create(테이블그룹);

        // then
        assertThat(테이블_그룹_등록).isEqualTo(테이블그룹);
    }

    @DisplayName("주문 테이블 리스트가 없거나 테이블이 2개 미만이면 테이블 그룹을 등록할 수 없다.")
    @Test
    void create_error_order_table_less_then_2() {
        // given
        테이블그룹.setOrderTables(new ArrayList<>());

        // when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 리스트가 저장된 주문 테이블 리스트의 개수와 일치하지 않으면 테이블 그룹을 등록할 수 없다.")
    @Test
    void create_error_not_match_size() {
        // given
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(주문테이블_4명));

        // when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있지 않거나 주문 테이블의 다른 그룹 테이블 정보가 있다면 테이블 그룹을 등록할 수 없다.")
    @Test
    void create_error_using_table() {
        // given
        OrderTable 주문테이블_그룹있음 = OrderTableFixture.create(1L, 1L, 6, true);
        테이블그룹.setOrderTables(Arrays.asList(주문테이블_4명, 주문테이블_6명, 주문테이블_그룹있음));

        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(주문테이블_4명, 주문테이블_6명, 주문테이블_그룹있음));

        // when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록을 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        OrderTable 주문테이블_그룹있음 = OrderTableFixture.create(1L, 99L, 6, true);
        테이블그룹.setOrderTables(Arrays.asList(주문테이블_그룹있음));

        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(주문테이블_그룹있음));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);

        // when
        tableGroupService.ungroup(테이블그룹.getId());

        // then
        assertAll(
                () -> assertThat(주문테이블_그룹있음.getTableGroupId()).isNull(),
                () -> assertThat(테이블그룹.getOrderTables()).hasSize(1)
        );
    }

    @DisplayName("테이블 그룹의 주문 테이블 리스트 중에 주문상태가 요리 식사 중인 경우 테이블 그룹 등록을 해제할 수 없다.")
    @Test
    void ungroup_error_table_meal() {
        // given
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(주문테이블_4명, 주문테이블_6명));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(주문테이블_4명.getId(), 주문테이블_6명.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(true);

        // when && then
        Assertions.assertThatThrownBy(() -> {
            tableGroupService.ungroup(테이블그룹.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
