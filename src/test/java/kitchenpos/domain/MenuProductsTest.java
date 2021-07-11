package kitchenpos.domain;

import static kitchenpos.domain.MenuProductTest.*;
import static kitchenpos.domain.MenuTest.*;
import static kitchenpos.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 제품 컬렉션 단위 테스트")
class MenuProductsTest {

    @Test
    @DisplayName("메뉴 제품이 0개일 때 생성 실패")
    void create_failed() {
        List<MenuProduct> 빈_리스트 = Collections.emptyList();
        assertThatThrownBy(() -> MenuProducts.of(빈_리스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 제품의 합이 가격과 일치한다")
    void summation() {
        MenuProducts 메뉴제품컬렉션_각_1개씩 = MenuProducts.of(MP1후라이드, MP2양념치킨, MP3반반치킨);
        Price addedPrice = 후라이드.getPrice().add(양념치킨.getPrice()).add(반반치킨.getPrice());
        assertThat(메뉴제품컬렉션_각_1개씩.summation().hasSameValueAs(addedPrice)).isTrue();
    }

    @Test
    @DisplayName("모든 메뉴 제품의 메뉴값이 세팅된다")
    void withMenu() {
        MenuProducts 메뉴제품컬렉션_각_1개씩 = MenuProducts.of(MP1후라이드, MP2양념치킨, MP3반반치킨);
        메뉴제품컬렉션_각_1개씩.withMenu(후라이드_메뉴).forEach(menuProduct -> {
            assertThat(menuProduct.getMenu()).isEqualTo(후라이드_메뉴);
        });
    }
}
