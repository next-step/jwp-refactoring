package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 테이블 테스트")
public class OrderTableTest {

    public static final boolean 빈_상태 = true;
    public static final boolean 비어있지_않은_상태 = false;
    public static final int 두_명의_방문객 = 2;
    public static final int 한_명의_방문객 = 1;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        assertThat(OrderTable.ofNumberOfGuestsAndEmpty(한_명의_방문객, 빈_상태)).isEqualTo(
                OrderTable.ofNumberOfGuestsAndEmpty(한_명의_방문객, 빈_상태));
    }

    @DisplayName("빈 테이블 상태 변경 성공")
    @Test
    void 빈_테이블_상태_변경_성공() {
        //given:
        final OrderTable 비어있지_않은_테이블 = OrderTable.ofNumberOfGuestsAndEmpty(한_명의_방문객, 비어있지_않은_상태);
        //when:
        비어있지_않은_테이블.changeEmpty(빈_상태);
        //then:
        assertThat(비어있지_않은_테이블.isEmpty()).isTrue();
    }

    public static OrderTable 빈_테이블() {
        return OrderTable.ofNumberOfGuestsAndEmpty(0, 빈_상태);
    }

    public static OrderTable 두_명의_방문객이_존재하는_테이블() {
        return OrderTable.ofNumberOfGuestsAndEmpty(두_명의_방문객, 비어있지_않은_상태);
    }
}
