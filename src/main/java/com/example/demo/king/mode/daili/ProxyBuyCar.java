package com.example.demo.king.mode.daili;

public class ProxyBuyCar implements BuyCar {
    private MySelfBuyCar mySelfBuyCar;

    public ProxyBuyCar(MySelfBuyCar mySelfBuyCar) {
        this.mySelfBuyCar = mySelfBuyCar;
    }

    @Override
    public void buyCar() {
        mySelfBuyCar.buyCar();
        System.out.println("代理帮你上牌---手续费+500");
    }

    public static void main(String[] args) {
        BuyCar buyCar = new ProxyBuyCar(new MySelfBuyCar());
        buyCar.buyCar();
    }
}
