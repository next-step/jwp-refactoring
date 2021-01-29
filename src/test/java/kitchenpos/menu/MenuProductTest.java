package kitchenpos.menu;

import kitchenpos.menu.domain.*;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MenuProductTest {
    
    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;
    
    @Autowired
    private ProductRepository productRepository;


    @Test
    @DisplayName("메뉴 상품을 저장합니다.")
    void save() {
        // given
        int quantity = 3;
        MenuProduct menuProduct = createMenuProduct(quantity);

        // when
        MenuProduct persistMenuProduct = this.menuProductRepository.save(menuProduct);

        // then
        assertThat(persistMenuProduct.getSeq()).isNotNull();
        assertThat(persistMenuProduct.getQuantity()).isEqualTo(quantity);
    }

    private MenuProduct createMenuProduct(int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        Menu persistMenu = MenuTestSupport.createMenu("신메뉴", 20000
                , this.menuGroupRepository.save(new MenuGroup("그룹1")));
        menuProduct.changeMenu(persistMenu);
        Product product = new Product("치킨", BigDecimal.valueOf(15000));
        Product savedProduct = this.productRepository.save(product);
        menuProduct.changeProduct(savedProduct);
        menuProduct.changeQuantity(quantity);
        return menuProduct;
    }


    @Test
    @DisplayName("특정 메뉴 상품을 조회합니다.")
    void findById() {
        // given
        int quantity = 3;
        MenuProduct menuProduct = createMenuProduct(quantity);
        MenuProduct persistMenuProduct = this.menuProductRepository.save(menuProduct);

        // when
        MenuProduct foundMenuProduct = this.menuProductRepository.findById(persistMenuProduct.getSeq()).get();

        // then
        assertThat(foundMenuProduct.getSeq()).isEqualTo(persistMenuProduct.getSeq());
        assertThat(foundMenuProduct.getQuantity()).isEqualTo(quantity);
    }


    @Test
    @DisplayName("전체 메뉴 상품을 조회합니다.")
    void findAll() {
        // when
        List<MenuProduct> foundMenuProducts = this.menuProductRepository.findAll();

        // then
        assertThat(foundMenuProducts).hasSize(6);
    }


    @Test
    @DisplayName("메뉴의 ID로 메뉴 상품을 조회합니다.")
    void findAllByMenuId() {
        // when
        List<MenuProduct> foundMenuProducts = this.menuProductRepository.findAllByMenuId(1L);

        // then
        assertThat(foundMenuProducts).hasSize(1);
    }
}
