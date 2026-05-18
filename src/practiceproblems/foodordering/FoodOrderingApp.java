package src.practiceproblems.foodordering;
import java.util.*;


enum OrderStatus {
    PLACED,
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED
}

class User {
    private final int id;
    private final String username;


    User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}

class FoodItem {
    private final int id;
    String name;


    FoodItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }

        if(!(o instanceof FoodItem)) {
            return false;
        }

        FoodItem item = (FoodItem) o;

        return Objects.equals(item.getId(), this.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}

class RatingStats {
    double averageRating;
    int count;

    public double updateRating(int rating) {
        double currentRating =  averageRating * (double) count;
        currentRating += (double) rating;
        count++;
        averageRating = currentRating/count;
        return averageRating;
    }

    public double getAverageRating() {
        return averageRating;
    }
}

class Restaurant {
    private final int id;
    String name;
    private final Map<FoodItem, Integer> menu;
    private final RatingStats restaurantRating;
    private final Map<FoodItem, RatingStats> menuRatings;

    public Restaurant(int id, String name) {
        this.id = id;
        this.name = name;
        menu = new HashMap<>();
        restaurantRating = new RatingStats();
        menuRatings = new HashMap<>();
    }

    public void addOrUpdateFoodItem(FoodItem item, int price) {
        if(item==null) {
            throw new IllegalArgumentException("Item is invalid");
        }
        menu.put(item, price);
        menuRatings.computeIfAbsent(item, x-> new RatingStats());
    }

    public void addRestaurantRating(int rating) {
        restaurantRating.updateRating(rating);
    }

    public void addFoodItemRating(FoodItem item, int rating) {
        if(item==null) {
            throw new IllegalArgumentException("Item is invalid");
        }

        if(!menuRatings.containsKey(item)) {
            throw new IllegalArgumentException("Item not on menu");
        }
        menuRatings.get(item).updateRating(rating);
    }

    public double getRestaurantRating() {
        return restaurantRating.getAverageRating();
    }

    public double getFoodItemRating(FoodItem item) {
        if(item==null) {
            throw new IllegalArgumentException("Item is invalid");
        }

        if(!menuRatings.containsKey(item)) {
            throw new IllegalArgumentException("Item not on menu");
        }

        return menuRatings.get(item).getAverageRating();
    }

    public int getFoodItemPrice(FoodItem item) {
        if(item==null) {
            throw new IllegalArgumentException("Item is invalid");
        }

        if(!menu.containsKey(item)) {
            throw new IllegalArgumentException("Item not on menu");
        }

        return menu.get(item);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}

class OrderItem {
    FoodItem item;
    int quantity;

    public OrderItem(FoodItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public FoodItem getFoodItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }
}

class Order {
    int orderId;
    List<OrderItem> orderItems;
    Restaurant restaurant;
    OrderStatus orderStatus;
    User user;

    public Order(int orderId, List<OrderItem> orderItems, Restaurant restaurant, User user) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.restaurant = restaurant;
        this.orderStatus = OrderStatus.PLACED;
        this.user = user;
    }

