package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.UnitTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private UnitTestFixture 식당_포스;

    @BeforeEach
    void setUp() {
        식당_포스 = new UnitTestFixture();
    }

    @Test
    void 주문_테이블을_등록할_수_있어야_한다() {
        // given
        final OrderTable given = new OrderTable(1L, null, 2, false);
        when(orderTableDao.save(given)).thenReturn(given);

        // when
        final OrderTable actual = tableService.create(given);

        // then
        assertThat(given).isEqualTo(actual);
    }

    @Test
    void 주문_테이블_목록을_조회할_수_있어야_한다() {
        // given
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(식당_포스.테이블, 식당_포스.빈_테이블1));

        // when
        final List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).containsExactly(식당_포스.테이블, 식당_포스.빈_테이블1);
    }

    @Test
    void 주문_테이블이_비었는지_여부를_변경할_수_있어야_한다() {
        // given
        when(orderTableDao.findById(식당_포스.테이블.getId())).thenReturn(Optional.of(식당_포스.테이블));

        // when
        tableService.changeEmpty(식당_포스.테이블.getId(),
                new OrderTable(식당_포스.테이블.getId(), 식당_포스.테이블.getTableGroupId(), 0, true));

        // then
        assertThat(식당_포스.테이블.isEmpty()).isTrue();
    }

    @Test
    void 비었는지_여부_변경_시_주문_테이블이_단체_등록_테이블이면_에러가_발생해야_한다() {
        // given
        when(orderTableDao.findById(식당_포스.단체_지정_테이블1.getId())).thenReturn(Optional.of(식당_포스.단체_지정_테이블1));

        // when and then
        assertThatThrownBy(() -> tableService.changeEmpty(식당_포스.단체_지정_테이블1.getId(), new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 비었는지_여부_변경_시_주문_테이블의_주문이_결제완료_상태가_아니면_에러가_발생해야_한다() {
        // given
        when(orderTableDao.findById(식당_포스.테이블.getId())).thenReturn(Optional.of(식당_포스.테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                식당_포스.테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(true);

        // when and then
        assertThatThrownBy(() -> tableService.changeEmpty(식당_포스.테이블.getId(), new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_손님_수를_변경할_수_있어야_한다() {
        // given
        when(orderTableDao.findById(식당_포스.테이블.getId())).thenReturn(Optional.of(식당_포스.테이블));

        // when
        tableService.changeNumberOfGuests(식당_포스.테이블.getId(),
                new OrderTable(식당_포스.테이블.getId(), 식당_포스.테이블.getTableGroupId(), 1, false));

        // then
        assertThat(식당_포스.테이블.getNumberOfGuests()).isEqualTo(1);
    }

    @Test
    void 손님_수_변경_시_0명보다_적은_수로_변경하려고_하면_에러가_발생한다() {
        // when and then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(식당_포스.테이블.getId(),
                new OrderTable(식당_포스.테이블.getId(), 식당_포스.테이블.getTableGroupId(), -1, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님_수_변경_시_주문_테이블의_빈_테이블이면_에러가_발생해야_한다() {
        // given
        when(orderTableDao.findById(식당_포스.빈_테이블1.getId())).thenReturn(Optional.of(식당_포스.빈_테이블1));

        // when and then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(식당_포스.빈_테이블1.getId(), new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
