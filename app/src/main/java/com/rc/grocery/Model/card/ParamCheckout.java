package com.rc.grocery.Model.card;


public class ParamCheckout  {
    private String first_name = "";
    private String last_name = "";
    private String email = "";
    private String phone = "";
    private float total_amount = 0.0f;
    private String transaction_id = "";

    public ParamCheckout() {
    }

    public ParamCheckout(String first_name, String last_name, String email, String phone, float total_amount, String transaction_id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.total_amount = total_amount;
        this.transaction_id = transaction_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(float total_amount) {
        this.total_amount = total_amount;
    }

    @Override
    public String toString() {
        return "ParamCheckout{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", total_amount=" + total_amount +
                ", transaction_id='" + transaction_id + '\'' +
                '}';
    }
}
