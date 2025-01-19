# Real-Time Balance Calculation System

## [相关文档](docs/README.md)

1. **目标（Objective）**：
    - 在Java中开发一个实时余额计算系统，并将其部署在云平台（AWS/GCP/Ali）上的Kubernetes（K8s）集群中，确保满足高可用性和弹性要求。
2. **要求（Requirements）**：
    - **核心功能（Core Functionality）**：
        - 实现一个能实时处理金融交易并更新账户余额的服务。
        - 每笔交易应包含唯一的交易ID、源账户号、目标账户号、金额和时间戳，且服务应能处理并发交易并相应地更新余额。
    - **高可用性和弹性（High Availability and Resilience）**：
        - 将服务部署在Kubernetes集群（如AWS EKS、GCP GKE、Alibaba ACK）上。
        - 使用Kubernetes的特性如Deployment、Service、Horizontal Pod Autoscaler（HPA）来确保高可用性和可扩展性。
        - 为失败的交易实现重试机制。
        - 使用托管数据库服务（如AWS RDS、GCP Cloud SQL、Alibaba RDS）来存储账户和交易数据，通过数据库事务和锁来确保数据的一致性和完整性。
    - **性能（Performance）**：
        - 优化服务以处理高频交易。
        - 使用分布式缓存服务（如AWS ElastiCache、GCP Memorystore、Alibaba Cloud ApsaraDB for Redis）实现缓存。
    - **测试（Testing）**：
        - 使用JUnit编写单元测试。
        - 编写集成测试以确保服务与数据库和缓存正确协作。
        - 进行弹性测试以确保服务能从故障（如pod重启、节点故障）中恢复。
        - 使用负载测试工具（如Apache Meter）进行性能测试。
    - **模拟数据（Mocking Data）**：
        - 使用模拟数据生成器来模拟大量交易和账户余额以用于测试目的。
3. **文档（Documentation）**：
    - 提供一个包含服务部署和测试说明的README文件。
    - 包括架构图和设计选择的解释。
4. **交付物（Deliverables）**：
    - 源代码仓库（如GitHub）。
    - Kubernetes部署清单或Helm charts。
    - 测试覆盖率报告。
    - 弹性测试结果。
    - 性能测试结果。
    - 文档。
