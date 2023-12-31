package main

import (
	"encoding/json"
	"flag"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"strings"
)
type Device_Msg struct{
	Mac string `json:"mac"`
	Sn string `json:"sn"`
	Ver string `json:"ver"`
	Opt string `json:"opt"`
	Device string `json:"device"`
	Pkgname string `json:"pkgname"`
	RomVer string `json:"romver"`
	System string `json:"system"`
	Currmdm int `json:"currmdm"`
	Currstate string `json:"currstate"`
	LcmdmVer string `json:"lcmdm_version"`

}
type Result struct {
	Mac    string    `json:"mac"`
	Device string `json:"device"`
}

type Switch struct {
	Switcher    bool    `json:"switcher"`
}
type Announce struct{
	IsapiGw string `json:"isApigw"`
}
type DingTalkJson struct{
	Msgtype string `json:"msgtype"`
	Text D_Content `json:"text"`
}
type D_Content struct{
	Content string `json:"content"`
}
type Sword_Device struct{
	Mac string `json:"mac"`
	Sn string `json:"sn"`
	Ver string `json:"ver"`
	Device string `json:"device"`
	RomVer string `json:"romver"`
	Pkgname string `json:"pkgname"`
	System string `json:"system"`
	Currmdm int `json:"currmdm"`
	Currstate string `json:"currstate"`
	LcmdmVer string `json:"lcmdm_version"`
}



//-------args---------
var announcement_file string 
var port string 
var istest bool
var cloudauthorize bool //auto cloudAuth
var genkey string
var only_add_key string
var prod_test bool

var Swordplan_mode="0"

//-------args---------
var lastStr=""
var lastTime int64
var msgcount=0
var autoreg=0 
var autoreg_failed=0
var normal_auth=0
var get_ann=0
var onResume=0
var addkey=0

func AnnounceMent(w http.ResponseWriter, request *http.Request){
	log.SetPrefix("[ANNOUNCEMENT] ")
	if request.Method != http.MethodPost {
		w.WriteHeader(http.StatusMethodNotAllowed)
		fmt.Fprintf(w, "method not allowed!")
		log.Println("get error,method not allowed")
		return
	}
	defer request.Body.Close()
	con, _ := io.ReadAll(request.Body)
	var res Announce
	res.IsapiGw    = "unknown"

	errs := json.Unmarshal([]byte(con), &res)
	if errs != nil {
		log.Println("Err:json unmarshal error:", errs)
		fmt.Fprintf(w, "Error!")
		return
	}
	if res.IsapiGw !="tencent"{
		log.Println("announcement get error,not ApiGw func call")
		fmt.Fprintf(w, "Error!")
		return
	}
	log.Println("Get AnnounceMent")
	get_ann++
	fmt.Fprint(w, readfile(announcement_file))
	
}
func SwordPlan(w http.ResponseWriter, request *http.Request){
	log.SetPrefix("[SwordPlan] ")
	if request.Method != http.MethodPost {
		w.WriteHeader(http.StatusMethodNotAllowed)
		fmt.Fprintf(w, "method not allowed!")
		log.Println("get error,method not allowed")
		return
	}
	defer request.Body.Close()
	con, _ := io.ReadAll(request.Body)
	var device Sword_Device
	err:=json.Unmarshal([]byte(con), &device)
	if err!=nil{
		fmt.Fprintf(w, "error")
		log.Println("error on SwordPlan json.Unmarshal")
		return
	}
	currmdm:="genericMDM"
	if device.Currmdm==3{
		currmdm="Lenovo_Mia"
	}else if device.Currmdm==2{
		currmdm="Lenovo_CSDK"
	}else if device.Currmdm==4{
		currmdm="Supi T11"
	}
	log.Println("-------DeviceInfo-SwordPlan---------")
	log.Println("mac:",strings.ToLower(device.Mac)+"\t")
	log.Println("SN:",device.Sn+"\t")
	log.Println("Version:",device.Ver+"\t")
	log.Println("pkgname:",device.Pkgname+"\t")
	log.Println("device:",device.Device+"\t")
	log.Println("systemInfo:",device.System+"\t")
	log.Println("MDM:",currmdm+"\t")
	log.Println("AdminState:",device.Currstate+"\t")
	log.Println("LcmdmVersion:",device.LcmdmVer+"\t")
	log.Println("-------DeviceInfo-SwordPlan---------")
	onResume++

	if (device.Pkgname!="cn.lspdemo_bronya"&&
	device.Pkgname!="cn.lspdemo_mana"&&
	device.Pkgname!="vizpower.imeeting"&&
	device.Pkgname!="cn.wps.moffice_eng"&&
	device.Pkgname!="com.sohu.inputmethod.sogou.oem"&&
	device.Pkgname!="cn.lspdemo_transparent"){
		fmt.Fprint(w, "2")
	}
	fmt.Fprint(w, Swordplan_mode)
}
func readfile(file string) string{
	log.SetPrefix("[READFILE] ")
	content, err := os.ReadFile(file)
    if err != nil {
		log.Println("file not found")
		return ""
	}
    return string(content)
}
func writefile  (file string,content string){
	log.SetPrefix("[WRITEFILE] ")
	ffile,err :=os.OpenFile(file, os.O_RDWR|os.O_TRUNC|os.O_CREATE, 0644)
	if err != nil{
		log.Println("Open file err =", err)
		return
	}
	defer ffile.Close()
	n, err := ffile.Write([]byte(content))
	if err != nil{
		log.Println("Write file error =", err)
		return
	}
	log.Println("WriteTo file success, n =", n)
}

