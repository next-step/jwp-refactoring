package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.UnitTestFixture;
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

    private UnitTestFixture 식당_포스;

    @BeforeEach
    void setUp() {
        식당_포스 = new UnitTestFixture();
    }

    @Test
    void 단체_지정을_할_수_있어야_한다() {
        // given
        final TableGroup given = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(식당_포스.빈_테이블1, 식당_포스.빈_테이블2));
        when(orderTableDao.findAllByIdIn(Arrays.asList(식당_포스.빈_테이블1.getId(), 식당_포스.빈_테이블2.getId())))
                .thenReturn(Arrays.asList(식당_포스.빈_테이블1, 식당_포스.빈_테이블2));
        when(tableGroupDao.save(given)).thenReturn(given);

        // when
        final TableGroup actual = tableGroupService.create(given);

        // then
        assertThat(given).isEqualTo(actual);
    }
    
    @Test
    void 단체_지정_시_주문_테이블이_2개_이상이_아니면_에러가_발생해야_한다() {
        // given
        final TableGroup given = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(식당_포스.빈_테이블1));
        
        // when and then
        assertThatThrownBy(() -> tableGroupService.create(given))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_시_주문_테이블이_하나라도_존재하지_않으면_에러가_발생해야_한다() {
        // given
        final Long invalidOrderTableId = -1L;
        final TableGroup given = new TableGroup(
                1L, LocalDateTime.now(), Arrays.asList(식당_포스.빈_테이블1, new OrderTable(invalidOrderTableId, null, 0, true)));
        when(orderTableDao.findAllByIdIn(Arrays.asList(식당_포스.빈_테이블1.getId(), invalidOrderTableId)))
                .thenReturn(Arrays.asList(식당_포스.빈_테이블1));

        // when and then
        assertThatThrownBy(() -> tableGroupService.create(given))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_시_주문_테이블이_하나라도_빈_테이블이_아니면_에러가_발생해야_한다() {
        // given
        final TableGroup given = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(식당_포스.빈_테이블1, 식당_포스.테이블));
        when(orderTableDao.findAllByIdIn(Arrays.asList(식당_포스.빈_테이블1.getId(), 식당_포스.테이블.getId())))
                .thenReturn(Arrays.asList(식당_포스.빈_테이블1, 식당_포스.테이블));

        // when and then
        assertThatThrownBy(() -> tableGroupService.create(given))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_시_주문_테이블이_하나라도_단체_지정이_되어_있으면_에러가_발생해야_한다() {
        // given
        final TableGroup given = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(식당_포스.빈_테이블1, 식당_포스.단체_지정_빈_테이블));
        when(orderTableDao.findAllByIdIn(Arrays.asList(식당_포스.빈_테이블1.getId(), 식당_포스.단체_지정_빈_테이블.getId())))
                .thenReturn(Arrays.asList(식당_포스.빈_테이블1, 식당_포스.단체_지정_빈_테이블));

        // when and then
        assertThatThrownBy(() -> tableGroupService.create(given))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정을_해제할_수_있어야_한다() {
        // given
        when(orderTableDao.findAllByTableGroupId(식당_포스.단체1.getId()))
                .thenReturn(Arrays.asList(식당_포스.단체_지정_테이블1, 식당_포스.단체_지정_테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(식당_포스.단체_지정_테이블1.getId(), 식당_포스.단체_지정_테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(false);

        // when
        tableGroupService.ungroup(식당_포스.단체1.getId());

        // then
        assertThat(식당_포스.단체_지정_테이블1.getTableGroupId()).isNull();
        assertThat(식당_포스.단체_지정_테이블2.getTableGroupId()).isNull();
    }

    @Test
    void 단체_지정_해제_시_조리_또는_식사_중인_테이블이_있으면_에러가_발생해야_한다() {
        // given
        when(orderTableDao.findAllByTableGroupId(식당_포스.단체1.getId()))
                .thenReturn(Arrays.asList(식당_포스.단체_지정_테이블1, 식당_포스.단체_지정_테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(식당_포스.단체_지정_테이블1.getId(), 식당_포스.단체_지정_테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(true);

        // when and then
        assertThatThrownBy(() -> tableGroupService.ungroup(식당_포스.단체1.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
