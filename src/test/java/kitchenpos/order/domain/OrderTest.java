package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static kitchenpos.common.domain.PriceTest.가격_생성;
import static kitchenpos.menu.MenuGenerator.*;
import static kitchenpos.menu.domain.QuantityTest.수량_생성;
import static kitchenpos.order.OrderGenerator.주문_물품_생성;
import static kitchenpos.order.OrderGenerator.주문_생성;
import static kitchenpos.product.ProductGenerator.상품_생성;
import static kitchenpos.table.TableGenerator.주문_테이블_생성;
import static kitchenpos.table.domain.NumberOfGuestsTest.손님_수_생성;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class OrderTest {
    private final OrderTable 주문_테이블 = 주문_테이블_생성(손님_수_생성(10));
    private final MenuGroup 메뉴_그룹 = 메뉴_그룹_생성("메뉴 그룹");
    private final Product 상품 = 상품_생성("상품", 가격_생성(1_000));
    private final MenuProduct 메뉴_상품 = 메뉴_상품_생성(상품, 수량_생성(1L));
    private final Menu 메뉴 = 메뉴_생성("메뉴", 1_000, 메뉴_그룹, 메뉴_상품_목록_생성(Collections.singletonList(메뉴_상품)));

    @DisplayName("주문 생성 시 주문 물품이 포함되어 있지 않으면 예외가 발생해야 한다")
    @Test
    void createOrderByNotIncludeOrderItemsTest() {
        assertThatIllegalArgumentException().isThrownBy(() -> 주문_생성(주문_테이블, Collections.emptyList()));
    }

    @DisplayName("정상 상태로 주문을 생성하면 정상 생성되어야 한다")
    @Test
    void createOrderTest() {
        OrderLineItem 주문_물품 = 주문_물품_생성(메뉴, 1);
    }
}
