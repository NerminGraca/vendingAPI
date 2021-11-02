package ba.ngraca.vending.payload.response;

import ba.ngraca.vending.dtos.ProductDTO;
import ba.ngraca.vending.enums.CoinEnum;

import java.util.Map;

public class BuyResponse {
    private int spent;

    private ProductDTO boughtProducts;

    private Map<CoinEnum, Integer> change;

    public int getSpent() {
        return spent;
    }

    public void setSpent(int spent) {
        this.spent = spent;
    }

    public ProductDTO getBoughtProducts() {
        return boughtProducts;
    }

    public void setBoughtProducts(ProductDTO boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    public Map<CoinEnum, Integer> getChange() {
        return change;
    }

    public void setChange(Map<CoinEnum, Integer> change) {
        this.change = change;
    }
}
