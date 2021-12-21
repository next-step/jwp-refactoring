package kitchenpos.application;

import kitchenpos.common.fixtrue.OrderTableFixture;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @InjectMocks
    TableService tableService;

    OrderTable 빈_테이블;
    OrderTable 주문_테이블;
    OrderTable 단체_지정_테이블;

    @BeforeEach
    void setUp() {
        빈_테이블 = OrderTableFixture.of(1L, null, 0, true);
        주문_테이블 = OrderTableFixture.of(2L, null, 5, false);
        단체_지정_테이블 = OrderTableFixture.of(3L, 1L, 5, false);
    }

    @Test
    void 테이블_생성() {
        // given
        given(orderTableDao.save(any())).willReturn(빈_테이블);

        // when
        OrderTable actual = tableService.create(빈_테이블);

        // then
        assertAll(() -> {
            assertThat(actual).isEqualTo(빈_테이블);
            assertThat(actual.getTableGroupId()).isNull();
        });
    }

    @Test
    void 테이블_조회() {
        // given
        List<OrderTable> orderTables = Arrays.asList(빈_테이블, 주문_테이블);
        given(orderTableDao.findAll()).willReturn(orderTables);

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(2);
            assertThat(actual).containsExactlyElementsOf(Arrays.asList(빈_테이블, 주문_테이블));
        });
    }

    @DisplayName("빈 테이블을 주문 테이블로 변경한다.")
    @Test
    void 테이블_상태_변경() {
        // given
        OrderTable 변경된_빈_테이블 = OrderTableFixture.of(빈_테이블.getId(), null, 0, false);

        given(orderTableDao.findById(빈_테이블.getId())).willReturn(Optional.of(빈_테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(
                any(), anyList())).willReturn(false);
        given(orderTableDao.save(빈_테이블)).willReturn(변경된_빈_테이블);

        // when
        OrderTable actual = tableService.changeEmpty(빈_테이블.getId(), 빈_테이블);

        // then
        assertAll(() -> {
            assertThat(actual).isEqualTo(변경된_빈_테이블);
            assertThat(actual.isEmpty()).isEqualTo(변경된_빈_테이블.isEmpty());
        });
    }

    @Test
    void 테이블_상태_변경_시_테이블은_반드시_존재해야한다() {
        // given
        given(orderTableDao.findById(빈_테이블.getId())).willReturn(Optional.empty());

        // when
        ThrowingCallable throwingCallable = () -> tableService.changeEmpty(빈_테이블.getId(), 빈_테이블);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 단체_지정_된_테이블은_상태를_변경할_수_없다() {
        // given
        given(orderTableDao.findById(단체_지정_테이블.getId())).willReturn(Optional.of(단체_지정_테이블));

        // when
        ThrowingCallable throwingCallable = () -> tableService.changeEmpty(단체_지정_테이블.getId(), 단체_지정_테이블);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 주문_테이블의_상태가_조리이거나_식사이면_변경할_수_없다() {
        // given
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.of(주문_테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(
                any(), anyList())).willReturn(true);

        // when
        ThrowingCallable throwingCallable = () -> tableService.changeEmpty(주문_테이블.getId(), 주문_테이블);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 방문한_손님_수_변경() {
        // given
        OrderTable 방문자_수_변경된_주문_테이블 = OrderTableFixture.of(주문_테이블.getId(), null, 3, false);
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.of(주문_테이블));
        given(orderTableDao.save(any())).willReturn(방문자_수_변경된_주문_테이블);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블);

        // then
        assertAll(() -> {
            assertThat(actual).isEqualTo(방문자_수_변경된_주문_테이블);
            assertThat(actual.getNumberOfGuests()).isEqualTo(방문자_수_변경된_주문_테이블.getNumberOfGuests());
        });
    }

    @Test
    void 방문한_손님의_수_변경_시_손님는_0명_이상이어야_한다() {
        // given
        OrderTable 방문자수_0명_미만_테이블 = OrderTableFixture.of(4L, 1L, -1, false);

        // when
        ThrowingCallable throwingCallable = () -> tableService.changeNumberOfGuests(방문자수_0명_미만_테이블.getId(), 방문자수_0명_미만_테이블);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 방문한_손님_수_변경_시_빈_테이블이면_변경할_수_없다() {
        // given
        given(orderTableDao.findById(빈_테이블.getId())).willReturn(Optional.of(빈_테이블));

        // when
        ThrowingCallable throwingCallable = () -> tableService.changeNumberOfGuests(빈_테이블.getId(), 빈_테이블);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }
}
