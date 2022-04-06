### 设备登录

#### param

```
账号 密码 swdid(华为安卓10要手动填SN)
```

```python
        dataa=json.dumps({"id":"1","!version":"1","jsonrpc":"2.0","is_encrypt":"true","client_version":"tongyongshengchan_5.02.003.0","params":aes_encrypt("3901254795DA7CC3",json.dumps({"userid":account,"password":password,"swdid":swdid})),"method":"com.linspirer.user.login"})    
resB=requests.post(url="https://cloud.linspirer.com:883/public-interface.php",data=dataa)
```

### sso登录

param

忘了 没研究呜呜呜

### fakeapp上传应用列表造假应用名单

```
用户姓名 设备model appname 包名 versioncode versionname
```

```python
datar={"email":acc,"model":model,"reportlist":
       
       [{"appname": appname,"isWhiteApp": 1,"issystemapp": 0,"packagename": packagename,"versioncode": versioncode,"versionname": versionname}]
       ,"swdid":swdid}
        datab=json.dumps({"!version":6,"cilent_version":"tongyongshengchan_LinspirerToolBox","id":1,"method":"com.linspirer.device.setdeviceapps","params":aes_encrypt_new("1191ADF18489D8DA",json.dumps(datar))})
        anss=requests.post(url="https://cloud.linspirer.com:883/public-interface.php",data=datab)

```

**注意 这里要用新aes接口**

reportlist里呢 是这样的 [这里可以放很多{}{}{}]啊啥啥啥的

### 造假push日志

新接口嗷

```python
        reportlist=[]
        for i in range (300):
            temp={}
            temp['eliminate_data']=0
            temp['event_is_illegal']=0
            temp['lock_workspace']=0
            temp['notify_admin']=0
            temp['event_result']=0
            temp['user_id']=name
            temp['user_name']=acc
            temp['event_time']=str(time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(days=-3)))
            temp['event_name']="screen_unlock_normal"
            temp['description']="unlock"
            reportlist.append(temp)
        #第二层
        datafake={}
        datafake['email']=acc
        datafake['model']=model
        datafake["swdid"]=swdid
        datafake['reportlist']=reportlist
        #在最后赋值给它
        datafake_base={}
        datafake_base['!version']=6
        datafake_base['id']=1
        datafake_base['client_version']="tongyongshengchan_5.03.007.0"
        datafake_base['jsonrpc']="2.0"
        datafake_base['method']="com.linspirer.device.setdevicelogs"
        datafake_base['params']=aes_encrypt_new("1191ADF18489D8DA",json.dumps(datafake))#datafake
        anss=requests.post(url="https://cloud.linspirer.com:883/public-interface.php",data=json.dumps(datafake_base))
        print(aes_decrypt_new("1191ADF18489D8DA",anss.text))

```

