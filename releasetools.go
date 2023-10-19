package main

import (
	"encoding/json"
	"flag"
	"fmt"
	"io/ioutil"
	"strings"
)

type ApplicationInfo struct {
	ApplicationID string `json:"applicationId"`
}

type ReleaseTagsInfo struct {
	Code          int    `json:"Code"`
	Msg           string `json:"Msg"`
	UpdateStatus  int    `json:"UpdateStatus"`
	VersionCode   string `json:"VersionCode"`
	VersionName   string `json:"VersionName"`
	ModifyContent string `json:"ModifyContent"`
	DownloadUrl   string `json:"DownloadUrl"`
	ApkSize       int    `json:"ApkSize"`
	ApkMd5        string `json:"ApkMd5"`
}

func main() {
    nameFlag := flag.String("name", "", "替换VersionName的值")
    flag.Parse()

    // 解析未被解析的命令行参数
    remainingArgs := flag.Args()

    // 将未被解析的参数拼接成一个字符串，以空格分隔
    modifyContent := strings.Join(remainingArgs, " ")
	filePath := "LinspirerDemo-master/app/build.gradle"
	content, err := ioutil.ReadFile(filePath)
	if err != nil {
		fmt.Println("无法读取文件:", err)
		return
	}
	fileContent := string(content)
	var versionCode string
	lines := strings.Split(fileContent, "\n")
	for _, line := range lines {
		if strings.Contains(line, "versionCode") {
			parts := strings.Split(line, " ")
			for i, part := range parts {
				if part == "versionCode" && i < len(parts)-1 {
					versionCode = parts[i+1]
					break
				}
			}
		}
	}
	applicationIDs := make(map[string]string)
	inProductFlavorsSection := false
	lines = strings.Split(fileContent, "\n")
	for _, line := range lines {
		if strings.Contains(line, "productFlavors") {
			inProductFlavorsSection = true
			continue
		}
		if inProductFlavorsSection {
			if strings.Contains(line, "applicationId") {
				parts := strings.Split(line, "\"")
				if len(parts) >= 2 {
					flavorName := parts[1]
					applicationId := parts[len(parts)-2]
					applicationIDs[flavorName] = applicationId
				}
			}
		}
		if strings.Contains(line, "compileOptions") {
			inProductFlavorsSection = false
		}
	}
	releaseTags := make(map[string]ReleaseTagsInfo)
	for flavorName := range applicationIDs {
		releaseTag := ReleaseTagsInfo{
			Code:          0,
			Msg:           "OK",
			UpdateStatus:  1,
			VersionCode:   versionCode,
			VersionName:   *nameFlag,  
			ModifyContent: modifyContent, 
			DownloadUrl:   "",
			ApkSize:       6666,
			ApkMd5:        "",
		}
		releaseTags[flavorName] = releaseTag
	}

	for flavorName, releaseTag := range releaseTags {
		jsonBytes, err := json.Marshal(releaseTag)
		if err != nil {
			fmt.Println("JSON编码错误:", err)
			return
		}

		jsonFilePath := fmt.Sprintf("releasetags/%s.json", flavorName)
		err = ioutil.WriteFile(jsonFilePath, jsonBytes, 0644)
		if err != nil {
			fmt.Println("无法写入JSON文件:", err)
		}
	}
}
