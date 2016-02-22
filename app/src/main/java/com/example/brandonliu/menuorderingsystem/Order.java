package com.example.brandonliu.menuorderingsystem;

/**
 * Created by brandonliu on 2/20/16.
 */
public class Order {
    private int m_itemId;
    private int m_quantity;
    private String m_comment;
    public Order(int id, int quan, String comm) {
        m_itemId = id;
        m_quantity = quan;
        m_comment = comm;
    }
    public void setId(int id) {
        m_itemId = id;
    }
    public void setQuantity(int num) {
        m_quantity = num;
    }
    public void setComment(String comm){
        m_comment = comm;
    }

    public int getId() {
        return m_itemId;
    }

    public int getQuantity(){
        return m_quantity;
    }

    public String getComment() {
        return m_comment;
    }
}
