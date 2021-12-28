package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MenuProductRepositoryTest {
    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuProductRepository repository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductRepository productRepository;

    private MenuGroup 한마리메뉴;

    private Product 후라이드치킨;

    private Menu 치킨세트;

    @BeforeEach
    void setUp() {
        한마리메뉴 = MenuGroup.of("한마리메뉴");
        menuGroupRepository.save(한마리메뉴);
        후라이드치킨 = Product.of("후라이드치킨", 16000L);
        productRepository.save(후라이드치킨);
        치킨세트 = Menu.of("치킨세트", 16000L, 한마리메뉴, Arrays.asList(MenuProduct.of(후라이드치킨, 1)));
        menuRepository.save(치킨세트);
    }

    @DisplayName("메뉴 제품 저장 테스트")
    @Test
    public void save() {
        //given
        final MenuProduct menProduct = MenuProduct.of(null, 치킨세트, 후라이드치킨, 1);

        //when
        final MenuProduct expected = repository.save(menProduct);

        //then
        assertThat(menProduct.getQuantity()).isEqualTo(expected.getQuantity());
    }
}
