package kitchenpos.menu;

import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;

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
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

	@Mock
	private MenuDao menuDao;

	@Mock
	private MenuGroupDao menuGroupDao;

	@Mock
	private ProductDao productDao;

	@InjectMocks
	private MenuService menuService;

	MenuGroup 와퍼세트;
	Menu 와퍼;
	MenuProduct 불고기패티;
	MenuProduct 양배추;
	MenuProduct 빵;
	MenuProduct 토마토;
	Menu 치즈와퍼;

	@BeforeEach
	void setUp() {
		와퍼세트 = new MenuGroup();
		와퍼세트.setId(1L);
		와퍼세트.setName("와퍼세트");

		불고기패티 = new MenuProduct();
		불고기패티.setSeq(1L);
		불고기패티.setMenuId(1L);
		불고기패티.setQuantity(1000);

		양배추 = new MenuProduct();
		양배추.setSeq(2L);
		양배추.setMenuId(1L);
		양배추.setQuantity(100);

		빵 = new MenuProduct();
		빵.setSeq(3L);
		빵.setMenuId(1L);
		빵.setQuantity(300);

		토마토 = new MenuProduct();
		토마토.setSeq(4L);
		토마토.setMenuId(1L);
		토마토.setQuantity(200);

		와퍼 = new Menu();
		와퍼.setId(1L);
		와퍼.setName("와퍼");
		와퍼.setMenuGroupId(1L);
		와퍼.setMenuProducts(Arrays.asList(불고기패티, 양배추, 빵, 토마토));
		와퍼.setPrice(new BigDecimal(5000));

	}

	@DisplayName("메뉴를 생성한다.")
	@Test
	void 메뉴를_생성한다() {
		given(menuGroupDao.existsById(와퍼.getMenuGroupId())).willReturn(true);
		given(menuDao.save(와퍼)).willReturn(와퍼);
		//given(productDao.findById(양배추.getProductId())).willReturn();
		Menu created = menuService.create(와퍼);
	}
}
