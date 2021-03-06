# 项目简介
在最开始，我们定义消息消费者的配置要么是硬编码，要么是在配置文件中，这通常是不会有什么问题的。但一旦出现如下问题，我们就会很苦恼
- 消息中有不明字符，造成消息无法ack，导致阻塞了后续所有消息
- 消息消费者线程数设置过低，造成消息堆积，从而造成业务延迟
- 想要调整消息处理方式，比如：在消息消费失败时把消息从放回队头调整为放回队尾进行重新消费
- 消息消费者出现问题时，由于未标记责任人，无法找到对应责任人进行处理
- 当外部公司系统维护时，同时，我们的消费者依赖外部公司系统的接口，无法灵活地在维护期间对消息消费者关闭
- 无法对公司消息消费项目进行统一管理
- 当消息大量消费失败时，手工处理耗时耗力，也会极大地影响到业务进展

由于以上种种问题，就急切需要一个好的解决方案，而此项目就是这个好的解决方案。

此项目分为两部分
- listener服务依赖<br/>
  * ttpai-mqlistener-spring：spring项目依赖
  * ttpai-mqlistener-starter：spring boot项目依赖
- 管理端<br/>
  mqlistener admin是mqlistener的统一配置与管理的入口，它为用户提供了服务列表、listener列表、listener配置、节点启停等功能。在mqlistener admin中我们可以配置规则来控制如何处理消息。

目前此项目仅支持消费 RabbitMQ 的消息。
相关项目均由 Java 代码实现。

---

其他文档入口
- [1 快速开始](./doc/1%20快速开始.md)
- [2 功能详细介绍](./doc/2%20功能详细介绍.md)
- [3 架构设计介绍](./doc/3%20架构设计介绍.md)