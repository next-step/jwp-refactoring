package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 관련 Service 기능 테스트 - Stub")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;
    private MenuService menuService;

    private MenuGroup 인기메뉴;
    private Product 돈까스상품;
    private Product 미니우동상품;
    private Product 제육덮밥상품;
    private MenuProduct 돈까스;
    private MenuProduct 미니우동;
    private MenuProduct 제육덮밥;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        인기메뉴 = new MenuGroup(1L, "인기 메뉴");
        돈까스상품 = new Product(1L, "돈까스", convert(8000));
        미니우동상품 = new Product(2L, "미니우동", convert(3000));
        제육덮밥상품 = new Product(3L, "제육덮밥", convert(7000));
        돈까스 = new MenuProduct(null, 돈까스상품.getId(), 100);
        미니우동 = new MenuProduct(null, 미니우동상품.getId(), 50);
        제육덮밥 = new MenuProduct(null, 제육덮밥상품.getId(), 50);
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        //given
        long generateMenuId = 1;
        Menu request = new Menu(null, "돈까스 정식", convert(10000), 인기메뉴.getId(), Arrays.asList(돈까스, 미니우동));
        when(menuGroupDao.existsById(인기메뉴.getId())).thenReturn(true);
        when(productDao.findById(돈까스.getProductId())).thenReturn(Optional.of(돈까스상품));
        when(productDao.findById(미니우동.getProductId())).thenReturn(Optional.of(미니우동상품));
        doAnswer(invocation -> new Menu(generateMenuId, request.getName(), request.getPrice(), request.getMenuGroupId(), request.getMenuProducts()))
                .when(menuDao).save(request);
        when(menuProductDao.save(돈까스)).thenReturn(돈까스);
        when(menuProductDao.save(미니우동)).thenReturn(미니우동);

        //when
        Menu result = menuService.create(request);

        //then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(generateMenuId),
                () -> assertThat(result.getMenuProducts().get(0).getMenuId()).isEqualTo(generateMenuId),
                () -> assertThat(result.getMenuProducts().get(1).getMenuId()).isEqualTo(generateMenuId)
        );

    }

    @DisplayName("메뉴 가격이 null 이거나 0원 미만이면 등록에 실패한다.")
    @Test
    void create_price_null_or_less_then_zero() {
        //given
        Menu request_price_less_then_zero = new Menu(1L, "돈까스 정식", convert(-1), 인기메뉴.getId(), Arrays.asList(돈까스, 미니우동));
        Menu request_price_null = new Menu(1L, "돈까스 정식", null, 인기메뉴.getId(), Arrays.asList(돈까스, 미니우동));

        //when then
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(request_price_less_then_zero)),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(request_price_null))
        );

    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {

        //given
        Menu menu1 = new Menu(1L, "돈까스 정식", convert(10000), 인기메뉴.getId(), Arrays.asList(돈까스, 미니우동));
        Menu menu2 = new Menu(2L, "제육덮밥", convert(7000), 인기메뉴.getId(), Collections.singletonList(제육덮밥));

        when(menuDao.findAll()).thenReturn(Arrays.asList(menu1, menu2));
        when(menuProductDao.findAllByMenuId(menu1.getId())).thenReturn(Arrays.asList(돈까스, 미니우동));
        when(menuProductDao.findAllByMenuId(menu2.getId())).thenReturn(Collections.singletonList(제육덮밥));

        //when
        List<Menu> results = menuService.list();

        //then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getMenuProducts().get(0).getProductId()).isEqualTo(돈까스.getProductId());
        assertThat(results.get(0).getMenuProducts().get(1).getProductId()).isEqualTo(미니우동.getProductId());
        assertThat(results.get(1).getMenuProducts().get(0).getProductId()).isEqualTo(제육덮밥.getProductId());

    }

    private BigDecimal convert(long price) {
        return BigDecimal.valueOf(price);
    }

}
