package kitchenpos.menu.application;

import kitchenpos.common.application.NotFoundException;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class MenuServiceTest {

	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuGroupService menuGroupService;

	@Autowired
	private ProductService productService;

	@Autowired
	private MenuRepository menuRepository;

	private static final int 콜라가격 = 1000;
	private static final int 감튀가격 = 2000;
	private static final int 버거가격 = 7000;
	private MenuGroupResponse 메뉴그룹;
	private ProductResponse 콜라;
	private ProductResponse 감튀;
	private ProductResponse 버거;
	private MenuProductRequest 요청_콜라;
	private MenuProductRequest 요청_감튀;
	private MenuProductRequest 요청_버거;
	private MenuRequest 메뉴_생성_요청_버거세트;

	@BeforeEach
	void setUp() {
		메뉴그룹 = menuGroupService.create(new MenuGroupRequest("버거킹"));
		콜라 = productService.create(new ProductRequest("콜라", new BigDecimal(콜라가격)));
		감튀 = productService.create(new ProductRequest("감자튀김", new BigDecimal(감튀가격)));
		버거 = productService.create(new ProductRequest("콰트로치즈와퍼", new BigDecimal(버거가격)));
		요청_콜라 = new MenuProductRequest(콜라.getId(), 1L);
		요청_감튀 = new MenuProductRequest(감튀.getId(), 1L);
		요청_버거 = new MenuProductRequest(버거.getId(), 1L);

		메뉴_생성_요청_버거세트 = new MenuRequest("콰트로치즈와퍼세트",
				new BigDecimal(콜라가격 + 감튀가격 + 버거가격 - 2000L),
				메뉴그룹.getId(),
				Arrays.asList(요청_콜라, 요청_감튀, 요청_버거));
	}

	@DisplayName("메뉴를 생성한다.")
	@Test
	void create() {
		// when
		MenuResponse menuResponse = menuService.create(메뉴_생성_요청_버거세트);

		// then
		assertThat(menuResponse.getPrice().longValue()).isEqualTo(콜라가격 + 감튀가격 + 버거가격 - 2000L);
		assertThat(menuResponse.getName()).isEqualTo("콰트로치즈와퍼세트");
		assertThat(menuResponse.getMenuGroupId()).isEqualTo(메뉴그룹.getId());
		Iterable<MenuProduct> menuProducts = menuRepository.findById(menuResponse.getId())
				.get().getMenuProducts();
		assertThat(menuProducts)
				.hasSize(3)
				.map(menuProduct -> menuProduct.getProduct().getId())
				.containsExactly(콜라.getId(), 감튀.getId(), 버거.getId());
	}

	@DisplayName("메뉴를 생성시 존재하지 않는 GroupId 사용시 예외발생.")
	@Test
	void create_WrongGroupId() {
		// given
		final long 잘못된_GroupId = -55L;
		MenuRequest 콰트로치즈와퍼세트 = new MenuRequest("콰트로치즈와퍼세트",
				new BigDecimal(콜라가격 + 감튀가격 + 버거가격 - 2000L),
				잘못된_GroupId,
				Arrays.asList(요청_콜라, 요청_감튀, 요청_버거));

		// when
		assertThatThrownBy(() -> menuService.create(콰트로치즈와퍼세트))
				.isInstanceOf(NotFoundException.class)
				.hasMessageMatching(MenuService.MSG_CANNOT_FIND_MENUGROUP);
	}

	@DisplayName("메뉴 생성시 존재하지 않는 ProductId 사용시 예외발생.")
	@Test
	void create_WrongProductId() {
		// given
		final long 잘못된_ProductId = -55L;
		MenuRequest 잘못된_요청 = new MenuRequest("콰트로치즈와퍼세트",
				new BigDecimal(콜라가격 + 감튀가격 + 버거가격 - 2000L),
				메뉴그룹.getId(),
				Arrays.asList(요청_콜라, 요청_감튀, new MenuProductRequest(잘못된_ProductId, 1)));

		// when
		assertThatThrownBy(() -> menuService.create(잘못된_요청))
				.isInstanceOf(NotFoundException.class)
				.hasMessageMatching(MenuService.MSG_CANNOT_FIND_PRODUCT);
	}

	@DisplayName("메뉴 리스트를 반환한다.")
	@Test
	void list() {
		// given
		List<MenuResponse> responses = Arrays.asList(
				menuService.create(메뉴_생성_요청_버거세트),
				menuService.create(메뉴_생성_요청_버거세트),
				menuService.create(메뉴_생성_요청_버거세트));

		// when then
		final List<Long> menuIds = responses.stream().map(MenuResponse::getId).collect(Collectors.toList());
		assertThat(menuService.list())
				.map(MenuResponse::getId)
				.containsAll(menuIds);
	}
}
