package com.iflytek.web.viewmodel;

import com.iflytek.web.pojo.Order;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 订单和订单明细，1：N
 * */
@Data
@ToString
public class OrderModel extends Order {
//   private List<OrderMxModel> mxList;
   private Integer mxId;
   private Integer goodsId;
   private Integer num;
   private Double goodsPrice;
   private String goodsName;
   private String url;
   private Integer categoryId;
}
