package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    private OrderTable 첫번째_주문_테이블;
    private OrderTable 두번째_주문_테이블;

    @BeforeEach
    void setUp() {
        첫번째_주문_테이블 = OrderTable.of(1L, null, 4, false);
        두번째_주문_테이블 = OrderTable.of(2L, null, 2, true);
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void 주문_테이블을_생성할_수_있다() {
        // given
        when(orderTableDao.save(첫번째_주문_테이블)).thenReturn(첫번째_주문_테이블);

        // when
        OrderTable 저장된_주문_테이블 = tableService.create(첫번째_주문_테이블);

        // then
        assertAll(() -> {
            assertThat(저장된_주문_테이블.getId()).isEqualTo(첫번째_주문_테이블.getId());
            assertThat(저장된_주문_테이블.getTableGroupId()).isNull();
            assertThat(저장된_주문_테이블.getNumberOfGuests()).isEqualTo(첫번째_주문_테이블.getNumberOfGuests());
            assertThat(저장된_주문_테이블.isEmpty()).isFalse();
        });
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void 주문_테이블_목록을_조회할_수_있다() {
        // given
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(첫번째_주문_테이블, 두번째_주문_테이블));

        // when
        List<OrderTable> 조회된_주문_테이블_목록 = tableService.list();

        // then
        assertThat(조회된_주문_테이블_목록).hasSize(2);
        assertThat(조회된_주문_테이블_목록).containsExactly(첫번째_주문_테이블, 두번째_주문_테이블);
    }

    @DisplayName("주문 테이블은 반드시 등록되어 있어야 한다.")
    @Test
    void 주문_테이블은_반드시_등록되어_있어야_한다() {
        // given
        when(orderTableDao.findById(첫번째_주문_테이블.getId())).thenReturn(Optional.empty());

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(첫번째_주문_테이블.getId(), 첫번째_주문_테이블));
    }

    @DisplayName("주문 테이블은 단체 지정이 되어 있지 않아야 한다.")
    @Test
    void 주문_테이블은_단체_지정이_되어_있지_않아야_한다() {
        // given
        첫번째_주문_테이블.setTableGroupId(1L);
        when(orderTableDao.findById(첫번째_주문_테이블.getId())).thenReturn(Optional.of(첫번째_주문_테이블));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(첫번째_주문_테이블.getId(), 첫번째_주문_테이블));
    }

    @DisplayName("주문 테이블의 주문 상태는 조리 중이거나 식사 중이면 안된다.")
    @Test
    void 주문_테이블의_주문_상태는_조리_중이거나_식사_중이면_안된다() {
        // given
        when(orderTableDao.findById(첫번째_주문_테이블.getId())).thenReturn(Optional.of(첫번째_주문_테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                첫번째_주문_테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(true);

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(첫번째_주문_테이블.getId(), 첫번째_주문_테이블));
    }

    @DisplayName("주문 테이블이 비어있는지 여부를 변경할 수 있다.")
    @Test
    void 주문_테이블이_비어있는지_여부를_변경할_수_있다() {
        // given
        when(orderTableDao.findById(첫번째_주문_테이블.getId())).thenReturn(Optional.of(첫번째_주문_테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                첫번째_주문_테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(false);
        when(orderTableDao.save(첫번째_주문_테이블)).thenReturn(첫번째_주문_테이블);

        // when
        OrderTable 변경된_주문_테이블 = tableService.changeEmpty(첫번째_주문_테이블.getId(), 두번째_주문_테이블);

        // then
        assertThat(변경된_주문_테이블.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블의 방문한 손님 수가 0명 이상이어야 한다.")
    @Test
    void 주문_테이블의_방문한_손님_수가_0명_이상이어야_한다() {
        // given
        OrderTable 세번째_주문_테이블 = OrderTable.of(3L, null, -1, false);

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(첫번째_주문_테이블.getId(), 세번째_주문_테이블));
    }

    @DisplayName("주문 테이블은 등록되어 있어야 한다.")
    @Test
    void 주문_테이블은_등록되어_있어야_한다() {
        // given
        when(orderTableDao.findById(첫번째_주문_테이블.getId())).thenReturn(Optional.empty());

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(첫번째_주문_테이블.getId(), 두번째_주문_테이블));
    }

    @DisplayName("주문 테이블은 빈 테이블이 아니어야 한다.")
    @Test
    void 주문_테이블은_빈_테이블이_아니어야_한다() {
        // given
        when(orderTableDao.findById(두번째_주문_테이블.getId())).thenReturn(Optional.of(두번째_주문_테이블));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(두번째_주문_테이블.getId(), 첫번째_주문_테이블));
    }

    @DisplayName("주문 테이블에 방문한 손님 수를 변경할 수 있다.")
    @Test
    void 주문_테이블에_방문한_손님_수를_변경할_수_있다() {
        // given
        when(orderTableDao.findById(첫번째_주문_테이블.getId())).thenReturn(Optional.of(첫번째_주문_테이블));
        when(orderTableDao.save(첫번째_주문_테이블)).thenReturn(첫번째_주문_테이블);

        // when
        OrderTable 변경된_주문_테이블 = tableService.changeNumberOfGuests(첫번째_주문_테이블.getId(), 두번째_주문_테이블);

        // then
        assertThat(변경된_주문_테이블.getNumberOfGuests()).isEqualTo(2);
    }
}
