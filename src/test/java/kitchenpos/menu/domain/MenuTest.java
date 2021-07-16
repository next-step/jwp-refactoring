package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    private Product 후라이드;
    private Product 양념;
    private MenuProduct 반1;
    private MenuProduct 반2;
    private BigDecimal total;



    @BeforeEach
    void setUp() {
        후라이드 = new Product("후라이드",BigDecimal.valueOf(15000));
        양념 = new Product("양념",BigDecimal.valueOf(17000));
        반1 =  new MenuProduct( 후라이드, 1);
        반2 =  new MenuProduct( 양념, 2);
        BigDecimal 반1가격 = 반1.menuPrice().multiply(BigDecimal.valueOf(반1.getQuantity()));
        BigDecimal 반2가격 = 반2.menuPrice().multiply(BigDecimal.valueOf(반2.getQuantity()));
        total = 반1가격.add(반2가격);
    }

    @DisplayName("메뉴 가격은 상품 가격 합보다 클 수 없다")
    @Test
    void createFailBecauseOfPrice() {

        //when && then
        assertThatThrownBy(() -> new Menu("반반치킨", new BigDecimal(50000), null, new MenuProducts(Arrays.asList(반1,반2))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 가격은 상품 가격의 총 합보다 클 수 없습니다.");
    }

    @DisplayName("메뉴 생성")
    @Test
    void create() {

        //when
        Menu menu = new Menu("반반치킨", new BigDecimal(49000), null, new MenuProducts(Arrays.asList(반1,반2)));

        //then
        assertThat(menu.getPrice()).isLessThanOrEqualTo(total);
    }




}