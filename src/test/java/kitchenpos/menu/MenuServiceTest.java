package kitchenpos.menu;

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

import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.menugroup.MenuGroupServiceTest;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

	@Mock
	private MenuDao menuDao;

	@Mock
	private MenuGroupDao menuGroupDao;

	@Mock
	private ProductDao productDao;

	@Mock
	private MenuProductDao menuProductDao;

	@InjectMocks
	private MenuService menuService;

	MenuGroup 피자;
	Menu 하와이안_피자_라지_원_플러스_원;
	Product 하와이안_피자;
	MenuProduct 하와이안_피자_라지;
	MenuProduct 하와이안_피자_미들;

	@BeforeEach
	void setUp() {
		피자 = MenuGroupServiceTest.메뉴그룹_생성(1L, "피자");
		하와이안_피자_라지_원_플러스_원 = 메뉴생성(1L, "하와이안 피자 라지 원플원", new BigDecimal(20000), 피자.getId(), Arrays.asList(하와이안_피자_라지));
	}

	@DisplayName("메뉴를 생성한다.")
	@Test
	void 메뉴를_생성한다() {
		/*
		given(menuGroupDao.existsById(와퍼.getMenuGroupId())).willReturn(true);
		given(productDao.findById(불고기패티.getId())).willReturn(Optional.of(불고기패티));
		given(productDao.findById(양배추.getId())).willReturn(Optional.of(양배추));
		given(productDao.findById(빵.getId())).willReturn(Optional.of(빵));
		given(productDao.findById(토마토.getId())).willReturn(Optional.of(토마토));
		given(menuProductDao.save(와퍼_불고기패티)).willReturn(와퍼_불고기패티);
		given(menuProductDao.save(와퍼_양배추)).willReturn(와퍼_양배추);
		given(menuProductDao.save(와퍼_빵)).willReturn(와퍼_빵);
		given(menuProductDao.save(와퍼_토마토)).willReturn(와퍼_토마토);
		given(menuDao.save(와퍼)).willReturn(와퍼);

		 */
		Menu created = menuService.create(하와이안_피자_라지_원_플러스_원);
	}

	@DisplayName("")
	@Test
	void 메뉴의_가격이_NULL_이면_에러_발생() {

	}

	public static Menu 메뉴생성(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
		Menu 메뉴 = new Menu();
		메뉴.setId(id);
		메뉴.setMenuProducts(menuProducts);
		메뉴.setName(name);
		메뉴.setPrice(price);
		메뉴.setMenuGroupId(menuGroupId);
		return 메뉴;
	}
}
