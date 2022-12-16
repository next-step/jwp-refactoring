package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 주문_테이블_등록_요청;

    @BeforeEach
    void setUp() {
        주문_테이블_등록_요청 = new OrderTable(0, true);
    }

    @Test
    void 주문_테이블_등록시_성공하고_주문_테이블_정보를_반환한다() {
        // given
        given(orderTableDao.save(any())).willReturn(주문_테이블_등록_요청);

        // when
        OrderTable orderTable = tableService.create(주문_테이블_등록_요청);

        // then
        주문_테이블_등록됨(orderTable, 주문_테이블_등록_요청.getNumberOfGuests(), 주문_테이블_등록_요청.isEmpty());
    }

    @Test
    void 주문_테이블_목록_조회시_등록된_주문_테이블_목록을_반환한다() {
        // given
        List<OrderTable> expectedOrderTables = Arrays.asList(
                new OrderTable(0, true),
                new OrderTable(0, true)
        );
        given(orderTableDao.findAll()).willReturn(expectedOrderTables);

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        주문_테이블_목록_조회됨(orderTables, expectedOrderTables);
    }

    @ParameterizedTest(name = "주문 테이블의 이용 여부 변경시 변경에 성공한다 - 기존 이용 여부 : [{0}], 변경 된 이용 여부 : [{1}]")
    @MethodSource("주문_테이블의_이용_여부_변경_테스트_파라미터")
    void 주문_테이블의_이용_여부_변경시_변경에_성공한다(boolean currentEmpty, boolean expectedEmpty) {
        // given
        OrderTable 기존_주문_테이블 = new OrderTable(0, currentEmpty);
        given(orderTableDao.findById(any())).willReturn(Optional.of(기존_주문_테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(기존_주문_테이블);

        // when
        OrderTable orderTable = tableService.changeEmpty(1L, new OrderTable(0, !currentEmpty));

        // then
        주문_테이블_이용_여부_변경됨(orderTable, expectedEmpty);
    }

    @Test
    void 주문_테이블의_이용_여부_변경시_테이블이_미등록된경우_변경에_실패한다() {
        // given
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable(0, true)))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(orderTableDao).should(times(1)).findById(any());
    }

    @Test
    void 주문_테이블의_이용_여부_변경시_테이블_그룹에_포함되어있는경우_변경에_실패한다() {
        // given
        OrderTable 기존_주문_테이블 = new OrderTable(0, true);
        기존_주문_테이블.setTableGroupId(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(기존_주문_테이블));

        // when
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable(0, false)))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(orderTableDao).should(times(1)).findById(any());
    }

    @Test
    void 주문_테이블의_이용_여부_변경시_테이블의_상태가_조리_또는_식사중인경우_변경에_실패한다() {
        // given
        OrderTable 기존_주문_테이블 = new OrderTable(0, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(기존_주문_테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        // when
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable(0, false)))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(orderTableDao).should(times(1)).findById(any());
        then(orderDao).should(times(1)).existsByOrderTableIdAndOrderStatusIn(any(), any());
    }

    @Test
    void 주문_테이블의_손님수_변경시_변경에_성공한다() {
        // given
        OrderTable 기존_주문_테이블 = new OrderTable(0, false);
        OrderTable 주문_테이블_변경_요청 = new OrderTable(1, false);
        given(orderTableDao.findById(any())).willReturn(Optional.of(기존_주문_테이블));
        given(orderTableDao.save(any())).willReturn(기존_주문_테이블);

        // when
        OrderTable orderTable = tableService.changeNumberOfGuests(1L, 주문_테이블_변경_요청);

        // then
        주문_테이블_손님수_변경됨(orderTable, 1);
    }

    @Test
    void 주문_테이블의_손님수_변경시_변경가능한_최소_인원보다_적게_주어진다면_변경에_실패한다() {
        // given
        OrderTable 주문_테이블_변경_요청 = new OrderTable(-1, false);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 주문_테이블_변경_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_손님수_변경시_해당_테이블이_이용중이라면_변경에_실패한다() {
        // given
        OrderTable 기존_주문_테이블 = new OrderTable(0, true);
        OrderTable 주문_테이블_변경_요청 = new OrderTable(1, false);
        given(orderTableDao.findById(any())).willReturn(Optional.of(기존_주문_테이블));

        // when
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 주문_테이블_변경_요청))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(orderTableDao).should(times(1)).findById(any());
    }

    private static Stream<Arguments> 주문_테이블의_이용_여부_변경_테스트_파라미터() {
        return Stream.of(
                Arguments.of(true, false),
                Arguments.of(false, true)
        );
    }

    private void 주문_테이블_등록됨(OrderTable orderTable, int numberOfGuests, boolean empty) {
        assertAll(
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests),
                () -> assertThat(orderTable.isEmpty()).isEqualTo(empty)
        );
    }

    private void 주문_테이블_목록_조회됨(List<OrderTable> orderTables, List<OrderTable> expectedOrderTables) {
        assertAll(
                () -> assertThat(orderTables).hasSize(expectedOrderTables.size()),
                () -> assertThat(orderTables).containsAll(expectedOrderTables)
        );
    }

    private void 주문_테이블_이용_여부_변경됨(OrderTable orderTable, boolean empty) {
        assertThat(orderTable.isEmpty()).isEqualTo(empty);
        then(orderTableDao).should(times(1)).findById(any());
        then(orderDao).should(times(1)).existsByOrderTableIdAndOrderStatusIn(any(), any());
        then(orderTableDao).should(times(1)).save(any());
    }

    private void 주문_테이블_손님수_변경됨(OrderTable orderTable, int numberOfGuests) {
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
        then(orderTableDao).should(times(1)).findById(any());
        then(orderTableDao).should(times(1)).save(any());
    }
}
