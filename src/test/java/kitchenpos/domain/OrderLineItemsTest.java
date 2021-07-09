package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 상세내역 컬렉션 단위 테스트")
class OrderLineItemsTest {

    @Test
    @DisplayName("주문 상세 컬렉션이 사이즈가 0일 경우 생성 실패")
    void create_failed() {
        assertThatThrownBy(() -> OrderLineItems.of(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
