package kitchenpos.application;

import static kitchenpos.application.fixture.OrderTableFixture.주문테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 관리 기능")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        // given
        주문테이블 = 주문테이블_생성(1L, null, true, 1);
    }

    @Test
    @DisplayName("`주문 테이블`을 등록할 수 있다.")
    void create() {
        // given
        given(orderTableDao.save(주문테이블)).willReturn(주문테이블);

        // when
        OrderTable 등록된_주문테이블 = tableService.create(주문테이블);

        // then
        주문테이블_등록됨(등록된_주문테이블);
        주문테이블_초기값_검증(등록된_주문테이블);
    }

    @Test
    @DisplayName("`주문 테이블`목록을 조회할 수 있다.")
    void list() {
        // given
        given(orderTableDao.findAll()).willReturn(Collections.singletonList(주문테이블));

        // when
        List<OrderTable> 주문목록 = tableService.list();

        // then
        assertThat(주문목록).contains(주문테이블);
    }

    @Test
    @DisplayName("`주문테이블`에 `단체 지정`이 되어 있으면 `빈 테이블` 여부를 변경 할 수 없다.")
    void changeEmpty_fail1() {
        // given
        주문테이블.setTableGroupId(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문테이블));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableService.changeEmpty(1L, 주문테이블);

        // then
        빈테이블_여부변경_실패(actual);
    }


    @Test
    @DisplayName("`주문 테이블`에서 `주문 항목`들의 `주문상태`가 `조리`,`식사`인 경우 `빈 테이블` 여부를 변경 할 수 없다.")
    void changeEmpty_fail2() {
        // given
        Long 주문번호 = 1L;
        given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(주문테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문번호,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableService.changeEmpty(주문번호, 주문테이블);

        // then
        빈테이블_여부변경_실패(actual);
    }

    @Test
    @DisplayName("`주문 테이블`의 `방문한 손님 수`는 0명 이상이어야 변경할 수 있다.")
    void changeNumberOfGuests_fail1() {
        // given
        Long 주문번호 = 1L;
        주문테이블.setNumberOfGuests(-1);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableService.changeNumberOfGuests(주문번호,
            주문테이블);

        // then
        주문테이블_손님수_변경_실패(actual);
    }

    @Test
    @DisplayName("`주문 테이블`이 `빈 테이블`이 아니어야, 손님 수 를 변경할 수 있다.")
    void changeNumberOfGuests_fail2() {
        // given
        Long 주문번호 = 1L;
        given(orderTableDao.findById(주문번호)).willReturn(Optional.of(OrderTable.EMPTY_TABLE));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableService.changeNumberOfGuests(주문번호,
            주문테이블);

        // then
        주문테이블_손님수_변경_실패(actual);
    }

    private void 주문테이블_손님수_변경_실패(ThrowingCallable actual) {
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    private void 주문테이블_등록됨(OrderTable 등록된_주문테이블) {
        assertThat(등록된_주문테이블).isNotNull();
    }

    private void 주문테이블_초기값_검증(OrderTable 등록된_주문테이블) {
        assertThat(등록된_주문테이블.isEmpty()).isTrue();
        assertThat(등록된_주문테이블.getTableGroupId()).isNull();
    }

    private void 빈테이블_여부변경_실패(ThrowingCallable actual) {
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }
}
