package kitchenpos.domain;

import kitchenpos.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 관련 인수테스트")
class OrderAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();

    }

    @DisplayName("주문을 할 수 있다.")
    @Test
    void create01() {
        // when

        // then
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given

        // when

        // then
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void change02() {
        // given

        // when

        // then
    }
}