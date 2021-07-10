package kitchenpos.domain.table;

import kitchenpos.common.exception.TableEmptyException;
import kitchenpos.domain.NumberOfGuest;
import kitchenpos.fixture.CleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderTableTest {
    @BeforeEach
    void setUp() {
        CleanUp.cleanUp();
    }

    @Test
    @DisplayName("모든 주문이 끝났으면 단체지정이 해제가 가능하다 ")
    void 모든_주문이_끝났으면_단체지정이_해제가_가능하다() {
        assertThat(사용중인_1명_2건_결제완료2.isUnGroupable()).isTrue();
    }


    @Test
    @DisplayName("모든 주문이 안끝났으면 단체지정이 해제가 불가능하다 ")
    void 모든_주문이_안끝났으면_단체지정이_해제가_불가능하다() {
        assertThat(사용중인_1명_1건_조리_1건_식사.isUnGroupable()).isFalse();
    }

    @Test
    @DisplayName("모든 주문이 안끝났으면 단체지정이 해제가 불가능 하므로 IllegalStateException이 발생한다 ")
    void 모든_주문이_안끝났으면_단체지정이_해제가_불가능_하므로_IllegalStateException이_발생한다() {
        assertThatIllegalStateException().isThrownBy(() -> 사용중인_1명_1건_조리_1건_식사.ungroup());
    }

    @Test
    @DisplayName("빈 테이블일 경우 인원수를 바꾸려 하면 IllegalStateException이 발생한다")
    void 빈_테이블을_경우_인원수를_바꾸려_하면_IllegalStateException이_발생한다() {
        assertThatIllegalStateException().isThrownBy(() -> 빈_테이블.changeNumberOfGuest(new NumberOfGuest(1)));
    }


    @Test
    @DisplayName("빈 테이블이 아닐경우 인원수를 바꾸려 하면 성공한다")
    void 빈_테이블이_아닐경우_인원수를_바꾸려_하면_성공한다() {
        assertDoesNotThrow(() -> 사용중인_1명_1건_조리_1건_식사.changeNumberOfGuest(new NumberOfGuest(1)));
    }

    @Test
    @DisplayName("이미 예약이 되어있으면 예약이 불가능하다")
    void 이미_예약이_되어있으면_예약이_불가능하다() {
        assertThatIllegalStateException().isThrownBy(() -> 사용중인_1명_1건_조리_1건_식사.bookedBy(null));
    }

    @Test
    @DisplayName("정상적인 예약")
    void 정상적인_예약() {
        TableGroup tableGroup = new TableGroup(1L, null, null);

        빈_테이블.bookedBy(tableGroup.getId());

        assertThat(빈_테이블.isEmpty()).isFalse();
        assertThat(빈_테이블.getTableGroupId()).isEqualTo(tableGroup.getId());
    }


    @Test
    @DisplayName("빈 테이블이면 TableEmptyException이 발생한다")
    void 빈_테이블이면_TableEmptyException이_발생한다() {
        // when & then
        assertThatExceptionOfType(TableEmptyException.class)
                .isThrownBy(() -> OrderTable.newOrder(미사용중인_테이블, null, null));
    }
}