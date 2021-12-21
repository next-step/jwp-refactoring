package kitchenpos.domain;

import kitchenpos.exception.IllegalPriceException;
import kitchenpos.exception.MismatchPriceException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixtures.MenuGroupFixtures.반반메뉴그룹요청;
import static kitchenpos.fixtures.ProductFixtures.양념치킨요청;
import static kitchenpos.fixtures.ProductFixtures.후라이드요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * packageName : kitchenpos.domain
 * fileName : MenuRepositoryTest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
@DataJpaTest
@DisplayName("메뉴 리파지토리 테스트")
class MenuRepositoryTest {
    private final BigDecimal 메뉴가격 = new BigDecimal(32000);
    private Menu menu;
    private Product 양념치킨;
    private Product 후라이드;
    private MenuGroup 메뉴그룹;
    private MenuProduct 양념치킨메뉴상품;
    private MenuProduct 후라이드메뉴상품;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuRepository menuRepository;

    @BeforeEach
    void setUp() {
        양념치킨 = productRepository.save(양념치킨요청().toEntity());
        후라이드 = productRepository.save(후라이드요청().toEntity());
        메뉴그룹 = menuGroupRepository.save(반반메뉴그룹요청().toEntity());
        양념치킨메뉴상품 = new MenuProduct(양념치킨, 1L);
        후라이드메뉴상품 = new MenuProduct(후라이드, 1L);
        menu = new Menu("후라이드반양념반메뉴", 메뉴가격, 메뉴그룹, Lists.newArrayList(양념치킨메뉴상품, 후라이드메뉴상품));
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
    @DisplayName("메뉴 조회 시 메뉴그룹과 메뉴상품을 조회할 수 있다.")
    public void listWithMenuGroupAndMenuProducts() throws Exception {
        //when
        final List<Menu> menus = menuRepository.findAllJoinFetch();

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

    @ParameterizedTest(name = "value: " + ParameterizedTest.ARGUMENTS_PLACEHOLDER)
    @ValueSource(ints = {-10, -5, -1})
    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다: int")
    public void createFailByPrice(int candidate) {
        //given
        BigDecimal illegalPrice = new BigDecimal(candidate);

        //when
        assertThatThrownBy(() -> new Menu("가격불일치메뉴", illegalPrice, 메뉴그룹, Lists.newArrayList(양념치킨메뉴상품, 후라이드메뉴상품)))
                .isInstanceOf(IllegalPriceException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다: null")
    public void createFailByPriceNull() {
        //then
        assertThatThrownBy(() -> new Menu("가격불일치메뉴", null, 메뉴그룹, Lists.newArrayList(양념치킨메뉴상품, 후라이드메뉴상품)))
                .isInstanceOf(IllegalPriceException.class);
    }

    @Test
    @DisplayName("메뉴의 가격은 메뉴상품들의 수량과 가격의 합과 일치하여야 한다.")
    public void createFailByMenusPrices() {
        //then
        assertThatThrownBy(() -> new Menu("가격불일치메뉴", new BigDecimal(Long.MAX_VALUE), 메뉴그룹, Lists.newArrayList(양념치킨메뉴상품, 후라이드메뉴상품)))
                .isInstanceOf(MismatchPriceException.class);
    }
}
