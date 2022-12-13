package kitchenpos.menu.application;

import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    private final Product 참치김밥 = new Product(1L, "참치김밥", new Price(new BigDecimal(3000)));
    private final Product 치즈김밥 = new Product(2L, "치즈김밥", new Price(new BigDecimal(2500)));
    private final Product 라볶이 = new Product(3L, "라볶이", new Price(new BigDecimal(4500)));
    private final Product 돈까스 = new Product(4L, "돈까스", new Price(new BigDecimal(7000)));
    private final Product 쫄면 = new Product(5L, "쫄면", new Price(new BigDecimal(5000)));

    private final MenuProduct 라볶이세트참치김밥 = new MenuProduct(1L, 1L, 참치김밥.getId(), 1);
    private final MenuProduct 라볶이세트라볶이 = new MenuProduct(2L, 1L, 라볶이.getId(), 1);
    private final MenuProduct 라볶이세트돈까스 = new MenuProduct(3L, 1L, 돈까스.getId(), 1);

    private final MenuProduct 쫄면세트치즈김밥 = new MenuProduct(4L, 2L, 치즈김밥.getId(), 1);
    private final MenuProduct 쫄면세트쫄면 = new MenuProduct(5L, 2L, 쫄면.getId(), 1);
    private final MenuProduct 쫄면세트돈까스 = new MenuProduct(6L, 2L, 돈까스.getId(), 1);

    private final MenuGroup 분식 = new MenuGroup(1L, "분식");

    private final Menu 라볶이세트 = new Menu(1L, "라볶이세트", new BigDecimal(14000), 분식.getId(),
            Arrays.asList(라볶이세트참치김밥, 라볶이세트라볶이, 라볶이세트돈까스));

    private final Menu 쫄면세트 = new Menu(2L, "쫄면세트", new BigDecimal(14000), 분식.getId(),
            Arrays.asList(쫄면세트치즈김밥, 쫄면세트쫄면, 쫄면세트돈까스));

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴등록 테스트")
    @Test
    void createMenuTest() {
        //given
        when(menuGroupDao.existsById(분식.getId())).thenReturn(true);
        when(productRepository.findById(참치김밥.getId())).thenReturn(Optional.ofNullable(참치김밥));
        when(productRepository.findById(라볶이.getId())).thenReturn(Optional.ofNullable(라볶이));
        when(productRepository.findById(돈까스.getId())).thenReturn(Optional.ofNullable(돈까스));
        when(menuDao.save(라볶이세트)).thenReturn(라볶이세트);
        when(menuProductDao.save(라볶이세트참치김밥)).thenReturn(라볶이세트참치김밥);
        when(menuProductDao.save(라볶이세트라볶이)).thenReturn(라볶이세트라볶이);
        when(menuProductDao.save(라볶이세트돈까스)).thenReturn(라볶이세트돈까스);

        //when
        Menu menu = menuService.create(라볶이세트);

        //then
        assertThat(menu).isEqualTo(라볶이세트);
    }

    @DisplayName("메뉴목록 조회 테스트")
    @Test
    void RetrieveMenuListTest() {
        //given
        when(menuDao.findAll()).thenReturn(Arrays.asList(라볶이세트, 쫄면세트));
        when(menuProductDao.findAllByMenuId(라볶이세트.getId())).thenReturn(Arrays.asList(라볶이세트참치김밥, 라볶이세트라볶이, 라볶이세트돈까스));
        when(menuProductDao.findAllByMenuId(쫄면세트.getId())).thenReturn(Arrays.asList(쫄면세트치즈김밥, 쫄면세트쫄면, 쫄면세트돈까스));

        //when
        List<Menu> menus = menuService.list();

        //then
        assertThat(menus).contains(라볶이세트, 쫄면세트);
    }

    @DisplayName("메뉴등록 가격정보 없을 경우 오류 테스트")
    @Test
    void createMenuPriceNullExceptionTest() {
        //when
        //then
        assertThatThrownBy(() -> menuService.create(new Menu(라볶이세트.getId(), 라볶이세트.getName(), null, 라볶이세트.getMenuGroupId(),
                라볶이세트.getMenuProducts())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴등록 가격정보 0보다작은 경우 오류 테스트")
    @Test
    void createMenuPriceUnderZeroExceptionTest() {
        //when
        //then
        assertThatThrownBy(() -> menuService.create(new Menu(라볶이세트.getId(), 라볶이세트.getName(), new BigDecimal(-1), 라볶이세트.getMenuGroupId(),
                라볶이세트.getMenuProducts())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴그룹이 존재하지 않는 경우 오류 테스트")
    @Test
    void notExistMenuGroupExceptionTest() {
        //given
        when(menuGroupDao.existsById(분식.getId())).thenReturn(false);

        //when
        //then
        assertThatThrownBy(() -> menuService.create(라볶이세트)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품목록에 있는 상품 정보가 존재하지 않는 경우 오류 테스트")
    @Test
    void notExistProductInMenuProductListExceptionTest() {
        //given
        when(productRepository.findById(참치김밥.getId())).thenReturn(Optional.ofNullable(참치김밥));
        when(productRepository.findById(라볶이.getId())).thenReturn(Optional.ofNullable(라볶이));
        when(productRepository.findById(돈까스.getId())).thenReturn(Optional.ofNullable(null));

        when(menuGroupDao.existsById(분식.getId())).thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> menuService.create(라볶이세트)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 상품목록이 비어있는 경우 오류 테스트")
    void emptyMenuProductListExceptionTest() {
        //given
        Menu 새메뉴 = new Menu(1L, "새메뉴", new BigDecimal(14000), 분식.getId(),
                Arrays.asList());
        when(menuGroupDao.existsById(분식.getId())).thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> menuService.create(새메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품의 총합이 메뉴의 가격보다 적거나 같은 경우 오류 발생")
    void productPriceSumUpperThanMenuPriceExceptionTest() {
        //given
        when(menuGroupDao.existsById(분식.getId())).thenReturn(true);
        when(productRepository.findById(참치김밥.getId())).thenReturn(Optional.ofNullable(참치김밥));
        when(productRepository.findById(라볶이.getId())).thenReturn(Optional.ofNullable(라볶이));
        when(productRepository.findById(돈까스.getId())).thenReturn(Optional.ofNullable(돈까스));
        Menu newMenu = new Menu(1L, "라볶이세트", new BigDecimal(20000), 분식.getId(),
                Arrays.asList(라볶이세트참치김밥, 라볶이세트라볶이, 라볶이세트돈까스));

        //when
        //then
        assertThatThrownBy(() -> menuService.create(newMenu)).isInstanceOf(IllegalArgumentException.class);
    }

}
