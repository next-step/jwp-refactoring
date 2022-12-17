package kitchenpos.table.unit;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupValidator;
import kitchenpos.table.dto.OrderTableIdRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("주문그룹검증기능")
public class TableGroupValidatorTest {

    @Test
    @DisplayName("존재하는 주문테이블로만 요청이 가능하다.")
    void tableGroupValidatorTest1() {
        List<OrderTableIdRequest> orderTables = Arrays.asList(new OrderTableIdRequest(1L));
        List<OrderTable> savedOrderTables =
                Arrays.asList(new OrderTable(null, 5, false), new OrderTable(null, 5, false));

        assertThatThrownBy(() -> TableGroupValidator.validateExist(orderTables, savedOrderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문테이블 리스트는 비어있으면 안된다.")
    void tableGroupValidatorTest2() {
        List<OrderTableIdRequest> savedOrderTables = Arrays.asList();

        assertThatThrownBy(() -> TableGroupValidator.validateEmptyOrderTables(savedOrderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문테이블은 리스트는 2개 미만이어선 안된다.")
    void tableGroupValidatorTest3() {
        List<OrderTableIdRequest> savedOrderTables = Arrays.asList(new OrderTableIdRequest(1L));

        assertThatThrownBy(() -> TableGroupValidator.validateSizeOrderTables(savedOrderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문테이블 비어있어야 한다.")
    void tableGroupValidatorTest4() {
        OrderTable orderTable = new OrderTable(null, 5, false);

        assertThatThrownBy(() -> TableGroupValidator.validateEmptyOrderTable(orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문테이블은 테이블 그룹에 포함되어 있으면 안된다.")
    void tableGroupValidatorTest5() {
        OrderTable orderTable = new OrderTable(new TableGroup(1L, null), 5, true);

        assertThatThrownBy(() -> TableGroupValidator.validateIncludeTableGroup(orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
