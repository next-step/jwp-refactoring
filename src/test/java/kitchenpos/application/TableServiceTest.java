package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private TableService tableService;

    @BeforeEach
    void setUp() {
        this.tableService = new TableService(orderDao, orderTableDao);
    }

    @Test
    @DisplayName("create - 정상적인 주문 테이블 등록")
    void 정상적인_주문_테이블_등록() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 1, true);

        // when
        when(orderTableDao.save(orderTable))
                .thenReturn(orderTable);

        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable.getTableGroupId()).isNull();

        verify(orderTableDao, VerificationModeFactory.only())
                .save(orderTable);

    }

    @Test
    @DisplayName("list - 정상적인 주문 테이블 전체 조회")
    void 정상적인_주문_테이블_전체_조회() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 1L, 1, true);
        OrderTable orderTable2 = new OrderTable(2L, 2L, 2, false);

        // when
        when(orderTableDao.findAll())
                .thenReturn(Arrays.asList(orderTable1, orderTable2));

        List<OrderTable> list = tableService.list();

        // then
        assertThat(list)
                .containsExactly(orderTable1, orderTable2);

        verify(orderTableDao, VerificationModeFactory.only())
                .findAll();
    }

    @Test
    @DisplayName("changeEmpty - DB에서 주문 테이블을 고유 아이디로 가져온다. 없으면 IllegalArgumentException이 발생한다.")
    void DB에서_주문_테이블을_고유_아이디로_가져온다_없으면_IllegalArgumentException이_발생한다() {
        // given
        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable(orderTableId, 1L, 1, true);

        // when
        when(orderTableDao.findById(orderTableId))
                .thenReturn(Optional.empty());

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable));

        verify(orderTableDao, VerificationModeFactory.only())
                .findById(orderTableId);
    }

    @Test
    @DisplayName("changeEmpty - 주문 테이블이 단체지정이 되어있을경우 IllegalArgumentException이 발생한다.")
    void 주문_테이블이_단체지정이_되어있을경우_IllegalArgumentException이_발생한다() {

    }

    @Test
    @DisplayName("changeEmpty - 주문 테이블에 등록된 주문들의 상태가 조리 또는 식사 일 경우 IllegalArgumentException이 발생한다.")
    void 주문_테이블에_등록된_주문들의_상탱가_조리_또는_식사_일_경우_IllegalArgumentException이_발생한다() {

    }

    @Test
    @DisplayName("changeEmpty - 정상적인 빈 테이블 변경")
    void 정상적인_빈_테이블_변경() {

    }

    @Test
    @DisplayName("changeNumberOfGuests - 방문한 손님 수가 0보다 적으면 IllegalArgumentException이 발생한다")
    void 방문한_손님_수가_0보다_작으면_IllegalArgumentException이_발생한다() {

    }

    @Test
    @DisplayName("changeNumberOfGuests - DB에서 변경을 원하는 주문 테이블을 가져오고, 주문 테이블이 없을경우 IllegalArgumentException이 발생한다.")
    void DB에서_변경을_원하는_주문_테이블을_가져오고_주문_테이블이_없을경우_IllegalArgumentException이_발생한다() {

    }

    @Test
    @DisplayName("changeNumberOfGuests - 주문 테이블이 빈 테이블이면 IllegalArgumentException이 발생한다.")
    void 주문_테이블이_빈_테이블이면_IllegalArgumentException이_발생한다() {

    }

    @Test
    @DisplayName("changeNumberOfGuest - 정상적인 방문한 손님 변경")
    void 정상적인_방문한_손님_변경() {

    }
}