package kitchenpos.table.application;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderTableDao;
import kitchenpos.table.domain.TableGroupDao;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.application.TableGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    TableGroupDao tableGroupDao;

    @InjectMocks
    TableGroupService tableGroupService;

    private TableGroup 생성할_단체_지정;
    private TableGroup 저장된_단체_지정;

    private OrderTable 단체_지정할_테이블1;
    private OrderTable 단체_지정할_테이블2;
    private List<OrderTable> 단체_지정할_테이블_목록;

    private OrderTable 단체_지정된_테이블1;
    private OrderTable 단체_지정된_테이블2;
    private List<OrderTable> 단체_지정된_테이블_목록;

    @BeforeEach
    void setUp() {
        단체_지정할_테이블1 = new OrderTable(1L, null, 4, true);
        단체_지정할_테이블2 = new OrderTable(2L, null, 6, true);
        단체_지정할_테이블_목록 = Arrays.asList(단체_지정할_테이블1, 단체_지정할_테이블2);
        생성할_단체_지정 = new TableGroup(단체_지정할_테이블_목록);

        단체_지정된_테이블1 = new OrderTable(1L, 1L, 4, false);
        단체_지정된_테이블2 = new OrderTable(2L, 1L, 6, false);
        단체_지정된_테이블_목록 = Arrays.asList(단체_지정된_테이블1, 단체_지정된_테이블2);
        저장된_단체_지정 = new TableGroup(1L, LocalDateTime.now(), 단체_지정된_테이블_목록);
    }

    @DisplayName("단체 지정(주문 테이블 그룹화)을 할 수 있다.")
    @Test
    void createTableGroup() {
        // given
        given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(단체_지정할_테이블_목록);
        given(tableGroupDao.save(생성할_단체_지정))
                .willReturn(저장된_단체_지정);
        given(orderTableDao.save(단체_지정할_테이블1))
                .willReturn(단체_지정된_테이블1);
        given(orderTableDao.save(단체_지정할_테이블2))
                .willReturn(단체_지정된_테이블2);

        // when
        TableGroup 생성된_단체_지정 = tableGroupService.create(생성할_단체_지정);

        // then
        단체_지정_성공(생성된_단체_지정, 생성할_단체_지정);
    }

    @DisplayName("주문 테이블이 2개 미만인 경우 단체 지정에 실패한다.")
    @Test
    void createTableGroupFailsWhenNotEnoughOrderTables() {
        // given
        생성할_단체_지정 = new TableGroup(Arrays.asList(단체_지정할_테이블1));

        // when & then
        단체_지정_실패(생성할_단체_지정);
    }

    @DisplayName("중복된 주문 테이블이 존재하면 단체 지정에 실패한다.")
    @Test
    void createTableGroupFailsWhenDuplicateOrderTables() {
        List<OrderTable> 유니크한_주문테이블_목록 = Arrays.asList(단체_지정할_테이블1);
        given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(유니크한_주문테이블_목록);

        // when & then
        단체_지정_실패(생성할_단체_지정);
    }

    @DisplayName("비어있지 않은 주문 테이블이 존재하면 단체 지정에 실패한다.")
    @Test
    void createTableGroupFailsWhenNotEmptyOrderTables() {
        // given
        OrderTable 빈_테이블 = new OrderTable(1L, null, 4, true);
        OrderTable 비어있지_않은_테이블 = new OrderTable(2L, null, 6, false);
        단체_지정할_테이블_목록 = Arrays.asList(빈_테이블, 비어있지_않은_테이블);

        given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(단체_지정할_테이블_목록);

        // when & then
        단체_지정_실패(생성할_단체_지정);
    }

    @DisplayName("단체 지정되어 있는 주문 테이블이 존재하면 단체 지정에 실패한다.")
    @Test
    void createTableGroupFailsWhenAlreadyGrouped() {
        // given
        OrderTable 단체_저장되지_않은_테이블 = new OrderTable(1L, null, 4, true);
        OrderTable 단체_지정된_테이블 = new OrderTable(2L, 1L, 6, true);
        단체_지정할_테이블_목록 = Arrays.asList(단체_저장되지_않은_테이블, 단체_지정된_테이블);

        given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(단체_지정할_테이블_목록);

        // when & then
        단체_지정_실패(생성할_단체_지정);
    }

    @DisplayName("단체 지정을 해제 할 수 있다.")
    @Test
    void ungroupTableGroup() {
        // given
        given(orderTableDao.findAllByTableGroupId(저장된_단체_지정.getId()))
                .willReturn(단체_지정된_테이블_목록);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(false);

        // when & then
        단체_지정_해제_성공(저장된_단체_지정.getId());
    }

    @DisplayName("주문 테이블이 '조리' 혹은 '식사' 상태이면 단체 지정에 실패한다.")
    @Test
    void ungroupTableGroupFailsWhenCookingOrMealOrderStatus() {
        // given
        given(orderTableDao.findAllByTableGroupId(anyLong()))
                .willReturn(단체_지정된_테이블_목록);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(true);

        // when & then
        단체_지정_해제_실패(저장된_단체_지정.getId());
    }

    private void 단체_지정_성공(TableGroup 생성된_단체_지정, TableGroup 생성할_단체_지정) {
        assertAll(
                () -> assertThat(생성된_단체_지정.getId())
                        .isNotNull(),
                () -> assertThat(생성된_단체_지정.getCreatedDate())
                        .isNotNull(),
                () -> assertThat(생성된_단체_지정.getOrderTables())
                        .containsExactlyElementsOf(생성할_단체_지정.getOrderTables())
        );
    }

    private void 단체_지정_실패(TableGroup 생성할_단체_지정) {
        assertThatThrownBy(() -> tableGroupService.create(생성할_단체_지정))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private void 단체_지정_해제_성공(Long 해제할_단체_지정ID) {
        tableGroupService.ungroup(해제할_단체_지정ID);
    }

    private void 단체_지정_해제_실패(Long 해제할_단체_지정ID) {
        assertThatThrownBy(() -> tableGroupService.ungroup(해제할_단체_지정ID))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
