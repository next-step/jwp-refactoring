package kitchenpos.order.application;

import static kitchenpos.util.TestDataSet.원플원_양념;
import static kitchenpos.util.TestDataSet.원플원_후라이드;
import static kitchenpos.util.TestDataSet.주문_1번;
import static kitchenpos.util.TestDataSet.테이블_1번;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.constant.OrderStatus;
import kitchenpos.menu.Menu;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;

public class OrderValidatorTest {

    private OrderValidator validator;

    @BeforeEach
    void setUp() {
        validator = new OrderValidator();
    }

    @Test
    @DisplayName("입력으로 들어온 menu 크기와, DB에 저장된 Menu가 다를 시 에러를 밷는다")
    void notMatch() {
        OrderRequest orderRequest = new OrderRequest(주문_1번.getId(), OrderStatus.COOKING,
            Arrays.asList(new OrderLineItemRequest(1L, 3L)));
        List<Menu> menuList = Arrays.asList(원플원_후라이드, 원플원_양념);
        assertThrows(IllegalArgumentException.class, () -> {
            validator.createValidator(orderRequest, null, menuList);
        });
    }

    @Test
    @DisplayName("테이블이 비었을 경우 에러를 뱉는다.")
    void emptyTable() {
        OrderRequest orderRequest = new OrderRequest(주문_1번.getId(), OrderStatus.COOKING,
            Arrays.asList(new OrderLineItemRequest(1L, 3L)));
        List<Menu> menuList = Arrays.asList(원플원_후라이드);
        assertThrows(IllegalArgumentException.class, () -> {
            validator.createValidator(orderRequest, 테이블_1번, menuList);
        });
    }

}
