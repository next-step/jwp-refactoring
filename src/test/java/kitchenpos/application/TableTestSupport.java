package kitchenpos.application;

import kitchenpos.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;

@Component
public class TableTestSupport {

	@Autowired
	private OrderService orderService;

	@Autowired
	private MenuGroupService menuGroupService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private ProductService productService;

	public TableTestSupport(OrderService orderService, MenuGroupService menuGroupService, MenuService menuService, ProductService productService) {
		this.orderService = orderService;
		this.menuGroupService = menuGroupService;
		this.menuService = menuService;
		this.productService = productService;
	}

	public void addOrder(Long tableId) {
		orderService.create(new OrderRequest_Create(Collections.singletonList(createRequest("짜장", 1000, 1)), tableId));
	}

	private OrderLineItemRequest createRequest(String menuName, int price, int quantity) {
		MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("음식"));
		ProductResponse product = productService.create(new ProductRequest(menuName, new BigDecimal(price)));
		MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1);
		MenuResponse menu = menuService.create(new MenuRequest("짜장면", new BigDecimal(price), menuGroup.getId(),
				Collections.singletonList(menuProductRequest)));
		return new OrderLineItemRequest(menu.getId(), quantity);
	}
}
