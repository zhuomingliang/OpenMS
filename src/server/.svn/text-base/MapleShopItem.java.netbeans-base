package server;

public class MapleShopItem {

    private short buyable;
    private int itemId;
    private int price;
    private int reqItem;
    private int reqItemQ;
    private int category;
    private int minLevel;
    private int expiration;
    private byte rank;

    public MapleShopItem(int itemId, int price, short buyable) {
        this.buyable = buyable;
        this.itemId = itemId;
        this.price = price;
        this.reqItem = 0;
        this.reqItemQ = 0;
        this.rank = (byte) 0;
        this.category = 0;
        this.minLevel = 0;
        this.expiration = 0;
    }

    public MapleShopItem(short buyable, int itemId, int price, int reqItem, int reqItemQ, byte rank, int category, int minLevel, int expiration) {
        this.buyable = buyable;
        this.itemId = itemId;
        this.price = price;
        this.reqItem = reqItem;
        this.reqItemQ = reqItemQ;
        this.rank = rank;
        this.category = category;
        this.minLevel = minLevel;
        this.expiration = expiration;
    }

    public short getBuyable() {
        return buyable;
    }

    public int getItemId() {
        return itemId;
    }

    public int getPrice() {
        return price;
    }

    public int getReqItem() {
        return reqItem;
    }

    public int getReqItemQ() {
        return reqItemQ;
    }

    public byte getRank() {
        return rank;
    }

    public int getCategory() {
        return category;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getExpiration() {
        return expiration;
    }
}
