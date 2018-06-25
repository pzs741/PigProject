# 前端APP的安装和使用

## APP的安装
+ 手机登录百度云盘
+ 打开https://pan.baidu.com/s/1rRqrIRXYcYdPaiG4q-Ufrw
+ 密码：d5jw
+ 下载APK
+ 点击信任后安装APP

## APP使用
采用Andriod设计并实现QA对话界面，用户打开APP后在对话框输入问题，可以为关键词、短语、问句，APP会实时监测用户输入的信息，并在输入栏上方显示搜索建议，当用户点击搜索建议或者输入问题发送按钮时，相关的问题或者答案会返回给用户供用户浏览和选择。

## 使用示例
通过关键词的输入可以实现搜索建议提供功能，如图1所示：


![image](https://raw.githubusercontent.com/pzs741/PigProject/master/photos/1.png)


图1 搜索建议

用户输入完问题点击发送按钮后，如果在问题知识库中存在相关的匹配问题则显示相关问题序列，实现界面如图2所示：


![image](https://raw.githubusercontent.com/pzs741/PigProject/master/photos/2.png)


图 2 候选问题排序


如果问题与知识库问题完全匹配或者BM25得分超过一定阈值，则直接返回该问题答案，如图3所示:


![image](https://raw.githubusercontent.com/pzs741/PigProject/master/photos/3.png)


图3问题答案解析


