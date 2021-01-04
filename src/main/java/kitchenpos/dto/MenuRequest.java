package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {
	private String name;
	private BigDecimal price;
	private Long menuGroupId;
	private List<Long> menuProductIds;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public void setMenuGroupId(Long menuGroupId) {
		this.menuGroupId = menuGroupId;
	}

	public List<Long> getMenuProductIds() {
		return menuProductIds;
	}

	public void setMenuProductIds(List<Long> menuProductIds) {
		this.menuProductIds = menuProductIds;
	}
}
