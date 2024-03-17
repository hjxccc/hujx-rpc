一款基于Vert.x+Etcd实现的轻量级Java RPC框架。提供服务注册，发现，负载均衡（还没实现）。是一个学习RPC工作原理的良好示例。

通过这个简易项目的学习，可以让你从零开始实现一个类似 Dubbo 服务框架 mini 版RPC，学到 RPC 的底层原理以及各种 Java 编码实践的运用。RPC的调用流程：
![image](https://github.com/hjxccc/hujx-rpc/assets/96366907/3b7ebfb9-4cc4-49df-8721-1e7db872ad2f)

cong-rpc-core	  rpc核心实现类
example-common	示例代码的公共依赖，包括接口、Model 等
example-consumer	服务消费者
example-provider	服务提供者
