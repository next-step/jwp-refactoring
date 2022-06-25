package kitchenpos.application;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.application.helper.ServiceTestHelper;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuProductFixtureFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {
    @Autowired
    private ServiceTestHelper serviceTestHelper;

    @Autowired
    private MenuService menuService;

    private MenuGroup menuGroup;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp(){
        menuGroup = serviceTestHelper.메뉴그룹_생성됨("메뉴그룹1");
        product1 = serviceTestHelper.상품_생성됨("상품1",1000);
        product2 = serviceTestHelper.상품_생성됨("상품2",2000);
    }

    @Test
    void 메뉴그룹에_메뉴추가(){
        String menuName = "메뉴1";
        int menuPrice = 5000;
        Menu savedMenu = 테스트_메뉴_생성(menuGroup,menuName,menuPrice);

        assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroup.getId());
        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getName()).isEqualTo(menuName);
        assertThat(savedMenu.getPrice().intValue()).isEqualTo(menuPrice);

        List<MenuProduct> menuProducts = savedMenu.getMenuProducts();
        List<Long> menuProductIds = menuProducts.stream()
                .map(menuProduct -> menuProduct.getProductId())
                .collect(toList());
        assertThat(menuProducts).hasSize(2);
        assertThat(menuProductIds).containsExactlyInAnyOrder(product1.getId(), product2.getId());
    }

    @Test
    void 메뉴그룹에_메뉴추가_존재하지않는_메뉴그룹의_경우(){
        MenuGroup notSavedMenuGroup = new MenuGroup();
        String menuName = "메뉴1";
        int menuPrice = 5000;
        assertThatIllegalArgumentException().isThrownBy(()->{
            테스트_메뉴_생성(notSavedMenuGroup,menuName,menuPrice);
        });
    }

    @Test
    void 메뉴그룹에_메뉴추가_가격이_음수인경우(){
        String menuName = "메뉴1";
        int menuPrice = -1 * 1000;

        assertThatIllegalArgumentException().isThrownBy(()->{
            테스트_메뉴_생성(menuGroup,menuName,menuPrice);
        });
    }

    @Test
    void 메뉴그룹에_메뉴추가_가격이_상품가격의_합보다_큰경우(){
        String menuName = "메뉴1";
        int menuPrice = 10000;
        assertThatIllegalArgumentException().isThrownBy(()->{
            테스트_메뉴_생성(menuGroup,menuName,menuPrice);
        });
    }

    @Test
    void 메뉴목록_조회(){
        테스트_메뉴_생성(menuGroup,"menu1",6000);
        테스트_메뉴_생성(menuGroup,"menu2",5000);
        List<Menu> menus = menuService.list();
        assertThat(menus).hasSize(2);
    }

    private Menu 테스트_메뉴_생성(MenuGroup menuGroup, String menuName, int menuPrice){
        MenuProduct menuProduct1 = MenuProductFixtureFactory.createMenuProduct(product1.getId(),4);
        MenuProduct menuProduct2 = MenuProductFixtureFactory.createMenuProduct(product2.getId(),1);
        return serviceTestHelper.메뉴_생성됨(menuGroup,menuName,menuPrice, Lists.newArrayList(menuProduct1,menuProduct2));
    }

}
