package dsalldmix;

import java.time.LocalDateTime;
import java.util.HashMap;

enum CouponType {
    FIXED,
    PERCENTAGE
}

class Coupon {

    private final String couponCode;
    private final CouponType couponType;
    private LocalDateTime start;
    private LocalDateTime end;
    private Double value;
    private final Double orderMinValue;
    private final Integer maxUses;
    private int totalRedemptions = 0;
    private final Integer userPerRedemptionLimit;
    private HashMap<String, Integer> userPerRedemption = new HashMap<>();

    public Coupon(String couponCode, CouponType couponType,
                  LocalDateTime start, LocalDateTime end,
                  Double value, Double orderMinValue,
                  Integer maxUses, Integer userPerRedemptionLimit) {
        this.couponCode = couponCode;
        this.couponType = couponType;
        this.start = start;
        this.end = end;
        this.value = value;
        this.orderMinValue = orderMinValue;
        this.maxUses = maxUses;
        this.userPerRedemptionLimit = userPerRedemptionLimit;

    }


    public boolean isValidCoupon(String userId, double orderValue, String couponCode) {
        if (LocalDateTime.now().isAfter(end) || LocalDateTime.now().isBefore(start)) {
            System.out.println("Coupon is invalid for this date: " + LocalDateTime.now());
            return false;
        }

        if (orderValue < orderMinValue) {
            System.out.println("Invalid orderValue!");
            return false;
        }
        if (totalRedemptions >= maxUses) {
            System.out.println("Coupon Redemption limit exceed!");
            return false;
        }
        if (userPerRedemption.containsKey(userId)) {
            int userRedemption = userPerRedemption.getOrDefault(userId, 0);
            if (userRedemption >= userPerRedemptionLimit) {
                System.out.println("User per redemption limit for this coupon exceeded ");
                return false;
            }
        }

        return true;

    }

    public Double applyCoupon(String userId, Double amount) {

        if (!isValidCoupon(userId, amount, couponCode)) {
            throw new RuntimeException("Invalid Coupon!");
        }
        double discount = 0.0;
        if (couponType == CouponType.FIXED) {
            discount = amount - value;
            System.out.println("Coupon has been applied!");

        } else {
            discount = amount - (amount * value) / 100;
            System.out.println("Coupon has been applied!");
        }

        totalRedemptions++;
        userPerRedemption.put(userId, userPerRedemption.getOrDefault(userId, 0) + 1);
        return discount;
    }


}


public class CouponDiscountService {


    public static void main(String[] args) {
        Coupon coupon = new Coupon("SALE123",
                CouponType.FIXED,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(3),
                20.0,
                200.0,
                4,
                2);

        Double currentOrder = 350.0;
        Double afterDiscount = coupon.applyCoupon("user123", currentOrder);
        System.out.println("Order After discount is: " + afterDiscount);
        afterDiscount = coupon.applyCoupon("user123", 220.0);
        System.out.println("Order After discount is: " + afterDiscount);
    }
}
