package kitchenpos.application;

import kitchenpos.menu.domain.*;
import kitchenpos.dao.ProductDao;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.testFixture.MenuGroupTestFixture;
import kitchenpos.testFixture.MenuTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private MenuProductRepository menuProductRepository;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    private Product 후라이드;
    private MenuProduct 후라이드두마리구성;
    private MenuGroup 치킨류;

    private Menu 후라이드두마리세트;


    @BeforeEach
    void setUp() {
        후라이드 = new Product();
        후라이드.setId(1L);
        후라이드.setPrice(new BigDecimal("5000"));

        후라이드두마리구성 = new MenuProduct();
        후라이드두마리구성.setSeq(1L);
        후라이드두마리구성.setProductId(1L);
        후라이드두마리구성.setQuantity(2L);

        치킨류 = MenuGroupTestFixture.메뉴그룹생성(1L, "치킨");

        후라이드두마리세트 = MenuTestFixture.메뉴생성(1L, "후라이드두마리세트", new BigDecimal("10000"), 치킨류, Arrays.asList(후라이드두마리구성));
    }

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        //given
        given(menuGroupRepository.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(java.util.Optional.ofNullable(후라이드));
        given(menuRepository.save(후라이드두마리세트)).willReturn(후라이드두마리세트);
        given(menuProductRepository.save(후라이드두마리구성)).willReturn(후라이드두마리구성);

        Menu creatMenu = menuService.create(후라이드두마리세트);

        assertAll(
                () -> assertThat(creatMenu).isNotNull(),
                () -> assertThat(creatMenu.getPrice()).isEqualTo("10000")
        );
    }

    @DisplayName("금액은 0원 이상이어야한고 금액이 있어야한다.")
    @Test
    void createPriceError() {
        Menu 금액_없는_세트 = new Menu();
        Menu 금액이_음수인_세트 = new Menu();
        금액이_음수인_세트.setPrice(new BigDecimal("-1000"));

        assertThatThrownBy(() ->
                menuService.create(금액_없는_세트)
        ).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() ->
                menuService.create(금액이_음수인_세트)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격 x 수량 = 금액 보다 등록한 금액이 더 작아야 한다.")
    @Test
    void createPriceLessError() {
        Menu 비싼_후라이드두마리세트 = MenuTestFixture.메뉴생성(2L,"비싼_후라이드두마리세트",new BigDecimal("100000"),치킨류,Arrays.asList(후라이드두마리구성));

        assertThatThrownBy(() ->
                menuService.create(비싼_후라이드두마리세트)
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("메뉴 목록")
    @Test
    void list() {
        given(menuRepository.findAll()).willReturn(Arrays.asList(후라이드두마리세트));
        given(menuProductRepository.findAllByMenuId(후라이드두마리세트.getId())).willReturn(Arrays.asList(후라이드두마리구성));

        List<Menu> menus = menuService.list();

        assertAll(
                () -> assertThat(menus.size()).isEqualTo(1),
                () -> assertThat(menus.contains(후라이드두마리세트)).isTrue()
        );
    }
}
