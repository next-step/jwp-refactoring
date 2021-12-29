package kitchenpos.domain;

import kitchenpos.common.exceptions.NegativeNumberOfGuestsException;
import kitchenpos.common.exceptions.NotEmptyOrderTableGroupException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블 도메인 테스트")
public class OrderTableTest {

    private OrderTable 테이블;
    private OrderTable 빈_테이블;
    private TableGroup 테이블그룹;
    private OrderTable 주문테이블;

    @BeforeEach
    void setup() {
        
        Product 상품 = Product.of("상품", BigDecimal.valueOf(17_000));
        MenuGroup 메뉴그룹 = MenuGroup.of("추천메뉴");
        MenuProduct 메뉴상품 = MenuProduct.of(상품, 2L);

        Menu 메뉴 = Menu.of("메뉴", BigDecimal.valueOf(32_000), 메뉴그룹);
        메뉴.addMenuProducts(Collections.singletonList(메뉴상품));
        테이블 = OrderTable.of(4, false);
        빈_테이블 = OrderTable.of(4, true);
        테이블그룹 = TableGroup.from(Lists.newArrayList(테이블, 테이블));
        주문테이블 = OrderTable.of(테이블그룹, 4, false);
    }

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        assertThat(OrderTable.of(3, false))
                .isEqualTo(OrderTable.of(3, false));
    }

    @DisplayName("상태 변경을 할 수 있다")
    @Test
    void changeEmptyStatusTest() {
        테이블.changeEmptyStatus(true);
        assertThat(테이블.isEmpty()).isTrue();
    }

    @DisplayName("상태 변경 시, 테이블 그룹에 속해 있지 않아야 한다")
    @Test
    void changeEmptyStatusTest2() {
        assertThatThrownBy(() -> 주문테이블.changeEmptyStatus(true))
                .isInstanceOf(NotEmptyOrderTableGroupException.class);
    }

    @DisplayName("손님 인원을 변경을 할 수 있다")
    @Test
    void changeNumberOfGuestsTest() {
        테이블.changeNumberOfGuests(10);
        assertThat(테이블.getNumberOfGuests().toInt()).isEqualTo(10);
    }

    @DisplayName("빈 테이블의 인원을 변경할 수 없다")
    @Test
    void changeNumberOfGuestsTest2() {
        assertThatThrownBy(() -> 빈_테이블.changeNumberOfGuests(10))
                .isInstanceOf(NegativeNumberOfGuestsException.class);
    }

    @DisplayName("테이블 그룹을 해제할 수 있다")
    @Test
    void unGroupTest() {
        주문테이블.unGroup();
        assertThat(주문테이블.getTableGroup()).isNull();
    }
}