package com.iflytek.web.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * 数据表Goods映射类
 */

@Data
@ToString
public class Goods {
    private Integer id;
    private Integer categoryId;
    private String goodsName;
    private String goodsIntroduce;
    private long goodsPrice;
    private String url;
    private String slidePicture;
    private double grade;
    private Integer rankNum;
    private String detailPicture;
}

/***
 *@Override
 *     public boolean equals(Object o) {
 *         if (this == o) return true;
 *         if (!(o instanceof Product)) return false;
 *
 *         Product product = (Product) o;
 *
 *         if (getId() != null ? !getId().equals(product.getId()) : product.getId() != null) return false;
 *         if (getCategoryId() != null ? !getCategoryId().equals(product.getCategoryId()) : product.getCategoryId() != null)
 *             return false;
 *         if (getItemType() != null ? !getItemType().equals(product.getItemType()) : product.getItemType() != null)
 *             return false;
 *         if (getTitle() != null ? !getTitle().equals(product.getTitle()) : product.getTitle() != null) return false;
 *         if (getSellPoint() != null ? !getSellPoint().equals(product.getSellPoint()) : product.getSellPoint() != null)
 *             return false;
 *         if (getPrice() != null ? !getPrice().equals(product.getPrice()) : product.getPrice() != null) return false;
 *         if (getNum() != null ? !getNum().equals(product.getNum()) : product.getNum() != null) return false;
 *         if (getImage() != null ? !getImage().equals(product.getImage()) : product.getImage() != null) return false;
 *         if (getStatus() != null ? !getStatus().equals(product.getStatus()) : product.getStatus() != null) return false;
 *         return getPriority() != null ? getPriority().equals(product.getPriority()) : product.getPriority() == null;
 *     }
 *
 *     @Override
 *     public int hashCode() {
 *         int result = getId() != null ? getId().hashCode() : 0;
 *         result = 31 * result + (getCategoryId() != null ? getCategoryId().hashCode() : 0);
 *         result = 31 * result + (getItemType() != null ? getItemType().hashCode() : 0);
 *         result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
 *         result = 31 * result + (getSellPoint() != null ? getSellPoint().hashCode() : 0);
 *         result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
 *         result = 31 * result + (getNum() != null ? getNum().hashCode() : 0);
 *         result = 31 * result + (getImage() != null ? getImage().hashCode() : 0);
 *         result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
 *         result = 31 * result + (getPriority() != null ? getPriority().hashCode() : 0);
 *         return result;
 *     }
 * */