func getTestmode()string{

	if istest||prod_test{
		return "true"
	}else{
		return "false"
	}
	
}
func DeviceMsg(w http.ResponseWriter,request *http.Request){
	log.SetPrefix("[DeviceMsg] ")
	if request.Method != http.MethodPost {
		w.WriteHeader(http.StatusMethodNotAllowed)
		fmt.Fprintf(w, "method not allowed!")
		log.Println("get error,method not allowed")
		return
	}
	defer request.Body.Close()
	con, _ := io.ReadAll(request.Body)
	var param Device_Msg
	errs := json.Unmarshal([]byte(con), &param)
	if errs!=nil{
		fmt.Fprintf(w, "error")
		log.Println("json decode error")
	}
	msg:="-------------\n"
    msg+="设备mac:"+strings.ToLower(param.Mac)+"\n"
    msg+="设备版本:"+param.Ver+"\n"
    if param.Sn!="null" {
        msg+="设备SN码:"+param.Sn+"\n"
	}
    if param.Pkgname!=""{
        msg+="包名:"+param.Pkgname+"\n"
	}
    if param.Device!=""{
		msg+="设备:"+param.Device+"\n"	
	}
    if param.RomVer!=""{
        msg+="ROM版本:"+param.RomVer+"\n"
	}
    if param.System!=""{
		msg+="系统版本:"+param.System+"\n"
	}
    if param.Currstate!=""{
		msg+="当前状态:"+param.Currstate+"\n"
	}
    msg+="当前mdm:"
	currmdm:="通用模式"
	if param.Currmdm==3{
		currmdm="Lenovo_Mia"
	}else if param.Currmdm==2{
		currmdm="Lenovo_CSDK"
	}else if param.Currmdm==4{
		currmdm="Supi T11"
	}
	msg+=currmdm+"\n"
	if param.LcmdmVer!="设备未安装管控"{
		msg+="领创版本:"+param.LcmdmVer+"\n"
	}
    msg+="操作:"+param.Opt+"\n"
	log.Println("-------DeviceMessage---------")
	log.Println("mac:",strings.ToLower(param.Mac)+"\t")
	log.Println("SN:",param.Sn+"\t")
	log.Println("Version:",param.Ver+"\t")
	log.Println("Device:",param.Device+"\t")
	log.Println("pkgname:",param.Pkgname+"\t")
	log.Println("systemInfo:",param.System+"\t")
	log.Println("romversion:",param.RomVer+"\t")
	log.Println("MDM:",currmdm+"\t")
	log.Println("AdminState:",param.Currstate+"\t")
	log.Println("LcmdmVersion:",param.LcmdmVer+"\t")
	log.Println("opt:",param.Opt+"\t")
	log.Println("-------DeviceMessage---------")
	msgcount++
}



func ParseArgs(){
	flag.StringVar(&announcement_file,"Ann", "announcement.txt", "announcement_file")
	flag.StringVar(&port,"port","6584","web Port")
	flag.BoolVar(&istest,"apitest",false,"api test")
	flag.BoolVar(&cloudauthorize,"cloudauthorize",false,"cloudauthorize")
	flag.StringVar(&genkey,"genkey","","generate auth key and exit")
	flag.StringVar(&only_add_key,"addkey","","add auth key and exit")
	flag.BoolVar(&prod_test,"pro",false,"production test")
	flag.StringVar(&Swordplan_mode,"sword","0","SwordPlan global mode")
	flag.Parse()
}
func Initlog(){
	logFile, err := os.OpenFile(`./logs.log`, os.O_WRONLY|os.O_CREATE|os.O_APPEND, 0666)
    if err != nil {
        panic(err)
    }
	multiWriter := io.MultiWriter(os.Stdout, logFile)
    log.SetOutput(multiWriter)
}
func main() {	
	ParseArgs()
	Initlog() //同时输出到文件和控制台
	log.SetPrefix("[BASE] ")
	log.Printf("LinspirerDemo Base web service V2.0 on port:%s with announcement file:%s",port,announcement_file)
	var httpport=""
	if istest {
		httpport="127.0.0.1:"+port
	}else{
		httpport=":"+port
	}
	http.HandleFunc("/getAnnouncement",AnnounceMent)
	http.HandleFunc("/SwordPlan",SwordPlan)
	http.HandleFunc("/DeviceMsg",DeviceMsg)
	http.ListenAndServe(httpport, nil)
}