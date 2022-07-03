package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.OrderTableFixture.주문_테이블_데이터_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    private OrderTable 주문_테이블;
    private OrderTable 변경_테이블;

    @DisplayName("테이블 생성")
    @Test
    void create() {
        // given
        주문_테이블 = 주문_테이블_데이터_생성(1L, null, 0, true);
        given(orderTableDao.save(any())).willReturn(주문_테이블);

        // when
        OrderTable orderTable = tableService.create(주문_테이블);

        // then
        assertThat(orderTable).isEqualTo(주문_테이블);
    }

    @DisplayName("테이블 목록 조회")
    @Test
    void list() {
        // given
        주문_테이블 = 주문_테이블_데이터_생성(1L, null, 0, true);
        given(orderTableDao.findAll()).willReturn(Collections.singletonList(주문_테이블));

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertAll(
                () -> assertThat(orderTables).hasSize(1),
                () -> assertThat(orderTables).containsExactly(주문_테이블)
        );
    }

    @DisplayName("빈 테이블로 변경")
    @Test
    void changeEmpty() {
        // given
        주문_테이블 = 주문_테이블_데이터_생성(1L, null, 3, false);
        변경_테이블 = 주문_테이블_데이터_생성(2L, null, 0, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(주문_테이블);

        // when
        OrderTable orderTable = tableService.changeEmpty(1L, 변경_테이블);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void 존재하지않는_테이블의_상태를_변경할_경우() {
        // given
        변경_테이블 = 주문_테이블_데이터_생성(2L, null, 0, true);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, 변경_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정된_주문_테이블의_상태를_변경할_경우() {
        // given
        주문_테이블 = 주문_테이블_데이터_생성(1L, 1L, 3, false);
        변경_테이블 = 주문_테이블_데이터_생성(2L, null, 0, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, 변경_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 조리_또는_식사중인_상태의_테이블을_변경하는_경우() {
        // given
        주문_테이블 = 주문_테이블_데이터_생성(1L, null, 3, false);
        변경_테이블 = 주문_테이블_데이터_생성(2L, null, 0, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, 변경_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인원 수 변경")
    @Test
    void changeNumberOfGuests() {
        // given
        주문_테이블 = 주문_테이블_데이터_생성(1L, null, 3, false);
        변경_테이블 = 주문_테이블_데이터_생성(2L, null, 0, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블));
        given(orderTableDao.save(any())).willReturn(주문_테이블);

        // when
        OrderTable orderTable = tableService.changeNumberOfGuests(1L, 변경_테이블);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(0);
    }

    @Test
    void 손님수를_0미만으로_변경할_경우() {
        // given
        변경_테이블 = 주문_테이블_데이터_생성(2L, null, -1, true);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 변경_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_테이블의_손님_수를_변경할_경우() {
        // given
        변경_테이블 = 주문_테이블_데이터_생성(2L, null, 0, true);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 변경_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 비어있는_테이블의_손님_수를_변경할_경우() {
        // given
        주문_테이블 = 주문_테이블_데이터_생성(1L, null, 0, true);
        변경_테이블 = 주문_테이블_데이터_생성(2L, null, 0, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 변경_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
