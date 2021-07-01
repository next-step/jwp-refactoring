package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.NumberOfGuest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableCreate;
import kitchenpos.fixture.CleanUp;
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

import static kitchenpos.fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
        CleanUp.cleanUpTableFirst();

        this.tableService = new TableService(orderDao, orderTableDao);
    }

    @Test
    @DisplayName("create - 정상적인 주문 테이블 등록")
    void 정상적인_주문_테이블_등록() {
        // given
        OrderTableCreate orderTable = new OrderTableCreate(new NumberOfGuest(1), true);

        // when
        when(orderTableDao.save(any())).thenAnswer(i -> i.getArgument(0));

        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable.getTableGroup()).isNull();

        verify(orderTableDao, VerificationModeFactory.times(1)).save(savedOrderTable);
    }

    @Test
    @DisplayName("list - 정상적인 주문 테이블 전체 조회")
    void 정상적인_주문_테이블_전체_조회() {
        // when
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(미사용중인_테이블, 사용중인_2명_테이블));

        List<OrderTable> list = tableService.list();

        // then
        assertThat(list).containsExactly(미사용중인_테이블, 사용중인_2명_테이블);

        verify(orderTableDao, VerificationModeFactory.times(1))
                .findAll();
    }

    @Test
    @DisplayName("changeEmpty - DB에서 주문 테이블을 고유 아이디로 가져온다. 없으면 IllegalArgumentException이 발생한다.")
    void DB에서_주문_테이블을_고유_아이디로_가져온다_없으면_IllegalArgumentException이_발생한다() {
        // given
        Long orderTableId = 1L;

        // when
        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.empty());

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(orderTableId, true));

        verify(orderTableDao, VerificationModeFactory.times(1))
                .findById(orderTableId);
    }

    @Test
    @DisplayName("changeEmpty - 주문 테이블이 단체지정이 되어있을경우 IllegalArgumentException이 발생한다.")
    void 주문_테이블이_단체지정이_되어있을경우_IllegalArgumentException이_발생한다() {
        // given
        Long orderTableId = 1L;

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(사용중인_1명_테이블));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(orderTableId, true));

        verify(orderTableDao, VerificationModeFactory.times(1))
                .findById(orderTableId);
    }

    @Test
    @DisplayName("changeEmpty - 주문 테이블에 등록된 주문들의 상태가 조리 또는 식사 일 경우 IllegalArgumentException이 발생한다.")
    void 주문_테이블에_등록된_주문들의_상탱가_조리_또는_식사_일_경우_IllegalArgumentException이_발생한다() {
        // given
        given(orderTableDao.findById(사용중인_1명_1건_조리_1건_식사.getId()))
                .willReturn(Optional.of(사용중인_1명_1건_조리_1건_식사));

        // when & then
        assertThatIllegalStateException().isThrownBy(() -> tableService.changeEmpty(사용중인_1명_1건_조리_1건_식사.getId(), true));

        verify(orderTableDao, VerificationModeFactory.times(1))
                .findById(사용중인_1명_1건_조리_1건_식사.getId());
    }

    @Test
    @DisplayName("changeEmpty - 정상적인 빈 테이블 변경")
    void 정상적인_빈_테이블_변경() {
        // given
        given(orderTableDao.findById(빈_테이블.getId())).willReturn(Optional.of(빈_테이블));

        // when
        OrderTable savedOrderTable = tableService.changeEmpty(빈_테이블.getId(), false);

        // then
        assertThat(savedOrderTable.getTableGroup()).isNull();
        assertThat(savedOrderTable.getId()).isEqualTo(빈_테이블.getId());
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(빈_테이블.getNumberOfGuests());
        assertThat(savedOrderTable.isEmpty()).isFalse();

        verify(orderTableDao, VerificationModeFactory.times(1))
                .findById(빈_테이블.getId());
    }

    @Test
    @DisplayName("changeNumberOfGuests - DB에서 변경을 원하는 주문 테이블을 가져오고, 주문 테이블이 없을경우 IllegalArgumentException이 발생한다.")
    void DB에서_변경을_원하는_주문_테이블을_가져오고_주문_테이블이_없을경우_IllegalArgumentException이_발생한다() {
        // given
        Long orderTableId = 1L;

        // when
        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.empty());

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, new NumberOfGuest(1)));
        verify(orderTableDao, VerificationModeFactory.times(1))
                .findById(orderTableId);

    }

    @Test
    @DisplayName("changeNumberOfGuests - 주문 테이블이 빈 테이블이면 IllegalArgumentException이 발생한다.")
    void 주문_테이블이_빈_테이블이면_IllegalArgumentException이_발생한다() {
        // given
        given(orderTableDao.findById(빈_테이블.getId())).willReturn(Optional.of(빈_테이블));

        // when & then
        assertThatIllegalStateException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(빈_테이블.getId(), new NumberOfGuest(1)));
        verify(orderTableDao, VerificationModeFactory.times(1))
                .findById(빈_테이블.getId());
    }

    @Test
    @DisplayName("changeNumberOfGuest - 정상적인 방문한 손님 변경")
    void 정상적인_방문한_손님_변경() {
        // given
        NumberOfGuest toBe = new NumberOfGuest(200);

        given(orderTableDao.findById(사용중인_1명_1건_조리_1건_식사.getId())).willReturn(Optional.of(사용중인_1명_1건_조리_1건_식사));

        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(사용중인_1명_1건_조리_1건_식사.getId(), toBe);

        // when & then
        assertThat(changedOrderTable.getId()).isEqualTo(사용중인_1명_1건_조리_1건_식사.getId());
        assertThat(changedOrderTable.getTableGroup()).isEqualTo(사용중인_1명_1건_조리_1건_식사.getTableGroup());
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(toBe);
        assertThat(changedOrderTable.isEmpty()).isEqualTo(사용중인_1명_1건_조리_1건_식사.isEmpty());
    }
}