package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author : byungkyu
 * @date : 2021/01/25
 * @description :
 **/
@Embeddable
public class MenuProducts {

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	public MenuProducts() {
	}

	public MenuProducts(List<MenuProduct> menuProducts) {
		if(menuProducts == null){
			menuProducts = new ArrayList<>();
		}
		this.menuProducts = menuProducts;
	}


	public List<MenuProduct> getMenuProducts() {
		return menuProducts;
	}

	public void add(MenuProduct menuProduct){
		this.menuProducts.add(menuProduct);
	}
}
