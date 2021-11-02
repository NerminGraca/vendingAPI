package ba.ngraca.vending.enums;

public enum CoinEnum {
    FIVE_CENTS(5),
    TEN_CENTS(10),
    TWENTY_CENTS(20),
    FIFTY_CENTS(50),
    HUNDRED_CENTS(100);

    int value;

    CoinEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
