package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@DisplayName("메뉴 BO 테스트")
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

	@InjectMocks
	private MenuService menuService;

	private Product 상품1;
	private Product 상품2;
	private MenuGroup 메뉴_그룹;

	@BeforeEach
	void setUp() {
		// given
		상품1 = new Product();
		상품1.setId(1L);
		상품1.setPrice(BigDecimal.valueOf(1000L));
		상품1.setName("새_상품1");
		상품2 = new Product();
		상품2.setId(2L);
		상품2.setPrice(BigDecimal.valueOf(2000L));
		상품2.setName("새_상품2");
		메뉴_그룹 = new MenuGroup();
		메뉴_그룹.setId(1L);
	}

	@DisplayName("메뉴 생성 : 메뉴 이름과 가격, 메뉴 그룹 번호, 상품 번호 및 가격 목록을 받는다")
	@Test
	void create() {
		// given
		Menu 메뉴 = new Menu();
		메뉴.setName("메뉴");
		메뉴.setMenuGroupId(메뉴_그룹.getId());

		MenuProduct 메뉴_상품1 = new MenuProduct();
		메뉴_상품1.setProductId(상품1.getId());
		메뉴_상품1.setQuantity(3L);
		BigDecimal 메뉴_상품1_가격 = 상품1.getPrice().multiply(BigDecimal.valueOf(메뉴_상품1.getQuantity()));
		MenuProduct 메뉴_상품2 = new MenuProduct();
		메뉴_상품2.setProductId(상품2.getId());
		메뉴_상품2.setQuantity(2L);
		BigDecimal 메뉴_상품2_가격 = 상품2.getPrice().multiply(BigDecimal.valueOf(메뉴_상품2.getQuantity()));

		메뉴.setMenuProducts(Arrays.asList(메뉴_상품1, 메뉴_상품2));

		given(productDao.findById(상품1.getId())).willReturn(Optional.of(상품1));
		given(productDao.findById(상품2.getId())).willReturn(Optional.of(상품2));
		given(menuGroupDao.existsById(메뉴_그룹.getId())).willReturn(true);
		given(menuDao.save(메뉴)).willAnswer(invocation -> {
			메뉴.setId(1L);
			return 메뉴;
		});
		given(menuProductDao.save(메뉴_상품1)).willAnswer(invocation -> {
			메뉴_상품1.setSeq(1L);
			return 메뉴_상품1;
		});
		given(menuProductDao.save(메뉴_상품2)).willAnswer(invocation -> {
			메뉴_상품2.setSeq(2L);
			return 메뉴_상품2;
		});

		// when : 예외 케이스
		Menu saveMenu;
		메뉴.setPrice(메뉴_상품1_가격.add(메뉴_상품2_가격).add(BigDecimal.ONE));

		// then : 메뉴 가격은 메뉴에 속한 상품들의 금액보다 클 수 없음.
		assertThatThrownBy(() -> menuService.create(메뉴)).isInstanceOf(IllegalArgumentException.class);

		// when : 정상 케이스
		메뉴.setPrice(메뉴_상품1_가격.add(메뉴_상품2_가격));
		saveMenu = menuService.create(메뉴);

		// then : 메뉴 가격이 메뉴에 속한 상품들의 금액과 같거나 작음
		assertThat(saveMenu.getId()).isEqualTo(1L);
		assertThat(saveMenu.getPrice()).isEqualTo(메뉴.getPrice());
		assertThat(saveMenu.getMenuGroupId()).isEqualTo(메뉴.getMenuGroupId());
		assertThat(saveMenu.getPrice()).isEqualTo(메뉴.getPrice());
		assertThat(saveMenu.getMenuProducts())
			.map(MenuProduct::getProductId)
			.contains(상품1.getId(), 상품2.getId());
	}

	@DisplayName("메뉴 목록 : 조회 결과에는 메뉴 번호, 메뉴 이름, 가격, 메뉴 그룹 번호, 메뉴 상품 목록이 포함됨.")
	@Test
	void list() {
		// given
		Menu 메뉴 = new Menu();
		메뉴.setId(1L);
		메뉴.setName("메뉴");
		메뉴.setPrice(BigDecimal.valueOf(10000L));
		메뉴.setMenuGroupId(메뉴_그룹.getId());
		MenuProduct 메뉴_상품1 = new MenuProduct();
		메뉴_상품1.setProductId(상품1.getId());
		MenuProduct 메뉴_상품2 = new MenuProduct();
		메뉴_상품2.setProductId(상품2.getId());
		given(menuDao.findAll()).willReturn(Arrays.asList(메뉴));
		given(menuProductDao.findAllByMenuId(메뉴.getId())).willReturn(Arrays.asList(메뉴_상품1, 메뉴_상품2));

		// when
		List<Menu> saveMenuList = menuService.list();

		// then
		assertThat(saveMenuList).anySatisfy(saveMenu -> {
			assertThat(saveMenu.getId()).isEqualTo(1L);
			assertThat(saveMenu.getPrice()).isEqualTo(메뉴.getPrice());
			assertThat(saveMenu.getMenuGroupId()).isEqualTo(메뉴.getMenuGroupId());
			assertThat(saveMenu.getPrice()).isEqualTo(메뉴.getPrice());
			assertThat(saveMenu.getMenuProducts()).map(MenuProduct::getProductId)
				.contains(상품1.getId(), 상품2.getId());
		});
	}
}
