package kitchenpos.menu;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MenuProductTest {
    
    @Autowired
    private MenuProductDao menuProductDao;


    @Test
    @DisplayName("메뉴 상품을 저장합니다.")
    void save() {
        // given
        int quantity = 3;
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(2L);
        menuProduct.setQuantity(quantity);

        // when
        MenuProduct persistMenuProduct = this.menuProductDao.save(menuProduct);

        // then
        assertThat(persistMenuProduct.getSeq()).isNotNull();
        assertThat(persistMenuProduct.getQuantity()).isEqualTo(quantity);
    }


    @Test
    @DisplayName("특정 메뉴 상품을 조회합니다.")
    void findById() {
        // given
        int quantity = 3;
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(2L);
        menuProduct.setQuantity(quantity);
        MenuProduct persistMenuProduct = this.menuProductDao.save(menuProduct);

        // when
        MenuProduct foundMenuProduct = this.menuProductDao.findById(persistMenuProduct.getSeq()).get();

        // then
        assertThat(foundMenuProduct.getSeq()).isEqualTo(persistMenuProduct.getSeq());
        assertThat(foundMenuProduct.getQuantity()).isEqualTo(quantity);
    }


    @Test
    @DisplayName("전체 메뉴 상품을 조회합니다.")
    void findAll() {
        // when
        List<MenuProduct> foundMenuProducts = this.menuProductDao.findAll();

        // then
        assertThat(foundMenuProducts).hasSize(6);
    }


    @Test
    @DisplayName("메뉴의 ID로 메뉴 상품을 조회합니다.")
    void findAllByMenuId() {
        // when
        List<MenuProduct> foundMenuProducts = this.menuProductDao.findAllByMenuId(1L);

        // then
        assertThat(foundMenuProducts).hasSize(1);
    }
}
