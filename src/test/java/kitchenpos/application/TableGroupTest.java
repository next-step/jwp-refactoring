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
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 첫번째_주문_테이블;
    private OrderTable 두번째_주문_테이블;
    private OrderTable 세번째_주문_테이블;
    private TableGroup 우아한형제들_단체그룹;
    private TableGroup 우테코5기_단체그룹;

    @BeforeEach
    void setUp() {
        첫번째_주문_테이블 = OrderTable.of(1L, null, 4, false);
        두번째_주문_테이블 = OrderTable.of(2L, null, 2, true);
        세번째_주문_테이블 = OrderTable.of(3L, 1L, 4, false);
        우아한형제들_단체그룹 = TableGroup.of(1L, null, Arrays.asList(첫번째_주문_테이블, 두번째_주문_테이블));
        우테코5기_단체그룹 = TableGroup.of(2L, null, Collections.singletonList(첫번째_주문_테이블));
    }

    @DisplayName("주문 테이블들이 2개 이상 있어야 한다.")
    @Test
    void 주문_테이블들이_2개_이상_있어야_한다() {
        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(우테코5기_단체그룹));
    }

    @DisplayName("주문 테이블들은 모두 등록된 주문 테이블이어야 한다.")
    @Test
    void 주문_테이블들은_모두_등록된_주문_테이블이어야_한다() {
        // given
        when(orderTableDao.findAllByIdIn(Arrays.asList(첫번째_주문_테이블.getId(), 두번째_주문_테이블.getId())))
                .thenReturn(Collections.singletonList(첫번째_주문_테이블));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(우아한형제들_단체그룹));
    }

    @DisplayName("주문 테이블들은 빈 테이블이어야 한다.")
    @Test
    void 주문_테이블들은_빈_테이블이어야_한다() {
        // given
        when(orderTableDao.findAllByIdIn(Arrays.asList(첫번째_주문_테이블.getId(), 두번째_주문_테이블.getId())))
                .thenReturn(Arrays.asList(첫번째_주문_테이블, 두번째_주문_테이블));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(우아한형제들_단체그룹));
    }

    @DisplayName("이미 단체 지정된 주문 테이블은 단체 지정할 수 없다.")
    @Test
    void 이미_단체_지정된_주문_테이블은_단체_지정할_수_없다() {
        // given
        우아한형제들_단체그룹.setOrderTables(Arrays.asList(첫번째_주문_테이블, 세번째_주문_테이블));

        when(orderTableDao.findAllByIdIn(Arrays.asList(첫번째_주문_테이블.getId(), 세번째_주문_테이블.getId())))
                .thenReturn(Arrays.asList(첫번째_주문_테이블, 세번째_주문_테이블));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(우아한형제들_단체그룹));
    }

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void 단체_지정을_등록한다() {
        // given
        OrderTable 세번째_주문_테이블 = OrderTable.of(3L, null, 3, true);
        우아한형제들_단체그룹.setOrderTables(Arrays.asList(두번째_주문_테이블, 세번째_주문_테이블));

        when(orderTableDao.findAllByIdIn(Arrays.asList(두번째_주문_테이블.getId(), 세번째_주문_테이블.getId())))
                .thenReturn(Arrays.asList(두번째_주문_테이블, 세번째_주문_테이블));
        when(tableGroupDao.save(우아한형제들_단체그룹)).thenReturn(우아한형제들_단체그룹);
        when(orderTableDao.save(두번째_주문_테이블)).thenReturn(두번째_주문_테이블);
        when(orderTableDao.save(세번째_주문_테이블)).thenReturn(세번째_주문_테이블);

        // when
        TableGroup 저장된_단체그룹 = tableGroupService.create(우아한형제들_단체그룹);

        // then
        assertAll(() -> {
            assertThat(저장된_단체그룹.getId()).isEqualTo(우아한형제들_단체그룹.getId());
            assertThat(저장된_단체그룹.getCreatedDate()).isNotNull();
            assertThat(저장된_단체그룹.getOrderTables()).hasSize(2);
            assertThat(저장된_단체그룹.getOrderTables()).containsExactly(두번째_주문_테이블, 세번째_주문_테이블);
        });
    }

    @DisplayName("단체 내 주문 테이블들의 상태는 조리 중이거나 식사중이면 안된다.")
    @Test
    void 단체_내_주문_테이블들의_상태는_조리_중이거나_식사중이면_안된다() {
        // given
        when(orderTableDao.findAllByTableGroupId(우아한형제들_단체그룹.getId()))
                .thenReturn(Arrays.asList(두번째_주문_테이블, 세번째_주문_테이블));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(두번째_주문_테이블.getId(), 세번째_주문_테이블.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(true);

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(우아한형제들_단체그룹.getId()));
    }

    @DisplayName("단체 지정을 삭제할 수 있다.")
    @Test
    void 단체_지정을_삭제할_수_있다() {
        // given
        when(orderTableDao.findAllByTableGroupId(우아한형제들_단체그룹.getId()))
                .thenReturn(Arrays.asList(두번째_주문_테이블, 세번째_주문_테이블));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(두번째_주문_테이블.getId(), 세번째_주문_테이블.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(false);
        when(orderTableDao.save(두번째_주문_테이블)).thenReturn(두번째_주문_테이블);
        when(orderTableDao.save(세번째_주문_테이블)).thenReturn(세번째_주문_테이블);

        // when
        tableGroupService.ungroup(우아한형제들_단체그룹.getId());

        // then
        assertThat(두번째_주문_테이블.getTableGroupId()).isNull();
        assertThat(세번째_주문_테이블.getTableGroupId()).isNull();
    }
}
