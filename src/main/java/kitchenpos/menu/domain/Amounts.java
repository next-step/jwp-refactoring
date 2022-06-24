package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;

public class Amounts {

    private final List<Amount> amounts = new ArrayList<>();

    public void addAmount(Amount amount) {
        amounts.add(amount);
    }

    public int calculateTotalAmount() {
        return amounts.stream()
                .mapToInt(Amount::calculateAmount)
                .sum();
    }

}
