# 旁观时间管理大师 (NoMoreF3F4)

一个模组用来管理玩家的旁观模式时间。本模组让暮色森林的屏障不再被随意穿过；让结构里面的门自己也是能过人的，而不是从旁边的墙壁穿过去。

我求求你们不要3秒10旁观了，没找到结构的时候又没有不让你们用旁观跑图，怎么进了结构还在那不停地按F3+F4呢？早知道当初就让全民制作者们把结构全做成一个房间了，好好走路好不好。

## 🎯 功能特色

### 核心机制
每个玩家都有自己的信用点，不用旁观就加，用旁观就减，在旁观和生存之间切换立马大把大把的减。不管你是谁，没有信用点就不要用旁观了！

### 管理功能
有op并不代表就可以控制系统了，利用白名单系统，自己的服务器自己做主。想在哪个维度禁用旁观、让规则怎么小气都可以！

## 🛠️ 配置说明

配置文件位于: `config/nomoref3f4-common.toml`
这是模组唯一的配置文件

```toml
# 服务器配置
[服务器配置]
# 允许使用旁观模式的管理员列表
allowed_admins = ["Player", "Dev", "Admin", "Yanbwe", "TestUser"]

# 禁用旁观模式的维度列表
# 格式: ["minecraft:the_nether", "minecraft:the_end"]
disabled_dimensions = ["twilightforest:twilight_forest"]

# 信用点配置
[信用点配置]
# 信用点上限值
credit_max = 12000

# 信用点下限值（可为负数）
credit_min = -2400

# 切换到旁观模式时扣除的信用点数量
spectator_cost = 2400

# 非旁观模式下每tick恢复的信用点数量
tick_recovery = 1

# 旁观模式下每tick消耗的信用点数量（应为负数）
tick_consumption = -1
```

### 管理员命令
```
/nomoref3f4 check                    # 查看自己的信用点
/nomoref3f4 check <玩家名>           # 查看指定玩家信用点
/nomoref3f4 set <玩家名> <数值>      # 设置玩家信用点
/nomoref3f4 add <玩家名> <数值>      # 增加玩家信用点
/nomoref3f4 remove <玩家名> <数值>   # 减少玩家信用点
/nomoref3f4 reload                   # 重载配置文件
```

*让旁观不再滥用，让游戏更加耐玩！* 🎮✨😋