    public int getOrderId() {
        return orderId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public int getBillAmount() {
        int total = 0;
        for(OrderItem item: orderItems) {
            total += item.getQuantity() * restaurant.getFoodItemPrice(item.getFoodItem());
        }
        return total;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}


class FoodOrderingSystem {
    private final Map<Integer, Restaurant> restaurantMap;
    private final Map<Integer, FoodItem> foodItemMap;
    private final Map<Integer, Order> orderMap;
    private final TreeSet<Restaurant> restarantRatings;
    private final Map<FoodItem, TreeSet<Restaurant>> foodItemRatings;
    private final Map<Integer, User> userMap;

    private static int foodItemCount = 0;
    private static int restaurantCount = 0;
    private static int orderCount = 0;
    private static int userCount=0;

    public FoodOrderingSystem() {
        restaurantMap = new HashMap<>();
        foodItemMap = new HashMap<>();
        orderMap = new HashMap<>();

        this.restarantRatings = new TreeSet<>((a, b) -> {
            int compare = Double.compare(b.getRestaurantRating(), a.getRestaurantRating());
            if(compare!=0) return compare;
            return Integer.compare(a.getId(), b.getId());
        });

        this.foodItemRatings = new HashMap<>();
        this.userMap = new HashMap<>();
    }

    public int addFoodItem(String name) {
        foodItemCount++;
        FoodItem foodItem = new FoodItem(foodItemCount, name);
        foodItemMap.put(foodItemCount, foodItem);
        foodItemRatings.put(foodItem, new TreeSet<>((a, b) -> {
            int compare = Double.compare(b.getFoodItemRating(foodItem), a.getFoodItemRating(foodItem));
            if(compare != 0)
                return compare;
            return Integer.compare(a.getId(), b.getId());
        }));
        return foodItemCount;
    }

    public int addRestaurant(String name) {
        if(name==null || name.isEmpty()) {
            throw new IllegalArgumentException("Restaurant name is empty");
        }
        restaurantCount++;
        Restaurant restaurant = new Restaurant(restaurantCount, name);
        restaurantMap.put(restaurantCount, restaurant);
        restarantRatings.add(restaurant);
        return restaurantCount;
    }

    public int addUser(String name) {
        if(name==null || name.isEmpty()) {
            throw new IllegalArgumentException("User name is empty");
        }
        userCount++;
        User user = new User(userCount, name);
        userMap.put(userCount, user);
        return userCount;
    }

    public int placeOrder(int restaurantId, int userId, List<OrderItem> orderItems) {
        if(!restaurantMap.containsKey(restaurantId)){
            throw new IllegalArgumentException("Invalid restaurant");
        }

        Restaurant restaurant = restaurantMap.get(restaurantId);

        if(!userMap.containsKey(userId)){
            throw new IllegalArgumentException("Invalid user");
        }

        User user = userMap.get(userId);

        orderCount++;
        Order order = new Order(orderCount, orderItems, restaurant, user);
        orderMap.put(orderCount, order);
        return orderCount;
    }

    public void addRating(int restaurantId, int rating) {
        if(!restaurantMap.containsKey(restaurantId)){
            throw new IllegalArgumentException("Invalid restaurant");
        }

        Restaurant restaurant = restaurantMap.get(restaurantId);
        restarantRatings.remove(restaurant);
        restaurant.addRestaurantRating(rating);
        restarantRatings.add(restaurant);
    }

    public void addRating(int foodItemId, int restaurantId, int rating) {
        if(!restaurantMap.containsKey(restaurantId)){
            throw new IllegalArgumentException("Invalid restaurant");
        }

        Restaurant restaurant = restaurantMap.get(restaurantId);

        if(!foodItemMap.containsKey(foodItemId)){
            throw new IllegalArgumentException("Invalid food item Id");
        }

        FoodItem item = foodItemMap.get(foodItemId);

        foodItemRatings.get(item).remove(restaurant);
        restaurant.addFoodItemRating(item, rating);
        foodItemRatings.get(item).add(restaurant);
    }

    public void updateOrderStatus(int orderId, OrderStatus orderStatus) {
        if(!orderMap.containsKey(orderId)) {
            throw new IllegalArgumentException("Invalid order id");
        }

        orderMap.get(orderId).setOrderStatus(orderStatus);
    }

    public Restaurant getTopKRestaurant() {
        return restarantRatings.first();
    }

    public Restaurant getTopRestaurantForFoodItem(int foodItemId) {
        if(!foodItemMap.containsKey(foodItemId)){
            throw new IllegalArgumentException("Invalid food item Id");
        }

        FoodItem item = foodItemMap.get(foodItemId);

        return foodItemRatings.get(item).first();
    }
}

public class FoodOrderingApp {
}
