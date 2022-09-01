package com.petrovskiy.epm.constants;

public class SqlQuery{
    public static final String FIND_CERTIFICATES_BY_SEARCH_PART =
            "SELECT c FROM GiftCertificate c WHERE c.name LIKE %:searchPart% OR c.description LIKE %:searchPart%";
    public static final String FIND_CERTIFICATES_BY_TAG_NAMES_AND_SEARCH_PART =
            "SELECT c FROM GiftCertificate c JOIN c.tagList tl WHERE tl.name IN :tagNameList" +
                    " AND (c.name LIKE %:searchPart% OR c.description LIKE %:searchPart%) GROUP BY c HAVING COUNT(tl)>=:tagNumber";
    public static final String FIND_MOST_POPULAR_TAG =
            """
            SELECT t.id, t.name FROM tag as t
            JOIN gift_certificate_has_tag as gst ON gst.tag_id=t.id
            JOIN gift_certificate_has_orders as gco ON gco.gift_certificate_id= gst.gift_certificate_id
            JOIN orders as o ON o.id = gco.orders_id AND o.users_id=
                (SELECT u.id FROM users as u
                JOIN orders as ord ON ord.users_id = u.id
            	GROUP BY u.id ORDER BY sum(ord.order_cost) DESC LIMIT 1)
            	GROUP BY t.id ORDER BY count(t.id) DESC LIMIT 1""";

    private SqlQuery() {
    }
}
