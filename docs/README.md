# 项目文档

## 项目结构

```
calculation-core 核心业务逻辑
calculation-api 对外提供接口
calculation-domain 业务对象定义
calculation-e2e 集成测试
scripts 压测脚本,弹性扩容测试脚本
```

## 架构图
```mermaid
graph TB
    subgraph "API服务层 API Service Layer"
        A1[用户API User API]
        A2[账户API Account API]
        A3[交易API Transaction API]
        A4[余额API Balance API]
    end

    subgraph "服务层 Service Layer"
        B1[用户服务 UserService]
        B2[账户服务 AccountService]
        B3[交易服务 TransactionService]
        B4[余额服务 BalanceService]
    end

    subgraph "数据访问层 Data Access Layer"
        C1[用户DAO UserDAO]
        C2[账户DAO AccountDAO]
        C3[交易DAO TransactionDAO]
        C4[余额DAO BalanceDAO]
    end

    subgraph "缓存层 Cache Layer"
        D1[用户缓存]
        D2[账户缓存]
        D3[交易缓存]
        D4[余额缓存]
    end

    subgraph "存储层 Storage Layer"
        E1[(MySQL主库)]
        E2[(MySQL从库)]
        E3[(Redis集群)]
    end

    %% API到Service的调用关系
    A1 --> B1
    A2 --> B2
    A3 --> B3
    A4 --> B4

    %% Service到DAO的调用关系
    B1 --> C1
    B2 --> C2
    B3 --> C3
    B4 --> C4

    %% DAO到存储层的访问关系
    C1 & C2 & C3 & C4 --> E1
    E1 --> E2

    %% 缓存访问关系
    B1 --> D1
    B2 --> D2
    B3 --> D3
    B4 --> D4
    D1 & D2 & D3 & D4 --> E3

```

## 快速开始

- 启动redis
```
docker-compose service redis up -d
```
- 启动mysql
```
docker-compose service mysql up -d
```
- 启动项目
```
mvn spring-boot:run
```
- 执行性能测试

如果本地执行,需要修改测试计划,将接口地址调整为本地

```
jmeter -n -t scripts/load-test-plan.jmx -l scripts/load-test-result.jtl
```

- 执行单元测试
```
mvn test
```

- Helm 安装
```
helm install calculation-core ./charts/calculation-core
```

- 利用 [testkube](https://testkube.io/blog/jmeter-and-kubernetes-how-to-run-tests-efficiently-with-testkube) 执行 压力测试

```
kubeclt apply -f scripts/load-test.yaml
testkube run testworkflow jmeter
```
- 

# 代码覆盖率报告

![alt text](image.png)

# 水平扩容测试报告

为了更容易触发水平扩容条件,CPU与内存调整为 8%;

![alt text](autoscaling1.png)

执行压力测试触发扩容后:



# 性能测试报告

## 测试资源

CPU: 500m
内存: 1280Mi

## 测试结果

![alt text](jmeter1.png)
![alt text](jmeter2.png)


