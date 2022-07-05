package kitchenpos.application;

import static kitchenpos.__fixture__.MenuGroupTestFixture.메뉴_그룹_생성;
import static kitchenpos.__fixture__.MenuProductTestFixture.메뉴_상품_1개_생성;
import static kitchenpos.__fixture__.MenuTestFixture.메뉴_생성;
import static kitchenpos.__fixture__.ProductTestFixture.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MenuService 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    private Menu 한마리치킨;
    private MenuGroup 한마리_메뉴_그룹;
    private MenuProduct 후라이드치킨;
    private Product 후라이드치킨_상품;
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        한마리_메뉴_그룹 = 메뉴_그룹_생성(1L, "한마리메뉴");
        한마리치킨 = 메뉴_생성(1L, "한마리치킨", BigDecimal.valueOf(16_000), 한마리_메뉴_그룹.getId(), Collections.emptyList());
        후라이드치킨 = 메뉴_상품_1개_생성(1L);
        후라이드치킨_상품 = 상품_생성(1L, "후라이드치킨", BigDecimal.valueOf(16_000));
    }

    @Test
    @DisplayName("메뉴 생성 시도 시 메뉴 가격이 0보다 작을 경우 Exception")
    public void createPriceException() {
        final Menu 메뉴 = 메뉴_생성(1L, "한마리치킨", BigDecimal.valueOf(-16_000), 한마리_메뉴_그룹.getId(), Collections.emptyList());

        assertThatThrownBy(() -> menuService.create(메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되지 않은 메뉴 그룹으로 메뉴 등록 시 Exception")
    public void createNotExistsMenuGroupException() {
        given(menuGroupDao.existsById(한마리_메뉴_그룹.getId())).willReturn(false);

        assertThatThrownBy(() -> menuService.create(한마리치킨)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 상품의 합계보다 클 경우 Exception")
    public void createPriceIsGreaterThanProductsPriceSumException() {
        final Menu 메뉴 = 메뉴_생성(1L, "한마리치킨", BigDecimal.valueOf(17_000), 한마리_메뉴_그룹.getId(), Arrays.asList(후라이드치킨));

        given(menuGroupDao.existsById(한마리_메뉴_그룹.getId())).willReturn(true);
        given(productDao.findById(후라이드치킨_상품.getId())).willReturn(Optional.of(후라이드치킨_상품));

        assertThatThrownBy(() -> menuService.create(메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성")
    public void create() {
        final Menu 메뉴 = 메뉴_생성(1L, "한마리치킨", BigDecimal.valueOf(16_000), 한마리_메뉴_그룹.getId(), Arrays.asList(후라이드치킨));

        given(menuGroupDao.existsById(한마리_메뉴_그룹.getId())).willReturn(true);
        given(productDao.findById(후라이드치킨_상품.getId())).willReturn(Optional.of(후라이드치킨_상품));
        given(menuProductDao.save(후라이드치킨)).willReturn(후라이드치킨);
        given(menuDao.save(메뉴)).willReturn(메뉴);

        assertThat(menuService.create(메뉴).getId()).isEqualTo(메뉴.getId());
    }

    @Test
    @DisplayName("메뉴 조회")
    public void list() {
        given(menuDao.findAll()).willReturn(Arrays.asList(한마리치킨));
        given(menuProductDao.findAllByMenuId(한마리치킨.getId())).willReturn(Arrays.asList(후라이드치킨));

        assertThat(menuService.list()).contains(한마리치킨);
    }
}
