package kitchenpos.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.application.TableService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.domain.OrderTableTest.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블 생성 시 정상 생성되어야 한다")
    @Test
    void createOrderTableTest() {
        // given
        int numberOfGuests = 10;
        OrderTable 주문_테이블 = 주문_테이블_생성(null, numberOfGuests, true);
        when(tableService.create(주문_테이블)).thenReturn(주문_테이블);

        // when
        OrderTable 생성_된_주문_테이블 = tableService.create(주문_테이블);

        // then
        주문_테이블_저장됨(생성_된_주문_테이블, 주문_테이블_생성(null, numberOfGuests, true));
    }

    @DisplayName("주문 테이블 목록 조회 시 정상 조회되어야 한다")
    @Test
    void findAllOrderTableTest() {
        // given
        List<OrderTable> 주문_테이블_리스트 = Arrays.asList(
                주문_테이블_생성(null, 10, true),
                주문_테이블_생성(null, 10, true),
                주문_테이블_생성(null, 10, true),
                주문_테이블_생성(null, 10, true)
        );
        when(tableService.list()).thenReturn(주문_테이블_리스트);

        // when
        List<OrderTable> 주문_테이블_목록_조회_결과 = tableService.list();

        // then
        assertThat(주문_테이블_목록_조회_결과.size()).isGreaterThanOrEqualTo(주문_테이블_리스트.size());
        assertThat(주문_테이블_목록_조회_결과).containsAll(주문_테이블_리스트);
    }

    @DisplayName("저장되지 않은 주문 테이블의 빈 자리 여부를 변경하면 예외가 발생해야 한다")
    @Test
    void changeEmptyByBeforeSavedOrderTableTest() {
        // given
        OrderTable 변경_될_주문_테이블 = 주문_테이블_생성(null, 1, true);
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        // then
        주문_테이블_인원_변경_실패됨(() -> tableService.changeEmpty(0L, 변경_될_주문_테이블));
    }

    @DisplayName("테이블 그룹에 포함된 주문 테이블의 빈 자리 여부를 변경하면 예외가 발생해야 한다")
    @Test
    void changeEmptyByNotBelongTableGroupTest() {
        // given
        OrderTable Id_값이_없는_주문_테이블 = 주문_테이블_생성(0L, 1, false);
        OrderTable 변경_될_주문_테이블 = 주문_테이블_생성(0L, 1, true);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(Id_값이_없는_주문_테이블));

        // then
        주문_테이블_인원_변경_실패됨(() -> tableService.changeEmpty(0L, 변경_될_주문_테이블));
    }

    @DisplayName("요리중 또는 식사중인 주문 테이블의 빈 테이블 여부를 변경하면 예외가 발생해야 한다")
    @Test
    void changeEmptyByCockingOrMealStatusOrderTableTest() {
        // given
        OrderTable 원본_주문_테이블 = 주문_테이블_생성(null, 1, false);
        OrderTable 변경_될_주문_테이블 = 주문_테이블_생성(null, 1, true);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(원본_주문_테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);

        // then
        주문_테이블_인원_변경_실패됨(() -> tableService.changeEmpty(0L, 변경_될_주문_테이블));
    }

    @DisplayName("정상 상태의 주문 테이블의 빈 자리 여부를 변경하면 정상 변경되어야 한다")
    @Test
    void changeEmptyTest() {
        // given
        OrderTable 원본_주문_테이블 = 주문_테이블_생성(null, 1, false);
        OrderTable 변경_될_주문_테이블 = 주문_테이블_생성(null, 1, true);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(원본_주문_테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(false);
        when(orderTableDao.save(any())).then(it -> it.getArgument(0));

        // when
        OrderTable 빈_자리_상태_변경_된_주문_테이블 = tableService.changeEmpty(0L, 변경_될_주문_테이블);

        // then
        빈_자리_여부_상태_변경_성공됨(빈_자리_상태_변경_된_주문_테이블, 변경_될_주문_테이블);
    }

    @DisplayName("테이블 인원 변경 시 테이블의 인원이 0명 미만일 경우 예외가 발생해야 한다")
    @Test
    void changeNumberOfGuestByMinusGuests() {
        // given
        OrderTable 변경_될_주문_테이블 = 주문_테이블_생성(null, -1, true);

        // then
        주문_테이블_인원_변경_실패됨(() -> tableService.changeNumberOfGuests(0L, 변경_될_주문_테이블));
    }

    @DisplayName("저장되지 않은 주문 테이블의 인원을 변경하면 예외가 발생해야 한다")
    @Test
    void changeNumberOfGuestsByBeforeSavedOrderTableTest() {
        // given
        OrderTable 변경_될_주문_테이블 = 주문_테이블_생성(null, 10, true);
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        // then
        주문_테이블_인원_변경_실패됨(() -> tableService.changeNumberOfGuests(0L, 변경_될_주문_테이블));
    }

    @DisplayName("변경 할 주문 테이블이 빈 자리인 경우 인원을 변경하면 예외가 발생해야 한다")
    @Test
    void changeNumberOfGuestsByEmptyOrderTableTest() {
        // given
        OrderTable 원본_주문_테이블 = 주문_테이블_생성(null, 1, true);
        OrderTable 변경_될_주문_테이블 = 주문_테이블_생성(null, 10, true);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(원본_주문_테이블));

        // then
        주문_테이블_인원_변경_실패됨(() -> tableService.changeNumberOfGuests(0L, 변경_될_주문_테이블));
    }

    @DisplayName("정상 상태의 주문 테이블의 인원을 변경하면 정상 변경되어야 한다")
    @Test
    void changeNumberOfGuestsTest() {
        // given
        OrderTable 원본_주문_테이블 = 주문_테이블_생성(null, 1, false);
        OrderTable 변경_될_주문_테이블 = 주문_테이블_생성(null, 10, false);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(원본_주문_테이블));
        when(orderTableDao.save(any())).then(it -> it.getArgument(0));

        // when
        OrderTable 인원_변경_된_주문_테이블 = tableService.changeNumberOfGuests(0L, 변경_될_주문_테이블);

        // then
        주문_테이블_인원_변경_성공됨(인원_변경_된_주문_테이블, 변경_될_주문_테이블);
    }

    void 주문_테이블_저장됨(OrderTable source, OrderTable expected) {
        assertThat(source.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
        assertThat(source.isEmpty()).isEqualTo(expected.isEmpty());
    }

    void 주문_테이블_인원_변경_실패됨(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 빈_자리_여부_상태_변경_성공됨(OrderTable source, OrderTable expected) {
        assertThat(source.isEmpty()).isEqualTo(expected.isEmpty());
    }

    void 주문_테이블_인원_변경_성공됨(OrderTable source, OrderTable expected) {
        assertThat(source.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
    }
}
