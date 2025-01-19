USE calculation_db;

DELIMITER //

CREATE PROCEDURE init_accounts()
BEGIN
    DECLARE i INT DEFAULT 10000;
    
    -- 清空现有数据
    TRUNCATE TABLE account;
    
    -- 循环插入30000条记录
    WHILE i < 40000 DO
        INSERT INTO account (account_number, balance)
        VALUES (
            i,
            2000 + FLOOR(RAND() * 3001)  -- 随机生成2000到5000之间的数
        );
        SET i = i + 1;
    END WHILE;
END //

DELIMITER ;

-- 执行存储过程
CALL init_accounts();

-- 删除存储过程
DROP PROCEDURE init_accounts; 