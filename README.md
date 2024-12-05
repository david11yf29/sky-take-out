# Sky Take Out
一個使用 Spring Boot 框架的外賣平台，用於下單和管理訂單。

## 目錄
- [功能](#功能)
- [使用方法](#使用方法)
- [技術細節](#技術細節)
- [貢獻](#貢獻)
- [授權](#授權)

## 功能
- 訂單管理
- 歷史訂單查看
- 即時 WebSocket 訊息通知
- 使用 Redis 緩存菜單
- 提供 RESTful API

## 使用方法
1. 打開瀏覽器，訪問 `http://localhost:80` 並且登入。
2. 使用 Postman 測試系統：
   - 加入菜品或訂單至購物車：`POST /user/shoppingCart/add`
   - 查詢購物車物品：`GET /user/shoppingCart/list`
   - 提交訂單：`POST /user/order/submit`
   - 支付訂單：`PUT /user/order/payment`
   - 創建訂單：`POST /api/orders`
   - 客戶催單：`GET /user/order/reminder/18`
  
## 技術細節
- **後端**：Spring Boot, MyBatis, Websocket
- **資料庫**：MySQL, Redis
- **前端**：Nginx

## 示例
![系統主界面](https://example.com/screenshot.png)

## 授權
本項目基於 [MIT License](LICENSE) 許可使用。




