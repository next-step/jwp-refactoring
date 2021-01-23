package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.MenuProduct;

/**
 * @author : byungkyu
 * @date : 2021/01/22
 * @description :
 **/
public class MenuRequest {
	private String name;
	private BigDecimal price;
	private Long menuGroupId;
	private List<Long> menuProductIds;

	public MenuRequest() {
	}

	public MenuRequest(String name, BigDecimal price, Long menuGroupId,
		List<Long> menuProductIds) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProductIds = menuProductIds;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public List<Long> getMenuProductIds() {
		return menuProductIds;
	}
}
