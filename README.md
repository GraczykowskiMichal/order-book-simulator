# order-book-simulator
Simple order book (https://en.wikipedia.org/wiki/Order_book_(trading)) simulator.

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

## Requirements
Java SE Development Kit 8 is required to build the project.  
You can download it from here http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html.

## Build instructions
git clone https://github.com/GraczykowskiMichal/order-book-simulator  
cd order-book-simulator/src  
javac -cp ../jars/json.jar *.java
  
You can also open it as a project in IDE, such as IntelliJ IDEA. Do not forget to attach json.jar.

## Run instructions
java -cp .:../jars/json.jar OrderBook
