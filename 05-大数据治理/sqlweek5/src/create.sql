CREATE TABLE "hjqt2"."sale" ( "year_month" VARCHAR(10) NOT NULL , "city" VARCHAR(20) NOT NULL , "cate_id" int NOT NULL , "payment" FLOAT4,PRIMARY KEY ( "year_month" , "city"  , "cate_id" ) );

COMMENT ON TABLE "hjqt2"."sale" IS '销售信息';

COMMENT ON COLUMN "hjqt2"."sale"."year_month" IS '销售年月';

COMMENT ON COLUMN "hjqt2"."sale"."city" IS '销售城市';

COMMENT ON COLUMN "hjqt2"."sale"."cate_id" IS '类别id';

COMMENT ON COLUMN "hjqt2"."sale"."payment" IS '金额';

CREATE TABLE "hjqt2"."cate" ( "cate_id" int NOT NULL , "cate_name" VARCHAR(100),PRIMARY KEY ( "cate_id" ) );

COMMENT ON TABLE "hjqt2"."cate" IS '类别信息';

COMMENT ON COLUMN "hjqt2"."cate"."cate_id" IS '类别id';

COMMENT ON COLUMN "hjqt2"."cate"."cate_name" IS '类别名';

CREATE TABLE "hjqt2"."goods" ( "goods_id" int NOT NULL , "seller_id" int NOT NULL , "cate_id" int NOT NULL , "spu_id" int, "price" FLOAT4,PRIMARY KEY ( "goods_id" ) );

COMMENT ON TABLE "hjqt2"."goods" IS '商品信息';

COMMENT ON COLUMN "hjqt2"."goods"."goods_id" IS '商品id';

COMMENT ON COLUMN "hjqt2"."goods"."seller_id" IS '商家id';

COMMENT ON COLUMN "hjqt2"."goods"."cate_id" IS '类别id';

COMMENT ON COLUMN "hjqt2"."goods"."spu_id" IS 'spu_id';

COMMENT ON COLUMN "hjqt2"."goods"."price" IS '商品价格';

CREATE TABLE "hjqt2"."address" ( "address_id" int NOT NULL , "province" VARCHAR(20), "city" VARCHAR(20), "detail_address" VARCHAR(100),PRIMARY KEY ( "address_id" ) );

COMMENT ON TABLE "hjqt2"."address" IS '地址信息';

COMMENT ON COLUMN "hjqt2"."address"."address_id" IS '地址id';

COMMENT ON COLUMN "hjqt2"."address"."province" IS '所在省份';

COMMENT ON COLUMN "hjqt2"."address"."city" IS '所在城市';

COMMENT ON COLUMN "hjqt2"."address"."detail_address" IS '地址';

CREATE TABLE "hjqt2"."order" ( "order_id" int NOT NULL , "goods_id" int, "number" int, "total_payment" FLOAT4, "order_time" DATE, "buyer_id" int, "address_id" int, "order_state" int, "year_month" VARCHAR(10),PRIMARY KEY ( "order_id" ) );

COMMENT ON TABLE "hjqt2"."order" IS '订单信息';

COMMENT ON COLUMN "hjqt2"."order"."order_id" IS '订单id';

COMMENT ON COLUMN "hjqt2"."order"."goods_id" IS '货物id';

COMMENT ON COLUMN "hjqt2"."order"."number" IS '数量';

COMMENT ON COLUMN "hjqt2"."order"."total_payment" IS 'total_payment';

COMMENT ON COLUMN "hjqt2"."order"."order_time" IS '订单时间';

COMMENT ON COLUMN "hjqt2"."order"."buyer_id" IS '买家id';

COMMENT ON COLUMN "hjqt2"."order"."address_id" IS '地址id';

COMMENT ON COLUMN "hjqt2"."order"."order_state" IS '订单状态';

COMMENT ON COLUMN "hjqt2"."order"."year_month" IS '订单年月';

CREATE TABLE "hjqt2"."seller" ( "user_id" int NOT NULL , "user_name" VARCHAR(20), "user_address" int, "telephone" VARCHAR(20), "password" VARCHAR(20), "email" VARCHAR(30), "sex" CHAR(1), "age" int, "register_time" DATE, "year_month" VARCHAR(10),PRIMARY KEY ( "user_id" ) );

COMMENT ON TABLE "hjqt2"."seller" IS '商家信息';

COMMENT ON COLUMN "hjqt2"."seller"."user_id" IS '用户id';

COMMENT ON COLUMN "hjqt2"."seller"."user_name" IS '用户名';

COMMENT ON COLUMN "hjqt2"."seller"."user_address" IS '地址id';

COMMENT ON COLUMN "hjqt2"."seller"."telephone" IS '电话';

COMMENT ON COLUMN "hjqt2"."seller"."password" IS '密码';

COMMENT ON COLUMN "hjqt2"."seller"."email" IS '邮件';

COMMENT ON COLUMN "hjqt2"."seller"."sex" IS '性别';

COMMENT ON COLUMN "hjqt2"."seller"."age" IS '年龄';

COMMENT ON COLUMN "hjqt2"."seller"."register_time" IS '注册时间';

COMMENT ON COLUMN "hjqt2"."seller"."year_month" IS '注册年月';

CREATE TABLE "hjqt2"."customer" ( "user_id" int NOT NULL , "user_name" character varying(20), "user_address" int, "telephone" VARCHAR(20), "password" VARCHAR(20), "email" VARCHAR(30), "sex" CHAR(1), "age" int, "register_time" DATE, "year_month" VARCHAR(10),PRIMARY KEY ( "user_id" ) );

COMMENT ON TABLE "hjqt2"."customer" IS '买家信息';

COMMENT ON COLUMN "hjqt2"."customer"."user_id" IS '用户id';

COMMENT ON COLUMN "hjqt2"."customer"."user_name" IS '用户名';

COMMENT ON COLUMN "hjqt2"."customer"."user_address" IS '地址id';

COMMENT ON COLUMN "hjqt2"."customer"."telephone" IS '用户电话';

COMMENT ON COLUMN "hjqt2"."customer"."password" IS '用户密码';

COMMENT ON COLUMN "hjqt2"."customer"."email" IS '用户邮件';

COMMENT ON COLUMN "hjqt2"."customer"."sex" IS '用户性别';

COMMENT ON COLUMN "hjqt2"."customer"."age" IS '用户年龄';

COMMENT ON COLUMN "hjqt2"."customer"."register_time" IS '注册时间';

COMMENT ON COLUMN "hjqt2"."customer"."year_month" IS '注册年月';