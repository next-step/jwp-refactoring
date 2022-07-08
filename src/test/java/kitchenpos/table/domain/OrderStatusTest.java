package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sun.security.x509.OtherName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class OrderStatusTest {

    @Test
    @DisplayName("테이블 청소 가능 우무")
    void enabledTableClear() {
        // when
        final boolean enabledTableClear = OrderStatus.COOKING.enabledTableClear();
        // then
        assertThat(enabledTableClear).isFalse();
    }

}
