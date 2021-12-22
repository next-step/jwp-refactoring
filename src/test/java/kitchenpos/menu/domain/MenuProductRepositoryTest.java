package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuProductRepositoryTest {

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    private MenuGroup 추천메뉴그룹;
    private Menu 나홀로세트;
    private Product 타코야끼;

    @DisplayName("메뉴상품 저장")
    @BeforeEach
    void setUp() {
        타코야끼 = productDao.save(new Product(1L, "타코야끼", BigDecimal.valueOf(12000)));
        추천메뉴그룹 = menuGroupDao.save(new MenuGroup("추천메뉴"));
        나홀로세트 = menuDao.save(new Menu("나홀로세트", BigDecimal.valueOf(15000), 추천메뉴그룹));
    }

    @DisplayName("메뉴 id로 메뉴상품 리스트 조회")
    @Test
    void findAllByMenuId() {
        MenuProduct menuProduct = menuProductDao.save(new MenuProduct(나홀로세트, 타코야끼, 2));
        List<MenuProduct> allByMenuId = menuProductDao.findAllByMenuId(
            menuProduct.getMenu().getId());

        assertThat(allByMenuId.size()).isEqualTo(1);
        assertThat(allByMenuId.get(0)).isEqualTo(menuProduct);
    }
}
