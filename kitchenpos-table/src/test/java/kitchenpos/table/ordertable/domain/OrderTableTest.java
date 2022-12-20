package kitchenpos.table.ordertable.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static kitchenpos.table.ordertable.domain.OrderTableTestFixture.비어있지_않은_상태;
import static kitchenpos.table.ordertable.domain.OrderTableTestFixture.빈_상태;
import static kitchenpos.table.ordertable.domain.OrderTableTestFixture.한_명의_방문객;

@DisplayName("주문 테이블 테스트")
public class OrderTableTest {

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

}
