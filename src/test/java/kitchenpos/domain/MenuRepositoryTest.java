package kitchenpos.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixtures.MenuGroupFixtures.반반메뉴;
import static kitchenpos.fixtures.ProductFixtures.양념치킨;
import static kitchenpos.fixtures.ProductFixtures.후라이드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * packageName : kitchenpos.domain
 * fileName : MenuRepositoryTest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
@DataJpaTest
class MenuRepositoryTest {
    private Menu menu;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuRepository menuRepository;

    @BeforeEach
    void setUp() {
        final Product 양념치킨 = productRepository.save(양념치킨().toEntity());
        final Product 후라이드 = productRepository.save(후라이드().toEntity());
        final MenuGroup 메뉴그룹 = menuGroupRepository.save(반반메뉴().toEntity());

//  엔드포인트 테스트 데이터
//        final ArrayList<MenuProductRequest> menuProductRequests = Lists.newArrayList(
//                MenuProductRequest.of(양념치킨.getId(), 1L),
//                MenuProductRequest.of(후라이드.getId(), 1L)
//        );
//
//        후라이드반양념반메뉴 = 후라이드반양념반메뉴(new BigDecimal(32000), 메뉴그룹.getId(), menuProductRequests);

        final MenuProduct 양념치킨메뉴상품 = new MenuProduct(양념치킨, 1L);
        final MenuProduct 후라이드메뉴상품 = new MenuProduct(후라이드, 1L);
        menu = new Menu("후라이드반양념반메뉴", new BigDecimal(32000), 메뉴그룹, Lists.newArrayList(양념치킨메뉴상품, 후라이드메뉴상품));
    }

    @Test
    @DisplayName("메뉴 목록을 조회할 수 있다.")
    public void list() throws Exception {
        //when
        final List<Menu> menus = menuRepository.findAll();

        //then
        assertThat(menus.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    public void create() throws Exception {
        //when
        final Menu actual = menuRepository.save(menu);

        //then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull()
        );
    }


    //TODO 메뉴테스트 추가

}
