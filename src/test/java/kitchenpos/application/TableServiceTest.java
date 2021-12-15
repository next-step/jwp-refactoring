package kitchenpos.application;

import kitchenpos.application.fixture.OrderTableFixture;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;

    @InjectMocks
    TableService tableService;

    private OrderTable 빈_테이블;
    private OrderTable 사용중인_테이블;
    private OrderTable 상태변경_전_테이블;
    private OrderTable 상태변경_후_테이블;
    private OrderTable 손님_수_변경_전_테이블;
    private OrderTable 손님_수_변경_후_테이블;

    @BeforeEach
    void setup() {
        빈_테이블 = OrderTableFixture.create(1L, null, 0, true);
        사용중인_테이블 = OrderTableFixture.create(2L, null, 4, false);
        상태변경_전_테이블 = OrderTableFixture.create(3L, 1L, 4, true);
        상태변경_후_테이블 = OrderTableFixture.create(3L, null, 4, false);
        손님_수_변경_전_테이블 = OrderTableFixture.create(4L, null, 2, false);
        손님_수_변경_후_테이블 = OrderTableFixture.create(4L, null, 4, false);
    }

    @DisplayName("테이블 등록 확인")
    @Test
    void 테이블_등록_확인() {
        // given
        OrderTable 등록_요청_데이터 = new OrderTable();
        등록_요청_데이터.setNumberOfGuests(0);

        given(orderTableDao.save(any(OrderTable.class))).willReturn(빈_테이블);

        // when
        OrderTable 등록된_테이블 = tableService.create(등록_요청_데이터);

        // then
        assertAll(
                () -> assertThat(등록된_테이블).isEqualTo(빈_테이블),
                () -> assertThat(등록된_테이블.getTableGroupId()).isNull()
        );
    }

    @DisplayName("테이블 목록 확인")
    @Test
    void 메뉴_목록_확인() {
        // given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(빈_테이블, 사용중인_테이블));

        // when
        List<OrderTable> 메뉴_목록 = tableService.list();

        // then
        assertThat(메뉴_목록).containsExactly(빈_테이블, 사용중인_테이블);
    }

    @DisplayName("테이블 상태 변경 테스트")
    @Nested
    class TestChangeTableEmpty {
        @DisplayName("테이블 상태 변경")
        @Test
        void 테이블_상태_변경() {
            // given
            OrderTable 변경_요청_데이터 = new OrderTable();
            변경_요청_데이터.setEmpty(false);

            given(orderTableDao.findById(any(Long.TYPE)))
                    .willReturn(Optional.of(사용중인_테이블));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(Long.TYPE), any()))
                    .willReturn(false);
            given(orderTableDao.save(any(OrderTable.class)))
                    .willReturn(상태변경_후_테이블);

            // when
            OrderTable 변경된_테이블 = tableService.changeEmpty(3L, 변경_요청_데이터);

            // then
            assertThat(변경된_테이블.isEmpty()).isEqualTo(변경_요청_데이터.isEmpty());
        }

        @DisplayName("존재하지 않는 테이블 상태 변경")
        @Test
        void 존재하지_않는_테이블_상태_변경() {
            // given
            OrderTable 변경_요청_데이터 = new OrderTable();
            변경_요청_데이터.setEmpty(false);

            given(orderTableDao.findById(any(Long.TYPE)))
                    .willReturn(Optional.empty());

            // when
            ThrowableAssert.ThrowingCallable 상태_변경_요청 = () -> tableService.changeEmpty(3L, 변경_요청_데이터);

            // then
            assertThatThrownBy(상태_변경_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("그룹이 존재하는 테이블 상태 변경")
        @Test
        void 그룹이_존재하는_테이블_상태_변경() {
            // given
            OrderTable 변경_요청_데이터 = new OrderTable();
            변경_요청_데이터.setEmpty(false);

            given(orderTableDao.findById(any(Long.TYPE)))
                    .willReturn(Optional.of(상태변경_전_테이블));

            // when
            ThrowableAssert.ThrowingCallable 상태_변경_요청 = () -> tableService.changeEmpty(3L, 변경_요청_데이터);

            // then
            assertThatThrownBy(상태_변경_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조리 또는 식사 상태의 주문을 가진 테이블 상태 변경")
        @Test
        void 조리_또는_식사_상태의_주문을_가진_테이블_상태_변경() {
            // given
            OrderTable 변경_요청_데이터 = new OrderTable();
            변경_요청_데이터.setEmpty(false);

            given(orderTableDao.findById(any(Long.TYPE)))
                    .willReturn(Optional.of(사용중인_테이블));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(Long.TYPE), any()))
                    .willReturn(true);

            // when
            ThrowableAssert.ThrowingCallable 상태_변경_요청 = () -> tableService.changeEmpty(3L, 변경_요청_데이터);

            // then
            assertThatThrownBy(상태_변경_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("테이블 손님 수 변경 테스트")
    @Nested
    class TestChangeNumberOfGuests {
        @DisplayName("테이블 손님 수 변경")
        @Test
        void 테이블_손님_수_변경() {
            // given
            OrderTable 변경_요청_데이터 = new OrderTable();
            변경_요청_데이터.setNumberOfGuests(4);

            given(orderTableDao.findById(any(Long.TYPE)))
                    .willReturn(Optional.of(손님_수_변경_전_테이블));
            given(orderTableDao.save(any(OrderTable.class)))
                    .willReturn(손님_수_변경_후_테이블);

            // when
            OrderTable 변경된_테이블 = tableService.changeNumberOfGuests(4L, 변경_요청_데이터);

            // then
            assertThat(변경된_테이블.getNumberOfGuests()).isEqualTo(변경_요청_데이터.getNumberOfGuests());
        }

        @DisplayName("변경되는 손님 수가 0이상이 아님")
        @Test
        void 변경되는_손님_수가_0이상이_아님() {
            // given
            OrderTable 변경_요청_데이터 = new OrderTable();
            변경_요청_데이터.setNumberOfGuests(-1);

            // when
            ThrowableAssert.ThrowingCallable 손님_수_변경_요청 = () -> tableService.changeNumberOfGuests(4L, 변경_요청_데이터);

            // then
            assertThatThrownBy(손님_수_변경_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("대상 테이블이 존재하지 않음")
        @Test
        void 대상_테이블이_존재하지_않음() {
            // given
            OrderTable 변경_요청_데이터 = new OrderTable();
            변경_요청_데이터.setNumberOfGuests(4);

            given(orderTableDao.findById(any(Long.TYPE)))
                    .willReturn(Optional.empty());

            // when
            ThrowableAssert.ThrowingCallable 손님_수_변경_요청 = () -> tableService.changeNumberOfGuests(4L, 변경_요청_데이터);

            // then
            assertThatThrownBy(손님_수_변경_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("대상 테이블 상태가 비어있음")
        @Test
        void 대상_테이블_상태가_비어있음() {
            // given
            OrderTable 변경_요청_데이터 = new OrderTable();
            변경_요청_데이터.setNumberOfGuests(4);

            given(orderTableDao.findById(any(Long.TYPE)))
                    .willReturn(Optional.of(빈_테이블));

            // when
            ThrowableAssert.ThrowingCallable 손님_수_변경_요청 = () -> tableService.changeNumberOfGuests(1L, 변경_요청_데이터);

            // then
            assertThatThrownBy(손님_수_변경_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
