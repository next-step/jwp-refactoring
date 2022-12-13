package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MenuServiceTest {
    @Autowired
    MenuProductDao menuProductDao;

    @Autowired
    MenuDao menuDao;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    ProductDao productDao;

    MenuService menuService;
    Product 천원;
    Product 이천원;
    MenuGroup menuGroup;
    @BeforeEach
    void beforeEach(){
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        천원 = productDao.save(new Product(101L, "1번 상품", new BigDecimal(1000)));
        이천원 = productDao.save(new Product(102L, "2번 상품", new BigDecimal(2000)));
        menuGroup = menuGroupDao.save(new MenuGroup(101L, "메뉴그룹1"));
    }

    @Test
    @DisplayName("메뉴 생성 성공 테스트")
    void createTest(){
        // given
        Menu menu = new Menu("메뉴1", new BigDecimal(3000), menuGroup.getId(), Arrays.asList(
                new MenuProduct(천원.getId(), 1), new MenuProduct(이천원.getId(), 1)
        ));

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
    }

    @Test
    @DisplayName("메뉴 등록 시, 상품정보가 비어있으면 안된다.")
    void createFailTest1(){
        // given
        Menu menu = new Menu("메뉴1", new BigDecimal(4000), menuGroup.getId(), Collections.emptyList());

        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(menu)
        );

        // then
    }

    @Test
    @DisplayName("메뉴의 가격이 음수면 안된다.")
    void createFailTest2(){
        // given
        Menu menu = new Menu("메뉴1", new BigDecimal(-1000), menuGroup.getId(), Arrays.asList(
                new MenuProduct(천원.getId(), 1), new MenuProduct(이천원.getId(), 1)
        ));

        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(menu)
        );

        // then
    }

    @Test
    @DisplayName("메뉴의 가격은 포함된 각 상품의 가격*수량 의 총 합보다 크면 안됩니다.")
    void createFailTest3(){
        // given
        Menu menu = new Menu("메뉴1", new BigDecimal(8001), menuGroup.getId(), Arrays.asList(
                new MenuProduct(천원.getId(), 2),
                new MenuProduct(이천원.getId(), 3)
        ));

        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(menu)
        );

        // then
    }

    @Test
    @DisplayName("메뉴를 메뉴그룹에 분류할 때, 없는 그룹에 등록할 수 없다.")
    void createFailTest4(){
        // given
        Menu menu = new Menu("메뉴1", new BigDecimal(8001), -1L, Arrays.asList(
                new MenuProduct(천원.getId(), 2),
                new MenuProduct(이천원.getId(), 3)
        ));

        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(menu)
        );

        // then
    }
}