package dao;

import model.Payment;

public interface PaymentDao {
    int insertPayment(Payment payment);

    double getTodaySales();
}
