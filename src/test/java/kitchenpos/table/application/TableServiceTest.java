package kitchenpos.table.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
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

import static kitchenpos.table.fixture.TableFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
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

    @Test
    @DisplayName("테이블 등록 성공 테스트")
    void create() {
        when(orderTableDao.save(비어있는_주문_테이블_그룹_없음)).thenReturn(비어있는_주문_테이블_그룹_없음);

        OrderTable 테이블_등록_결과 = tableService.create(비어있는_주문_테이블_그룹_없음);

        Assertions.assertThat(테이블_등록_결과).isEqualTo(비어있는_주문_테이블_그룹_없음);
    }

    @Test
    @DisplayName("테이블 조회 성공 테스트")
    void list() {
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(비어있는_주문_테이블_그룹_없음, 비어있는_주문_테이블_그룹_있음));

        List<OrderTable> 테이블_조회_결과 = tableService.list();

        assertAll(
                () -> Assertions.assertThat(테이블_조회_결과).hasSize(2),
                () -> Assertions.assertThat(테이블_조회_결과).containsExactly(비어있는_주문_테이블_그룹_없음, 비어있는_주문_테이블_그룹_있음)
        );
    }

    @Test
    @DisplayName("테이블을 비어있지 않은 상태로 수정")
    void changeEmpty() {
        OrderTable 비어있지_않은_테이블 = new OrderTable();
        비어있지_않은_테이블.setEmpty(false);

        when(orderTableDao.findById(비어있는_주문_테이블_그룹_없음.getId())).thenReturn(Optional.of(비어있는_주문_테이블_그룹_없음));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).thenReturn(false);
        when(orderTableDao.save(비어있는_주문_테이블_그룹_없음)).thenReturn(비어있는_주문_테이블_그룹_없음);

        OrderTable 테이블_생성_결과 = tableService.changeEmpty(비어있는_주문_테이블_그룹_없음.getId(), 비어있지_않은_테이블);

        Assertions.assertThat(테이블_생성_결과.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("주문 테이블 정보가 없는 경우 실패 테스트")
    void changeEmpty2() {
        when(orderTableDao.findById(비어있는_주문_테이블_그룹_없음.getId())).thenReturn(Optional.empty());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeEmpty(비어있는_주문_테이블_그룹_없음.getId(), 비어있는_주문_테이블_그룹_있음));
    }

    @Test
    @DisplayName("주문 테이블 비어있는 정보로 변경시 주문 상태가 요리와 식사 상태인 경우 실패 테스트")
    void changeEmpty3() {
        when(orderTableDao.findById(비어있는_주문_테이블_그룹_없음.getId())).thenReturn(Optional.of(비어있는_주문_테이블_그룹_없음));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).thenReturn(true);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeEmpty(비어있는_주문_테이블_그룹_없음.getId(), 비어있는_주문_테이블_그룹_있음));
    }

    @Test
    @DisplayName("주문 테이블 인원수 수정 테스트")
    void changeNumberOfGuests() {
        when(orderTableDao.findById(주문_테이블_여섯이_있음.getId())).thenReturn(Optional.of(주문_테이블_여섯이_있음));
        when(orderTableDao.save(주문_테이블_여섯이_있음)).thenReturn(주문_테이블_여섯이_있음);

        OrderTable 주문_테이블_등록_결과 = tableService.changeNumberOfGuests(주문_테이블_여섯이_있음.getId(), 주문_테이블_그룹_없음);

        Assertions.assertThat(주문_테이블_등록_결과).isEqualTo(주문_테이블_여섯이_있음);
    }

    @Test
    @DisplayName("주문 테이블 수정시 인원이 0보다 작은 경우 실패 테스트")
    void changeNumberOfGuests2() {
        OrderTable 인원수_0명_테스트 = new OrderTable();
        인원수_0명_테스트.setNumberOfGuests(0);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeEmpty(비어있는_주문_테이블_그룹_없음.getId(), 인원수_0명_테스트));
    }

    @Test
    @DisplayName("주문 테이블 조회시 정보가 조회되지 않은 경우 실패 테스트")
    void changeNumberOfGuests3() {
        when(orderTableDao.findById(비어있는_주문_테이블_그룹_없음.getId())).thenReturn(Optional.empty());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeEmpty(비어있는_주문_테이블_그룹_없음.getId(), 비어있는_주문_테이블_그룹_없음));
    }
}
