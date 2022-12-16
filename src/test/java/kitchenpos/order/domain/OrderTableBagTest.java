package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTableBag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.order.domain.OrderTableTest.두_명의_방문객이_존재하는_테이블;
import static kitchenpos.order.domain.OrderTableTest.빈_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("주문 테이블 목록 일급 컬렉션 테스트")
public class OrderTableBagTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        OrderTableBag 주문_테이블_목록 = 비어_있지_않은_테이블_목록();
        //when, then:
        assertThat(주문_테이블_목록).isEqualTo(비어_있지_않은_테이블_목록());
    }

    @DisplayName("생성 예외 - 주문 테이블이 빈 테이블일 경우")
    @Test
    void 생성_예외_주문_테이블이_빈_테이블일_경우() {
        assertThatIllegalArgumentException().isThrownBy(() -> 빈_테이블_목록());
    }

    private static OrderTableBag 빈_테이블_목록() {
        return OrderTableBag.from(Arrays.asList(
                빈_테이블(),
                빈_테이블()
        ));
    }

    private static OrderTableBag 비어_있지_않은_테이블_목록() {
        return OrderTableBag.from(Arrays.asList(
                두_명의_방문객이_존재하는_테이블(),
                두_명의_방문객이_존재하는_테이블()
        ));
    }
}
