package kitchenpos.domain;

import kitchenpos.fixture.CleanUp;
import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class OrdersTest {

    @BeforeEach
    void setUp() {
        CleanUp.cleanUpTableFirst();
    }

    @Test
    @DisplayName("모든 주문이 안끝났으면 isAllFinished는 false이다")
    void 모든_주문이_안끝났으면_isAllFinished는_false이다() {
        Orders orders = new Orders(Arrays.asList(OrderFixture.식사1, OrderFixture.결제완료1));

        assertThat(orders.isAllFinished()).isFalse();
    }

    @Test
    @DisplayName("모든 주문이 끝났으면 isAllFinished는 true이다")
    void 모든_주문이_끝났으면_isAllFinished는_true이다() {
        Orders orders = new Orders(Arrays.asList(OrderFixture.결제완료1, OrderFixture.결제완료2));

        assertThat(orders.isAllFinished()).isTrue();
    }

}