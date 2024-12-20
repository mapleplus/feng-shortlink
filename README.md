# 枫短链平台
## SaaS 短链接系统，为企业和个人提供一个高效、安全和可靠的短链接管理平台，平台提供短链接精确的追踪分析功能，并有效应对高峰期跳转访问并发
## 技术架构：SpringBoot + SpringCloudAlibaba + Nacos + Sentinel + RocketMQ + ShardingSphere + Redis + MySQL + Redisson
![](./readme_image/jishujiagou.png)
## 什么是枫短链系统？
![](./readme_image/url.png)
## 详细介绍：
1. 生成唯一标识符：当用户输入或提交一个长 URL 时，短链接服务会生成一个唯一的标识符或者短码。
2. 将标识符与长 URL 关联：短链接服务将这个唯一标识符与用户提供的长 URL 关联起来，并将其保存在数据库或者其他持久化存储中。
3. 创建短链接：将生成的唯一标识符加上短链接服务的域名（例如：http://s.fxink.cn/）作为前缀，构成一个短链接。
4. 重定向：当用户访问该短链接时，短链接服务接收到请求后会根据唯一标识符查找关联的长 URL，然后将用户重定向到这个长 URL。
5. 跟踪统计：一些短链接服务还会提供访问统计和分析功能，记录访问量、来源、地理位置等信息。
   短链接应用场景
## 应用场景
短链接经常出现在咱们日常生活中，大家总是能在某些活动节日里收到各种营销短信，里边就会出现短链接。
淘宝、抖音、快手、微博等场景都需要短链接。大家在转发淘宝商品、抖音短视频时会有段文本，其中就有短链接。
举例：
> 6.17 复制打开抖音，看看【雷军的作品】大家小夜灯一般都装在哪些地方？听说有的朋友最多在家... https://v.douyin.com/iU2rFAdB/ C@u.SY 08/27 UYm:/

通过短链接帮助企业在营销活动中，识别用户行为、点击率等关键信息监控。
主要作用包括但不限于以下几个方面：
* 提升用户体验：用户更容易记忆和分享短链接，增强了用户的体验。
* 节省空间：短链接相对于长 URL 更短，可以节省字符空间，特别是在一些限制字符数的场合，如微博、短信等。
* 美化：短链接通常更美观、简洁，不会包含一大串字符。
* 统计和分析：可以追踪短链接的访问情况，了解用户的行为和喜好。
## 项目质量
> 短链接项目采用 SaaS 方式开发。SaaS代表“软件即服务”（Software as a Service），与传统的软件模型不同，SaaS 不需要用户在本地安装和维护软件，而是通过互联网直接访问在线应用程序。

### 既然是 SaaS 系统，那势必会带来 N 多个问题。在我看来，问题即项目亮点。一起来看下：
* 海量并发：可能会面对大量用户同时访问的情况，尤其在高峰期，这会对系统的性能和响应速度提出很高的要求。
* 海量存储：可能需要存储大量的用户数据，包括数据库、缓存等，需要足够的存储空间和高效的存储管理方案。
* 多租户场景：通常支持多个租户共享同一套系统，需要保证租户间的数据隔离、安全性和性能。
* 数据安全性：需要保证用户数据的安全性和隐私，防止未经授权的访问和数据泄露。

> 项目实现过程中会充分考虑以上问题，最终实现高可用、可扩展、支持海并发以及存储的 SaaS 短链接系统。
## 项目演示
### 在线地址：http://www.fxink.cn
![](./readme_image/url_1.jpg)
![](./readme_image/url_2.jpg)
![](./readme_image/url_3.jpg)
![](./readme_image/url_4.jpg)
![](./readme_image/url_5.jpg)
![](./readme_image/url_6.jpg)