package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:scripts/data.sql")
public class MenuRepositoryTest {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ProductRepository productRepository;


    @Test
    @DisplayName("메뉴 저장 시 메뉴 상품 등록 확인")
    void save() {

        Product 후라이드 = productRepository.findById(1L).get();
        Product 양념치킨 = productRepository.findById(2L).get();

        MenuProduct 후라이드_1 = MenuProduct.of(후라이드, 1L);
        MenuProduct 양념치킨_1 = MenuProduct.of(양념치킨, 1L);

        Menu menu = Menu.of("두마리세트", 28000, MenuGroup.from("치킨"));
        menu.addMenuProducts(Arrays.asList(후라이드_1, 양념치킨_1));

        Menu persist = menuRepository.save(menu);

        //query 확인
        menuRepository.flush();

        Optional<Menu> optional = menuRepository.findById(persist.getId());

        assertThat(optional.get().getMenuProducts().size()).isEqualTo(2);
    }

}
