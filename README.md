# 基于协同过滤算法的漫跃短视频推荐系统

本项目是一个仿b站的web和小程序，作为自己的毕业设计。

使用到的技术有：
* 后端
  * SpringBoot
  * MySQL
  * MyBatis
  * RabbitMQ
  * Redis
  * WebSocket
  * Webrtc
  * ElasticSearch
  * AOP
  * 协同过滤算法
* 前端：
  * Vue2
  * uniapp
  * dplayer

以下是项目的一些简要介绍：
1. 登录注册、修改资料、查看资料
2. 发布视频、观看视频、观看历史、收藏视频、评论视频、发送弹幕、实时更新同时在看人数、实时收发弹幕
3. 利用 WebSocket 技术和消息队列优化弹幕体验
4. 具备关注订阅功能，被关注用户发布视频后，粉丝能够接收通知
5. 集成 Elasticsearch 的搜索体验：随机排序（不同用户不同顺序），关键词高亮
6. 使用协同过滤算法，有基于用户推荐视频、基于视频推荐视频两种推荐算法
7. 使用消息队列、定时任务、阿里云视频点播服务对视频进行自动审核视频和发布
8. 使用 AOP 进行登录鉴权、接口限流
9. 视频等相关用户数据（点赞、收藏、观看次数）使用定时任务、多线程技术持久化
10. 新增Webrtc直播功能和私信以及视频通话功能

# 页面展示

```text
这里展示的vue前端
```
![image](https://github.com/user-attachments/assets/65365149-6e11-4918-932b-2d3ea2935560)
***
![image](https://github.com/user-attachments/assets/f985b048-ecd1-4885-bfc0-fc1cb02cdab0)
***
![image](https://github.com/user-attachments/assets/67851326-62f9-4830-8e62-68b800fc306c)

***
![image](https://github.com/user-attachments/assets/d9bdfbeb-17dc-41a1-b9ea-79475691c7d2)
***
![image](https://github.com/user-attachments/assets/df39a7b3-94f7-4353-bd20-f44a2542ddf2)
***
![image](https://github.com/user-attachments/assets/8789212a-9f01-484c-b279-d6cb295f477b)
***
![image](https://github.com/user-attachments/assets/8e994afc-202f-483c-b20c-7db91f9d7aa4)
***
![image](https://github.com/user-attachments/assets/000978bd-4d7c-4545-8779-428a31c795df)
***
![image](https://github.com/user-attachments/assets/2bfc5f62-1019-4a59-b1d0-e14bfcd4ba47)
***

![image](https://github.com/user-attachments/assets/3e3e18b3-a563-4cb2-b2f7-0300afc498f7)
***

![image](https://github.com/user-attachments/assets/457d41b0-d328-4e47-8d6f-db083e9649a5)
***

![image](https://github.com/user-attachments/assets/21746d7e-3c88-4e07-a98b-7e1317bf1e1b)

***

![image](https://github.com/user-attachments/assets/aa47ea97-c201-4f5d-a4bb-ea830ada883d)
***

![image](https://github.com/user-attachments/assets/a02f6fd2-9f96-4973-91b1-4ca2df17046e)
***

![image](https://github.com/user-attachments/assets/20a1e314-8922-4869-a34b-4c3154a3ee7b)
***

![image](https://github.com/user-attachments/assets/daba8a5d-ace7-40c5-882a-bc25d4497429)
***

![image](https://github.com/user-attachments/assets/272a9e2e-dfd8-45d2-90e3-b4b6cb67e422)
***

![image](https://github.com/user-attachments/assets/d32b0f8e-fb11-42f8-97e2-69343f3b0057)
***

![image](https://github.com/user-attachments/assets/91477bcc-e431-4c64-aee0-7819dd55ed46)



```text
这里展示的uniapp前端
```
![image](https://github.com/heshixing/hbbili/assets/102710734/71d53149-97cc-4cbc-8157-384ec0717c1a)

***

![image](https://github.com/heshixing/hbbili/assets/102710734/a7f6a1f6-3ad4-4634-8fd0-895ae933de42)


***

![image](https://github.com/heshixing/hbbili/assets/102710734/a0004210-415f-40db-97e7-0d15fe0ce2c0)


***

![image](https://github.com/heshixing/hbbili/assets/102710734/2e1b1c69-6ecf-4857-8ba8-9f9b49c972ae)


***
```text
uniapp没有找到合适的弹幕组件，下面是我用vue2+Dplayer模拟的实时弹幕推送以及记录在线人数
```

![image](https://github.com/heshixing/hbbili/assets/102710734/49a0ea26-264d-483a-995c-24a01dbd6152)


***

![image](https://github.com/heshixing/hbbili/assets/102710734/183ee35d-f2b1-4c94-9647-8cac8e68a0ab)


***

![image](https://github.com/heshixing/hbbili/assets/102710734/e3c8cd91-32a7-4a05-a1d4-1b39aba307ea)


***
![image](https://github.com/heshixing/hbbili/assets/102710734/4d6df7c4-91a4-4a71-8d0d-e21e90d64fb4)

