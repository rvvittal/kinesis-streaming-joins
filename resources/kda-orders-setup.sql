CREATE OR REPLACE STREAM "ORDERSTREAM"(
   "orderId" INTEGER, 
   "orderAmount" DECIMAL(1,1),
   "orderStatus" VARCHAR(8),
   "orderDateTime" TIMESTAMP,
   "shipToName" VARCHAR(32),
   "shipToAddress" VARCHAR(32),
   "shipToCity" VARCHAR(32),
   "shipToState" VARCHAR(16),
   "shipToZip" VARCHAR(16),
   "recordType" VARCHAR(16));
   
CREATE OR REPLACE STREAM "ORDERITEMSTREAM"(
   "orderId" INTEGER, 
   "itemId" INTEGER, 
   "itemAmount" DECIMAL(1,1),
   "itemStatus" VARCHAR(8),
   "itemQuantity" INTEGER, 
   "orderDateTime" TIMESTAMP,
   "recordType" VARCHAR(16));
   
CREATE OR REPLACE STREAM "ORDER_ITEM_ENRICHED_STREAM"(
   "event_ts" TIMESTAMP,
   "orderId" INTEGER, 
   "orderAmount" DECIMAL(1,1),
   "orderStatus" VARCHAR(8),
   "orderDateTime" TIMESTAMP,
   "shipToName" VARCHAR(32),
   "shipToAddress" VARCHAR(32),
   "shipToCity" VARCHAR(32),
   "shipToState" VARCHAR(16),
   "shipToZip" VARCHAR(16),
    "itemId" INTEGER, 
   "itemAmount" DECIMAL(1,1),
    "itemQuantity" INTEGER, 
   "itemStatus" VARCHAR(8) 
   );
   
   
   
CREATE OR REPLACE PUMP "ORDERPUMP" AS 
INSERT INTO "ORDERSTREAM" (
						  "orderId", 
                          "orderAmount", 
                          "orderStatus",
                          "orderDateTime",
                          "shipToName",
                          "shipToAddress",
                          "shipToCity",
                          "shipToState",
                          "shipToZip",
                          "recordType"
                    	  ) 
SELECT STREAM 
			  "orderId", 
              "orderAmount", 
              "orderStatus",
              "orderDateTime",
              "shipToName",
              "shipToAddress",
              "shipToCity",
              "shipToState",
              "shipToZip",
              "recordType"
FROM "SOURCE_SQL_STREAM_001" where "recordType" = 'Order';

CREATE OR REPLACE PUMP "ORDERITEMPUMP" AS 
INSERT INTO "ORDERITEMSTREAM" (
						  "orderId", 
						  "itemId",
                          "itemAmount", 
                          "itemQuantity",
                          "itemStatus",
                          "recordType"
                    	  ) 
SELECT STREAM 
			  "orderId", 
			  "itemId",
              "itemAmount", 
              "itemQuantity",
              "itemStatus",
              "recordType"
FROM "SOURCE_SQL_STREAM_001" where "recordType" = 'OrderItem';

CREATE OR REPLACE PUMP "ORDER_ITEM_ENRICHED_PUMP" AS 
INSERT INTO "ORDER_ITEM_ENRICHED_STREAM" (
                          "event_ts",
						  "orderId", 
						  "orderAmount", 
			              "orderStatus",
			              "orderDateTime",
			              "shipToName",
			              "shipToAddress",
			              "shipToCity",
			              "shipToState",
			              "shipToZip",
						  "itemId",
                          "itemAmount", 
                          "itemQuantity",
                          "itemStatus"
                    	  ) 

SELECT STREAM
     ROWTIME,
     o."orderId", o."orderAmount", o."orderStatus", o."orderDateTime", o."shipToName",o."shipToAddress", o."shipToCity", o."shipToState", o."shipToZip", 
     i."itemId", i."itemAmount", i."itemQuantity", i."itemStatus"
FROM ORDERITEMSTREAM OVER (RANGE INTERVAL '1' MINUTE PRECEDING)  AS i
JOIN ORDERSTREAM OVER (RANGE INTERVAL '1' MINUTE PRECEDING)  AS o
ON   i."orderId" = o."orderId";
