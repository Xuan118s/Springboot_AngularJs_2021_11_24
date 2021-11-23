# Springboot_AngularJs
```js
  package com.github.xuan118s.springboot_angularjs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

	@Autowired
	private OrderDao orderDao;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(@RequestBody Order order) {
		Map<String, Object> result = new HashMap<>();

		orderDao.update(order);

		result.put("id", order.id);
		return result;
	}
	
	@PostMapping("/newdata")
	public @ResponseBody Map<String, Object> newdata(@RequestBody Order order) {
		Map<String, Object> result = new HashMap<>();
	
		orderDao.insert(order);
		
		result.put("id", order.id);
		return result;
	}
	
	@PostMapping("/get")
	public @ResponseBody Object get(String id) {
		return orderDao.get(id);
	}

	@PostMapping("/findAll")
	public @ResponseBody Object findAll() {
		return orderDao.findAll();
	}

	@PostMapping("/delete")
	public @ResponseBody Map<String, Object> delete(String id) {
		Map<String, Object> result = new HashMap<>();
		orderDao.delete(id);
		result.put("id", id);
		return result;
	}
}

 ```
 
<h1>MainController介紹:</h1>
<br>1.@Controller 最後會回到我們的Web Page頁面</br>
<br>2.@Autowired 可以對class成員變量、方法及構造函數進行標註，完成自動裝配的工作</br>
<br>3.@GetMapping 可在method層面上用作處理 http 的請求</br>
<br>4.@PostMapping 使用RequestMethod.POST進行註釋</br>
<br>5.@Responsebody 註解表示該方法的返回的結果直接寫入 HTTP 響應正文（ResponseBody）中，一般在非同步獲取資料時使用</br>
<br>6.@RequestBody 註解則是將 HTTP 請求正文插入方法中，使用適合的 HttpMessageConverter 將請求體寫入某個物件</br>
<hr>

>@PostMapping("/save")
>>寫入一個Map到Html上，

>Quote two sentences
>>>Quote three sentences
