//package kitchenpos.menu.domain;
//
//import kitchenpos.product.domain.Product;
//import kitchenpos.product.domain.ProductRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.math.BigDecimal;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//public class MenuProductRepositoryTest {
//    @Autowired
//    MenuProductRepository menuProductRepository;
//    @Autowired
//    ProductRepository productRepository;
//
//    private Product 불고기버거;
//    @BeforeEach
//    void setUp() {
//        불고기버거 = productRepository.save(Product.of("불고기버거", BigDecimal.valueOf(1500)));
//        menuProductRepository.save(MenuProduct.of(불고기버거, 5L));
//    }
//
//    @Test
//    @DisplayName("메뉴상품 생성")
//    void create () {
//        // when
//        final MenuProduct menuProduct = menuProductRepository.save(MenuProduct.of(불고기버거, 5L));
//        // then
//        assertThat(menuProduct).isInstanceOf(MenuProduct.class);
//
//    }
//}
