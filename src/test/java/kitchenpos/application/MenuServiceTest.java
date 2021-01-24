package kitchenpos.application;

import static kitchenpos.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import kitchenpos.domain.MenuProduct;

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

	@DisplayName("메뉴 생성 : 메뉴 이름과 가격, 메뉴 그룹 번호, 상품 번호 및 가격 목록을 받는다")
	@Test
	void create_happyPath() {
		// given
		BigDecimal 메뉴_상품1_가격 = 상품1.getPrice().multiply(BigDecimal.valueOf(메뉴_상품1.getQuantity()));
		BigDecimal 메뉴_상품2_가격 = 상품2.getPrice().multiply(BigDecimal.valueOf(메뉴_상품2.getQuantity()));

		Menu 새_메뉴 = newMenu(null, "새_메뉴", 메뉴_상품1_가격.add(메뉴_상품2_가격).longValue(), 메뉴_그룹1.getId(),
			Arrays.asList(메뉴_상품1, 메뉴_상품2));

		given(productDao.findById(상품1.getId())).willReturn(Optional.of(상품1));
		given(productDao.findById(상품2.getId())).willReturn(Optional.of(상품2));
		given(menuGroupDao.existsById(메뉴_그룹1.getId())).willReturn(true);
		given(menuDao.save(새_메뉴)).willAnswer(invocation -> {
			새_메뉴.setId(1L);
			return 새_메뉴;
		});
		given(menuProductDao.save(메뉴_상품1)).willAnswer(invocation -> {
			메뉴_상품1.setSeq(1L);
			return 메뉴_상품1;
		});
		given(menuProductDao.save(메뉴_상품2)).willAnswer(invocation -> {
			메뉴_상품2.setSeq(2L);
			return 메뉴_상품2;
		});

		// when : 정상 케이스
		새_메뉴.setPrice(메뉴_상품1_가격.add(메뉴_상품2_가격));
		Menu saveMenu = menuService.create(새_메뉴);

		// then : 메뉴 가격이 메뉴에 속한 상품들의 금액과 같거나 작음
		assertThat(saveMenu.getId()).isEqualTo(1L);
		assertThat(saveMenu.getPrice()).isEqualTo(새_메뉴.getPrice());
		assertThat(saveMenu.getMenuGroupId()).isEqualTo(새_메뉴.getMenuGroupId());
		assertThat(saveMenu.getPrice()).isEqualTo(새_메뉴.getPrice());
		assertThat(saveMenu.getMenuProducts())
			.map(MenuProduct::getProductId)
			.contains(상품1.getId(), 상품2.getId());
	}

	@DisplayName("메뉴 생성 : 메뉴 가격이 메뉴에 속한 상품들의 금액보다 큼")
	@Test
	void create_exceptionCase1() {
		// given
		BigDecimal 메뉴_상품1_가격 = 상품1.getPrice().multiply(BigDecimal.valueOf(메뉴_상품1.getQuantity()));
		BigDecimal 메뉴_상품2_가격 = 상품2.getPrice().multiply(BigDecimal.valueOf(메뉴_상품2.getQuantity()));

		Menu 새_메뉴 = newMenu(null, "새_메뉴",
			메뉴_상품1_가격.add(메뉴_상품2_가격).add(BigDecimal.ONE).longValue(),
			메뉴_그룹1.getId(), Arrays.asList(메뉴_상품1, 메뉴_상품2));

		given(productDao.findById(상품1.getId())).willReturn(Optional.of(상품1));
		given(productDao.findById(상품2.getId())).willReturn(Optional.of(상품2));
		given(menuGroupDao.existsById(메뉴_그룹1.getId())).willReturn(true);

		// when : 예외 케이스
		Menu saveMenu;
		새_메뉴.setPrice(메뉴_상품1_가격.add(메뉴_상품2_가격).add(BigDecimal.ONE));

		// then : 메뉴 가격은 메뉴에 속한 상품들의 금액보다 클 수 없음.
		assertThatThrownBy(() -> menuService.create(새_메뉴)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 생성 : 메뉴가 그룹에 속해있지 않음")
	@Test
	void create_exceptionCase2() {
		// given
		BigDecimal 메뉴_상품1_가격 = 상품1.getPrice().multiply(BigDecimal.valueOf(메뉴_상품1.getQuantity()));
		BigDecimal 메뉴_상품2_가격 = 상품2.getPrice().multiply(BigDecimal.valueOf(메뉴_상품2.getQuantity()));

		Menu 새_메뉴 = newMenu(null, "새_메뉴",
			메뉴_상품1_가격.add(메뉴_상품2_가격).longValue(),
			null, Arrays.asList(메뉴_상품1, 메뉴_상품2));

		given(menuGroupDao.existsById(any())).willReturn(false);

		// when : 예외 케이스
		Menu saveMenu;
		새_메뉴.setPrice(메뉴_상품1_가격.add(메뉴_상품2_가격).add(BigDecimal.ONE));

		// then : 메뉴 가격은 메뉴에 속한 상품들의 금액보다 클 수 없음.
		assertThatThrownBy(() -> menuService.create(새_메뉴)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 목록 : 조회 결과에는 메뉴 번호, 메뉴 이름, 가격, 메뉴 그룹 번호, 메뉴 상품 목록이 포함됨.")
	@Test
	void list() {
		// given
		given(menuDao.findAll()).willReturn(Arrays.asList(메뉴1));
		given(menuProductDao.findAllByMenuId(메뉴1.getId())).willReturn(Arrays.asList(메뉴_상품1, 메뉴_상품2));

		// when
		List<Menu> saveMenuList = menuService.list();

		// then
		assertThat(saveMenuList).anySatisfy(saveMenu -> {
			assertThat(saveMenu.getId()).isEqualTo(1L);
			assertThat(saveMenu.getPrice()).isEqualTo(메뉴1.getPrice());
			assertThat(saveMenu.getMenuGroupId()).isEqualTo(메뉴1.getMenuGroupId());
			assertThat(saveMenu.getPrice()).isEqualTo(메뉴1.getPrice());
			assertThat(saveMenu.getMenuProducts()).map(MenuProduct::getProductId)
				.contains(상품1.getId(), 상품2.getId());
		});
	}
}
