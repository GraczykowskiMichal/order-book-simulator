# order-book-simulator
Simple order book ( https://en.wikipedia.org/wiki/Order_book_(trading) ) simulator

## Example session
>{“type”:“Limit”,“order”:{“direction”:“Buy”,“id”:1,“price”:14,“quantity”:20}}  
{“buyOrders”:[{“id”:1,“price”:14,“quantity”:20}],
“sellOrders”:[]}  

>{“type”:“Iceberg”,“order”:{“direction”:“Buy”,“id”:2,“price”:15,“quantity”:50,“peak”:20}}  
{“buyOrders”: [{“id”: 2, “price”: 15, “quantity”: 20}, {“id”: 1, “price”: 14, “quantity”: 20}],  
“sellOrders”:[]}  

>{“type”:“Limit”,“order”:{“direction”:“Sell”,“id”:3,“price”:16,“quantity”:15}}  
{“buyOrders”: [{“id”: 2, “price”: 15, “quantity”: 20}, {“id”: 1, “price”: 14, “quantity”: 20}], “sellOrders”:[{“id”:3,“price”:16,“quantity”:15}]}

>{“type”:“Limit”,“order”:{“direction”:“Sell”,“id”:4,“price”:13,“quantity”:60}}  
{“buyOrders”: [{“id”: 1, “price”: 14, “quantity”: 10}],  
“sellOrders”: [{“id”: 3, “price”: 16, “quantity”:15}]}  
{“buyOrderId”:2,“sellOrderId”:4,“price”:15,“quantity”:20}  
{“buyOrderId”:2,“sellOrderId”:4,“price”:15,“quantity”:20}  
{“buyOrderId”:2,“sellOrderId”:4,“price”:15,“quantity”:10}  
{“buyOrderId”:1,“sellOrderId”:4,“price”:14,“quantity”:10}  
