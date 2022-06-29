package kitchenpos.helper;

import java.lang.reflect.Field;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

public class ReflectionHelper {

    public static void SetProductId(Long id, Product product) {
        Field field = null;
        try {
            field = Product.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(product, id);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setMenuId(Long id, Menu menu) {
        Field field = null;
        try {
            field = Menu.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(menu, id);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setOrderId(Long id, Order order) {
        Field field = null;
        try {
            field = Order.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(order, id);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setOrderLineItemId(Long id, OrderLineItem orderLineItem) {
        Field field = null;
        try {
            field = OrderLineItem.class.getDeclaredField("seq");
            field.setAccessible(true);
            field.set(orderLineItem, id);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setTableGroupId(Long id, TableGroup tableGroup) {
        Field field = null;
        try {
            field = TableGroup.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(tableGroup, id);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
