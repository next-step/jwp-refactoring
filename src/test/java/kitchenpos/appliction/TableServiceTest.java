//package kitchenpos.appliction;
//
//import kitchenpos.table.application.TableService;
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.order.domain.OrderStatus;
//import kitchenpos.table.domain.OrderTable;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static kitchenpos.domain.OrderTableFixture.*;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("테이블 테스트")
//public class TableServiceTest {
//    @Mock
//    private OrderDao orderDao;
//    @Mock
//    private OrderTableDao orderTableDao;
//    @InjectMocks
//    private TableService tableService;
//
//    private OrderTable 테이블1;
//    private OrderTable 테이블2;
//    private OrderTable 테이블3;
//    private OrderTable 테이블4;
//
//    @BeforeEach
//    void setup() {
//        테이블1 = 테이블(1L);
//        테이블2 = 주문테이블(2L, null, 4, false);
//        테이블3 = 빈주문테이블(3L);
//        테이블4 = 주문테이블(4L, 1L, 4, false);
//    }
//
//    @DisplayName("테이블을 생성한다")
//    @Test
//    void 테이블_생성() {
//        // given
//        given(orderTableDao.save(테이블1)).willReturn(테이블1);
//
//        // when
//        OrderTable orderTable = tableService.create(테이블1);
//
//        // then
//        assertAll(
//                () -> assertThat(orderTable.getId()).isEqualTo(1L),
//                () -> assertThat(orderTable.getTableGroupId()).isNull()
//        );
//    }
//
//    @DisplayName("전체 테이블 목록을 조회한다")
//    @Test
//    void 전체_테이블_목록_조회() {
//        // given
//        given(orderTableDao.findAll()).willReturn(Arrays.asList(테이블1, 테이블2, 테이블3));
//
//        // when
//        List<OrderTable> tables = tableService.list();
//
//        // then
//        assertAll(
//                () -> assertThat(tables).hasSize(3),
//                () -> assertThat(tables.stream()
//                        .filter(orderTable -> orderTable.getId().equals(테이블2.getId()))
//                        .findAny()
//                        .get().getNumberOfGuests()).isEqualTo(4)
//        );
//    }
//
//    @DisplayName("빈 테이블 여부 값을 갱신한다")
//    @Test
//    void 빈_테이블_여부_값_갱신() {
//        // given
//        테이블3.setEmpty(false);
//        given(orderTableDao.findById(테이블3.getId())).willReturn(Optional.ofNullable(테이블3));
//        given(orderTableDao.save(테이블3)).willReturn(테이블3);
//
//        // when
//        OrderTable orderTable = tableService.changeEmpty(테이블3.getId(), 테이블3);
//
//        // then
//        assertThat(orderTable.isEmpty()).isFalse();
//    }
//
//    @DisplayName("방문한 손님 수 값을 갱신한다")
//    @Test
//    void 방문한_손님_수_값_갱신() {
//        // given
//        테이블3.setEmpty(false);
//        테이블3.setNumberOfGuests(5);
//        given(orderTableDao.findById(테이블3.getId())).willReturn(Optional.ofNullable(테이블3));
//
//        // when
//        OrderTable orderTable = tableService.changeEmpty(테이블3.getId(), 테이블3);
//
//        // then
//        assertAll(
//                () -> assertThat(테이블3.isEmpty()).isFalse(),
//                () -> assertThat(테이블3.getNumberOfGuests()).isEqualTo(5)
//        );
//    }
//
//    @DisplayName("존재하지 않는 테이블의 빈 테이블 여부 값을 갱신한다")
//    @Test
//    void 존재하지_않는_테이블의_빈_테이블_여부_값_갱신() {
//        // given
//        테이블3.setEmpty(false);
//        given(orderTableDao.findById(테이블3.getId())).willReturn(Optional.ofNullable(null));
//
//        // when & then
//        assertThatThrownBy(
//                () -> tableService.changeEmpty(테이블3.getId(), 테이블3)
//        ).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("테이블그룹이 지정된 테이블의 빈 테이블 여부 값을 갱신한다")
//    @Test
//    void 테이블그룹_지정된_테이블_빈_테이블_여부_값_갱신() {
//        // given
//        테이블4.setEmpty(true);
//        given(orderTableDao.findById(테이블4.getId())).willReturn(Optional.ofNullable(테이블4));
//
//        // when & then
//        assertThatThrownBy(
//                () -> tableService.changeEmpty(테이블4.getId(), 테이블4)
//        ).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("주문상태가 완료가 아닌 주문이 있는 테이블의 빈 테이블 여부 값을 갱신한다")
//    @Test
//    void 주문상태가_완료_아닌_주문_가진_테이블_빈_테이블_여부_값_갱신() {
//        // given
//        테이블2.setEmpty(true);
//        given(orderTableDao.findById(테이블2.getId())).willReturn(Optional.ofNullable(테이블2));
//        given(orderDao.existsByOrderTableIdAndOrderStatusIn(테이블2.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);
//
//        // when & then
//        assertThatThrownBy(
//                () -> tableService.changeEmpty(테이블2.getId(), 테이블2)
//        ).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("음수인 방문한 손님 수를 갱신한다")
//    @Test
//    void 음수인_방문한_손님_수_갱신() {
//        // given
//        테이블3.setNumberOfGuests(-4);
//
//        // when & then
//        assertThatThrownBy(
//                () -> tableService.changeNumberOfGuests(테이블3.getId(), 테이블3)
//        ).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("존재하지 않는 테이블의 방문한 손님 수를 갱신한다")
//    @Test
//    void 존재하지_않는_테이블의_방문한_손님_수_갱신() {
//        // given
//        테이블3.setNumberOfGuests(5);
//        given(orderTableDao.findById(테이블3.getId())).willReturn(Optional.ofNullable(null));
//
//        // when & then
//        assertThatThrownBy(
//                () -> tableService.changeNumberOfGuests(테이블3.getId(), 테이블3)
//        ).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("빈 테이블의 방문한 손님 수를 갱신한다")
//    @Test
//    void 빈_테이블의_방문한_손님_수_갱신() {
//        // give
//        테이블3.setNumberOfGuests(7);
//        given(orderTableDao.findById(테이블3.getId())).willReturn(Optional.ofNullable(테이블3));
//
//        // when & then
//        assertThatThrownBy(
//                () -> tableService.changeNumberOfGuests(테이블3.getId(), 테이블3)
//        ).isInstanceOf(IllegalArgumentException.class);
//    }
//}
