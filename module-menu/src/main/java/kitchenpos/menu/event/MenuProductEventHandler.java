package kitchenpos.menu.event;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Objects;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.exception.MenuException;

@Component
public class MenuProductEventHandler {

	private final MenuProductRepository menuProductRepository;
	private final ProductRepository productRepository;

	public MenuProductEventHandler(MenuProductRepository menuProductRepository, ProductRepository productRepository) {
		this.menuProductRepository = menuProductRepository;
		this.productRepository = productRepository;
	}

	@EventListener
	public void createMenuProducts(MenuCreateEvent createEvent) {
		MenuProducts menuProducts = toMenuProducts(createEvent.getMenuProductRequests());
		validate(menuProducts, createEvent.getMenu().getPrice());
		menuProducts.stream().forEach(menuProductRepository::save);
	}

	private MenuProducts toMenuProducts(List<MenuProductRequest> menuProductRequests) {
		return menuProductRequests.stream()
			.map(menuProductRequest -> {
				Product product = findProductById(menuProductRequest.getProductId());
				return menuProductRequest.toMenuProduct(product);
			})
			.collect(collectingAndThen(toList(), MenuProducts::new));
	}

	private Product findProductById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new MenuException("해당 상품이 존재하지 않습니다."));
	}

	private void validate(MenuProducts menuProducts, Price price) {
		validateMenuProducts(menuProducts);
		validatePrice(price, menuProducts);
	}

	private void validateMenuProducts(MenuProducts menuProducts) {
		if (Objects.isNull(menuProducts)) {
			throw new MenuException("메뉴 상품들이 하나도 존재하지 않습니다.");
		}
	}

	private void validatePrice(Price price, MenuProducts menuProducts) {
		if (price.compareTo(menuProducts.getSumMenuProductPrice()) > 0) {
			throw new MenuException("메뉴의 가격이 메뉴 상품들의 총합보다 클 수 없습니다.");
		}
	}
}
