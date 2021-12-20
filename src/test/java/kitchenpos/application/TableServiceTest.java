package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.TableFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @DisplayName("테이블 생성 성공 테스트")
    @Test
    void create_success() {
        // given
        OrderTable 요청_테이블 = new OrderTable();
        요청_테이블.setNumberOfGuests(0);
        요청_테이블.setEmpty(true);

        when(orderTableDao.save(any(OrderTable.class))).thenReturn(테이블_그룹에_속해있지_않은_테이블);

        // when
        OrderTable 생성된_테이블 = tableService.create(요청_테이블);

        // then
        assertThat(생성된_테이블).isEqualTo(테이블_그룹에_속해있지_않은_테이블);
    }

    @DisplayName("테이블 목록 조회 테스트")
    @Test
    void list() {
        // given
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(테이블_그룹에_속해있지_않은_테이블));

        // when
        List<OrderTable> 조회된_테이블_목록 = tableService.list();

        // then
        assertThat(조회된_테이블_목록).containsExactly(테이블_그룹에_속해있지_않은_테이블);
    }

    @DisplayName("기존 주문 테이블 수정 성공 테스트")
    @Test
    void changeEmpty_success() {
        // given
        OrderTable 요청_테이블 = new OrderTable();
        요청_테이블.setEmpty(false);

        when(orderTableDao.findById(any(Long.class))).thenReturn(Optional.of(테이블_그룹에_속해있지_않은_테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(Long.class), anyList())).thenReturn(false);
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(테이블_그룹에_속해있지_않은_테이블);

        // when
        OrderTable 수정된_테이블 = tableService.changeEmpty(테이블_그룹에_속해있지_않은_테이블.getId(), 요청_테이블);

        // then
        assertThat(수정된_테이블.isEmpty()).isFalse();
    }

    @DisplayName("기존 주문 테이블 수정 실패 테스트 - 테이블 그룹에 속해있음")
    @Test
    void changeEmpty_failure_existTableGroup() {
        // given
        OrderTable 요청_테이블 = new OrderTable();
        요청_테이블.setEmpty(false);

        when(orderTableDao.findById(any(Long.class))).thenReturn(Optional.of(테이블_그룹에_속해있는_테이블));

        // when & then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(테이블_그룹에_속해있는_테이블.getId(), 요청_테이블));
    }

    @DisplayName("기존 주문 테이블 수정 실패 테스트 - 주문 상태가 COOKING 또는 MEAL")
    @Test
    void changeEmpty_failure_orderStatus_cooking() {
        // given
        OrderTable 요청_테이블 = new OrderTable();
        요청_테이블.setEmpty(false);

        when(orderTableDao.findById(any(Long.class))).thenReturn(Optional.of(테이블_그룹에_속해있지_않은_테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(Long.class), anyList())).thenReturn(true);

        // when & then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(테이블_그룹에_속해있는_테이블.getId(), 요청_테이블));
    }

    @DisplayName("주문 테이블 손님 수 수정 성공 테스트")
    @Test
    void changeNumberOfGuests_success() {
        // given
        OrderTable 요청_테이블 = new OrderTable();
        요청_테이블.setNumberOfGuests(4);

        when(orderTableDao.findById(any(Long.class))).thenReturn(Optional.of(비어있지_않은_테이블));
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(비어있지_않은_테이블);

        // when
        OrderTable 수정된_테이블 = tableService.changeNumberOfGuests(비어있지_않은_테이블.getId(), 요청_테이블);

        // then
        assertThat(수정된_테이블.getNumberOfGuests()).isEqualTo(요청_테이블.getNumberOfGuests());
    }

    @DisplayName("주문 테이블 손님 수 수정 실패 테스트 - 주문 테이블 손님 수가 0보다 작음")
    @Test
    void changeNumberOfGuests_failure_invalidNumberOfGuests() {
        // given
        OrderTable 요청_테이블 = new OrderTable();
        요청_테이블.setNumberOfGuests(-1);

        // when & then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(비어있지_않은_테이블.getId(), 요청_테이블));
    }

    @DisplayName("주문 테이블 손님 수 수정 실패 테스트 - 주문 테이블이 비어있음")
    @Test
    void changeNumberOfGuests_failure() {
        // given
        OrderTable 요청_테이블 = new OrderTable();
        요청_테이블.setNumberOfGuests(4);

        when(orderTableDao.findById(any(Long.class))).thenReturn(Optional.of(비어있는_테이블));

        // when & then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(비어있는_테이블.getId(), 요청_테이블));
    }
}
