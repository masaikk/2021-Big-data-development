--按月份查询总体销售额
select year_month, sum(total_payment )
from "hjqt2"."order" o
where order_state = 1
group by year_month;

--按月份查询新注册用户人数
select year_month ,sum("人数") "总注册人数" from(
	(select year_month , count(year_month ) "人数" from "hjqt2".customer bi group by year_month )
	union all
	(select year_month , count(year_month ) "人数" from "hjqt2".seller si group by year_month)
) a group  by year_month ;

--按月查看城市销售额
select year_month , a.city , sum(total_payment ) "城市销售额"
from "hjqt2"."order" o , "hjqt2".address a 
where o.address_id = a.address_id 
group by year_month ,a.city;

--按月查询在城市与商品类别区分的销量额
select o.year_month , cate.cate_name , a.city , sum(total_payment ) "月销售额"
from "hjqt2"."order" o ,"hjqt2"."goods" s ,"hjqt2"."cate" cate,"hjqt2"."address" a
where o.goods_id =s.goods_id and s.cate_id = cate.cate_id and o.address_id = a.address_id
group by year_month , cate.cate_name , a.city;


--按月查看以性别与商品类别区分的销售额
select o.year_month , case bi.sex when '1' then '男'  else '女' end as 性别, c.cate_name , sum(total_payment ) "月销售额"
from "hjqt2"."order" o , "hjqt2".customer bi ,"hjqt2".goods s ,"hjqt2".cate c
where o.buyer_id  = bi.user_id and o.goods_id =s.goods_id  and s.cate_id = c.cate_id
group by o.year_month , bi.sex , c.cate_name