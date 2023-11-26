package utils

import "encoding/json"

func CopyByJson(dest interface{}, src interface{}) {
	data, _ := json.Marshal(src)
	_ = json.Unmarshal(data, dest)
}