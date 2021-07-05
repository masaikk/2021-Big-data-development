--"hjqt2"."address"
insert into "hjqt2"."address"  values (0,'广东省','广州市','白云区');
insert into "hjqt2"."address"  values (1,'广东省','广州市','天河区');
insert into "hjqt2"."address"  values (2,'广东省','汕头市','地址');
insert into "hjqt2"."address"  values (3,'广东省','深圳市','宝安区');
insert into "hjqt2"."address"  values (4,'广东省','深圳市','地址2');
insert into "hjqt2"."address"  values (5,'广西省','桂林市','阳朔');

--"hjqt2"."customer"
insert into "hjqt2"."customer" values (0,'Any',0,'18888888888','Any','Any@123.com','0',21,to_date('20210710','yyyymmdd'),'2021-07');
insert into "hjqt2"."customer" values (1,'Bob',1,'17777777777','Anysd','Bob@123.com','1',21,to_date('20210722','yyyymmdd'),'2021-07');
insert into "hjqt2"."customer" values (2,'Carry',2,'1777777777','sdf','Carry@123.com','0',21,to_date('20210612','yyyymmdd'),'2021-06');
insert into "hjqt2"."customer" values (3,'Dell',3,'1999999999','thstrh','Dell@123.com','1',21,to_date('20200502','yyyymmdd'),'2020-05');

--"hjqt2"."seller"
insert into "hjqt2"."seller" values (0,'淘宝最牛逼的旗舰店',3,'1888888888888','sadafefsdf','nuibi@qq.com','1',21,to_date('19710502','yyyymmdd'),'1971-05');
insert into "hjqt2"."seller" values (1,'淘宝第二牛逼的旗舰店',3,'1888888888888','sgergergsdf','nuibi2@qq.com','1',21,to_date('19810501','yyyymmdd'),'1981-05');
insert into "hjqt2"."seller" values (2,'淘宝第三牛逼的旗舰店',3,'1888888888888','saehtrhwsdf','nuibi3@qq.com','1',21,to_date('20210501','yyyymmdd'),'2021-05');

--"hjqt2"."cate"
insert  into "hjqt2"."cate" values(0,'手机');
insert  into "hjqt2"."cate" values(1,'电脑');
insert  into "hjqt2"."cate" values(2,'书籍');
insert  into "hjqt2"."cate" values(3,'食品');
insert  into "hjqt2"."cate" values(4,'饮料');

--"hjqt2"."goods"
insert  into "hjqt2"."goods" values(0,0,0,0,999);
insert  into "hjqt2"."goods" values(1,0,0,1,200);
insert  into "hjqt2"."goods" values(2,2,1,0,150);
insert  into "hjqt2"."goods" values(3,1,2,1,100);

--"hjqt2"."order"
insert  into "hjqt2"."order"  values(0,0,1,100,to_date('20200102','yyyymmdd'),0,0,1,'2020-01');
insert  into "hjqt2"."order"  values(1,1,1,200,to_date('20210102','yyyymmdd'),0,0,1,'2021-01');
insert  into "hjqt2"."order"  values(2,1,1,300,to_date('20210202','yyyymmdd'),1,1,1,'2021-02');
insert  into "hjqt2"."order"  values(3,0,1,400,to_date('20200302','yyyymmdd'),3,3,1,'2020-03');
insert  into "hjqt2"."order"  values(4,3,1,500,to_date('19720402','yyyymmdd'),2,2,1,'1972-04');
insert  into "hjqt2"."order"  values(5,3,1,600,to_date('19720102','yyyymmdd'),0,0,1,'1972-01');
insert  into "hjqt2"."order"  values(6,0,1,700,to_date('20210302','yyyymmdd'),3,1,1,'2021-03');
insert  into "hjqt2"."order"  values(7,0,1,800,to_date('20211102','yyyymmdd'),1,1,1,'2021-11');