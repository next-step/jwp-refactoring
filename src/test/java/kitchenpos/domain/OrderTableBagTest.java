package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.domain.OrderTableTest.빈_상태;
import static kitchenpos.domain.OrderTableTest.주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 테이블 목록 일급 컬렉션 테스트")
public class OrderTableBagTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        OrderTableBag 주문_테이블_목록 = 빈_테이블_목록();
        assertThat(주문_테이블_목록).isEqualTo(빈_테이블_목록());
    }

    private static OrderTableBag 빈_테이블_목록() {
        return OrderTableBag.from(Arrays.asList(
                주문_테이블(0, 빈_상태),
                주문_테이블(0, 빈_상태)
        ));
    }
}
