package jp.co.rakuten.ecommerce.application.session;

import jp.co.rakuten.ecommerce.common.dto.ItemDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {

    @Getter
    private final List<CartItem> items = new ArrayList<>();

    @Getter
    private Double totalPrice = 0d;

    @Getter @Setter
    private Integer totalPoint = 0;

    public void add(ItemDto item, int quantity) {
        // cartの中身がある場合ループに入る
        for (CartItem cartItem : items) {
            if (cartItem.getItemId() == item.getItemId()) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                totalPrice += (item.getPrice() * quantity);
                return;
            }
        }

        // カートの中身が存在しない場合
        CartItem newCartItem = new CartItem();
        newCartItem.setItemId(item.getItemId());
        newCartItem.setName(item.getName());
        newCartItem.setPrice(item.getPrice());
        newCartItem.setPhotoAddress(item.getPhotoAddress());
        newCartItem.setDescription(item.getDescription());
        newCartItem.setQuantity(quantity);
        newCartItem.setPoint(item.getPoint());
        totalPrice += item.getPrice() * quantity;
        items.add(newCartItem);
    }

    public void update(int itemId, int quantity) {  // +ボタンの時はこの第二引数が1になるから1足される
        for (int i = 0; i < items.size(); i++) {
            CartItem cartItem = items.get(i);
            // +-だけじゃなくてdetailから個数が追加される場合、そのidがグローバルで宣言されたCartItem型のitemsと一致するか見る
            if (cartItem.getItemId() == itemId) {
                int newQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(newQuantity);
                totalPrice += cartItem.getPrice() * quantity;
                if (newQuantity == 0) {
                    items.remove(i);
                }
                return;
            }
        }
    }

    public void clear() {  // 未完成
        System.out.println("a");
        this.items.clear();
        this.totalPrice = 0d;
        return;
    }
}