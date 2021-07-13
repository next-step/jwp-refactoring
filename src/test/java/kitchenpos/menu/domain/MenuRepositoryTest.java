package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("기본 저장 확인")
    void save() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("한마리"));
        Product product1 = productRepository.save(new Product("양념치킨", BigDecimal.valueOf(17000.00)));
        Product product2 = productRepository.save(new Product("후라이드치킨", BigDecimal.valueOf(15000.00)));
        Menu menu = new Menu("A", BigDecimal.valueOf(19000.00), menuGroup);
        menu.addMenuProduct(new MenuProduct(menu, product1, 1));
        menu.addMenuProduct(new MenuProduct(menu, product2, 1));

        // when
        Menu save = menuRepository.save(menu);

        // then
        assertThat(save.getId()).isNotNull();
    }

    @Test
    @DisplayName("기본 삭제 확인")
    void delete() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("한마리"));
        Product product1 = productRepository.save(new Product("양념치킨", BigDecimal.valueOf(17000.00)));
        Product product2 = productRepository.save(new Product("후라이드치킨", BigDecimal.valueOf(15000.00)));
        Menu menu = new Menu("A", BigDecimal.valueOf(19000.00), menuGroup);
        menu.addMenuProduct(new MenuProduct(menu, product1, 1));
        menu.addMenuProduct(new MenuProduct(menu, product2, 1));
        Menu save = menuRepository.save(menu);

        // when
        menuRepository.delete(save);

        // then
        assertThat(menuRepository.findById(save.getId())).isEmpty();
    }
}
