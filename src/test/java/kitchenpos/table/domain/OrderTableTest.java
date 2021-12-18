package kitchenpos.table.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("테이블 테스트")
class OrderTableTest {
    @DisplayName("테이블 생성")
    @Test
    void 테이블_생성() {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> OrderTable.of(4, false);

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("그룹 지정")
    @Test
    void 그룹_지정() {
        // given
        TableGroup 테이블_그룹 = TableGroup.of(1L);
        OrderTable 테이블 = OrderTable.of(1L, 4, true);

        // when
        테이블.grouping(테이블_그룹);

        // then
        assertAll(
                () -> assertThat(테이블.getTableGroupId()).isEqualTo(테이블_그룹.getId()),
                () -> assertThat(테이블.isEmpty()).isFalse()
        );
    }

    @DisplayName("그룹 해지")
    @Test
    void 그룹_해지() {
        // given
        TableGroup 테이블_그룹 = TableGroup.of(1L);
        OrderTable 테이블 = OrderTable.of(1L, 4, false);
        테이블.grouping(테이블_그룹);

        // when
        테이블.ungroup();

        // then
        assertThat(테이블.getTableGroupId()).isNull();
    }

    @DisplayName("테이블 상태 변경")
    @Test
    void 테이블_상태_변경() {
        // given
        OrderTable 테이블 = OrderTable.of(1L, 4, false);
        boolean 상태_변경_요청_데이터 = true;

        // when
        테이블.updateEmpty(상태_변경_요청_데이터);

        // then
        assertThat(테이블.isEmpty()).isEqualTo(상태_변경_요청_데이터);
    }

    @DisplayName("테이블 상태 변경시 그룹이 존재하면 안됨")
    @Test
    void 테이블_상태_변경시_그룹이_존재하면_안됨() {
        // given
        TableGroup 테이블_그룹 = TableGroup.of(1L);
        OrderTable 테이블 = OrderTable.of(1L, 4, false);
        테이블.grouping(테이블_그룹);
        boolean 상태_변경_요청_데이터 = true;

        // when
        ThrowableAssert.ThrowingCallable 상태_변경_요청 = () -> 테이블.updateEmpty(상태_변경_요청_데이터);

        // then
        assertThatThrownBy(상태_변경_요청).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 인원 변경")
    @Test
    void 테이블_인원_변경() {
        // given
        OrderTable 테이블 = OrderTable.of(1L, 4, false);
        int 인원_변경_요청_데이터 = 5;

        // when
        테이블.updateNumberOfGuest(인원_변경_요청_데이터);

        // then
        assertThat(테이블.getNumberOfGuests()).isEqualTo(인원_변경_요청_데이터);
    }

    @DisplayName("테이블 인원 변경시 테이블이 비어있으면 안됨")
    @Test
    void 테이블_인원_변경시_테이블이_비어있으면_안됨() {
        // given
        OrderTable 테이블 = OrderTable.of(1L, 4, true);
        int 인원_변경_요청_데이터 = 5;

        // when
        ThrowableAssert.ThrowingCallable 인원_변경_요청 = () -> 테이블.updateNumberOfGuest(인원_변경_요청_데이터);

        // then
        assertThatThrownBy(인원_변경_요청).isInstanceOf(IllegalArgumentException.class);
    }


}
