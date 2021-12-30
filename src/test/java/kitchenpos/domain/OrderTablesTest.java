package kitchenpos.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문테이블들 도메인 테스트")
public class OrderTablesTest {

    private OrderTable 테이블;
    private OrderTable 빈_테이블;
    private TableGroup 테이블그룹;
    private OrderTable 주문테이블;
    private OrderTables 주문테이블들;

    @BeforeEach
    void setup() {
        
        Product 상품 = Product.of("상품", 17000);
        MenuGroup 메뉴그룹 = MenuGroup.from("메뉴그룹");
        MenuProduct 메뉴상품 = MenuProduct.of(상품, 2L);

        Menu 메뉴 = Menu.of("메뉴", 32000, 메뉴그룹);
        메뉴.addMenuProducts(Collections.singletonList(메뉴상품));
        테이블 = OrderTable.of(4, false);
        빈_테이블 = OrderTable.of(4, true);
        테이블그룹 = TableGroup.from(Lists.newArrayList(테이블, 테이블));
        주문테이블 = OrderTable.of(테이블그룹, 4, false);
        주문테이블들 = OrderTables.from(Collections.singletonList(주문테이블));
    }

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        assertThat(OrderTables.from(Lists.newArrayList(테이블, 빈_테이블)))
                .isEqualTo(OrderTables.from(Lists.newArrayList(테이블, 빈_테이블)));
    }

    @DisplayName("테이블 그룹을 해제한다")
    @Test
    void unGroupTest() {
        주문테이블들.unGroup();
        assertThat(주문테이블.getTableGroup()).isNull();
    }
}
