package kitchenpos.order.domain;

import static kitchenpos.order.domain.TableGroupTest.첫번째_주문테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.order.exception.OutOfOrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문테이블 목록 테스트")
public class OrderTablesTest {

    @DisplayName("단체 지정될 주문테이블은 2개 이상이어야 한다.")
    @Test
    void create_Fail() {
        List<OrderTable> 비어있는_주문테이블_목록 = new ArrayList<>();
        assertThatThrownBy(() -> new OrderTables(비어있는_주문테이블_목록))
            .isInstanceOf(OutOfOrderTableException.class);

        List<OrderTable> 한개만_있는_주문테이블_목록 = new ArrayList<>(Arrays.asList(첫번째_주문테이블));
        assertThatThrownBy(() -> new OrderTables(한개만_있는_주문테이블_목록))
            .isInstanceOf(OutOfOrderTableException.class);
    }
}
