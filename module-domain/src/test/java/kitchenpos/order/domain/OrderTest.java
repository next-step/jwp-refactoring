package kitchenpos.order.domain;

import kitchenpos.common.Exception.UnchangeableException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order(1L, null, OrderStatus.COMPLETION, LocalDateTime.now(), null);
    }

    @DisplayName("이미 완료된 주문은 변경할 수 없다")
    @Test
    void updateStatusFailBecuaseOfAlreadyCompletion() {

        //when && then
        assertThatThrownBy(() ->order.updateStatus(OrderStatus.COOKING.name()))
                .isInstanceOf(UnchangeableException.class)
                .hasMessageContaining("이미 완료된 주문입니다.");
    }

}